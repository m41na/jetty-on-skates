package com.jarredweb.xmlstore.visitor;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class GetValueVisitor extends ClassVisitor {

    private final Pattern listPattern = Pattern.compile("^([_a-zA-Z].*)?\\[(.*)?\\]$");
    private final Object source;
    private final Class<?> clazz;
    private final String field;
    private Object value;

    public GetValueVisitor(Object source, Class<?> clazz, String field) {
        super(Opcodes.ASM5);
        this.source = source;
        this.clazz = clazz;
        this.field = field;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descr, String signature, Object value) {        
        if (this.field.equals(name)) {
            System.out.printf("access: %d, name: %s, descr: %s, signature: %s%n", access, name, descr, signature);
            try {
                Field prop = clazz.getDeclaredField(name);
                prop.setAccessible(true);
                this.value = prop.get(source);
                prop.setAccessible(false);
                return null;
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                return null;
            }
        }

        if (this.field.startsWith(name)) {
            System.out.printf("access: %d, name: %s, descr: %s, signature: %s%n", access, name, descr, signature);
            Matcher matcher = listPattern.matcher(this.field);
            if (matcher.find()) {
                String match = matcher.group(1);
                try {
                    Field prop = clazz.getDeclaredField(match);
                    prop.setAccessible(true);
                    Object val = prop.get(source);
                    prop.setAccessible(false);

                    //get list index
                    match = matcher.group(2);
                    if (match.matches("\\d+")) {
                        if ("Ljava/util/List;".equals(descr)) {
                            this.value = ((List) val).get(Integer.valueOf(match));
                            return null;
                        }
                        if ("Ljava/util/Set;".equals(descr)) {
                            this.value = new LinkedList<>((Set) val).get(Integer.valueOf(match));
                            return null;
                        }
                    }
                    if (match.matches("^('|\").*?('|\")$")) {
                        Object key = match.replaceAll("('|\")", "");
                        if("Ljava/util/Map;".equals(descr)){
                            this.value = ((Map) val).get(key);
                            return null;
                        }
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    return null;
                }

            }
        }
        return null;
    }

    public Object getValue() {
        return this.value;
    }

    public Class<?> toType(String descr) throws ClassNotFoundException {
        switch (descr) {
            case "Z":
                return boolean.class;
            case "C":
                return char.class;
            case "B":
                return byte.class;
            case "S":
                return short.class;
            case "I":
                return int.class;
            case "F":
                return float.class;
            case "J":
                return long.class;
            case "D":
                return double.class;
            default: {
                return Class.forName(descr);
            }
        }
    }
}
