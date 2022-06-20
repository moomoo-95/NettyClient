package moomoo.netty.client.service.scheduler;

import moomoo.netty.client.service.scheduler.handler.HeartbeatSender;
import moomoo.netty.client.service.scheduler.handler.base.IntervalTaskUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IntervalTaskManager {

    private static final Logger log = LoggerFactory.getLogger(IntervalTaskManager.class);

    private static final class Singleton{
        private static final IntervalTaskManager INSTANCE = new IntervalTaskManager();
    }

    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    private Set<ScheduledExecutorService> executorService;

    private int defaultInterval = 1000;
    private boolean isStarted = false;

    private IntervalTaskManager() {
        // nothing
    }

    public static IntervalTaskManager getInstance() {
        return Singleton.INSTANCE;
    }

    public IntervalTaskManager init() {
        addJob(HeartbeatSender.class.getSimpleName(), new HeartbeatSender(defaultInterval));
        return this;
    }

    public void start() {
        if (isStarted) {
            log.info("Already Started Interval Task Manager");
            return;
        }
        isStarted = true;
        executorService = new HashSet<>(jobs.size());
        for (IntervalTaskUnit runner : jobs.values()) {
            ScheduledExecutorService job = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "ITV_" + runner.getClass().getSimpleName()));
            executorService.add(job);
            job.scheduleAtFixedRate(runner,
                    runner.getInterval() - System.currentTimeMillis() % runner.getInterval(),
                    runner.getInterval(), TimeUnit.MILLISECONDS);
        }

        log.info("Interval Task Manager Start");
    }

    public void stop() {
        if (!isStarted) {
            log.info("Already Stopped Interval Task Manager");
            return;
        }
        isStarted = false;
        executorService.forEach(ExecutorService::shutdown);
        log.info("Interval Task Manager Stop");
    }

    public void addJob(String name, IntervalTaskUnit runner) {
        if (jobs.get(name) != null) {
            log.warn("() () () Hashmap Key duplication");
            return;
        }
        log.debug("() () () Add Runner [{}]", name);
        jobs.put(name, runner);
    }
}