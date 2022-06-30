package hu.bme.aut.nytimes.util

enum class Period {
    LAST_DAY, LAST_7_DAYS, LAST_30_DAYS
}

fun periodToDays(period: Period): Int{
    return when(period){
        Period.LAST_DAY -> 1
        Period.LAST_7_DAYS -> 7
        Period.LAST_30_DAYS -> 30
    }
}