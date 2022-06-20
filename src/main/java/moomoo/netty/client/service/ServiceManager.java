/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * Service Manager
 *
 * @file ServiceManager.java
 * @author Tony Lim
 */

package moomoo.netty.client.service;

import moomoo.netty.client.AppInstance;
import moomoo.netty.client.channel.NettyChannelManager;
import moomoo.netty.client.config.DefaultConfig;
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
    private static DefaultConfig defaultConfig = instance.getDefaultConfig();

    private NettyChannelManager nettyChannelManager;

    private static class SingleTon {
        public static final ServiceManager INSTANCE = new ServiceManager();
    }

    private boolean isQuit = false;

    /**
     * Reads a config file in the constructor
     */
    public ServiceManager() {
        // nothing
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
                Thread.sleep(20);
            } catch (Exception e) {
                log.error("ServiceManager.loop.Exception ",e);
                Thread.currentThread().interrupt();
            }
        }

        log.error("Process End");
    }

    private void startService() {
        log.info("Netty Client Process Start.");

        // System lock
        systemLock();

        nettyChannelManager = NettyChannelManager.getInstance();
        nettyChannelManager.startTcpClientChannel(defaultConfig.getCommonTargetIp(), defaultConfig.getCommonTargetPort());

        IntervalTaskManager.getInstance().init().start();

        instance.setCommandServer();
        instance.getCommandServer().run();

    }


    /**
     * Finalizes all the resources
     */
    public void stopService() {
        IntervalTaskManager.getInstance().stop();

        instance.getCommandServer().stop();

        nettyChannelManager.stopTcpClientChannel();
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
