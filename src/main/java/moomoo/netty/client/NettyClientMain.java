package moomoo.netty.client;

import moomoo.netty.client.service.ServiceManager;

public class NettyClientMain {
    public static void main(String[] args) {
        AppInstance.getInstance().setInstance(args[0]);

        ServiceManager serviceManager = ServiceManager.getInstance();
        serviceManager.loop();
    }
}
