package moomoo.netty.client.channel.handler.base.exception;

public class HandleException extends RuntimeException {
    private final int errCode;

    public HandleException(int errorCode, String message) {
        super(message);
        this.errCode = errorCode;
    }

    public HandleException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errorCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
