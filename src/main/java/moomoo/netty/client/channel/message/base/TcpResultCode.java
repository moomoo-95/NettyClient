package moomoo.netty.client.channel.message.base;

public enum TcpResultCode {
    SUCCESS("200"),
    // Engine fault
    BAD_REQUEST("400"),
    UNAUTHORIZED("401"),
    FORBIDDEN("403"),
    NOT_FOUND("404"),
    // ETC server fault
    SERVER_INTERNAL("500");

    public final String resultCode;

    TcpResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
