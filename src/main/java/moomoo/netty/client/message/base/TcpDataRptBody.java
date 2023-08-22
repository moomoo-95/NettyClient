package moomoo.netty.client.message.base;

import moomoo.netty.client.message.exception.TcpMessageException;
import moomoo.netty.client.util.ByteUtil;

import static moomoo.netty.client.message.base.TcpMessageType.DATA_RPT_BODY_MIN_SIZE;

public class TcpDataRptBody {

    private final long aiifPort;  // unsigned 32 bite
    private final long aiifLen;   // unsigned 32 bite
    private final long aiifSeq;   // unsigned 32 bite
    private final byte[] pcm;

    public TcpDataRptBody(byte[] data) throws TcpMessageException {
        if (data.length >= DATA_RPT_BODY_MIN_SIZE) {
            int index = 0;

            byte[] aiifPortByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, index, aiifPortByteData, ByteUtil.NUM_BYTES_IN_INT, aiifPortByteData.length - ByteUtil.NUM_BYTES_IN_INT);
            this.aiifPort = ByteUtil.bytesToLong(aiifPortByteData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            byte[] aiifLenByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, index, aiifLenByteData, ByteUtil.NUM_BYTES_IN_INT, aiifLenByteData.length - ByteUtil.NUM_BYTES_IN_INT);
            this.aiifLen = ByteUtil.bytesToLong(aiifLenByteData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            byte[] aiifSeqByteData = new byte[ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, index, aiifSeqByteData, ByteUtil.NUM_BYTES_IN_INT, aiifSeqByteData.length - ByteUtil.NUM_BYTES_IN_INT);
            this.aiifSeq = ByteUtil.bytesToLong(aiifSeqByteData, true);
            index += ByteUtil.NUM_BYTES_IN_INT;

            this.pcm = new byte[ (int) aiifLen - ByteUtil.NUM_BYTES_IN_LONG];
            System.arraycopy(data, index, pcm, 0, data.length - index);
        } else {
            this.aiifPort = -1;
            this.aiifLen = -1;
            this.aiifSeq = -1;
            this.pcm = null;
            throw new TcpMessageException("[TCP DATA RPT BODY] Fail to create the Body. Data length: (" + data.length + ")");
        }
    }

    public TcpDataRptBody(long aiifPort, long aiifLen, long aiifSeq, byte[] pcm) {
        this.aiifPort = aiifPort;
        this.aiifLen = aiifLen;
        this.aiifSeq = aiifSeq;
        this.pcm = pcm;
    }

    public byte[] getData() {
        byte[] data = new byte[ByteUtil.NUM_BYTES_IN_INT + getAiifLen()];
        int index = 0;

        byte[] aiifPortByteData = ByteUtil.longToBytes(aiifPort, true);
        System.arraycopy(aiifPortByteData, ByteUtil.NUM_BYTES_IN_INT, data, index, aiifPortByteData.length - ByteUtil.NUM_BYTES_IN_INT);
        index += ByteUtil.NUM_BYTES_IN_INT;

        byte[] aiifLenByteData = ByteUtil.longToBytes(aiifLen, true);
        System.arraycopy(aiifLenByteData, ByteUtil.NUM_BYTES_IN_INT, data, index, aiifLenByteData.length - ByteUtil.NUM_BYTES_IN_INT);
        index += ByteUtil.NUM_BYTES_IN_INT;

        byte[] aiifSeqByteData = ByteUtil.longToBytes(aiifSeq, true);
        System.arraycopy(aiifSeqByteData, ByteUtil.NUM_BYTES_IN_INT, data, index, aiifSeqByteData.length - ByteUtil.NUM_BYTES_IN_INT);
        index += ByteUtil.NUM_BYTES_IN_INT;

        System.arraycopy(pcm, 0, data, index, pcm.length);

        return data;
    }

    public long getAiifPort() {
        return aiifPort;
    }

    public int getAiifLen() {
        return (int) aiifLen;
    }

    public int getAiifSeq() {
        return (int) aiifSeq;
    }

    public byte[] getPcm() {
        return pcm;
    }

    @Override
    public String toString() {
        return "TcpDataRptBody{" +
                "aiifPort=" + aiifPort +
                ", aiifLen=" + aiifLen +
                ", aiifSeq=" + aiifSeq +
                ", pcm=" + pcm.toString() +
                '}';
    }
}

