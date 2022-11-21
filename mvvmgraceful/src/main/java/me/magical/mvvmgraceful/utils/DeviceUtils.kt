package me.magical.mvvmgraceful.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

object DeviceUtils {


    /**
     * 获取应用版本号
     *
     * @param mContext
     * @return
     */
    fun getAppVersionCode(mContext: Context): Int {
        return try {
            val manager = mContext.packageManager
            val info = manager.getPackageInfo(mContext.packageName, 0)
            info.versionCode
        } catch (e: java.lang.Exception) {
            -1
        }
    }

    /**
     * 是否为模拟器
     */
    @JvmStatic
    fun isVirtual(): Boolean {
        return Build.FINGERPRINT.contains("generic") || Build.PRODUCT.contains("sdk")
    }

    /**
     * 判断是否是pad设备
     */
    @JvmStatic
    fun isTablet(context: Context): Boolean {
        return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为手机设备
     */
    @JvmStatic
    fun isPhone(context: Context):Boolean{
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 获取设备的通用唯一标识符，Android O中，对于设备上的每个应用和每个用户，Android ID（Settings.Secure.ANDROID_ID 或 SSAID）
     */
    @JvmStatic
    fun getAndroidId(context: Context):String?{
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取 IMSI 码, 设备的国际移动用户识别码
     * <p>需添加权限
     * {@code <uses-com.common.permission android:name="android.com.common.permission.READ_PHONE_STATE" />}</p>
     * Android 10及以上版本获取为空
     * @return IMSI 码
     */
    @JvmStatic
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getIMSI(context: Context):String?{
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            tm.subscriberId
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取 IMEI 码, 设备的国际移动设备身份码
     *
     * 需添加权限
     * <uses-com.common.permission android:name="android.com.common.permission.READ_PHONE_STATE" />`
     *  Android 10及以上版本获取为空
     * @return IMEI 码
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getIMEI(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.deviceId == null) "" else tm.deviceId
    }

    /**
     * 设备序列号
     */
    @JvmStatic
    fun getSerial(context: Context):String?{
         return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            Build.SERIAL
        }
    }

    /**
     * 获取设备有效 MAC 地址
     *
     * 需添加权限 `<uses-com.common.permission android:name="android.com.common.permission.ACCESS_WIFI_STATE" />`
     *
     * 需添加权限 `<uses-com.common.permission android:name="android.com.common.permission.INTERNET" />`
     *
     * @return MAC 地址
     */
    @JvmStatic
    fun getMacAddress(context: Context): String? {
        var macAddress: String? =
            getMacAddressByWifiInfo(context)
        if ("02:00:00:00:00:00" != macAddress) {
            return macAddress
        }
        macAddress = getMacAddressByNetworkInterface()
        return if ("02:00:00:00:00:00" != macAddress) {
            macAddress
        } else "please open wifi"
    }

    /**
     * 获取设备 Wifi MAC 地址
     *
     * 需添加权限 `<uses-com.common.permission android:name="android.com.common.permission.ACCESS_WIFI_STATE" />`
     *
     * @return MAC 地址
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getMacAddressByWifiInfo(context: Context): String? {
        try {
            @SuppressLint("WifiManagerLeak") val wifi =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifi.connectionInfo
            if (info != null) return info.macAddress
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }

    /**
     * 获取设备 蜂窝 MAC 地址
     *
     * 需添加权限 `<uses-com.common.permission android:name="android.com.common.permission.INTERNET" />`
     *
     * @return MAC 地址
     */
    @JvmStatic
    fun getMacAddressByNetworkInterface(): String {
        try {
            val nis: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (ni in nis) {
                if (!ni.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.isNotEmpty()) {
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        res1.append(String.format("%02x:", b))
                    }
                    return res1.deleteCharAt(res1.length - 1).toString()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "02:00:00:00:00:00"
    }


    /**
     * 获取本机IPv4地址
     *
     * @param context
     * @return 本机IPv4地址；null：无网络连接
     */
    @JvmStatic
    fun getIpAddress(context: Context): String? {
        // 获取WiFi服务
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // 判断WiFi是否开启
        return if (wifiManager.isWifiEnabled) {
            // 已经开启了WiFi
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            intToIp(ipAddress)
        } else {
            // 未开启WiFi
            getIpAddress()
        }
    }

    @JvmStatic
    private fun intToIp(ipAddress: Int): String {
        return (ipAddress and 0xFF).toString() + "." +
                (ipAddress shr 8 and 0xFF) + "." +
                (ipAddress shr 16 and 0xFF) + "." +
                (ipAddress shr 24 and 0xFF)
    }

    /**
     * 获取本机IPv4地址
     *
     * @return 本机IPv4地址；null：无网络连接
     */
    @JvmStatic
    private fun getIpAddress(): String? {
        return try {
            var networkInterface: NetworkInterface
            var inetAddress: InetAddress
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                networkInterface = en.nextElement()
                val enumIpAddr = networkInterface.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
            null
        } catch (ex: SocketException) {
            ex.printStackTrace()
            null
        }
    }

    /**
     * 获取设备型号
     *
     * 如 MI2SC
     *
     * @return 设备型号
     */
    @JvmStatic
    fun getModel(): String {
        var model = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

    /**
     * 获取设备厂商
     *
     * 如 HUAWEI
     *
     * @return 设备厂商
     */
    @JvmStatic
    fun getManufacturer(): String? {
        return Build.MANUFACTURER
    }

    /**
     * 获取CPU的信息
     *
     * @return
     */
    @JvmStatic
    fun getCpuInfo(): String {
        var cpuInfo = ""
        try {
            if (File("/proc/cpuinfo").exists()) {
                val fr = FileReader("/proc/cpuinfo")
                val localBufferedReader = BufferedReader(fr, 8192)
                cpuInfo = localBufferedReader.readLine()
                localBufferedReader.close()
                if (cpuInfo != null) {
                    cpuInfo = cpuInfo.split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
                        .split(" ".toRegex()).toTypedArray()[0]
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return cpuInfo
    }

    /**
     * 获取移动终端类型
     *
     * @return 手机制式
     *
     *  * [TelephonyManager.PHONE_TYPE_NONE] : 0 手机制式未知
     *  * [TelephonyManager.PHONE_TYPE_GSM] : 1 手机制式为 GSM，移动和联通
     *  * [TelephonyManager.PHONE_TYPE_CDMA] : 2 手机制式为 CDMA，电信
     *  * [TelephonyManager.PHONE_TYPE_SIP] : 3
     *
     */
    @JvmStatic
    fun getPhoneType(context: Context): Int {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.phoneType ?: -1
    }

    /**
     * 判断 sim 卡是否准备好
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isSimCardReady(context: Context): Boolean {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.simState == TelephonyManager.SIM_STATE_READY
    }

    /**
     * 获取 Sim 卡运营商名称
     *
     * 中国移动、如中国联通、中国电信   4865
     *
     * @return 移动网络运营商名称
     */
    @JvmStatic
    fun getSimOperatorByMnc(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = (tm.simOperator) ?: return null
        return when (operator) {
            "46000", "46002", "46007" -> "中国移动"
            "46001" -> "中国联通"
            "46003" -> "中国电信"
            else -> operator
        }
    }

    /**
     * 判断设备是否 root
     *
     * @return the boolean`true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/sbin/",
            "/system/sd/xbin/",
            "/system/bin/failsafe/",
            "/data/local/xbin/",
            "/data/local/bin/",
            "/data/local/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

}