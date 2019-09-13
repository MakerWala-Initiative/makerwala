package com.finlitetech.livekeeping.helpers

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    /*
     *  SimpleDateFormat can be used to control the date/time display format:
     *  E (day of week): 3E or fewer (in text xxx), >3E (in full text)
     *  M (month): M (in number), MM (in number with leading zero)
     *             3M: (in text xxx), >3M: (in full text full)
     *  h (hour): h, hh (with leading zero)
     *  m (minute)
     *  s (second)
     *  a (AM/PM)
     *  H (hour in 0 to 23)
     *  z (time zone)
     *  (there may be more listed under the API - I didn't check)
     */

    const val DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_ZZZ = "yyyy-MM-dd'T'hh:mm:ss.SSS"
    const val DATE_FORMAT_DD_MMM_YYYY = "dd/MMM/yyyy"
    const val DATE_FORMAT_DDMMMYYYY = "dd MMM yyyy"
    const val DATE_FORMAT_MMMYY = "MMM yy"
    const val DATE_FORMAT_DDMMMYY = "dd MMM yy"
    const val DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy"
    const val DATE_FORMAT_YYYY_MM_DD = "yyyy/MM/dd"
    const val DATE_FORMAT_YYYY__MM__DD = "yyyy-MM-dd"
    const val DATE_FORMAT_YYYY__MM = "yyyy-MM"
    const val DATE_FORMAT_YYYY__MM__DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss"
    const val DATE_FORMAT_DD__MM__YYYY = "dd-MM-yyyy"

    fun getFormattedDate(sourceFormat: String, destFormat: String, dateString: String?): String {
        val sourceSimpleDateFormat = SimpleDateFormat(sourceFormat, Locale.US)
        val destSimpleDateFormat = SimpleDateFormat(destFormat, Locale.US)

        return try {
            val date = sourceSimpleDateFormat.parse(dateString)
            destSimpleDateFormat.format(date)
        } catch (e: Exception) {
            Log.e("get formatted ex", e.message)
            ""
        }
    }

    fun getFormattedCalendar(sourceFormat: String, dateString: String?): Calendar {
        val sourceSimpleDateFormat = SimpleDateFormat(sourceFormat, Locale.US)
        return try {
            val date = sourceSimpleDateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar
        } catch (e: Exception) {
            Log.e("get formatted ex", e.message)
            Calendar.getInstance()
        }
    }

    fun getFormattedCalendarRemoveTime(sourceFormat: String, dateString: String?): Calendar {
        val sourceSimpleDateFormat = SimpleDateFormat(sourceFormat, Locale.US)
        return try {
            val date = sourceSimpleDateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY,0)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.SECOND,0)
            calendar
        } catch (e: Exception) {
            Log.e("get formatted ex", e.message)
            Calendar.getInstance()
        }
    }

    fun getFormattedDate(sourceFormat: String, date: Calendar?): String {
        val sourceSimpleDateFormat = SimpleDateFormat(sourceFormat, Locale.US)

        return try {
            sourceSimpleDateFormat.format(date?.time)
        } catch (e: Exception) {
            Log.e("get formatted ex", e.message)
            ""
        }
    }

    fun isUserOlder(date: String, sourceFormat: String, minimumAge: Int): Boolean {
        return try {
            val dob = SimpleDateFormat(sourceFormat, Locale.US)
                    .parse(date)
            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - minimumAge)
            calendar.time.before(dob)
        } catch (e: Exception) {
            false
        }

    }

    /*
     * Return true if date1 come earlier than date2 otherwise false
     * For example, return true
     *              while date1 = 30 july 2018
     *              and   date2 = 2 Aug 2018
     */
    fun compareDate(date1: Date, date2: Date): Boolean {
        return date1.before(date2)
    }

    fun compareWithToday(calendar: Calendar): Boolean {
        return calendar.before(Calendar.getInstance())
    }

    fun compareWithTwoDate(calendar1: Calendar,calendar2: Calendar): Boolean {
        return calendar1.before(calendar2)
    }

    fun differenceInDays(sCalendar: Calendar, eCalendar: Calendar): Int {
        var diff = eCalendar.timeInMillis - sCalendar.timeInMillis
        diff /= (24 * 60 * 60 * 1000)
        return Math.ceil(diff.toDouble()).toInt()
    }

    fun differenceInMonths(sCalendar: Calendar, eCalendar: Calendar): Int {
        var monthsBetween = 0
        var dateDiff = eCalendar.get(Calendar.DAY_OF_MONTH) - sCalendar.get(Calendar.DAY_OF_MONTH)
        if (dateDiff < 0) {
            val borrow = eCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            dateDiff = (eCalendar.get(Calendar.DAY_OF_MONTH) + borrow) - sCalendar.get(Calendar.DAY_OF_MONTH)
            --monthsBetween

            if (dateDiff > 0) {
                ++monthsBetween
            }
        } else {
            ++monthsBetween
        }
        monthsBetween += eCalendar.get(Calendar.MONTH) - sCalendar.get(Calendar.MONTH)
        monthsBetween += (eCalendar.get(Calendar.YEAR) - sCalendar.get(Calendar.YEAR)) * 12
        return monthsBetween
    }

    /*
     * Returns number of days of difference between this two days
     * if date2 is come after date1, then integer returns in -1,-2
     * otherwise in +1,+2,etc.
     */

    fun differenceDate(date1: Date, date2: Date): Int {
        return date1.compareTo(date2)
    }

    /*
     * Below Two Functions
     * Returns time difference between two dates
     */

    fun differenceInMinutes(date1: Date, date2: Date): Int {
        val minutes: Int
        minutes = if (date1.before(date2)) {
            val mills = date2.time - date1.time
            (mills / (1000 * 60)).toInt()
        } else {
            val mills = date1.time - date2.time
            (mills / (1000 * 60)).toInt()
        }
        return minutes
    }

    fun differenceInHourMinutes(date1: Date, date2: Date): String {
        return if (date1.before(date2)) {
            val mills = date2.time - date1.time
            val hours = mills.toInt() / (1000 * 60 * 60)
            val minutes = (mills / (1000 * 60)).toInt() % 60
            "$hours:$minutes"
        } else {
            val mills = date1.time - date2.time
            val hours = mills.toInt() / (1000 * 60 * 60)
            val minutes = (mills / (1000 * 60)).toInt() % 60
            "$hours:$minutes"
        }
    }
}
