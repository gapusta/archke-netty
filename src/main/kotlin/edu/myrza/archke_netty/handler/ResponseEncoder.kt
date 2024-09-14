package edu.myrza.archke_netty.handler

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class ResponseEncoder : MessageToByteEncoder<ByteArray>() {

    override fun allocateBuffer(
        ctx: ChannelHandlerContext,
        msg: ByteArray,
        preferDirect: Boolean
    ): ByteBuf {
        return Unpooled.buffer(msg.size)
    }

    override fun encode(
        ctx: ChannelHandlerContext,
        message: ByteArray,
        out: ByteBuf
    ) {
        out.writeBytes(message)
    }

}
