package edu.myrza.archke_netty.server.io

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class ResponseEncoder : MessageToByteEncoder<ByteArray>() {

    override fun allocateBuffer(
        ctx: ChannelHandlerContext,
        response: ByteArray,
        preferDirect: Boolean
    ): ByteBuf {
        return Unpooled.buffer(response.size)
    }

    override fun encode(
        ctx: ChannelHandlerContext,
        message: ByteArray,
        out: ByteBuf
    ) {
        out.writeBytes(message)
    }

}
