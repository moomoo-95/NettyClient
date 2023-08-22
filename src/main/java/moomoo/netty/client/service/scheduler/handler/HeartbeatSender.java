package moomoo.netty.client.service.scheduler.handler;

import moomoo.netty.client.AppInstance;
import moomoo.netty.client.message.TcpHeartbeatReqMessage;
import moomoo.netty.client.service.scheduler.handler.base.IntervalTaskUnit;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * heartbeat를 주고 받는 thread를 생성하여 1초마다 AWF로 heartbeat를 보낸다.
 */
public class HeartbeatSender extends IntervalTaskUnit {
    private static final Logger log = getLogger(HeartbeatSender.class);

    private static AppInstance appInstance = AppInstance.getInstance();

    private int count = 0;

    public HeartbeatSender(int interval) {
        super(interval);
    }

    @Override
    public void run() {
        if (appInstance.isLogin()) {
            TcpHeartbeatReqMessage heartbeatReqMessage = new TcpHeartbeatReqMessage(AppInstance.SYSTEM_ID);
            // 로그는 5회 당 1 회 출력
            if (count > 5) {
                log.debug("hb req msg : {}", heartbeatReqMessage);
                count = 0;
            }
            count++;
        }
    }
}
