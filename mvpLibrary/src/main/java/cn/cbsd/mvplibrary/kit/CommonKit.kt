package cn.cbsd.mvplibrary.kit

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

/**
 * @author zhenyanjun
 * @date   2017/4/21 15:21
 */

object CommonKit {

    /**
     * UniquePsuedoID
     * Return pseudo unique ID
     * 通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码和设备硬件序列号作为种子生成UUID
     * 2018.8.22:release版本出现null，debug版正常
     * @return ID
     */
    // If all else fails, if the user does have lower than API 9 (lower
    // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
    // returns 'null', then simply the ID returned will be solely based
    // off their Android device information. This is where the collisions
    // can happen.
    // Thanks http://www.pocketmagic.net/?p=1662!
    // Try not to use DISPLAY, HOST or ID - these items could change.
    // If there are collisions, there will be overlapping data
    // Thanks to @Roman SL!
    // http://stackoverflow.com/a/4789483/950427
    // Only devices with API >= 9 have android.os.Build.SERIAL
    // http://developer.android.com/reference/android/os/Build.html#SERIAL
    // If a user upgrades software or roots their device, there will be a duplicate entry
    // Go ahead and return the serial for api => 9
    // String needs to be initialized
    // some value
    // Thanks @Joe!
    // http://stackoverflow.com/a/2853253/950427
    // Finally, combine the values we have found by using the UUID class to create a unique identifier
    @JvmStatic
    val uniquePsuedoID: String
        get() {
            val m_szDevIDShort = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 + Build.PRODUCT.length % 10
            var serial: String? = null
            try {
                serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
                return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
            } catch (exception: Exception) {
                serial = "serial"
            }

            return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
        }

    /**
     * 判断Activity是否Destroy
     * @param activity
     * @return
     */
    @JvmStatic
    fun isActivityDestroy(activity: Activity?): Boolean {
        return if (activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed) {
            true
        } else {
            false
        }
    }

    /**
     * 隐藏软键盘
     */
    @JvmStatic
    fun hideSoftKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun openSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 保留两位小数
     *
     * @param d
     * @return
     */
    @JvmStatic
    fun decimalFormat(d: Double): String {
        val df = DecimalFormat("#.00")
        return df.format(d)
    }

    /**
     * 保留两位小数,四舍五入
     *
     * @param d
     * @return
     */
    @JvmStatic
    fun bigDecimalFormat(d: Double): Double {
        val bigDecimal = BigDecimal(d)
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 当 Handler 闲置就向它发送空 Message，以确保不会发生 Message 的内存泄漏
     * (dialog内存泄露解决)
     */
    @JvmStatic
    fun flushStackLocalLeaks(looper: Looper) {
        val handler = Handler(looper)
        handler.post {
            Looper.myQueue().addIdleHandler {
                handler.sendMessageDelayed(handler.obtainMessage(), 1000)
                true
            }
        }
    }

    /**
     * Installtion ID
     * 在程序安装后第一次运行时生成一个ID，该方式和设备唯一标识不一样，不同的应用程序会产生不同的ID，同一个程序重新安装也会不同
     */
    object Installation {
        private var sID: String? = null
        private val INSTALLATION = "INSTALLATION"

        @Synchronized
        fun id(context: Context): String? {
            if (sID == null) {
                val installation = File(context.filesDir, INSTALLATION)
                try {
                    if (!installation.exists()) {
                        writeInstallationFile(installation)
                    }
                    sID = readInstallationFile(installation)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }

            }
            return sID
        }

        @Throws(IOException::class)
        private fun readInstallationFile(installation: File): String {
            val f = RandomAccessFile(installation, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            f.close()
            return String(bytes)
        }

        @Throws(IOException::class)
        private fun writeInstallationFile(installation: File) {
            val out = FileOutputStream(installation)
            val id = UUID.randomUUID().toString()
            out.write(id.toByteArray())
            out.close()
        }
    }

    /**
     * 首先通过读取Android_id,作为UUID的种子。若得到Android_Id等于9774d56d682e549c或者发生错误则random一个UUID作为备用方案，
     * 最后把得到的UUID同时存入内部存储和外部存储。下次使用UUID的时候优先从外部存储读取，再从内部存储读取，最后在重新生成，
     * 尽可能的保证其不变性。
     * @param context
     * @return
     */
    @JvmStatic
    fun getUniversalID(context: Context): String {
        val androidId: String
        //外部储存
        val filePath = Environment.getExternalStorageDirectory().toString() + "/UniversalID"
        //读取UUID
        val stringBuilder = Kits.FileKit.readFile(filePath, "UTF-8")
        var uuid = stringBuilder?.toString() ?: ""
        if (TextUtils.isEmpty(uuid)) {
            androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if ("9774d56d682e549c" == uuid) {
                //random一个UUID作为备用方案
                uuid = UUID.randomUUID().toString()
            } else {
                try {
                    //Android_id作为UUID的种子
                    uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(charset("UTF-8"))).toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    //random一个UUID作为备用方案
                    uuid = UUID.randomUUID().toString()
                }

            }

            //保存UUID
            if (!TextUtils.isEmpty(uuid)) {
                Kits.FileKit.writeFile(filePath, uuid)
            }
        }

        return uuid
    }

    /**
     * AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，如果刷机和恢复出厂设置会导致设备标识符重置
     * 这一点可以接受的话，那么将他们组合使用时，唯一性就可以应付绝大多数设备了。
     * 转化成Md5可以保护隐私
     * @param context
     * @return
     */
    @JvmStatic
    fun getUniqueId(context: Context): String? {
        val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val id = androidID + Build.SERIAL
        return MD5Util.parseStrToMd5U32(id)
    }

}
