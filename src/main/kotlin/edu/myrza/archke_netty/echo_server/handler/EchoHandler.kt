package edu.myrza.archke_netty.echo_server.handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class EchoHandler : ChannelInboundHandlerAdapter() {

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        super.handlerAdded(ctx)
//        println("Handler initialized")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        super.handlerRemoved(ctx)
//        println("Handler released")
    }

    override fun channelRead(ctx: ChannelHandlerContext, message: Any) {
        message as String
        println("Incoming message : $message")
        // starts the channel's outbound handlers
        ctx.writeAndFlush("+${message}\r\n".toByteArray(Charsets.US_ASCII))
    }

}
