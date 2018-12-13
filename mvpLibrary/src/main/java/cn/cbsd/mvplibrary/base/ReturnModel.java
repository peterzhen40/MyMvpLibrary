package cn.cbsd.mvplibrary.base;

import java.io.Serializable;

/**
 * 统一返回值
 */
public class ReturnModel<T> implements Serializable {
    private int code = 0;// 状态0失败 1成功
    private String info = ""; // 如果失败,此处为失败原因
    private T data;// 如果成功,此处为成功数据
    private int rowCount;// 查询结果记录数
    //上传是否成功true or false
    private String success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
