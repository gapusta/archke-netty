package edu.myrza.archke_netty.server.command

interface Command {

    fun command(): String

    fun handle(args: Array<ByteArray>): Array<ByteArray>

}
