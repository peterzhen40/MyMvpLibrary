package cn.cbsd.mvplibrary.kit

import android.content.Intent
import android.net.Uri
import java.io.File
import java.util.*

/**
 * 当前类注释:
 *
 * @author zhenyanjun
 * @date 2019/5/15 11:41
 */
object OpenFileUtil {

    /** 声明各种类型文件的dataType  */
    val DATA_TYPE_ALL = "*/*"//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    val DATA_TYPE_APK = "application/vnd.android.package-archive"
    val DATA_TYPE_VIDEO = "video/*"
    val DATA_TYPE_AUDIO = "audio/*"
    val DATA_TYPE_HTML = "text/html"
    val DATA_TYPE_IMAGE = "image/*"
    val DATA_TYPE_PPT = "application/vnd.ms-powerpoint"
    val DATA_TYPE_EXCEL = "application/vnd.ms-excel"
    val DATA_TYPE_WORD = "application/msword"
    val DATA_TYPE_CHM = "application/x-chm"
    val DATA_TYPE_TXT = "text/plain"
    val DATA_TYPE_PDF = "application/pdf"

    @JvmStatic
    fun openFile(filePath: String): Intent? {

        val file = File(filePath)
        if (!file.exists())
            return null
        /* 取得扩展名 */
        val end = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length).toLowerCase(Locale.getDefault())
        /* 依扩展名的类型决定MimeType */
        return if (end == "m4a" || end == "mp3" || end == "mid" || end == "xmf" || end == "ogg" || end == "wav") {
            getAudioFileIntent(filePath)
        } else if (end == "3gp" || end == "mp4") {
            getVideoFileIntent(filePath)
        } else if (end == "jpg" || end == "gif" || end == "png" || end == "jpeg" || end == "bmp") {
            getImageFileIntent(filePath)
        } else if (end == "apk") {
            getApkFileIntent(filePath)
        } else if (end == "ppt" || end == "pptx") {
            getPptFileIntent(filePath)
        } else if (end == "xls" || end == "xlsx") {
            getExcelFileIntent(filePath)
        } else if (end == "doc" || end == "docx") {
            getWordFileIntent(filePath)
        } else if (end == "pdf") {
            getPdfFileIntent(filePath)
        } else if (end == "chm") {
            getChmFileIntent(filePath)
        } else if (end == "txt") {
            getTextFileIntent(filePath, false)
        } else {
            getAllIntent(filePath)
        }
    }

    // Android获取一个用于打开A所有文件的intent
    @JvmStatic
    fun getAllIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_ALL)
        return intent
    }

    // Android获取一个用于打开APK文件的intent
    fun getApkFileIntent(param: String): Intent {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_APK)
        return intent
    }

    // Android获取一个用于打开VIDEO文件的intent
    fun getVideoFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_VIDEO)
        return intent
    }

    // Android获取一个用于打开AUDIO文件的intent
    fun getAudioFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_AUDIO)
        return intent
    }

    // Android获取一个用于打开Html文件的intent
    fun getHtmlFileIntent(param: String): Intent {

        val uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build()
        val intent = Intent("android.intent.action.VIEW")
        intent.setDataAndType(uri, DATA_TYPE_HTML)
        return intent
    }

    // Android获取一个用于打开图片文件的intent
    fun getImageFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_IMAGE)
        return intent
    }

    // Android获取一个用于打开PPT文件的intent
    fun getPptFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_PPT)
        return intent
    }

    // Android获取一个用于打开Excel文件的intent
    fun getExcelFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_EXCEL)
        return intent
    }

    // Android获取一个用于打开Word文件的intent
    fun getWordFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_WORD)
        return intent
    }

    // Android获取一个用于打开CHM文件的intent
    fun getChmFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_CHM)
        return intent
    }

    // Android获取一个用于打开文本文件的intent
    fun getTextFileIntent(param: String, paramBoolean: Boolean): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (paramBoolean) {
            val uri1 = Uri.parse(param)
            intent.setDataAndType(uri1, DATA_TYPE_TXT)
        } else {
            val uri2 = Uri.fromFile(File(param))
            intent.setDataAndType(uri2, DATA_TYPE_TXT)
        }
        return intent
    }

    // Android获取一个用于打开PDF文件的intent
    fun getPdfFileIntent(param: String): Intent {

        val intent = Intent("android.intent.action.VIEW")
        intent.addCategory("android.intent.category.DEFAULT")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromFile(File(param))
        intent.setDataAndType(uri, DATA_TYPE_PDF)
        return intent
    }

}
