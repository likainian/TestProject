/**
 *
 */
package com.example.testproject.util


import java.text.SimpleDateFormat

/**
 * Created by mike.li.
 */
object TimeUtil {
    const val FORMAT_YEAR_MONTH_DAY_TIME = "yyyy-MM-dd HH:mm"
    private var sdf: SimpleDateFormat? = null

    init {
        sdf = SimpleDateFormat(FORMAT_YEAR_MONTH_DAY_TIME)
    }

    fun longToString(time: Long, formatType: String): String {
        if (time == 0L) return ""
        sdf!!.applyPattern(formatType)
        return sdf!!.format(time)
    }
}
