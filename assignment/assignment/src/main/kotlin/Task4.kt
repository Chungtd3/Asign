import java.sql.DriverManager.println

fun format(input: String): Pair<String, Int> {
    var output = input
    var count = 1
    if (input[0] in 'a'..'z') {
        output = StringBuilder(input).apply {
            set(0, input[0].uppercaseChar())
        }.toString()

    }

    for (i in 0 until input.length) {
        when (output[i]) {
            ' ' -> count++
            '.' -> if (output[i + 2] in 'a'..'z') {
                output = StringBuilder(output).apply {
                    set(i + 2, output[i + 2].uppercaseChar())
                }.toString()
            }
        }
    }
    return Pair(output, count)
}

fun main() {
    val s :String
    println("Nhap chuoi ki tu:")
    s= readln()
    println(format(s).first)
    println(format(s).second)
}