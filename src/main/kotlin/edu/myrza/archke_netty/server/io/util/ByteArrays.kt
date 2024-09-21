package edu.myrza.archke_netty.server.io.util

data class ByteArrays(val arrays: Array<ByteArray>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ByteArrays

        return arrays.contentDeepEquals(other.arrays)
    }

    override fun hashCode(): Int {
        return arrays.contentDeepHashCode()
    }

}