package moomoo.netty.client.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class NettyUtil {

    private NettyUtil() {
    }

    public static ByteBuf createPooledHeapByteBuf(int capacity){
        return PooledByteBufAllocator.DEFAULT.heapBuffer(capacity);
    }

    public static ByteBuf createPooledDirectByteBuf(int capacity){
        return PooledByteBufAllocator.DEFAULT.directBuffer(capacity);
    }

    public static ByteBuf createUnPooledHeapByteBuf(int capacity){
        return Unpooled.buffer(capacity);
    }

    public static ByteBuf createUnPooledDirectByteBuf(int capacity){
        return Unpooled.directBuffer(capacity);
    }
}
