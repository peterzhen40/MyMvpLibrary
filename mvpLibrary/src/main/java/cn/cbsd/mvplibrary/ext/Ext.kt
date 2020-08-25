package cn.cbsd.mvplibrary.ext

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.math.BigInteger
import java.security.MessageDigest

/**
 * 当前类注释: 自定义扩展函数
 * @author zhenyanjun
 * @date   2019-12-12 09:00
 */

/**
 * 获取非空字符串
 */
fun String?.getNotNull():String{
    return if (isNullOrEmpty()) ""
    else this!!
}

fun Double?.getNotNull(): Double {
    return this ?: 0.0
}
fun Int?.getNotNull(): Int {
    return this ?: 0
}

fun Boolean?.getNotNull(): Boolean {
    return this ?: false
}

fun <T> Collection<T>?.isNotNullAndNotEmpty(): Boolean = !isNullOrEmpty()

fun <T> Collection<T>?.isNotNull(): Boolean = this != null

fun <T> List<T>.toArrayList() :ArrayList<T>{
    val arrayList = arrayListOf<T>()
    arrayList.addAll(this)
    return arrayList
}

/**
 * rxjava Disposable处理
 */
fun Disposable.addTo(cd: CompositeDisposable) {
    cd.add(this)
}

/**
 * 交换元素
 */
fun <T> MutableList<T>.swap(index1: Int, index2: Int):MutableList<T> {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
    return this
}

/**
 * 获取32位小写md5
 */
fun String.md5():String{
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
