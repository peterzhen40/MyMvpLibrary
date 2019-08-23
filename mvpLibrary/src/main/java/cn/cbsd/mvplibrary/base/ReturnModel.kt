package cn.cbsd.mvplibrary.base

import java.io.Serializable

/**
 * 统一返回值
 */
class ReturnModel<T> : Serializable {
    var code = 0// 状态0失败 1成功
    var info = "" // 如果失败,此处为失败原因
    var data: T? = null// 如果成功,此处为成功数据
    var rowCount: Int = 0// 查询结果记录数
    //上传是否成功true or false
    var success: String? = null
}
