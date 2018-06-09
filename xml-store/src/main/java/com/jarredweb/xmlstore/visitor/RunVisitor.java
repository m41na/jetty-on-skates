package com.jarredweb.xmlstore.visitor;

import com.jarredweb.xmlstore.model.Author;
import com.jarredweb.xmlstore.model.Blog;
import com.jarredweb.xmlstore.model.Comment;
import java.io.IOException;
import org.objectweb.asm.ClassReader;

public class RunVisitor {
    
    public static void main(String... args) throws IOException  {
        Author author = new Author(1l, "admin@user.com", "steve", "maich");
        Blog blog = new Blog(1l, "one day", author, "Science", "goo things all day", "content comes here");
                
        Comment com1 = new Comment(1l, new Author(2l, "reader1@gmail.com", "au1", "first"), "comment 1");
        Comment com2 = new Comment(1l, new Author(3l, "reader2@gmail.com", "au2", "second"), "comment 2");
        
        blog.getComments().add(com1);
        blog.getComments().add(com2);
        
        blog.getMoments().put("first", com1);
        blog.getMoments().put("second", com2);
        
        //System.out.println(getValue(blog, blog.getClass(), "author.lastName".split("\\."), 0));
        System.out.println(getValue(blog, blog.getClass(), "comments[0].author.firstName".split("\\."), 0));
        System.out.println(getValue(blog, blog.getClass(), "moments['second'].author.lastName".split("\\."), 0));
    }

    public static Object getValue(Object source, Class<?> clazz, String[] field, int index) throws IOException{
        GetValueVisitor gvv = new GetValueVisitor(source, clazz, field[index]);
        ClassReader cr = new ClassReader(clazz.getName());
        cr.accept(gvv, 0);
        Object value = gvv.getValue();
        if (index + 1 == field.length) {
            return value;
        }
        else {
            return getValue(value, value.getClass(), field, ++index);
        }
    }
}
