package edu.myrza.archke_netty.server.command

import edu.myrza.archke_netty.server.db.KeyValueStorage

class SetCommand(private val storage: KeyValueStorage) : Command {

    override fun command(): String = "SET"

    override fun handle(args: Array<ByteArray>): Array<ByteArray> {
        if (args.size != 3) return arrayOf("-INVALID_ARGUMENTS\r\n".toByteArray(Charsets.US_ASCII))

        storage.set(args[1], args[2])

        return arrayOf("+OK\r\n".toByteArray(Charsets.US_ASCII))
    }

}
