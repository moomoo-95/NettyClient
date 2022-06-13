package moomoo.netty.client.message;

import moomoo.netty.client.message.base.TcpLoginReqBody;
import moomoo.netty.client.message.base.TcpMessageHeader;
import moomoo.netty.client.message.exception.TcpMessageException;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpLoginReqMessage {

    private final TcpMessageHeader header;
    private final TcpLoginReqBody body;

    public TcpLoginReqMessage(byte[] data) throws TcpMessageException {
        if (data.length == HEADER_SIZE + LOGIN_REQ_BODY_SIZE) {
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
            throw new TcpMessageException("[TCP LOGIN REQ] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpLoginReqMessage(int systemId, long processArg, long index) {
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
        return "TcpLoginReqMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
