package com.jarredweb.jetty.sample.tasklet;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskletMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(TaskletMonitor.class);
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private static final Map<String, Future<?>> FUTURES = new HashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                synchronized (EXECUTOR) {
                    if (!EXECUTOR.isShutdown()) {
                        EXECUTOR.shutdownNow();
                        System.out.println("Shutting down executor");
                    }
                }
            }
        });
    }

    public static Future<?> deploy(String name, Runnable task) {
        if (!FUTURES.containsKey(name)) {
            Future<?> future = EXECUTOR.submit(task);
            FUTURES.put(name, future);
            //register mbean task
            try {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                ObjectName oname = new ObjectName(name + "com.jarredweb.webjar.runtime:type=RunnableTask");
                mbs.registerMBean(task, oname);
            } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
                e.printStackTrace(System.err);
            }
            return future;
        } else {
            return FUTURES.get(name);
        }
    }

    public static void undeploy(String name) {
        Future<?> future = FUTURES.remove(name);
        if (future != null) {
            LOG.info("Task '{}' will now be undeployed%n", name);
            future.cancel(true);
            //unregister mbean
            try {
                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                ObjectName oname = new ObjectName(name + "com.jarredweb.webjar.runtime:type=RunnableTask");
                mbs.unregisterMBean(oname);
            } catch (MalformedObjectNameException | InstanceNotFoundException | MBeanRegistrationException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    public static void stop() {
        EXECUTOR.shutdownNow();
    }

    public static Future<?> future(String name) {
        return FUTURES.get(name);
    }
}
