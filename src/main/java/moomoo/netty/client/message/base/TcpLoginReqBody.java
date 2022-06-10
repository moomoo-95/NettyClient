package moomoo.netty.client.message.base;

import moomoo.netty.client.message.exception.MessageHeaderException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpLoginReqBody {

    private final long processArg;
    private final long index;

    public TcpLoginReqBody(byte[] data) throws MessageHeaderException {
        if (data.length >= LOGIN_REQ_BODY_SIZE) {
            int idx = 0;

            byte[] processArgByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, idx, processArgByteData, 0, processArgByteData.length);
            this.processArg = ByteUtil.bytesToLong(processArgByteData, true);
            idx += processArgByteData.length;

            byte[] indexByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, idx, indexByteData, 0, indexByteData.length);
            this.index = ByteUtil.bytesToLong(indexByteData, true);
        } else {
            this.processArg = -1;
            this.index = -1;
            throw new MessageHeaderException("[TCP LOGIN REQ BODY] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpLoginReqBody(long processArg, long index) {
        this.processArg = processArg;
        this.index = index;
    }

    public byte[] getData() {
        byte[] data = new byte[LOGIN_REQ_BODY_SIZE];
        int idx = 0;

        byte[] processArgByteData = ByteUtil.longToBytes(processArg, true);
        System.arraycopy(processArgByteData, 0, data, idx, processArgByteData.length);
        idx += processArgByteData.length;

        byte[] indexByteData = ByteUtil.longToBytes(index, true);
        System.arraycopy(indexByteData, 0, data, idx, indexByteData.length);

        return data;
    }

    public long getProcessArg() {
        return processArg;
    }

    public long getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "TcpMngLoginReqBody{" +
                "processArg=" + processArg +
                ", index=" + index +
                '}';
    }
}
