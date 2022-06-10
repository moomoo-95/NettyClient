package moomoo.netty.client.message.base;

import moomoo.netty.client.message.exception.MessageHeaderException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpMessageHeader {

    private final int length;
    private final int msgId;
    private final int systemId;


    public TcpMessageHeader(byte[] data) throws MessageHeaderException {
        if (data.length >= HEADER_SIZE) {
            int index = 0;

            byte[] frameStartByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, frameStartByteData, 0, frameStartByteData.length);
            int checkMsg = ByteUtil.bytesToInt(frameStartByteData, true);
            if (checkMsg != FRAME_START) {
                this.length = -1;
                this.msgId = -1;
                this.systemId = -1;
                throw new MessageHeaderException("[TCP Message Header] Fail to create the header. FrameStart: (" + checkMsg + ")");
            }
            index += frameStartByteData.length;

            byte[] lengthByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, lengthByteData, 0, lengthByteData.length);
            this.length = ByteUtil.bytesToInt(lengthByteData, true);
            index += lengthByteData.length;

            byte[] msgIdByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, msgIdByteData, 0, msgIdByteData.length);
            this.msgId = ByteUtil.bytesToInt(msgIdByteData, true);
            index += msgIdByteData.length;

            byte[] systemIdByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, systemIdByteData, 0, systemIdByteData.length);
            this.systemId = ByteUtil.bytesToInt(systemIdByteData, true);
        } else {
            this.length = -1;
            this.msgId = -1;
            this.systemId = -1;
            throw new MessageHeaderException("[TCP Message Header] Fail to create the header. Data length: (" + data.length + ")");
        }

    }

    public TcpMessageHeader(int length, int msgId, int systemId) {
        this.length = length;
        this.msgId = msgId;
        this.systemId = systemId;
    }

    public byte[] getData() {
        byte[] data = new byte[HEADER_SIZE];
        int index = 0;

        byte[] frameStartByteData = ByteUtil.intToBytes(FRAME_START, true);
        System.arraycopy(frameStartByteData, 0, data, index, frameStartByteData.length);
        index += frameStartByteData.length;

        byte[] lengthByteData = ByteUtil.intToBytes(length, true);
        System.arraycopy(lengthByteData, 0, data, index, lengthByteData.length);
        index += lengthByteData.length;

        byte[] msgIdByteData = ByteUtil.intToBytes(msgId, true);
        System.arraycopy(msgIdByteData, 0, data, index, msgIdByteData.length);
        index += msgIdByteData.length;

        byte[] systemIdByteData = ByteUtil.intToBytes(systemId, true);
        System.arraycopy(systemIdByteData, 0, data, index, systemIdByteData.length);

        return data;
    }

    public int getLength() {
        return length;
    }

    public int getMsgId() {
        return msgId;
    }

    public int getSystemId() {
        return systemId;
    }

    @Override
    public String toString() {
        return "TcpMngHeader{" +
                "length=" + length +
                ", msgId=" + msgId +
                ", systemId=" + systemId +
                '}';
    }
}
