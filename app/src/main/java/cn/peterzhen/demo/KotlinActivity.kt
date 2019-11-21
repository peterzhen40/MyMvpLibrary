package cn.peterzhen.demo

import android.content.Intent
import android.os.Bundle
import cn.cbsd.mvplibrary.base.ReturnModel
import cn.cbsd.mvplibrary.mvp.IView
import cn.cbsd.mvplibrary.mvp.XActivity
import cn.peterzhen.demo.net.LoginModel
import cn.peterzhen.demo.net.LoginResult
import cn.peterzhen.demo.net.MyService
import es.dmoral.toasty.MyToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 当前类注释:
 * @author zhenyanjun
 * @date   2019-08-27 17:20
 */
class KotlinActivity :XActivity(){

    override val layoutId: Int = R.layout.view_all_list

    override fun initData(savedInstanceState: Bundle?) {
        //getvDelegate()?.showError(msg = "test")
        //Router.newIntent(context)

        //协程
        CoroutineScope(Dispatchers.Main).launch{
            getvDelegate().showLoading("加载中")
            try {
                val returnModel = login("test", "88888")
                getvDelegate().dismissLoading()
                if (returnModel.code == 1) {
                    val loginResult = returnModel.data
                    getvDelegate().showSuccess(loginResult?.realName)
                } else {
                    getvDelegate().showError(returnModel.info)
                }
                //executeResponse(returnModel,{
                //    val loginResult = returnModel.data
                //    getvDelegate().showSuccess(loginResult?.realName)
                //}, {
                //    getvDelegate().showError(returnModel.info)
                //})
            } catch (e:Exception) {
                getvDelegate().dismissLoading()
                getvDelegate().showError(e.message)
            }
        }
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

fun XActivity.handleError(showError:Boolean = true, isDialog:Boolean = true,e:Exception){
    if (e is HttpException) {
        val httpException = e as HttpException
        if (httpException.code() == 403) {
            MyToast.errorBig("登录已超时，正在跳转登录页")
            //token失效处理
            //LoginSp.setLoginState(AppKit.getContext(), false)
            //val intent = Intent(, LoginActivity::class.java)
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            //AppKit.getContext().startActivity(intent)
        } else {
            if (showError) {
                if ()
                getvDelegate().showError(ErrorKit.getErrorMessage(t))
            }
        }
    } else {
        if (showError) {
            getvDelegate().showError(ErrorKit.getErrorMessage(t))
        }
    }
}