package edu.myrza.archke_netty.server.io

import edu.myrza.archke.server.controller.Controller
import edu.myrza.archke_netty.server.io.util.ByteArrays
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class Processor(private val controller: Controller) : ChannelInboundHandlerAdapter() {

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        super.handlerAdded(ctx)
        println("Client handler initialized")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        super.handlerRemoved(ctx)
        println("Client handler released")
    }

    override fun channelRead(ctx: ChannelHandlerContext, request: Any) {
        request as ByteArrays

        val result = controller.handle(request.arrays).flatten()

        ctx.writeAndFlush(result) // starts the channel's outbound handlers
    }

    private fun Array<ByteArray>.flatten(): ByteArray {
        val size = this.map { it.size }.reduce { acc, i -> acc + i }
        val result = ByteArray(size)

        var pos = 0
        for (arr in this) {
            arr.copyInto(result, pos)
            pos += arr.size
        }

        return result
    }

}
