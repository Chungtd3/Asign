import com.sun.tools.jdeprscan.Pretty.print
import java.sql.DriverManager.println

fun main()
{
    for (x in 10..200)
    {
        if (x % 7 == 0 && x % 5 != 0)
        {
            println(x.toString())
            println(",")
        }
    }
}