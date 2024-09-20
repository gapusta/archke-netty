package edu.myrza.archke_netty.echo_server.reader

import edu.myrza.archke_netty.echo_server.reader.SimpleStringReader.State.*

class SimpleStringReader {

    private var state = READ
    private val builder = StringBuffer()

    fun done() = state == DONE

    fun payload() = builder.toString()

    fun read(chunk: ByteArray, occupied: Int) {
        for (idx in 0 until occupied) {
            val byte = chunk[idx]

            when (state) {
                READ -> {
                    if (byte != SIMPLE_STR) throw IllegalStateException("+ was expected")

                    state = READ_DATA
                }
                READ_DATA -> {
                    if (byte == CR) continue
                    if (byte == LF) {
                        state = DONE
                        continue
                    }
                    builder.append(byte.toInt().toChar())
                }
                DONE -> break
            }
        }
    }

    private enum class State {
        READ,
        READ_DATA,
        DONE
    }

    companion object {
        private const val SIMPLE_STR = 0x2b.toByte() // '+'
        private const val CR = 0x0d.toByte() // '\r'
        private const val LF = 0x0a.toByte() // '\n'
    }

}
