fun String.turnOff(byte: Int, bit: Int) = StringBuilder(this.toInt(16).toString(2)).apply {
    set(length - (4 - byte) * 8 - bit, '0')
}.toString().toInt(2).toString(16)

fun main() {
    println("12345678".turnOff(2, 5))
}

