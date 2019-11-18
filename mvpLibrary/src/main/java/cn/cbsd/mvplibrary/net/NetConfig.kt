package cn.cbsd.mvplibrary.net

import cn.cbsd.mvplibrary.kit.Kits
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by wanglei on 2016/12/24.
 * 网络框架retrofit设置
 */

object NetConfig {

    private val providerMap = HashMap<String, NetProvider>()
    private val retrofitMap = HashMap<String, Retrofit>()
    private val clientMap = HashMap<String, OkHttpClient>()

    @JvmStatic
    fun getRetrofit(baseUrl: String, useRx: Boolean): Retrofit {
        return getRetrofit(baseUrl, null, useRx)
    }

    @JvmStatic
    fun getRetrofit(baseUrl: String, provider: NetProvider?, useRx: Boolean): Retrofit {
        var provider = provider
        if (Kits.Empty.check(baseUrl)) {
            throw IllegalStateException("baseUrl can not be null")
        }
        if (retrofitMap[baseUrl] != null) return retrofitMap[baseUrl]!!

        if (provider == null) {
            provider = providerMap[baseUrl]
            if (provider == null) {
                provider = commonProvider
            }
        }
        checkProvider(provider)

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, provider!!))
                //                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
        if (useRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        }

        val retrofit = builder.build()
        retrofitMap[baseUrl] = retrofit
        providerMap[baseUrl] = provider

        return retrofit
    }

    private fun getClient(baseUrl: String, provider: NetProvider): OkHttpClient {
        if (Kits.Empty.check(baseUrl)) {
            throw IllegalStateException("baseUrl can not be null")
        }
        if (clientMap[baseUrl] != null) return clientMap[baseUrl]!!

        checkProvider(provider)

        val builder = OkHttpClient.Builder()

        builder.connectTimeout(if (provider.configConnectTimeoutMills() != 0L)
            provider.configConnectTimeoutMills()
        else
            connectTimeoutMills, TimeUnit.MILLISECONDS)
        builder.readTimeout(if (provider.configReadTimeoutMills() != 0L)
            provider.configReadTimeoutMills()
        else
            readTimeoutMills, TimeUnit.MILLISECONDS)

        val cookieJar = provider.configCookie()
        if (cookieJar != null) {
            builder.cookieJar(cookieJar)
        }
        provider.configHttps(builder)

        val interceptors = provider.configInterceptors()
        if (!Kits.Empty.check(interceptors)) {
            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
        }

        val client = builder.build()
        clientMap[baseUrl] = client
        providerMap[baseUrl] = provider

        return client
    }


    private fun checkProvider(provider: NetProvider?) {
        if (provider == null) {
            throw IllegalStateException("must register provider first")
        }
    }

    @JvmStatic
    fun getRetrofitMap(): Map<String, Retrofit> {
        return retrofitMap
    }

    @JvmStatic
    fun getClientMap(): Map<String, OkHttpClient> {
        return clientMap
    }

    var commonProvider: NetProvider? = null

    val connectTimeoutMills = 10 * 1000L
    val readTimeoutMills = 10 * 1000L

    @JvmStatic
    operator fun <S> get(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl, true)!!.create(service)
    }

    @JvmStatic
    fun registerProvider(provider: NetProvider) {
        NetConfig.commonProvider = provider
    }

    @JvmStatic
    fun registerProvider(baseUrl: String, provider: NetProvider) {
        providerMap[baseUrl] = provider
    }

    @JvmStatic
    fun clearCache() {
        retrofitMap.clear()
        clientMap.clear()
    }

}
