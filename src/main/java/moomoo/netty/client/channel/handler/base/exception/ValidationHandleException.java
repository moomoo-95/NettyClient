package moomoo.netty.client.channel.handler.base.exception;

public class ValidationHandleException extends HandleException {
    public ValidationHandleException(int errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationHandleException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
