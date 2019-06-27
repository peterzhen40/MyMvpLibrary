package cn.cbsd.mvplibrary.kit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: zhenyanjun
 * Date  : 2017/4/12 17:18
 */

public class IntentUtil {
    private static final String IMAGE_TYPE = "image/*";
    private static final String TAG = "IntentUtil";

    public static void openIntent(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);

    }

    public static void openIntent(Activity from, Class<?> to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        intent.putExtras(bundle);
        from.startActivity(intent);

    }

    public static void openIntentForResult(Activity from, Class<?> to,int requestCode) {
        Intent intent = new Intent(from, to);
        from.startActivityForResult(intent,requestCode);
    }

    public static void openIntentForResult(Activity from, Class<?> to,Bundle bundle,int requestCode) {
        Intent intent = new Intent(from, to);
        intent.putExtras(bundle);
        from.startActivityForResult(intent,requestCode);
    }

    /**
     * 打电话
     *
     * @param context
     * @param tel
     */
    public static void openCall(Context context, String tel) {
        tel = tel.replaceAll("-", "");
        Intent intent = new Intent();
        // 激活源代码,添加intent对象
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /***
     * 从相册中取图片
     */
    public static void pickPhoto(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照获取压缩图
     * @param context
     * @param requestCode
     */
    public static void takeCompressPhoto(Activity context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 拍照获取原图,指定路径
     */
    public static void takePhoto(Activity context, int requestCode, Uri cameraUri) {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            Log.i(TAG, "cameraUri.path------>" + cameraUri.getPath());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            context.startActivityForResult(intent, requestCode);
        } else {
            Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 拍照获取原图
     */
    public static void takePhoto(Activity context, int requestCode) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, String.valueOf(System.currentTimeMillis()) + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            // intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            context.startActivityForResult(intent, requestCode);

        } else {
            Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 本地照片调用
     *
     * @param context
     * @param requestCode
     */
    public void openPhotos(Activity context, int requestCode) {
        if (openPhotosNormal(context, requestCode) //
                && openPhotosBrowser(context, requestCode) //
                && openPhotosFinally(context))
            ;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    private boolean openPhotosFinally(Activity context) {
        Toast.makeText(context, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * 获取从本地图库返回来的时候的URI解析出来的文件路径
     *
     * @return
     */
    public static String getPhotoPathByLocalUri(Context context, Intent data) {
        Uri photoUri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(photoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /**
     * PopupMenu打开本地相册.
     */
    private boolean openPhotosNormal(Activity activity, int actResultCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
        try {
            activity.startActivityForResult(intent, actResultCode);

        } catch (android.content.ActivityNotFoundException e) {

            return true;
        }

        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    private boolean openPhotosBrowser(Activity activity, int requestCode) {
        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 打开照相机
     *
     * @param context    当前的activity
     * @param requestCode 拍照成功时activity forResult 的时候的requestCode
     */
    @SuppressLint("SimpleDateFormat")
    public static Uri openCamera(Activity context, int requestCode) {
        // 执行拍照前，应该先判断SD卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            Uri photoUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            context.startActivityForResult(intent, requestCode);

            return photoUri;
        } else {
            Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
        return null;
    }



    /**
     * FLAG_ACTIVITY_SINGLE_TOP
     * //当于加载模式中的singletop,在当前中的activity中转到当前activity，不增加新的
     *
     * @param file
     */
    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
        // 默认的跳转类型,它会重新创建一个新的Activity
        intent.setAction(Intent.ACTION_VIEW);
        // 调用getMIMEType()来取得MimeType
        // 设置intent的file与MimeType
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        context.startActivity(intent);
    }



    /**
     * 截取图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public static void cropImage(Activity context, Uri uri, int outputX, int outputY, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }

    public static void selectVideo(Activity activity,int requstcode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        activity.startActivityForResult(intent,requstcode);
    }

    public static void selectVideo(Fragment fragment, int requstcode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        fragment.startActivityForResult(intent,requstcode);
    }


}
