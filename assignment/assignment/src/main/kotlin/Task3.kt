import java.sql.DriverManager.println

/*task3: Enter an array of integer numbers a0, a1, a2, ..., an-1. Do not use any other array, print the above array screen in ascending order.*/
fun main() {
    var n: Int
    println("Nhap so luong so nguyen can nhap:")
    n = readLine()!!.toInt()
    println("Nhap cac so nguyen:")
    var list = Array<Int>(n) { 0 }
    var x = 0
    while (x < n) {
        list[x] = readLine()!!.toInt()
        x++
    }
    for (i in 0..n - 2) {
        for (j in i + 1..n - 1) {
            if (list[i] > list[j]) {
                val temp: Int
                temp = list[i]
                list[i] = list[i + 1]
                list[i + 1] = temp
            }
        }
    }
    println("Cac so nguyen trong mang theo thu tu tang dan la:")
    for (i in 0..n - 2)
    {
        println(list[i].toString())
        println(",")
    }
    println(list[n-1].toString())
}