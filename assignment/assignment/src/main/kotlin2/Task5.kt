fun checkAvailPAN(PAN: String):Boolean
{
    var count = 0
    for (i in PAN)
    {
        if (i in '0'..'9') count++
        else return false
    }
    if (count in 12..19) return true
    return false
}

fun checkLuhn(PAN: String):Boolean
{
    var checkSum = ""
    var sum = 0

    for (i in 0 .. PAN.length-1)
    {
        if (i%2 == 0) checkSum+=PAN[PAN.length-1- i]
        else checkSum+=PAN[PAN.length-1- i].digitToInt()*2
    }
    for (i in 0.. checkSum.length-1)
    {
        sum += checkSum[i].digitToInt()
    }
    println(sum)
    if (sum%10==0) return true
    return false
}

fun cardType(x: String):Int
{
    val n = x.length
    if(x[0]=='4') return 1
    else if((x[0]=='5'&&x[1]=='0'&&x[n-2]=='6'&&x[n-1]=='9')||(x[0]=='2'&&x[1]=='2'&&x[2]=='2'&&x[3]=='1'&&x[n-4]=='2'&&x[n-3]=='7'&&x[n-2]=='2'&&x[n-1]=='0')) return 2
    else if((x[0]=='3'&&x[1]=='5'&&x[2]=='2'&&x[3]=='8'&&x[n-4]=='3'&&x[n-3]=='5'&&x[n-2]=='8'&&x[n-1]=='9')) return 3
    else return 4
}

fun program(PAN: String)
{
    if (checkAvailPAN(PAN) == false) {
        println("error 1")
    }
    if (checkLuhn(PAN) == false)
    {
        println("error 2")

    }
    when (cardType(PAN))
    {
        1 -> println("VISA Card")
        2-> println("Master CardMaster Card")
        3-> println("JCB Card")
        4-> println("Unknown Card")
    }
}

fun main() {
    program("18")
}