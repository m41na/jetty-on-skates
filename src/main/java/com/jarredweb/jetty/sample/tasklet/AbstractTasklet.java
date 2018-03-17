package com.jarredweb.jetty.sample.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTasklet implements Runnable, Tasklet, AbstractTaskletMXBean {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTasklet.class);

    private String name;

    public AbstractTasklet() {
        this("_" + System.nanoTime());
    }

    public AbstractTasklet(String name) {
        super();
        this.name = name;
    }

    @Override
    public void init() {
        LOG.info("executing 'init()' for '{}' task. Override as need be.", this.getName());
    }

    @Override
    public void run() {
        deploy(TaskletMonitor.future(name));
        undeploy();
    }

    @Override
    public void undeploy() {
        LOG.info("executing 'undeploy()' for '{}' task. Override as need be", this.getName());
    }
    
    @Override
    public void destroy() {
        LOG.info("executing 'destroy()' for '{}' task. Override as need be", this.getName());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void start() {
        LOG.info("Executing 'start()' operation. Override as need be.");
        init();
        TaskletMonitor.deploy(name, this);
    }

    @Override
    public void stop() {
        LOG.info("Executing 'stop()' operation. Override as need be.");
        TaskletMonitor.undeploy(name);
        destroy();
    }
}
