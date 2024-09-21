package edu.myrza.archke_netty.server.controller

interface Controller {

    fun handle(request: Array<ByteArray>): Array<ByteArray>

}
