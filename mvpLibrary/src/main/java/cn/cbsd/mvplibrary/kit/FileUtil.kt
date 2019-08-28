/**
 *
 */
package cn.cbsd.mvplibrary.kit

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns
import android.provider.MediaStore.Video.VideoColumns
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文件操作的一些工具类
 *
 * @author zhaoyang 2015-8-19
 */
object FileUtil {

    private val TAG = "FileUtil"

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2

    // mnt/sdcard/
    val SDCARD_PATH = Environment.getExternalStorageDirectory().path

    //默认拍照路径
    val DEFAULT_IMG_PATH = Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DCIM).toString() + File.separator + "Camera"

    //SDCard/Android/data/youPackageName/files/ 目录
    lateinit var PACKAGE_PATH: String

    lateinit var DOWNLOAD_PATH: String

    lateinit var IMAGES_PATH: String

    lateinit var VIDEOS_PATH: String

    /*
     * 获取存储文件下的所有文件
     */
    val files: ArrayList<String>?
        get() {
            val mList = ArrayList<String>()
            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "jidiwuliu")
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs()
                return null
            } else {
                val mFile = mediaStorageDir.listFiles()
                for (file in mFile) {
                    val path = file.absolutePath
                    mList.add(path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 1])
                }
            }

            return mList
        }

    /*
     * 获取可用文件大小
     */
    val freeSpace: String
        get() {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val sdcard = File(Environment.getExternalStorageDirectory(), "/")
                return FormatFileSize(sdcard.freeSpace)
            }
            return "0KB"
        }

    /*
     * 获取已用文件大小
     */
    val usedSpace: String
        get() {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val sdcard = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "jidiwuliu")
                if (!sdcard.exists()) {
                    sdcard.mkdir()
                }
                var size: Long = 0
                for (file in sdcard.listFiles()) {
                    size += file.length()
                }
                return FormatFileSize(size)
            }
            return "0KB"
        }

    /**
     * 初始化应用路径
     */
    fun initPath(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val externalFilesDir = context.getExternalFilesDir("")
            if (externalFilesDir != null) {
                PACKAGE_PATH = externalFilesDir.path
                DOWNLOAD_PATH = PACKAGE_PATH + File.separator + "download"
                IMAGES_PATH = PACKAGE_PATH + File.separator + "images"
                VIDEOS_PATH = PACKAGE_PATH + File.separator + "videos"
                mkDirs(PACKAGE_PATH)
                mkDirs(DOWNLOAD_PATH)
                mkDirs(IMAGES_PATH)
                mkDirs(VIDEOS_PATH)
            }
        }
    }

    /**
     * 创建目录
     *
     * @param dirStr : 文件目录
     */
    fun mkDirs(dirStr: String) {
        val dir = File(dirStr)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    /**
     * 文件后缀名(包含.)
     */
    object SUFFIX {
        /**
         * 图片
         */
        val IMAGE = ".png"
        /**
         * 视频
         */
        val VIDEO = ".mp4"
        /**
         * 声音
         */
        val VOICE = ".amr"
        /**
         * 数据库
         */
        val DATABASE = ".db"
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    fun getImgRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * Try to return the absolute file path from the given Uri IMG
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    fun getVideoRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) {
            return null
        }
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) {
            data = uri.path
        } else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(VideoColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(VideoColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }


    /** Create a file Uri for saving an image or video  */
    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a file Uri for saving an image or video  */
    fun getOutputMediaFileUri(type: Int, userId: String): Uri {
        return Uri.fromFile(getOutputMediaFile(type, userId))
    }

    private fun getOutputMediaFile(type: Int, userId: String): File? {

        val imagesDir = File(FileUtil.IMAGES_PATH)
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败")
                return null
            }
        }

        val videoDir = File(FileUtil.VIDEOS_PATH)
        if (!videoDir.exists()) {
            if (!videoDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败")
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(imagesDir.path + File.separator + "IMG_" + timeStamp + "_" + userId + ".jpg")
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = File(videoDir.path + File.separator + "VID_" + timeStamp + "_" + userId + ".mp4")
        } else {
            return null
        }
        Log.i("FileUtils", "PATH=$mediaFile")
        return mediaFile
    }

    /** Create a File for saving an image or video  */
    @SuppressLint("SimpleDateFormat")
    fun getOutputMediaFile(type: Int): File? {
        val imagesDir = File(FileUtil.IMAGES_PATH)
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败")
                return null
            }
        }

        val videoDir = File(FileUtil.VIDEOS_PATH)
        if (!videoDir.exists()) {
            if (!videoDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败")
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile: File
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = File(imagesDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = File(videoDir.path + File.separator + "VID_" + timeStamp + ".mp4")
        } else {
            return null
        }
        Log.i("FileUtils", "PATH=$mediaFile")
        return mediaFile
    }

    /*
     * 压缩图片
     */
    fun computeSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels)

        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }

        return roundedSize
    }

    private fun computeInitialSampleSize(options: BitmapFactory.Options, minSideLength: Int, maxNumOfPixels: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()

        val lowerBound = if (maxNumOfPixels == -1) 1 else Math.ceil(Math.sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1)
            128
        else
            Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength)).toInt()

        if (upperBound < lowerBound) {
            return lowerBound
        }

        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?): String? {

        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    fun base64ToBitmap(base64Data: String): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br></br>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    @Throws(Exception::class)
    fun encodeBase64File(path: String): String {
        val file = File(path)
        val inputFile = FileInputStream(file)
        val buffer = ByteArray(file.length().toInt())
        inputFile.read(buffer)
        inputFile.close()
        return Base64.encodeToString(buffer, Base64.DEFAULT)
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br></br>
     *
     * @param base64Code 编码后的字串
     * @param savePath 文件保存路径
     * @throws Exception
     * @since JDK 1.6
     */
    @Throws(Exception::class)
    fun decoderBase64File(base64Code: String, savePath: String) {
        val buffer = Base64.decode(base64Code, Base64.DEFAULT)
        val out = FileOutputStream(savePath)
        out.write(buffer)
        out.close()

    }

    /*
     * 旋转Bitmap 270"
     */
    fun getRotationOfBitmap(degree: Int, oriBmp: Bitmap?): Bitmap {
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree.toFloat())
        return Bitmap.createBitmap(oriBmp!!, 0, 0, oriBmp.width, oriBmp.height, matrix, true)
    }

    /*
     * 转换文件
     */
    fun FormatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "K"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "M"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "G"
        }
        return fileSizeString
    }

    /**
     * 制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String 原文件路径 如：aa,不能有前面的/
     * @param newPath String 复制后路径 如：xx:/bb/cc
     */
    fun copyFilesFassets(context: Context, oldPath: String, newPath: String) {
        try {
            val fileNames = context.assets.list(oldPath)// 获取assets目录下的所有文件及目录名
            if (fileNames!!.size > 0) {// 如果是目录
                val file = File(newPath)
                file.mkdirs()// 如果文件夹不存在，则递归
                for (fileName in fileNames) {
                    copyFilesFassets(context, "$oldPath/$fileName", "$newPath/$fileName")
                }
            } else {// 如果是文件
                val ins = context.assets.open(oldPath)
                val fos = FileOutputStream(File(newPath))
                val buffer = ByteArray(1024)
                var byteCount = 0
                while (ins.read(buffer) != -1) {// 循环从输入流读取
                    byteCount = ins.read(buffer)
                    // buffer字节
                    fos.write(buffer, 0, byteCount)// 将读取的输入流写入到输出流
                }
                fos.flush()// 刷新缓冲区
                ins.close()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 保存bitmap,返回文件路径
     *
     * @param bitmap
     * @return
     */
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

    /**
     * 从路径中生成压缩的Bitmap
     *
     * @param bitmapPath 原始路径
     * @param degree 旋转度数
     */
    fun getBitmapFromPath(bitmapPath: String, degree: Int): Bitmap {

        var bmp: Bitmap? = null
        val opts = BitmapFactory.Options()
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(bitmapPath, opts)
        opts.inSampleSize = FileUtil.computeSampleSize(opts, -1, 800 * 600)
        // 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inJustDecodeBounds = false
        try {
            bmp = BitmapFactory.decodeFile(bitmapPath, opts) //
        } catch (err: OutOfMemoryError) {
        }

        return getRotationOfBitmap(degree, bmp)
    }

    /**
     * 从文件中获取压缩的bitmap
     */
    fun getCompressBitmap(bitmapPath: String): Bitmap? {
        var result: Bitmap? = null
        val opts = BitmapFactory.Options()
        //1.采样率压缩
        opts.inSampleSize = 4//原来的1/4
        //2.RGB_565法
        opts.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = BitmapFactory.decodeFile(bitmapPath, opts)
        //3.缩放法压缩
        val matrix = Matrix()
        matrix.setScale(0.5f, 0.5f)//原来的1/4
        result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        return result
    }

    fun getCompressBitmap(bitmap: Bitmap): Bitmap {
        //3.缩放法压缩
        val matrix = Matrix()
        matrix.setScale(0.25f, 0.25f)//1/100
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    object FileSize {
        val SIZETYPE_B = 1// 获取文件大小单位为B的double值
        val SIZETYPE_KB = 2// 获取文件大小单位为KB的double值
        val SIZETYPE_MB = 3// 获取文件大小单位为MB的double值
        val SIZETYPE_GB = 4// 获取文件大小单位为GB的double值

        /**
         * 获取文件指定文件的指定单位的大小
         *
         * @param filePath 文件路径
         * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
         * @return double值的大小
         */
        fun getFileOrFilesSize(filePath: String, sizeType: Int): Double {
            val file = File(filePath)
            var blockSize: Long = 0
            try {
                if (file.isDirectory) {
                    blockSize = getFileSizes(file)
                } else {
                    blockSize = getFileSize(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("获取文件大小", "获取失败!")
            }

            return FormetFileSize(blockSize, sizeType)
        }

        /**
         * 调用此方法自动计算指定文件或指定文件夹的大小
         *
         * @param filePath 文件路径
         * @return 计算好的带B、KB、MB、GB的字符串
         */
        fun getAutoFileOrFilesSize(filePath: String): String {
            val file = File(filePath)
            var blockSize: Long = 0
            try {
                if (file.isDirectory) {
                    blockSize = getFileSizes(file)
                } else {
                    blockSize = getFileSize(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("获取文件大小", "获取失败!")
            }

            return FormetFileSize(blockSize)
        }

        ///**
        // * 获取指定文件大小
        // *
        // * @param file
        // * @return
        // * @throws Exception
        // */
        //private static long getFileSize(File file) throws Exception {
        //    long size = 0;
        //    if (file.exists()) {
        //        FileInputStream fis = null;
        //        fis = new FileInputStream(file);
        //        size = fis.available();
        //        fis.close();
        //    } else {
        //        file.createNewFile();
        //        Log.e("获取文件大小", "文件不存在!");
        //    }
        //    return size;
        //}

        fun getFileSize(file: File): Long {
            var size: Long = 0
            if (file.exists()) {
                var fis: FileInputStream? = null
                try {
                    fis = FileInputStream(file)
                    size = fis.available().toLong()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        fis?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } else {
                try {
                    file.createNewFile()
                    Log.e("获取文件大小", "文件不存在!")
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return size
        }

        /**
         * 获取指定文件夹
         *
         * @param f
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getFileSizes(f: File): Long {
            var size: Long = 0
            val flist = f.listFiles()
            for (i in flist.indices) {
                if (flist[i].isDirectory) {
                    size = size + getFileSizes(flist[i])
                } else {
                    size = size + getFileSize(flist[i])
                }
            }
            return size
        }

        /**
         * 转换文件大小
         *
         * @param fileS
         * @return
         */
        fun FormetFileSize(fileS: Long): String {
            val df = DecimalFormat("#.00")
            var fileSizeString = ""
            val wrongSize = "0B"
            if (fileS == 0L) {
                return wrongSize
            }
            if (fileS < 1024) {
                fileSizeString = df.format(fileS.toDouble()) + "B"
            } else if (fileS < 1048576) {
                fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
            } else if (fileS < 1073741824) {
                fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
            } else {
                fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
            }
            return fileSizeString
        }

        /**
         * 转换文件大小,指定转换的类型
         *
         * @param fileS
         * @param sizeType
         * @return
         */
        fun FormetFileSize(fileS: Long, sizeType: Int): Double {
            val df = DecimalFormat("#.00")
            var fileSizeLong = 0.0
            when (sizeType) {
                SIZETYPE_B -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble()))
                SIZETYPE_KB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1024))
                SIZETYPE_MB -> fileSizeLong = java.lang.Double.valueOf(df.format(fileS.toDouble() / 1048576))
                SIZETYPE_GB -> fileSizeLong = java.lang.Double.valueOf(df
                        .format(fileS.toDouble() / 1073741824))
                else -> {
                }
            }
            return fileSizeLong
        }
    }

    /**
     * 创建图片文件
     */
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = System.currentTimeMillis().toString() + ""
        val imageFileName = "JPEG_$timeStamp.jpg"
        val imgPath = IMAGES_PATH
        val parentFile = File(imgPath)
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        return File(parentFile, imageFileName)
    }

    //删除文件夹所有文件
    fun deleteFile(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return true
        }

        val file = File(path)
        if (!file.exists()) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return true
    }

    fun writeFile(file: File, bytes: ByteArray) {
        // 创建FileOutputStream对象
        var outputStream: FileOutputStream? = null
        // 创建BufferedOutputStream对象
        var bufferedOutputStream: BufferedOutputStream? = null
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete()
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile()
            // 获取FileOutputStream对象
            outputStream = FileOutputStream(file)
            // 获取BufferedOutputStream对象
            bufferedOutputStream = BufferedOutputStream(outputStream)
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes)
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush()
        } catch (e: Exception) {
            // 打印异常信息
            e.printStackTrace()
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }

            }
        }
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        return if (Build.VERSION.SDK_INT >= 24) {
            getPathFromUri24(context, uri)
        } else {
            getPath(context, uri)
        }
    }

    fun getPathFromUri24(context: Context, uri: Uri): String? {
        val rootDataDir = context.filesDir
        val fileName = getFileName(uri)
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(rootDataDir.toString() + File.separator + fileName)
            copyFile(context, uri, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    fun copyFile(context: Context, srcUri: Uri, dstFile: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri) ?: return
            val outputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream, output: OutputStream): Int {
        val BUFFER_SIZE = 1024 * 2
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE) != -1) {
                n = `in`.read(buffer, 0, BUFFER_SIZE)
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
            }

            try {
                `in`.close()
            } catch (e: IOException) {
            }

        }
        return count
    }


    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                if (!TextUtils.isEmpty(id)) {
                    try {
                        val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        Log.i(TAG, e.message)
                        return null
                    }

                }

            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else getDataColumn(context, uri, null, null)

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } catch (ex: IllegalArgumentException) {
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", ex.message))
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * 获取文件后缀
     * @param filename
     * @return
     */
    fun getFileSuffix(filename: String?): String? {
        if (filename != null && filename.length > 0) {
            val dot = filename.lastIndexOf('.')
            if (dot > -1 && dot < filename.length - 1) {
                return filename.substring(dot + 1)
            }
        }
        return filename
    }

    /**
     * 是否为图片类型
     * @param suffix
     * @return
     */
    fun isPhoto(suffix: String): Boolean {
        if (TextUtils.isEmpty(suffix)) {
            return false
        }

        return if (suffix.equals("jpg", ignoreCase = true) || suffix.equals("png", ignoreCase = true) || suffix.equals("jpeg", ignoreCase = true)
                || suffix.equals("gif", ignoreCase = true) || suffix.equals("bmp", ignoreCase = true)) {
            true
        } else false
    }

    /**
     * 是否为允许的文件类型
     * @param suffix
     * @return
     */
    fun isPermitFileType(suffix: String): Boolean {
        if (TextUtils.isEmpty(suffix)) {
            return false
        }

        return if (suffix.equals("jpg", ignoreCase = true) || suffix.equals("png", ignoreCase = true) || suffix.equals("jpeg", ignoreCase = true)
                || suffix.equals("gif", ignoreCase = true) || suffix.equals("bmp", ignoreCase = true)
                || suffix.equals("zip", ignoreCase = true) || suffix.equals("rar", ignoreCase = true)
                || suffix.equals("doc", ignoreCase = true) || suffix.equals("docx", ignoreCase = true)
                || suffix.equals("ppt", ignoreCase = true) || suffix.equals("pptx", ignoreCase = true)
                || suffix.equals("xls", ignoreCase = true) || suffix.equals("xlsx", ignoreCase = true)
                || suffix.equals("pdf", ignoreCase = true)) {
            true
        } else false

    }
}
