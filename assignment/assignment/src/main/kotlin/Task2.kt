import java.sql.DriverManager.println

/*Task2: Write a program that input a two-digit integer number. Convert and printout the value of inputted number in binary and hexadecimal.*/
fun (Int).toHex(): String {
    var x: Int = this
    var result: String = ""
    var hexTable: String = "123456789ABCDEF"
    while (x > 0) {
        var phandu = x % 16
        result += hexTable[phandu - 1]
        x /= 16 //x=x/16
    }
    return result.reversed()
}

fun (String).HextoBinary(): String {
    var x: String = this
    var result: String = ""
    for (i in 1..x.length) {
        result += convert(x[i - 1])
    }
    return result
}

fun convert(input: Char): String? {
    return when (input) {
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
}
fun main() {
    var x: Int = 1245
    println(x.toHex())
}