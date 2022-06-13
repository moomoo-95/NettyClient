package moomoo.netty.client.message.base;

public class TcpMessageType {

    private TcpMessageType() {
        // nothing
    }

    public static final int HEADER_SIZE = 8;           // unsigned 16 byte * 4
    public static final int LOGIN_REQ_BODY_SIZE = 8;   // unsigned 32 byte * 2
    public static final int DATA_RPT_BODY_SIZE = 13;   // unsigned 32 byte * 3 + unsigned 8 byte * 1

    public static final int FRAME_START = 0xfefe;

    public static final int LOGIN_REQ_ID = 0x0001;
    public static final int LOGIN_RES_ID = 0x0002;
    public static final int HEARTBEAT_REQ_ID = 0x0011;
    public static final int HEARTBEAT_RES_ID = 0x0012;
    public static final int DATA_RPT_ID = 0x0020;

}
