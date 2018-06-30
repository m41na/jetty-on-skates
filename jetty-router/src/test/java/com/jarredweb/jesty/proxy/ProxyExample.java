package com.jarredweb.jesty.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;

public class ProxyExample {

    public static void main(String... args) {
        //Example 1
        Item proxyInstance = (Item) Proxy.newProxyInstance(ProxyExample.class.getClassLoader(), new Class[]{Item.class},
                new DynamicInvocationHandler(new HashMap<>()));

        proxyInstance.setId(10L);
        proxyInstance.setName("Mercury");
        proxyInstance.setValid(true);

        //retrieve values
        System.out.println(proxyInstance.getId());
        System.out.println(proxyInstance.getName());
        System.out.println(proxyInstance.isValid());
        System.out.println(proxyInstance.toString());

        //Example 2
        CharSequence csProxyInstance = (CharSequence) Proxy.newProxyInstance(ProxyExample.class.getClassLoader(),
                new Class[]{CharSequence.class},
                new TimingDynamicInvocationHandler("Hello World"));

        csProxyInstance.length();
    }
}
