package edu.myrza.archke_netty

import edu.myrza.archke.server.Archke
import edu.myrza.archke_netty.server.config.ArchkeConfig
import edu.myrza.archke_netty.echo_server.handler.EchoHandler
import edu.myrza.archke_netty.echo_server.handler.RequestDecoder
import edu.myrza.archke_netty.echo_server.handler.ResponseEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

fun main() {
    archkeServer()
//    echoServer()
}

fun archkeServer() {
    val archke = Archke(
        ArchkeConfig(port = 9999)
    )
    archke.start()
}

fun echoServer() {
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
            )
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        val future = bootstrap.bind(9999).sync()
        future.channel().closeFuture().sync()
    } finally {
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}
