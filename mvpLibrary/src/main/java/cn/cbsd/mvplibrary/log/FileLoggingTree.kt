package cn.cbsd.mvplibrary.log

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import cn.cbsd.mvplibrary.kit.DateUtil
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * 当前类注释:将timber log保存到文件
 * @author zhenyanjun
 * @date   2018/7/4 16:56
 */
class FileLoggingTree(private val mContext: Context) : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority >= Log.DEBUG) {
            // TODO: 2018/12/14 debug以上才记录？
        }
        //检查外部存储
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }

        val filesDir: File?
        if (Build.VERSION.SDK_INT >= 19) {
            filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            filesDir = mContext.getExternalFilesDir("")
        }
        if (filesDir == null) {
            return
        }
        if (!filesDir.exists()) {
            filesDir.mkdir()
        }

        val fileName = "log.txt"
        val file = File(filesDir, fileName)
        //Log.v("FileLoggingTree", "file.path:" + file.getAbsolutePath() + ",message:" + message);
        var writer: FileWriter? = null
        var bufferedWriter: BufferedWriter? = null
        try {
            writer = FileWriter(file)
            bufferedWriter = BufferedWriter(writer)
            bufferedWriter.write("========================")
            bufferedWriter.newLine()
            bufferedWriter.write("time:" + DateUtil.currentTime)
            bufferedWriter.newLine()
            val threadName = Thread.currentThread().name
            bufferedWriter.write("threadName:$threadName")
            bufferedWriter.newLine()
            bufferedWriter.write("message:$message")
            if (t != null) {
                bufferedWriter.newLine()
                bufferedWriter.write("Throwable:$t")
            }
            bufferedWriter.newLine()
            bufferedWriter.write("========================")
            bufferedWriter.flush()

        } catch (e: IOException) {
            //Log.v("FileLoggingTree", "存储文件失败");
            e.printStackTrace()
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
}
