package com.jarredweb.zesty.setup;

import com.jarredweb.zesty.http.app.ZestyBuilder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZestyLive extends BlockJUnit4ClassRunner {

    private final Logger LOG = LoggerFactory.getLogger(ZestyLive.class);

    private final AtomicInteger count = new AtomicInteger(0);
    private final CountDownLatch wait = new CountDownLatch(1);
    private final Class<? extends ZestyProvider> zestyProvider;

    private ZestyBuilder runner;
    private boolean ready = false;

    public ZestyLive(Class<?> klass) throws InitializationError {
        super(klass);
        LOG.info("**** Testing class '{}' *****", klass.getName());
        UseProvider provider = klass.getAnnotation(UseProvider.class);
        zestyProvider = provider.value();
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        List<FrameworkMethod> methods = super.getChildren();
        this.count.set(methods.size());
        return methods;
    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        try {
            synchronized (wait) {
                if (!ready) {
                    LOG.info("****** Starting server before clients can connect ******");
                    new Thread(() -> {
                        try {
                            runner = zestyProvider.newInstance().provide((int port, String host) -> {
                                ready = true;
                                wait.countDown();
                                LOG.info("{} ready at port {}", host, port);
                            });
                        } catch (InstantiationException | IllegalAccessException ex) {
                            throw new RuntimeException(ex);
                        }
                        runner.startApplication();

                    }).start();

                    wait.await();
                    LOG.info("***** Ready for clients to connect *****");
                }
            }
        } catch (InterruptedException ex) {
            LOG.error("Error awaiting on countdown latch", ex);
        }

        super.runChild(method, notifier);
        if (count.decrementAndGet() <= 0) {
            LOG.info("***** Completed testing. Shutting down server *****");
            runner.shutdownServer();
        }
    }
}
