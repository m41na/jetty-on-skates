package com.jarredweb.xmlstore.js;

public class Js {
    
    public static Class<?> require(String require) throws ClassNotFoundException {
        String pack = JsApp.class.getName();
        String prefix = pack.substring(0, pack.lastIndexOf("."));
        String className = String.format("%s.%s%s", prefix, Character.toUpperCase(require.charAt(0)), require.substring(1));
        return Class.forName(className);
    }
}
