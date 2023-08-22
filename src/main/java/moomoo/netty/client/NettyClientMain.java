package moomoo.netty.client;

import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.service.ServiceManager;

@Slf4j
public class NettyClientMain {
    public static void main(String[] args) {
        log.info("Netty Client Process Start [{}]", args[0]);
        AppInstance.getInstance().setConfigPath(args[0]);


        try {
            ServiceManager serviceManager = ServiceManager.getInstance();
            serviceManager.loop();
        } catch (Exception e){
            log.error("Fail to Start Service", e);
        }
    }
}
