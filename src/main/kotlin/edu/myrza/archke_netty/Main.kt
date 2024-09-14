package edu.myrza.archke_netty

import edu.myrza.archke_netty.handler.EchoHandler
import edu.myrza.archke_netty.handler.RequestDecoder
import edu.myrza.archke_netty.handler.ResponseEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

fun main() {
    println("main started...")
    val bossGroup = NioEventLoopGroup()
    val workerGroup = NioEventLoopGroup()

    try {
        val bootstrap = ServerBootstrap()
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(
                object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(channel: SocketChannel) {
                        channel.pipeline().addLast(RequestDecoder(), ResponseEncoder(), EchoHandler())
                    }
                }
            ).option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        val future = bootstrap.bind(9999).sync()
        future.channel().closeFuture().sync()
    } finally {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}
