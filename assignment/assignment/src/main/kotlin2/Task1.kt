fun (Int).toHexString(): String {
    var input: Int = this
    var result: String = ""

    var hexTable = "0123456789ABCDEF"

    do {
        var tmp: Int = input % 16
        result += hexTable[tmp]
        input /= 16
    }
    while (input != 0)
    return result.reversed()
}
fun main() {
    val v = 778
    println(v.toHexString())
}