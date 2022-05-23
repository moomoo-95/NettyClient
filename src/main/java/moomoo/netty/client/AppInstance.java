package moomoo.netty.client;

import moomoo.netty.client.command.CommandServer;
import moomoo.netty.client.config.DefaultConfig;

public class AppInstance {

    private static class SingleTon {
        public static final AppInstance INSTANCE = new AppInstance();
    }

    private DefaultConfig defaultConfig;
    private CommandServer commandServer;

    public AppInstance() {
        // nothing
    }

    public static AppInstance getInstance() {
        return SingleTon.INSTANCE;
    }

    public void setInstance(String configPath) {
        setDefaultConfig(configPath);
    }

    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String configPath) {
        this.defaultConfig = new DefaultConfig(configPath);
    }

    public CommandServer getCommandServer() {
        return commandServer;
    }

    public void setCommandServer() {
        this.commandServer = new CommandServer();
    }
}
