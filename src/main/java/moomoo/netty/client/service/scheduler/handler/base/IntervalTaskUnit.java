package moomoo.netty.client.service.scheduler.handler.base;

public abstract class IntervalTaskUnit implements Runnable {
    protected int interval;

    protected IntervalTaskUnit(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
