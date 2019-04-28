package cn.cbsd.mvplibrary.kit;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * @author zhenyanjun
 * @date   2017/4/21 15:21
 */

public class CommonKit {

    /**
     * 判断Activity是否Destroy
     * @param activity
     * @return
     */
    public static boolean isActivityDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 保留两位小数
     *
     * @param d
     * @return
     */
    public static String decimalFormat(double d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

    /**
     * 保留两位小数,四舍五入
     *
     * @param d
     * @return
     */
    public static Double bigDecimalFormat(double d) {
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 当 Handler 闲置就向它发送空 Message，以确保不会发生 Message 的内存泄漏
     * (dialog内存泄露解决)
     */
    public static void flushStackLocalLeaks(Looper looper) {
        final Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        handler.sendMessageDelayed(handler.obtainMessage(), 1000);
                        return true;
                    }
                });
            }
        });
    }

    /**
     * UniquePsuedoID
     * Return pseudo unique ID
     * 通过读取设备的ROM版本号、厂商名、CPU型号和其他硬件信息来组合出一串15位的号码和设备硬件序列号作为种子生成UUID
     * 2018.8.22:release版本出现null，debug版正常
     * @return ID
     */
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) +
                (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * Installtion ID
     * 在程序安装后第一次运行时生成一个ID，该方式和设备唯一标识不一样，不同的应用程序会产生不同的ID，同一个程序重新安装也会不同
     */
    public static class Installation {
        private static String sID = null;
        private static final String INSTALLATION = "INSTALLATION";

        public synchronized static String id(Context context) {
            if (sID == null) {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                try {
                    if (!installation.exists()) {
                        writeInstallationFile(installation);
                    }
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return sID;
        }

        private static String readInstallationFile(File installation) throws IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

        private static void writeInstallationFile(File installation) throws IOException {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }
    }

    /**
     * 首先通过读取Android_id,作为UUID的种子。若得到Android_Id等于9774d56d682e549c或者发生错误则random一个UUID作为备用方案，
     * 最后把得到的UUID同时存入内部存储和外部存储。下次使用UUID的时候优先从外部存储读取，再从内部存储读取，最后在重新生成，
     * 尽可能的保证其不变性。
     * @param context
     * @return
     */
    public static String getUniversalID(Context context) {
        String androidId;
        //外部储存
        String filePath = Environment.getExternalStorageDirectory() + "/UniversalID";
        //读取UUID
        StringBuilder stringBuilder = Kits.FileKit.readFile(filePath, "UTF-8");
        String uuid = stringBuilder ==null?"":stringBuilder.toString();
        if (TextUtils.isEmpty(uuid)) {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if ("9774d56d682e549c".equals(uuid)) {
                //random一个UUID作为备用方案
                uuid = UUID.randomUUID().toString();
            } else {
                try {
                    //Android_id作为UUID的种子
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("UTF-8")).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    //random一个UUID作为备用方案
                    uuid = UUID.randomUUID().toString();
                }
            }

            //保存UUID
            if (!TextUtils.isEmpty(uuid)) {
                Kits.FileKit.writeFile(filePath, uuid);
            }
        }

        return uuid;
    }

    /**
     * AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，如果刷机和恢复出厂设置会导致设备标识符重置
     * 这一点可以接受的话，那么将他们组合使用时，唯一性就可以应付绝大多数设备了。
     * 转化成Md5可以保护隐私
     * @param context
     * @return
     */
    public static String getUniqueId(Context context){
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        return MD5Util.parseStrToMd5U32(id);
    }
}
