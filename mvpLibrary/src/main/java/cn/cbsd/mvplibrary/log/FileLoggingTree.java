package cn.cbsd.mvplibrary.log;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cn.cbsd.mvplibrary.kit.DateUtil;
import timber.log.Timber;

/**
 * 当前类注释:将timber log保存到文件
 * @author zhenyanjun
 * @date   2018/7/4 16:56
 */
public class FileLoggingTree extends Timber.Tree{

    private Context mContext;

    public FileLoggingTree(Context context) {
        mContext = context;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
        if (priority >= Log.DEBUG) {
            // TODO: 2018/12/14 debug以上才记录？
        }
        //检查外部存储
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File filesDir;
        if (Build.VERSION.SDK_INT >= 19) {
            filesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        }else {
            filesDir = mContext.getExternalFilesDir("");
        }
        if (filesDir == null) {
            return;
        }
        if (!filesDir.exists()) {
            filesDir.mkdir();
        }

        String fileName = "log.txt";
        File file = new File(filesDir,fileName);
        //Log.v("FileLoggingTree", "file.path:" + file.getAbsolutePath() + ",message:" + message);
        FileWriter writer = null;
        BufferedWriter bufferedWriter = null;
        try {
            writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("========================");
            bufferedWriter.newLine();
            bufferedWriter.write("time:"+ DateUtil.getCurrentTime());
            bufferedWriter.newLine();
            String threadName = Thread.currentThread().getName();
            bufferedWriter.write("threadName:"+threadName);
            bufferedWriter.newLine();
            bufferedWriter.write("message:"+message);
            if (t != null) {
                bufferedWriter.newLine();
                bufferedWriter.write("Throwable:"+t.toString());
            }
            bufferedWriter.newLine();
            bufferedWriter.write("========================");
            bufferedWriter.flush();

        } catch (IOException e) {
            //Log.v("FileLoggingTree", "存储文件失败");
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
