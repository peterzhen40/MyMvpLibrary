package cn.cbsd.base.net.kit;

import com.google.gson.JsonParseException;

import android.text.TextUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.cbsd.base.net.ReturnModel;
import retrofit2.HttpException;

/**
 * 当前类注释:异常信息处理
 * @author zhenyanjun
 * @date   2019/1/8 10:42
 */
public class ErrorKit {

    /**
     * 获取Throwable错误信息
     * @param e
     * @return
     */
    public static String getErrorMessage(Throwable e) {
        if (e != null) {
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                switch (httpException.code()) {
                    case 400:
                        return "400:Bad Request";
                    case 403:
                        return "403:用户过期了，请重新登录";
                    case 401:
                        return "401:未授权";
                    case 404:
                        return "404:网页不存在";
                    case 405:
                        return "405:方法不被允许";
                    case 500:
                        return "500:服务器错误";
                    default:
                        return e.getMessage();
                }
            } else if (e instanceof UnknownHostException) {
                return "未知域名，连接失败";
            } else if (e instanceof JSONException
                    || e instanceof JsonParseException) {
                return "Json解析失败";
            } else if (e instanceof SocketTimeoutException) {
                return "连接超时";
            } else {
                return e.getMessage();
            }
        }
        return "";
    }

    /**
     * 获取后台返回的错误信息
     * @param data
     * @return
     */
    public static String getErrorMessage(ReturnModel data) {
        //如果不是1则提示错误信息
        switch (data.getCode()) {
            case 0:
                if (TextUtils.isEmpty(data.getInfo())) {
                    return "错误信息为空";
                } else {
                    return data.getInfo();
                }
            case 2:
            case 3:
            case 4:
            case 5:
            case 9:
            case 99:
                return getResultInfo(data.getCode());
            default:
                return data.getInfo();
        }
    }

    public static String getResultInfo(int resultCode) {
        switch (resultCode) {
            case 0:
                return "错误信息为空";
            case 1:
                return "成功";
            case 2:
                return "请求参数不合法";
            case 3:
                return "查询的结果集不存在";
            case 4:
                return "调用出现异常";
            case 5:
                return "没有访问权限";
            case 9:
                return "未登录、设备不可信，需要重新验证";
            case 99:
                return "服务器内部错误";
            default:
                return "";
        }
    }
}
