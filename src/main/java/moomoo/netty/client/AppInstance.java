package moomoo.netty.client;

import lombok.Getter;
import lombok.Setter;
import moomoo.netty.client.command.CommandServer;
import moomoo.netty.client.config.UserConfig;

@Getter
@Setter
public class AppInstance {
    private static final class Singleton { private static final AppInstance INSTANCE = new AppInstance(); }

    private String configPath;
    private UserConfig userConfig = new UserConfig();
    private CommandServer commandServer;

    public static final int SYSTEM_ID = (int) ProcessHandle.current().pid();

    private boolean isLogin = false;

    public AppInstance() { /* ignore */ }

    public static AppInstance getInstance() {
        return Singleton.INSTANCE;
    }
}
