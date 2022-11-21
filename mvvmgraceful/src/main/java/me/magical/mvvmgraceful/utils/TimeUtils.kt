package me.magical.mvvmgraceful.utils

import android.text.TextUtils
import java.lang.ref.SoftReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    /**
     * 获取当前日期字符串
     *
     * @return
     */
    @JvmStatic
    fun getDateString(format: String?): String? {
        val formatter = SimpleDateFormat(format, Locale.CHINA)
        val curDate = Date()
        return formatter.format(curDate)
    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    @JvmStatic
    fun dateStringToTimeStamp(time: String?, format: String?): Long {
        val simpleDateFormat = SimpleDateFormat(format)
        val date = simpleDateFormat.parse(time)
        return date.time
    }

    /**
     * 将时间戳转为时间字符串
     *
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    @JvmStatic
    fun timeStampToDateString(time: Long?, format: String?): String? {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(time)
    }

    /**
     * 判断时间戳是否在当前时间的前几天内
     *
     * @param days
     * @param time
     * @return
     */
    @JvmStatic
    fun belongCalendar(days: Int, time: Long): Boolean {
        val dateNow = Date()
        val cl = Calendar.getInstance()
        cl.time = dateNow
        cl[Calendar.DATE] = cl[Calendar.DATE] - days
        return cl.timeInMillis > time
    }

    /**
     * 判断字符串是否是日期类型
     *
     * @param datevalue
     * @param dateFormat
     * @return
     */
    @JvmStatic
    fun isDateString(datevalue: String, dateFormat: String?): Boolean {
        return if (TextUtils.isEmpty(datevalue)) {
            false
        } else try {
            val fmt = SimpleDateFormat(dateFormat)
            val dd = fmt.parse(datevalue)
            datevalue == fmt.format(dd)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 时间格式化
     * @param date
     * @param pattern
     * @return
     */
    @JvmStatic
    fun formatDate(date: Date?, pattern: String?): String? {
        requireNotNull(date) { "date is null" }
        requireNotNull(pattern) { "pattern is null" }
        val formatter: SimpleDateFormat = formatFor(pattern)
        return formatter.format(date)
    }

    @JvmStatic
    private val THREADLOCAL_FORMATS: ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>?> =
        object : ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>?>() {
            override fun initialValue(): SoftReference<Map<String, SimpleDateFormat>>? {
                return SoftReference(
                    HashMap()
                )
            }
        }

    @JvmStatic
    private fun formatFor(pattern: String): SimpleDateFormat {
        val ref: SoftReference<Map<String, SimpleDateFormat>>? =
            THREADLOCAL_FORMATS.get()
        var formats = ref!!.get()?.toMutableMap()
        if (formats == null) {
            formats = HashMap()
            THREADLOCAL_FORMATS.set(
                SoftReference<Map<String, SimpleDateFormat>>(
                    formats
                )
            )
        }
        var format = formats[pattern]
        if (format == null) {
            format = SimpleDateFormat(pattern, Locale.CHINA)
            format.timeZone = TimeZone.getTimeZone("GMT+8")
            formats[pattern] = format
        }
        return format
    }
}