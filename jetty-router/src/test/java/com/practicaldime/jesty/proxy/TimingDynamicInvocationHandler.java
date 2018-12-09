package com.practicaldime.jesty.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingDynamicInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimingDynamicInvocationHandler.class);

    private final Object target;

    public TimingDynamicInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        Object result = method.invoke(target, args);
        long elapsed = System.nanoTime() - start;

        LOGGER.info("Executing {} finished in {} ns", method.getName(), elapsed);

        return result;
    }
}
