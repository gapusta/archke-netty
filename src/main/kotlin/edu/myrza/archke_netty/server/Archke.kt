package edu.myrza.archke.server

import edu.myrza.archke_netty.server.config.ArchkeConfig
import edu.myrza.archke_netty.server.controller.ControllerImpl
import edu.myrza.archke_netty.server.db.KeyValueStorageImpl
import edu.myrza.archke_netty.server.command.*
import edu.myrza.archke_netty.server.io.Processor
import edu.myrza.archke_netty.server.io.RequestDecoder
import edu.myrza.archke_netty.server.io.ResponseEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.socket.SocketChannel

class Archke(private val archkeConfig: ArchkeConfig) {

    fun start() {
        // SERVICE LAYER
        val service = KeyValueStorageImpl()

        // CONTROLLER LAYER
        val commands = mutableMapOf<String, Command>().apply {
            SetCommand(service).also { this[it.command()] = it }
            GetCommand(service).also { this[it.command()] = it }
            DelCommand(service).also { this[it.command()] = it }
            ExistCommand(service).also { this[it.command()] = it }
        }
        val controller = ControllerImpl(commands)

        // IO LAYER
        val group = EpollEventLoopGroup(1)
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(group)
                .channel(EpollServerSocketChannel::class.java)
                .childHandler(
                    object : ChannelInitializer<SocketChannel>() {
                        override fun initChannel(channel: SocketChannel) {
                            channel.pipeline().addLast(RequestDecoder(), ResponseEncoder(), Processor(controller))
                        }
                    }
                )
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

            val future = bootstrap.bind(archkeConfig.port).sync()
            future.channel().closeFuture().sync()
        } finally {
            group.shutdownGracefully()
        }
    }

}
