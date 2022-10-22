fun fiboArr(number: Int): MutableList<Int> {
    val result: MutableList<Int> = mutableListOf<Int>(1, 1)
    var f0 = 1
    var f1 = 1
    var f2 = 0
    for (i in 3..number ) {
        f2 = f0 + f1
        result.add(f2)
        f0 = f1
        f1 = f2
    }
    return result
}

fun main() {
    for ( i in fiboArr(20)) println(i)
}