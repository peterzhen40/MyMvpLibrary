package cn.cbsd.mvplibrary.kit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Author: zhenyanjun
 * Date  : 2017/4/22 10:35
 */

object ImageUtil {
    private val TAG = "ImageUtil"

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    @JvmStatic
    fun doPhoto(context: Activity, data: Intent?, requestCode: Int, value: Int, editText: EditText,
                imageView: ImageView, photoUri: Uri?) {
        var photoUri = photoUri
        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == value) {
            if (data == null) {
                Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show()
                return
            }
            photoUri = data.data
            if (photoUri == null) {
                Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show()
                return
            }
        }

        val pojo = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        val resolver = context.contentResolver
        val cursor = resolver.query(photoUri!!, pojo, null, null, null)
        var picPath: String? = null
        var filename: String? = null
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(pojo[0])
            cursor.moveToFirst()
            picPath = cursor.getString(columnIndex)
            filename = cursor.getString(cursor.getColumnIndexOrThrow(pojo[1]))

            editText.requestFocus()
            editText.setText(filename)

            cursor.close()
        }
        val dix = filename!!.substring(filename.lastIndexOf("."), filename.length)

        if (filename != null && (dix.equals(".png", ignoreCase = true) || dix.equals(".jpg", ignoreCase = true)
                        || dix.equals(".gif", ignoreCase = true) || dix.equals(".bmp", ignoreCase = true) || dix.equals(".jpeg", ignoreCase = true)
                        || dix.equals(".tiff", ignoreCase = true))) {
            // lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(Uri.parse(picPath))
        } else {
            imageView.visibility = View.GONE
            Toast.makeText(context, "选择图片文件不正确", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path
     * @return
     */
    @JvmStatic
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return degree
    }

    /**
     * 打开base64图片
     */
    //    public static void openImage(Context context, String base64) {
    //        Activity activity = null;
    //        if (context instanceof Activity) {
    //            activity = ((Activity) context);
    //        }
    //        if (base64 != null) {
    //            String url = decoderBase64ToUrl(base64);
    //            Intent intent = new Intent(activity, SimplePhotoBrowserActivity.class);
    //            intent.putExtra(SimplePhotoBrowserActivity.ARG_IMAGE_URL, url);
    //            activity.startActivity(intent);
    //        }
    //    }

    /**
     * 打开图片
     */
    //    public static void openNativeImage(Context context, String path) {
    //        Activity activity = null;
    //        if (context instanceof Activity) {
    //            activity = ((Activity) context);
    //        }
    //        Intent intent = new Intent(activity, SimplePhotoBrowserActivity.class);
    //        intent.putExtra(SimplePhotoBrowserActivity.ARG_IMAGE_URL, "file://" + path);
    //        activity.startActivity(intent);
    //    }

    /**
     * 保存图片
     *
     * @param context
     * @param data
     * @param requestCode
     * @param imageView
     */
    @JvmStatic
    fun saveImage(context: Activity, data: Intent, requestCode: Int, imageView: ImageView) {
        var photo: Bitmap? = null
        val photoUri = data.data
        IntentUtil.cropImage(context, photoUri, 500, 500, requestCode)
        if (photoUri != null) {
            photo = BitmapFactory.decodeFile(photoUri.path)
        }
        if (photo == null) {
            val extra = data.extras
            if (extra != null) {
                photo = extra.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }
        imageView.setImageBitmap(photo)
    }

    /**
     * 保存照相后的图片
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun saveCamera(context: Activity, data: Intent, cameraUri: Uri?, editText: EditText,
                   imageView: ImageView): Boolean {
        try {

            val extras = data.extras

            if (extras != null) {
                val photo = extras.getParcelable<Bitmap>("data")
                imageView.setImageBitmap(photo)
            }

            if (cameraUri != null) {
                val path = cameraUri.path
                Log.i(TAG, "path-->" + path!!)
                val filename = path.substring(path.lastIndexOf("/") + 1, path.length)
                editText.setText(filename)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     * 加载图片
     */
    //    public static void loadImage(Context context, String url) {
    //        Activity activity = null;
    //        if (context instanceof Activity) {
    //            activity = ((Activity) context);
    //        }
    //        Intent intent = new Intent(activity, SimplePhotoBrowserActivity.class);
    //        intent.putExtra(SimplePhotoBrowserActivity.ARG_IMAGE_URL, url);
    //        activity.startActivity(intent);
    //
    //    }

    /**
     * 将base64字符串转成url
     */
    @JvmStatic
    fun decoderBase64ToUrl(base64: String): String {
        var url = ""
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val filePath = FileUtil.IMAGES_PATH + File
                .separator + fileName
        val sdStatus = Environment.getExternalStorageState()
        /** 检测sd是否可用  */
        if (sdStatus == Environment.MEDIA_MOUNTED) {
            try {
                FileUtil.decoderBase64File(base64, filePath)
                val photoUri = Uri.fromFile(File(filePath))
                url = "file://" + photoUri.path!!
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return url
    }

    @Throws(Exception::class)
    @JvmStatic
    fun saveBitmap(bitmap: Bitmap): String {
        val file = FileUtil.getOutputMediaFile(FileUtil.MEDIA_TYPE_IMAGE)
        var fOut: FileOutputStream? = null
        try {
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file!!.absolutePath
    }
}
