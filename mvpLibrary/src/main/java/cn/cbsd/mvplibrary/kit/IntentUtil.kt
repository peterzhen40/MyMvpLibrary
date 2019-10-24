package cn.cbsd.mvplibrary.kit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: zhenyanjun
 * Date  : 2017/4/12 17:18
 */

class IntentUtil {

    /**
     * 本地照片调用
     *
     * @param context
     * @param requestCode
     */
    fun openPhotos(context: Activity, requestCode: Int) {
        if (openPhotosNormal(context, requestCode) //

                && openPhotosBrowser(context, requestCode) //

                && openPhotosFinally(context))
        ;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private fun openPhotosFinally(context: Activity): Boolean {
        Toast.makeText(context, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG).show()
        return false
    }

    /**
     * PopupMenu打开本地相册.
     */
    private fun openPhotosNormal(activity: Activity, actResultCode: Int): Boolean {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE)
        try {
            activity.startActivityForResult(intent, actResultCode)

        } catch (e: android.content.ActivityNotFoundException) {

            return true
        }

        return false
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private fun openPhotosBrowser(activity: Activity, requestCode: Int): Boolean {
        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show()
        val intent = Intent(Intent.ACTION_GET_CONTENT) // "android.intent.action.GET_CONTENT"
        intent.type = IMAGE_TYPE // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        val wrapperIntent = Intent.createChooser(intent, null)
        try {
            activity.startActivityForResult(wrapperIntent, requestCode)
        } catch (e1: android.content.ActivityNotFoundException) {
            return true
        }

        return false
    }

    companion object {
        private val IMAGE_TYPE = "image/*"
        private val TAG = "IntentUtil"

        fun openIntent(from: Activity, to: Class<*>) {
            val intent = Intent(from, to)
            from.startActivity(intent)

        }

        fun openIntent(from: Activity, to: Class<*>, bundle: Bundle) {
            val intent = Intent(from, to)
            intent.putExtras(bundle)
            from.startActivity(intent)

        }

        fun openIntentForResult(from: Activity, to: Class<*>, requestCode: Int) {
            val intent = Intent(from, to)
            from.startActivityForResult(intent, requestCode)
        }

        fun openIntentForResult(from: Activity, to: Class<*>, bundle: Bundle, requestCode: Int) {
            val intent = Intent(from, to)
            intent.putExtras(bundle)
            from.startActivityForResult(intent, requestCode)
        }

        /**
         * 打电话
         *
         * @param context
         * @param tel
         */
        fun openCall(context: Context, tel: String) {
            var tel = tel
            tel = tel.replace("-".toRegex(), "")
            val intent = Intent()
            // 激活源代码,添加intent对象
            intent.action = "android.intent.action.CALL"
            intent.data = Uri.parse("tel:$tel")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }

        /***
         * 从相册中取图片
         */
        fun pickPhoto(context: Activity, requestCode: Int) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            context.startActivityForResult(intent, requestCode)
        }

        /**
         * 拍照获取压缩图
         * @param context
         * @param requestCode
         */
        fun takeCompressPhoto(context: Activity, requestCode: Int) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            context.startActivityForResult(intent, requestCode)
        }

        /**
         * 拍照获取原图,指定路径
         */
        fun takePhoto(context: Activity, requestCode: Int, cameraUri: Uri) {
            // 执行拍照前，应该先判断SD卡是否存在
            val SDState = Environment.getExternalStorageState()
            if (SDState == Environment.MEDIA_MOUNTED) {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)// "android.media.action.IMAGE_CAPTURE"
                Log.i(TAG, "cameraUri.path------>" + cameraUri.path!!)

                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
                context.startActivityForResult(intent, requestCode)
            } else {
                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show()
            }
        }

        /**
         * 拍照获取原图
         */
        fun takePhoto(context: Activity, requestCode: Int) {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

                val intent = Intent("android.media.action.IMAGE_CAPTURE")
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis().toString() + ".jpg")
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                // intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                context.startActivityForResult(intent, requestCode)

            } else {
                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show()
            }
        }

        /**
         * 获取从本地图库返回来的时候的URI解析出来的文件路径
         *
         * @return
         */
        fun getPhotoPathByLocalUri(context: Context, data: Intent): String {
            val photoUri = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(photoUri!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            return picturePath
        }

        /**
         * 打开照相机
         *
         * @param context    当前的activity
         * @param requestCode 拍照成功时activity forResult 的时候的requestCode
         */
        @SuppressLint("SimpleDateFormat")
        fun openCamera(context: Activity, requestCode: Int): Uri? {
            // 执行拍照前，应该先判断SD卡是否存在
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                val timeStampFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                val filename = timeStampFormat.format(Date())
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, filename)
                val photoUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                context.startActivityForResult(intent, requestCode)

                return photoUri
            } else {
                Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show()
            }
            return null
        }


        /**
         * FLAG_ACTIVITY_SINGLE_TOP
         * //当于加载模式中的singletop,在当前中的activity中转到当前activity，不增加新的
         *
         * @param file
         */
        fun openFile(context: Context, file: File) {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//
            // 默认的跳转类型,它会重新创建一个新的Activity
            intent.action = Intent.ACTION_VIEW
            // 调用getMIMEType()来取得MimeType
            // 设置intent的file与MimeType
            intent.setDataAndType(Uri.fromFile(file), "application/pdf")
            context.startActivity(intent)
        }


        /**
         * 截取图片
         *
         * @param uri
         * @param outputX
         * @param outputY
         * @param requestCode
         */
        fun cropImage(context: Activity, uri: Uri, outputX: Int, outputY: Int, requestCode: Int) {
            // 裁剪图片意图
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(uri, "image/*")
            intent.putExtra("crop", "true")
            // 裁剪框的比例，1：1
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            // 裁剪后输出图片的尺寸大小
            intent.putExtra("outputX", outputX)
            intent.putExtra("outputY", outputY)
            // 图片格式
            intent.putExtra("outputFormat", "JPEG")
            intent.putExtra("noFaceDetection", true)
            intent.putExtra("return-data", true)
            context.startActivityForResult(intent, requestCode)
        }

        fun selectVideo(activity: Activity, requstcode: Int) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            activity.startActivityForResult(intent, requstcode)
        }

        fun selectVideo(fragment: Fragment, requstcode: Int) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            fragment.startActivityForResult(intent, requstcode)
        }
    }


}
