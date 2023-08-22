package moomoo.netty.client.message.base;

import moomoo.netty.client.message.exception.TcpMessageException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.FRAME_START;
import static moomoo.netty.client.message.base.TcpMessageType.HEADER_SIZE;

public class TcpMessageHeader {

    private final int length;       // unsigned 16 bite
    private final int msgId;        // unsigned 16 bite
    private final int systemId;     // unsigned 16 bite

    public TcpMessageHeader(byte[] data) throws TcpMessageException {
        if (data.length == HEADER_SIZE) {
            int index = 0;

            byte[] frameStartByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, frameStartByteData, ByteUtil.NUM_BYTES_IN_SHORT, frameStartByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
            int checkMsg = ByteUtil.bytesToInt(frameStartByteData, true);
            if (checkMsg != FRAME_START) {
                this.length = -1;
                this.msgId = -1;
                this.systemId = -1;
                throw new TcpMessageException("[TCP Message Header] Fail to create the header. FrameStart: (" + checkMsg + ")");
            }
            index += ByteUtil.NUM_BYTES_IN_SHORT;

            byte[] lengthByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, lengthByteData, ByteUtil.NUM_BYTES_IN_SHORT, lengthByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
            this.length = ByteUtil.bytesToInt(lengthByteData, true);
            index += ByteUtil.NUM_BYTES_IN_SHORT;

            byte[] msgIdByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, msgIdByteData, ByteUtil.NUM_BYTES_IN_SHORT, msgIdByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
            this.msgId = ByteUtil.bytesToInt(msgIdByteData, true);
            index += ByteUtil.NUM_BYTES_IN_SHORT;

            byte[] systemIdByteData = new byte[ByteUtil.NUM_BYTES_IN_INT];
            System.arraycopy(data, index, systemIdByteData, ByteUtil.NUM_BYTES_IN_SHORT, systemIdByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
            this.systemId = ByteUtil.bytesToInt(systemIdByteData, true);
        } else {
            this.length = -1;
            this.msgId = -1;
            this.systemId = -1;
            throw new TcpMessageException("[TCP Message Header] Fail to create the header. Data length: (" + data.length + ")");
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
        System.arraycopy(frameStartByteData, ByteUtil.NUM_BYTES_IN_SHORT, data, index, frameStartByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
        index += ByteUtil.NUM_BYTES_IN_SHORT;

        byte[] lengthByteData = ByteUtil.intToBytes(length, true);
        System.arraycopy(lengthByteData, ByteUtil.NUM_BYTES_IN_SHORT, data, index, lengthByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
        index += ByteUtil.NUM_BYTES_IN_SHORT;

        byte[] msgIdByteData = ByteUtil.intToBytes(msgId, true);
        System.arraycopy(msgIdByteData, ByteUtil.NUM_BYTES_IN_SHORT, data, index, msgIdByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);
        index += ByteUtil.NUM_BYTES_IN_SHORT;

        byte[] systemIdByteData = ByteUtil.intToBytes(systemId, true);
        System.arraycopy(systemIdByteData, ByteUtil.NUM_BYTES_IN_SHORT, data, index, systemIdByteData.length - ByteUtil.NUM_BYTES_IN_SHORT);

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
        return "TcpMessageHeader{" +
                "length=" + length +
                ", msgId=" + msgId +
                ", systemId=" + systemId +
                '}';
    }
}
