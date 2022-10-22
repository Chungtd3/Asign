fun (String).hexaToBinary(): String {
    var input: String = this
    var output: String = ""
    for (i in 1..input.length) {
        output += convert(input[i - 1])
    }
    return output
}

fun convert(input: Char): String? = when (input) {
    '0' -> "0000 "
    '1' -> "0001 "
    '2' -> "0010 "
    '3' -> "0011 "
    '4' -> "0100 "
    '5' -> "0101 "
    '6' -> "0110 "
    '7' -> "0111 "
    '8' -> "1000 "
    '9' -> "1001 "
    'A' -> "1010 "
    'B' -> "1011 "
    'C' -> "1100 "
    'D' -> "1101 "
    'E' -> "1110 "
    'F' -> "1111 "
    else -> null
}

fun main() {
    val u = "1A2B"
    println(u.hexaToBinary())
}