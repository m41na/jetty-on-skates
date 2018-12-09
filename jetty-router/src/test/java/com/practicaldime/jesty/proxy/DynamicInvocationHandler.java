package com.practicaldime.jesty.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicInvocationHandler.class);

    private final Map<String, Object> target;

    public DynamicInvocationHandler(Map<String, Object> target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOGGER.info("Invoked method: {}", method.getName());
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
            String propertyName = methodName.substring("set".length());
            return target.put(propertyName, args[0]);
        }
        if (methodName.startsWith("get")) {
            String propertyName = methodName.substring("get".length());
            return target.get(propertyName);
        }
        if (methodName.startsWith("is")) {
            String propertyName = methodName.substring("is".length());
            return target.get(propertyName);
        }
        return method.invoke(target, args);
    }
}
