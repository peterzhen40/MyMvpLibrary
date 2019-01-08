package cn.cbsd.base.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Author: zhenyanjun
 * Date  : 2017/4/22 10:35
 */

public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    public static void doPhoto(Activity context, Intent data, int requestCode, int value, EditText editText,
                               ImageView imageView, Uri photoUri) {
        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == value) {
            if (data == null) {
                Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(context, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }

        String[] pojo = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(photoUri, pojo, null, null, null);
        String picPath = null;
        String filename = null;
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);
            filename = cursor.getString(cursor.getColumnIndexOrThrow(pojo[1]));

            editText.requestFocus();
            editText.setText(filename);

            cursor.close();
        }
        String dix = filename.substring(filename.lastIndexOf("."), filename.length());

        if (filename != null && (dix.equalsIgnoreCase(".png") || dix.equalsIgnoreCase(".jpg")
                || dix.equalsIgnoreCase(".gif") || dix.equalsIgnoreCase(".bmp") || dix.equalsIgnoreCase(".jpeg")
                || dix.equalsIgnoreCase(".tiff"))) {
            // lastIntent.putExtra(KEY_PHOTO_PATH, picPath);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(Uri.parse(picPath));
        } else {
            imageView.setVisibility(View.GONE);
            Toast.makeText(context, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取图片的旋转角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
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
    public static void saveImage(Activity context, Intent data, int requestCode, ImageView imageView) {
        Bitmap photo = null;
        Uri photoUri = data.getData();
        cropImage(context, photoUri, 500, 500, requestCode);
        if (photoUri != null) {
            photo = BitmapFactory.decodeFile(photoUri.getPath());
        }
        if (photo == null) {
            Bundle extra = data.getExtras();
            if (extra != null) {
                photo = (Bitmap) extra.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
        }
        imageView.setImageBitmap(photo);
    }

    /**
     * 保存照相后的图片
     *
     * @param context
     * @return
     */
    public static boolean saveCamera(Activity context, Intent data, Uri cameraUri, EditText editText,
                                     ImageView imageView) {
        try {

            final Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                imageView.setImageBitmap(photo);
            }

            if (cameraUri != null) {
                String path = cameraUri.getPath();
                Log.i(TAG, "path-->" + path);
                String filename = path.substring(path.lastIndexOf("/") + 1, path.length());
                editText.setText(filename);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
    public static String decoderBase64ToUrl(String base64) {
        String url = "";
        String fileName = System.currentTimeMillis() + ".jpg";
        String filePath = FileUtil.IMAGES_PATH + File
                .separator + fileName;
        String sdStatus = Environment.getExternalStorageState();
        /** 检测sd是否可用 */
        if (sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            try {
                FileUtil.decoderBase64File(base64, filePath);
                Uri photoUri = Uri.fromFile(new File(filePath));
                url = "file://" + photoUri.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static String saveBitmap(Bitmap bitmap) throws Exception {
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
}
