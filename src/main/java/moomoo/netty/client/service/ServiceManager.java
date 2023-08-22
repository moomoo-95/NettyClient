package moomoo.netty.client.service;

import moomoo.netty.client.AppInstance;
import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.command.CommandServer;
import moomoo.netty.client.config.UserConfig;
import moomoo.netty.client.service.scheduler.IntervalTaskManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.*;

public class ServiceManager {
    static final Logger log = LoggerFactory.getLogger(ServiceManager.class);


    private static AppInstance instance = AppInstance.getInstance();
    private static UserConfig userConfig = instance.getUserConfig();

    private NettyChannelManager nettyChannelManager;

    private static class SingleTon {
        public static final ServiceManager INSTANCE = new ServiceManager();
    }

    private boolean isQuit = false;

    /**
     * Reads a config file in the constructor
     */
    public ServiceManager() {
        try {
            userConfig = instance.getUserConfig();
            userConfig.init(instance.getConfigPath());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public static ServiceManager getInstance() {
        return SingleTon.INSTANCE;
    }


    /**
     * Main loop
     */
    public void loop() {
        this.startService();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.error("Process is about to quit (Ctrl+C)");
            this.isQuit = true;
            this.stopService();
        }));

        while (!isQuit) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("ServiceManager.loop.Exception ",e);
                Thread.currentThread().interrupt();
            }
        }

        log.error("Process End");
    }

    private ServiceManager startService() {
        log.info("Netty Client Process Start.");

        // System lock
        systemLock();

        nettyChannelManager = NettyChannelManager.getInstance();
        nettyChannelManager.startServer(userConfig.getCommonLocalIp(), userConfig.getCommonLocalPort());
        IntervalTaskManager.getInstance().init().start();

        instance.setCommandServer(new CommandServer());
        instance.getCommandServer().run();

        return this;
    }


    /**
     * Finalizes all the resources
     */
    public void stopService() {
        IntervalTaskManager.getInstance().stop();

        instance.getCommandServer().stop();

        nettyChannelManager.stopServer();
        log.info("Netty Client Process End.");
    }

    private void systemLock(){
        String tmpdir=System.getProperty("java.io.tmpdir");
        File f = new File(tmpdir, System.getProperty("lock_file", "nettyClient.lock"));

        try (FileChannel fileChannel = FileChannel.open(f.toPath(), CREATE, READ, WRITE)) {
            FileLock lock = fileChannel.tryLock();
            if (lock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        log.error("shutdown");
                        lock.release();
                        fileChannel.close();
                        Files.delete(f.toPath());
                    } catch (IOException e) {
                        //ignore
                    }
                }));
            } else {
                log.error("nettyClient process already running");
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("ServiceManager.systemLock.Exception ", e);
        }
    }

    public void setQuit(boolean quit) {
        isQuit = quit;
    }
}
