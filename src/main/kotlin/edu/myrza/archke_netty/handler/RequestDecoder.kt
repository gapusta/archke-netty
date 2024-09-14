package edu.myrza.archke_netty.handler

import edu.myrza.archke_netty.reader.SimpleStringReader
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class RequestDecoder : ByteToMessageDecoder() {

    private val input = ByteArray(1024)
    private var reader = SimpleStringReader()

    override fun decode(
        ctx: ChannelHandlerContext,
        buff: ByteBuf,
        out: MutableList<Any>
    ) {
        while (buff.readableBytes() > 0) {
            val bytesToProcess = minOf(input.size, buff.readableBytes())

            buff.readBytes(input, 0, bytesToProcess)

            reader.read(input, bytesToProcess)
        }

        if (!reader.done()) return

        out.add(reader.payload())

        reader = SimpleStringReader()
    }

}
