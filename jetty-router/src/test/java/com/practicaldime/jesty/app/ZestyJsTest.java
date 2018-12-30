package com.practicaldime.jesty.app;

import org.junit.Ignore;
import org.junit.Test;

public class ZestyJsTest {
    
    @Test
    public void testPing() throws Exception {
        System.out.println("ping");
        ZestySh.ping();
    }
    
    @Test
    @Ignore
    public void testStart() throws Exception {
        System.out.println("start");
        String[] args = new String[]{"www/zjdbc-junit.js"};
        new ZestySh().start(args);
    }
    
    @Test
    public void testSpring() throws Exception {
        System.out.println("start");
        String[] args = new String[]{"test/sample-inject.js"};
        new ZestySh().start(args);
    }
}
