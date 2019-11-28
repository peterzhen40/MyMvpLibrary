package cn.cbsd.mvplibrary.kit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider

import java.io.File

/**
 * 当前类注释:sdk版本适配工具
 *
 * @author zhenyanjun
 * @date 2018/11/1 14:22
 */
object VersionAdaptUtils {

    @JvmStatic
    fun getUriFromFile(intent: Intent, context: Context, file: File): Uri? {
        var fileUri: Uri? = null
        //7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }
}
