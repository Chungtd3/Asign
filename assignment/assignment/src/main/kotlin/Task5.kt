import java.sql.DriverManager.println

fun main() {
    do {
        var month: Int
        var year: Int
        do {
            println("Nhap thang can tim:")
            month = readLine()!!.toInt()
        } while (month < 1 || month > 12)
        do {
            println("Nhap nam can tim:")
            year = readLine()!!.toInt()
        } while (year < 0)
        fun checkNamNhuan() {
            if ((year % 4 == 0) && (year % 100 != 0)) {
                println("Thang 2 co 29 ngay")
            } else {
                println("thang 2 co 28 ngay")
            }
        }
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> println("thang $month co 31 ngay")
            4, 6, 9, 11 -> println("thang $month co 30 ngay")
            2 -> checkNamNhuan()
        }
    } while (true)
}