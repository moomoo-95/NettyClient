package moomoo.netty.client.command.base;

public class CommandType {

    private CommandType() {
        // nothing
    }

    // common command
    public static final String COMMAND_Q = "q";
    public static final String COMMAND_LOGIN_REQ = "login";
    public static final String COMMAND_HB = "hb";
    public static final String COMMAND_DATA_RPT = "data";
}
