package moomoo.netty.client.message.base;

import moomoo.netty.client.message.exception.TcpMessageException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.*;

public class TcpLoginReqBody {

    private final long processArg;  // unsigned 32 bite
    private final long index;       // unsigned 32 bite

    public TcpLoginReqBody(byte[] data) throws TcpMessageException {
        if (data.length == LOGIN_REQ_BODY_SIZE) {
            int idx = 0;

            byte[] processArgByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, idx, processArgByteData, ByteUtil.NUM_BYTES_IN_INT, processArgByteData.length - ByteUtil.NUM_BYTES_IN_INT);
            this.processArg = ByteUtil.bytesToLong(processArgByteData, true);
            idx += ByteUtil.NUM_BYTES_IN_INT;

            byte[] indexByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, idx, indexByteData, ByteUtil.NUM_BYTES_IN_INT, indexByteData.length - ByteUtil.NUM_BYTES_IN_INT);
            this.index = ByteUtil.bytesToLong(indexByteData, true);
        } else {
            this.processArg = -1;
            this.index = -1;
            throw new TcpMessageException("[TCP LOGIN REQ BODY] Fail to create the Body. Data length: (" + data.length + ")");
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
        System.arraycopy(processArgByteData, ByteUtil.NUM_BYTES_IN_INT, data, idx, processArgByteData.length - ByteUtil.NUM_BYTES_IN_INT);
        idx += ByteUtil.NUM_BYTES_IN_INT;

        byte[] indexByteData = ByteUtil.longToBytes(index, true);
        System.arraycopy(indexByteData, ByteUtil.NUM_BYTES_IN_INT, data, idx, indexByteData.length - ByteUtil.NUM_BYTES_IN_INT);

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
        return "TcpLoginReqBody{" +
                "processArg=" + processArg +
                ", index=" + index +
                '}';
    }
}
