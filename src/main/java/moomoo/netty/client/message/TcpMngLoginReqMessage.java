package moomoo.netty.client.message;

import moomoo.netty.client.message.base.TcpMessageHeader;
import moomoo.netty.client.message.base.TcpLoginReqBody;
import moomoo.netty.client.message.exception.MessageHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpMngLoginReqMessage {
    private static final Logger log = LoggerFactory.getLogger(TcpMngLoginReqMessage.class);

    private final TcpMessageHeader header;
    private final TcpLoginReqBody body;

    public TcpMngLoginReqMessage(byte[] data) throws MessageHeaderException {
        if (data.length >= HEADER_SIZE + LOGIN_REQ_BODY_SIZE) {
            int index = 0;

            byte[] headerByteData = new byte[HEADER_SIZE];
            System.arraycopy(data, index, headerByteData, 0, headerByteData.length);
            this.header = new TcpMessageHeader(headerByteData);
            index += headerByteData.length;

            byte[] bodyByteData = new byte[LOGIN_REQ_BODY_SIZE];
            System.arraycopy(data, index, bodyByteData, 0, bodyByteData.length);
            this.body = new TcpLoginReqBody(bodyByteData);
        } else {
            this.header = null;
            this.body = null;
            throw new MessageHeaderException("[TCP LOGIN REQ] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpMngLoginReqMessage(int systemId, long processArg, long index) {
        this.header = new TcpMessageHeader(HEADER_SIZE + LOGIN_REQ_BODY_SIZE, LOGIN_REQ_ID, systemId);
        this.body = new TcpLoginReqBody(processArg, index);
    }

    public byte[] getData() {
        byte[] data = new byte[header.getLength()];
        int index = 0;

        byte[] headerByteData = header.getData();
        System.arraycopy(headerByteData, 0, data, index, headerByteData.length);
        index += headerByteData.length;

        byte[] bodyByteData = body.getData();
        System.arraycopy(bodyByteData, 0, data, index, bodyByteData.length);

        return data;
    }

    @Override
    public String toString() {
        return "TcpMngLoginReqMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
