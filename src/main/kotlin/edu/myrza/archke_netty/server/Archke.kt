package edu.myrza.archke.server

import edu.myrza.archke.server.command.*
import edu.myrza.archke.server.config.ArchkeConfig
import edu.myrza.archke.server.controller.ControllerImpl
import edu.myrza.archke.server.db.KeyValueStorageImpl
import edu.myrza.archke_netty.server.io.Processor
import edu.myrza.archke_netty.server.io.RequestDecoder
import edu.myrza.archke_netty.server.io.ResponseEncoder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

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
        val group = NioEventLoopGroup(1)
        try {
            val bootstrap = ServerBootstrap()
            bootstrap.group(group)
                .channel(NioServerSocketChannel::class.java)
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
