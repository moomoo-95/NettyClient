package moomoo.netty.client.channel.handler.base;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import moomoo.netty.client.channel.handler.base.exception.HandleException;
import moomoo.netty.client.channel.handler.base.exception.ValidationHandleException;
import moomoo.netty.client.channel.message.base.TcpMessage;
import moomoo.netty.client.channel.module.TcpInfo;

@Slf4j
public abstract class TcpIncomingMsgHandler<T extends TcpMessage> {

    protected static final Gson GSON = new Gson();

    protected TcpMessage msg;
    protected String json;

    protected String msgType;
    protected String msgTransactionId;
    protected T msgBody;

    protected TcpInfo tcpInfo;

    public void proc(TcpMessage msg, String json, TcpInfo tcpInfo) {
        try {
            this.msg = msg;
            this.json = json;
            this.tcpInfo = tcpInfo;
            this.msgBody = (T) msg;
            fieldSetting();
            handle();
        } catch (ValidationHandleException e) {
            if (e.getCause() == null) {
                log.warn("Validation Fail [Error : {}]", e.getMessage());
            } else {
                log.warn("Validation Fail [Error : {}] [Cause : {}]", e.getMessage(), e.getCause().getMessage());
            }
            onFail(e.getMessage());
        } catch (HandleException e) {
            if (e.getCause() == null) {
                log.warn("Fail to handle message [Error : {}]", e.getMessage());
            } else {
                log.warn("Fail to handle message [Error : {}] [Cause : {}]", e.getMessage(), e.getCause().getMessage());
            }
            onFail(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected Exception Occurs", e);
            onFail("Unexpected Exception");
        }
    }

    protected void fieldSetting() {
        this.msgType = msg.getMsgType();
        this.msgTransactionId = msg.getTransactionId();
    }

    public abstract void handle();

    protected abstract void onFail(String reason);

    protected abstract void onSuccess();

    protected void reply(TcpMessage tcpMessage) {
        tcpInfo.sendMessage(tcpMessage);
    }
}
