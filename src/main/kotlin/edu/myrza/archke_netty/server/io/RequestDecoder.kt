package edu.myrza.archke_netty.server.io

import edu.myrza.archke.server.controller.parser.Reader
import edu.myrza.archke_netty.server.io.util.ByteArrays
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class RequestDecoder : ByteToMessageDecoder() {

    private val input = ByteArray(1024)
    private var reader = Reader()

    override fun decode(
        ctx: ChannelHandlerContext,
        buff: ByteBuf,
        out: MutableList<Any> // if 'out' is empty, next handler will not be triggered
    ) {
        while (buff.readableBytes() > 0) {
            val bytesToProcess = minOf(input.size, buff.readableBytes())

            buff.readBytes(input, 0, bytesToProcess)

            reader.read(input, 0, bytesToProcess)
        }

        if (!reader.done()) return

        val request = ByteArrays(reader.payload())

        out.add(request)

        reader = Reader()
    }

}
