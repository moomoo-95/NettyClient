package moomoo.netty.client.message.base;

public class TcpMessageType {

    public static final int HEADER_SIZE = 16;
    public static final int LOGIN_REQ_BODY_SIZE = 16;

    public static final int FRAME_START = 0xfefe;

    public static final int LOGIN_REQ_ID = 0x0001;
    public static final int LOGIN_RES_ID = 0x0002;
    public static final int HEARTBEAT_REQ_ID = 0x0011;
    public static final int HEARTBEAT_RES_ID = 0x0012;
    public static final int DATA_RPT_ID = 0x0020;

}
