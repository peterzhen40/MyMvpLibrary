/**
 *
 */
package cn.cbsd.mvplibrary.kit;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 文件操作的一些工具类
 *
 * @author zhaoyang 2015-8-19
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // mnt/sdcard/
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    //默认拍照路径
    public static final String DEFAULT_IMG_PATH = Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DCIM) + File.separator + "Camera";

    //SDCard/Android/data/youPackageName/files/ 目录
    public static String PACKAGE_PATH;

    public static String DOWNLOAD_PATH ;

    public static String IMAGES_PATH ;

    public static String VIDEOS_PATH ;

    /**
     * 初始化应用路径
     */
    public static void initPath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalFilesDir = context.getExternalFilesDir("");
            if (externalFilesDir != null) {
                PACKAGE_PATH = externalFilesDir.getPath();
                DOWNLOAD_PATH = PACKAGE_PATH + File.separator + "download";
                IMAGES_PATH = PACKAGE_PATH + File.separator + "images";
                VIDEOS_PATH = PACKAGE_PATH + File.separator + "videos";
                mkDirs(PACKAGE_PATH);
                mkDirs(DOWNLOAD_PATH);
                mkDirs(IMAGES_PATH);
                mkDirs(VIDEOS_PATH);
            }
        }
    }

    /**
     * 创建目录
     *
     * @param dirStr : 文件目录
     */
    public static void mkDirs(String dirStr) {
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 文件后缀名(包含.)
     */
    public static class SUFFIX {
        /**
         * 图片
         */
        public static final String IMAGE = ".png";
        /**
         * 视频
         */
        public static final String VIDEO = ".mp4";
        /**
         * 声音
         */
        public static final String VOICE = ".amr";
        /**
         * 数据库
         */
        public static final String DATABASE = ".db";
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getImgRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * Try to return the absolute file path from the given Uri IMG
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getVideoRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{VideoColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(VideoColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type, String userId) {
        return Uri.fromFile(getOutputMediaFile(type, userId));
    }

    private static File getOutputMediaFile(int type, String userId) {

        File imagesDir = new File(FileUtil.IMAGES_PATH);
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败");
                return null;
            }
        }

        File videoDir = new File(FileUtil.VIDEOS_PATH);
        if (!videoDir.exists()) {
            if (!videoDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(imagesDir.getPath() + File.separator + "IMG_" + timeStamp + "_" + userId + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(videoDir.getPath() + File.separator + "VID_" + timeStamp + "_" + userId + ".mp4");
        } else {
            return null;
        }
        Log.i("FileUtils", "PATH=" + mediaFile.toString());
        return mediaFile;
    }

    /** Create a File for saving an image or video */
    @SuppressLint("SimpleDateFormat")
    public static File getOutputMediaFile(int type) {
        File imagesDir = new File(FileUtil.IMAGES_PATH);
        if (!imagesDir.exists()) {
            if (!imagesDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败");
                return null;
            }
        }

        File videoDir = new File(FileUtil.VIDEOS_PATH);
        if (!videoDir.exists()) {
            if (!videoDir.mkdirs()) {
                Log.e("FileUtils", "Demolish创建目录失败");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(imagesDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(videoDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        Log.i("FileUtils", "PATH=" + mediaFile.toString());
        return mediaFile;
    }

    /*
     * 压缩图片
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h /
                minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     *
     * @param base64Code 编码后的字串
     * @param savePath 文件保存路径
     * @throws Exception
     * @since JDK 1.6
     */
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();

    }

    /*
     * 旋转Bitmap 270"
     */
    public static Bitmap getRotationOfBitmap(int degree, Bitmap oriBmp) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(oriBmp, 0, 0, oriBmp.getWidth(), oriBmp.getHeight(), matrix, true);
    }

    /*
     * 获取存储文件下的所有文件
     */
    public static ArrayList<String> getFiles() {
        ArrayList<String> mList = new ArrayList<String>();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , "jidiwuliu");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
            return null;
        } else {
            File[] mFile = mediaStorageDir.listFiles();
            for (File file : mFile) {
                String path = file.getAbsolutePath();
                mList.add(path.split("/")[path.split("/").length - 1]);
            }
        }

        return mList;
    }

    /*
     * 获取可用文件大小
     */
    public static String getFreeSpace() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcard = new File(Environment.getExternalStorageDirectory(), "/");
            return FormatFileSize(sdcard.getFreeSpace());
        }
        return "0KB";
    }

    /*
     * 获取已用文件大小
     */
    public static String getUsedSpace() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcard = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "jidiwuliu");
            if (!sdcard.exists()) {
                sdcard.mkdir();
            }
            long size = 0;
            for (File file : sdcard.listFiles()) {
                size += file.length();
            }
            return FormatFileSize(size);
        }
        return "0KB";
    }

    /*
     * 转换文件
     */
    public static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String 原文件路径 如：aa,不能有前面的/
     * @param newPath String 复制后路径 如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap,返回文件路径
     *
     * @param bitmap
     * @return
     */
    public static String saveBitmap(Bitmap bitmap) {
        File file = FileUtil.getOutputMediaFile(FileUtil.MEDIA_TYPE_IMAGE);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 从路径中生成压缩的Bitmap
     *
     * @param bitmapPath 原始路径
     * @param degree 旋转度数
     */
    public static Bitmap getBitmapFromPath(String bitmapPath, int degree) {

        Bitmap bmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapPath, opts);
        opts.inSampleSize = FileUtil.computeSampleSize(opts, -1, 800 * 600);
        // 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inJustDecodeBounds = false;
        try {
            bmp = BitmapFactory.decodeFile(bitmapPath, opts); //
        } catch (OutOfMemoryError err) {
        }
        return getRotationOfBitmap(degree, bmp);
    }

    /**
     * 从文件中获取压缩的bitmap
     */
    public static Bitmap getCompressBitmap(String bitmapPath) {
        Bitmap result = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        //1.采样率压缩
        opts.inSampleSize = 4;//原来的1/4
        //2.RGB_565法
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, opts);
        //3.缩放法压缩
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);//原来的1/4
        result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return result;
    }

    public static Bitmap getCompressBitmap(Bitmap bitmap) {
        //3.缩放法压缩
        Matrix matrix = new Matrix();
        matrix.setScale(0.25f, 0.25f);//1/100
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static class FileSize {
        public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
        public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
        public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
        public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

        /**
         * 获取文件指定文件的指定单位的大小
         *
         * @param filePath 文件路径
         * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
         * @return double值的大小
         */
        public static double getFileOrFilesSize(String filePath, int sizeType) {
            File file = new File(filePath);
            long blockSize = 0;
            try {
                if (file.isDirectory()) {
                    blockSize = getFileSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("获取文件大小", "获取失败!");
            }
            return FormetFileSize(blockSize, sizeType);
        }

        /**
         * 调用此方法自动计算指定文件或指定文件夹的大小
         *
         * @param filePath 文件路径
         * @return 计算好的带B、KB、MB、GB的字符串
         */
        public static String getAutoFileOrFilesSize(String filePath) {
            File file = new File(filePath);
            long blockSize = 0;
            try {
                if (file.isDirectory()) {
                    blockSize = getFileSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("获取文件大小", "获取失败!");
            }
            return FormetFileSize(blockSize);
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

        public static long getFileSize(File file) {
            long size = 0;
            if (file.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    size = fis.available();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    file.createNewFile();
                    Log.e("获取文件大小", "文件不存在!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return size;
        }

        /**
         * 获取指定文件夹
         *
         * @param f
         * @return
         * @throws Exception
         */
        private static long getFileSizes(File f) throws Exception {
            long size = 0;
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
            return size;
        }

        /**
         * 转换文件大小
         *
         * @param fileS
         * @return
         */
        private static String FormetFileSize(long fileS) {
            DecimalFormat df = new DecimalFormat("#.00");
            String fileSizeString = "";
            String wrongSize = "0B";
            if (fileS == 0) {
                return wrongSize;
            }
            if (fileS < 1024) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576) {
                fileSizeString = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                fileSizeString = df.format((double) fileS / 1048576) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            }
            return fileSizeString;
        }

        /**
         * 转换文件大小,指定转换的类型
         *
         * @param fileS
         * @param sizeType
         * @return
         */
        private static double FormetFileSize(long fileS, int sizeType) {
            DecimalFormat df = new DecimalFormat("#.00");
            double fileSizeLong = 0;
            switch (sizeType) {
                case SIZETYPE_B:
                    fileSizeLong = Double.valueOf(df.format((double) fileS));
                    break;
                case SIZETYPE_KB:
                    fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                    break;
                case SIZETYPE_MB:
                    fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                    break;
                case SIZETYPE_GB:
                    fileSizeLong = Double.valueOf(df
                            .format((double) fileS / 1073741824));
                    break;
                default:
                    break;
            }
            return fileSizeLong;
        }
    }

    /**
     * 创建图片文件
     */
    public static File createImageFile() {
        // Create an image file name
        String timeStamp = System.currentTimeMillis()+"";
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        String imgPath = IMAGES_PATH;
        File parentFile = new File(imgPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File image = new File(parentFile,imageFileName);
        return image;
    }

    //删除文件夹所有文件
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return true;
    }

    public static void writeFile(File file,byte[] bytes) {
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static String getPathFromUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 24) {
            return getPathFromUri24(context, uri);
        } else {
            return getPath(context, uri);
        }
    }

    public static String getPathFromUri24(Context context, Uri uri) {
        File rootDataDir = context.getFilesDir();
        String fileName = getFileName(uri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(rootDataDir + File.separator + fileName);
            copyFile(context, uri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            }
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return count;
    }


    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        Log.i(TAG, e.getMessage());
                        return null;
                    }
                }

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException ex) {
            Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", ex.getMessage()));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 获取文件后缀
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 是否为图片类型
     * @param suffix
     * @return
     */
    public static boolean isPhoto(String suffix) {
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }

        if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpeg")
                || suffix.equalsIgnoreCase("gif") || suffix.equalsIgnoreCase("bmp")
        ) {
            return true;
        }
        return false;
    }

    /**
     * 是否为允许的文件类型
     * @param suffix
     * @return
     */
    public static boolean isPermitFileType(String suffix) {
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }

        if (suffix.equalsIgnoreCase("jpg") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpeg")
                || suffix.equalsIgnoreCase("gif") || suffix.equalsIgnoreCase("bmp")
                || suffix.equalsIgnoreCase("zip") || suffix.equalsIgnoreCase("rar")
                || suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx")
                || suffix.equalsIgnoreCase("ppt") || suffix.equalsIgnoreCase("pptx")
                || suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")
                || suffix.equalsIgnoreCase("pdf")
        ) {
            return true;
        }

        return false;
    }
}
