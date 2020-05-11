package cn.cbsd.mvplibrary.net

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 当前类注释: 统一的接口
 * @author zhenyanjun
 * @date   2020/5/8 15:04
 */
interface ApiService {

    @GET
    suspend fun get(
        @Url url: String,
        @QueryMap map: HashMap<String, Any> = hashMapOf(),
        @HeaderMap headerMap: HashMap<String, String> = hashMapOf()
    ): Response<ResponseBody>

    @GET
    suspend fun getEncoded(
        @Url url: String,
        @QueryMap(encoded = true) map: HashMap<String, Any> = hashMapOf(),
        @HeaderMap headerMap: HashMap<String, String> = hashMapOf()
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url url: String,
        @FieldMap map: HashMap<String, Any> = hashMapOf(),
        @HeaderMap headerMap: HashMap<String, String> = hashMapOf()
    ): Response<ResponseBody>

    /**
     * 上传
     */
    @Multipart
    @POST
    suspend fun upload(
        @Url url: String,
        @Part part: MultipartBody.Part
    ): Response<ResponseBody>

    /**
     * 下载
     */
    @Streaming
    @GET
    suspend fun download(
        @Url url: String
    ): Response<ResponseBody>
}