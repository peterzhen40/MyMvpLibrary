package cn.peterzhen.demo

import android.os.Bundle
import cn.cbsd.mvplibrary.base.ReturnModel
import cn.cbsd.mvplibrary.ext.md5
import cn.cbsd.mvplibrary.kit.MD5Util
import cn.cbsd.mvplibrary.mvp.XActivity
import cn.peterzhen.demo.net.LoginModel
import cn.peterzhen.demo.net.LoginResult
import cn.peterzhen.demo.net.MyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-08-27 17:20
 */
class KotlinActivity :XActivity(){

    override val layoutId: Int = R.layout.view_all_list_page_state

    override fun initData(savedInstanceState: Bundle?) {
        //getvDelegate()?.showError(msg = "test")
        //Router.newIntent(context)

        //协程
        //CoroutineScope(Dispatchers.Main).launch{
        //    getvDelegate().showLoading("加载中")
        //    try {
        //        val returnModel = login("test", "88888")
        //        getvDelegate().dismissLoading()
        //        if (returnModel.code == 1) {
        //            val loginResult = returnModel.data
        //            getvDelegate().showSuccess(loginResult?.realName)
        //        } else {
        //            getvDelegate().showError(returnModel.info)
        //        }
        //        //executeResponse(returnModel,{
        //        //    val loginResult = returnModel.data
        //        //    getvDelegate().showSuccess(loginResult?.realName)
        //        //}, {
        //        //    getvDelegate().showError(returnModel.info)
        //        //})
        //    } catch (e:Exception) {
        //        getvDelegate().dismissLoading()
        //        getvDelegate().showError(e.message)
        //    }
        //}

        val md5Str1 = MD5Util.parseStrToMd5U32("88888")
        val md5Str2 = "88888".md5().toUpperCase()
        getvDelegate().showConfirm("1：$md5Str1\n2：$md5Str2")

    }

    suspend fun login(username:String, password:String):ReturnModel<LoginResult>{
        return try {
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .build()
            val retrofit = Retrofit.Builder()
                    .baseUrl(MyService.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val myService = retrofit.create(MyService::class.java)

            withContext(Dispatchers.IO){
                myService.login(LoginModel(username,password))
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
