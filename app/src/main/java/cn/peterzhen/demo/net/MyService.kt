package cn.peterzhen.demo.net

import cn.cbsd.mvplibrary.base.ReturnModel
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-11-20 15:12
 */
interface MyService {

    companion object {
        const val BASE_URL = ""
    }

    @POST("/appLogin/s/checkLogin")
    suspend fun login(@Body model: LoginModel): ReturnModel<LoginResult>
}