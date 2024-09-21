package edu.myrza.archke_netty.server.command

import edu.myrza.archke_netty.server.command.Constants.NULL
import edu.myrza.archke_netty.server.db.KeyValueStorage

class GetCommand(private val storage: KeyValueStorage) : Command {

    override fun command(): String = "GET"

    override fun handle(args: Array<ByteArray>): Array<ByteArray> {
        if (args.size != 2) return arrayOf("-INVALID_ARGUMENTS\r\n".toByteArray(Charsets.US_ASCII))

        val key = args[1]
        val value = storage.get(key) ?: return arrayOf(NULL)
        val header = "$${value.size}\r\n".toByteArray(Charsets.US_ASCII)

        return arrayOf(header, value)
    }

}
