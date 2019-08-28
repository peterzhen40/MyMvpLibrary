package cn.cbsd.mvplibrary.kit

import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * Created by wanglei on 2016/11/28.
 */

object CodecKit {
    object MD5 {

        /**
         * 十六进制
         *
         * @param buffer
         * @return
         */
        fun getMessageDigest(buffer: ByteArray): String? {
            val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
            try {
                val mdTemp = MessageDigest.getInstance(Algorithm.MD5.type)
                mdTemp.update(buffer)
                val md = mdTemp.digest()
                val j = md.size
                val str = CharArray(j * 2)
                var k = 0
                for (i in 0 until j) {
                    val byte0:Byte = md[i]
                    str[k++] = hexDigits[byte0.toInt().ushr(4).and(0xf)]
                    str[k++] = hexDigits[byte0.and(0xf).toInt()]
                }
                return String(str)
            } catch (e: Exception) {
                return null
            }

        }

        /**
         * @param buffer
         * @return
         */
        fun getRawDigest(buffer: ByteArray): ByteArray? {
            try {
                val mdTemp = MessageDigest.getInstance(Algorithm.MD5.type)
                mdTemp.update(buffer)
                return mdTemp.digest()

            } catch (e: Exception) {
                return null
            }

        }


        private fun getMD5(`is`: InputStream?, bufLen: Int): String? {
            if (`is` == null || bufLen <= 0) {
                return null
            }
            try {
                val md = MessageDigest.getInstance(Algorithm.MD5.type)
                val md5Str = StringBuilder(32)

                val buf = ByteArray(bufLen)
                var readCount = 0
                while (`is`.read(buf) != -1) {
                    readCount = `is`.read(buf)
                    md.update(buf, 0, readCount)
                }

                val hashValue = md.digest()

                for (i in hashValue.indices) {
                    md5Str.append(Integer.toString((hashValue[i].and(0xff.toByte())) + 0x100, 16).substring(1))
                }
                return md5Str.toString()
            } catch (e: Exception) {
                return null
            }

        }

        /**
         * 对文件进行md5
         *
         * @param filePath 文件路径
         * @return
         */
        fun getMD5(filePath: String?): String? {
            if (filePath == null) {
                return null
            }

            val f = File(filePath)
            return if (f.exists()) {
                getMD5(f, 1024 * 100)
            } else null
        }

        /**
         * 文件md5
         *
         * @param file
         * @return
         */
        fun getMD5(file: File): String? {
            return getMD5(file, 1024 * 100)
        }


        private fun getMD5(file: File?, bufLen: Int): String? {
            if (file == null || bufLen <= 0 || !file.exists()) {
                return null
            }

            var fin: FileInputStream? = null
            try {
                fin = FileInputStream(file)
                val md5 = getMD5(fin, (if (bufLen <= file.length()) bufLen.toInt() else file.length().toInt()))
                fin.close()
                return md5

            } catch (e: Exception) {
                return null

            } finally {
                try {
                    fin?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

    }


    object BASE64 {

        fun encode(plain: ByteArray): ByteArray {
            return Base64.encode(plain, Base64.DEFAULT)
        }

        fun encodeToString(plain: ByteArray): String {
            return Base64.encodeToString(plain, Base64.DEFAULT)
        }

        fun decode(text: String): ByteArray {
            return Base64.decode(text, Base64.DEFAULT)
        }

        fun decode(text: ByteArray): ByteArray {
            return Base64.decode(text, Base64.DEFAULT)
        }
    }


    object SHA {

        @Throws(Exception::class)
        fun encrypt(data: ByteArray): ByteArray {
            val sha = MessageDigest.getInstance(Algorithm.SHA.type)
            sha.update(data)
            return sha.digest()
        }

    }


    object MAC {
        /**
         * 初始化HMAC密钥
         *
         * @param algorithm 算法，可为空。默认为：Algorithm.Hmac_MD5
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun initMacKey(algorithm: Algorithm?): String {
            var algorithm = algorithm
            if (algorithm == null) algorithm = Algorithm.Hmac_MD5
            val keyGenerator = KeyGenerator.getInstance(algorithm.type)
            val secretKey = keyGenerator.generateKey()

            return BASE64.encodeToString(secretKey.encoded)
        }

        /**
         * HMAC加密
         *
         * @param plain     明文
         * @param key       key
         * @param algorithm 算法，可为空。默认为：Algorithm.Hmac_MD5
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun encrypt(plain: ByteArray, key: String, algorithm: Algorithm?): ByteArray {
            var algorithm = algorithm
            if (algorithm == null) algorithm = Algorithm.Hmac_MD5
            val secretKey = SecretKeySpec(BASE64.decode(key), algorithm.type)
            val mac = Mac.getInstance(secretKey.algorithm)
            mac.init(secretKey)

            return mac.doFinal(plain)
        }
    }


    object DES {

        /**
         * 转换秘钥
         *
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun toKey(key: ByteArray): Key {
            val dks = DESKeySpec(key)
            val keyFactory = SecretKeyFactory.getInstance(Algorithm.DES.type)
            return keyFactory.generateSecret(dks)
        }

        /**
         * 解密
         *
         * @param plain
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun decrypt(plain: ByteArray, key: String): ByteArray {
            val k = toKey(BASE64.decode(key))

            val cipher = Cipher.getInstance(Algorithm.DES.type)
            cipher.init(Cipher.DECRYPT_MODE, k)

            return cipher.doFinal(plain)
        }

        /**
         * 加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun encrypt(data: ByteArray, key: String): ByteArray {
            val k = toKey(BASE64.decode(key))
            val cipher = Cipher.getInstance(Algorithm.DES.type)
            cipher.init(Cipher.ENCRYPT_MODE, k)

            return cipher.doFinal(data)
        }

        /**
         * 生成密钥
         *
         * @param seed
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        @JvmOverloads
        fun initKey(seed: String? = null): String {
            var secureRandom: SecureRandom? = null

            if (seed != null) {
                secureRandom = SecureRandom(BASE64.decode(seed))
            } else {
                secureRandom = SecureRandom()
            }

            val kg = KeyGenerator.getInstance(Algorithm.DES.type)
            kg.init(secureRandom)

            val secretKey = kg.generateKey()

            return BASE64.encodeToString(secretKey.encoded)
        }
    }

    /**
     * 生成密钥
     *
     * @return
     * @throws Exception
     */


    object RSA {

        val SIGNATURE_ALGORITHM = "MD5withRSA"

        private val PUBLIC_KEY = "RSAPublicKey"
        private val PRIVATE_KEY = "RSAPrivateKey"


        /**
         * 用私钥对信息生成数字签名
         *
         * @param data       加密数据
         * @param privateKey 私钥
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun sign(data: ByteArray, privateKey: String): String {
            val keyBytes = BASE64.decode(privateKey)        // 解密由base64编码的私钥
            val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)   // 构造PKCS8EncodedKeySpec对象
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)    // KEY_ALGORITHM 指定的加密算法
            val priKey = keyFactory.generatePrivate(pkcs8KeySpec)        // 取私钥匙对象
            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)   // 用私钥对信息生成数字签名
            signature.initSign(priKey)
            signature.update(data)

            return BASE64.encodeToString(signature.sign())
        }

        /**
         * 校验数字签名
         *
         * @param data      加密数据
         * @param publicKey 公钥
         * @param sign      数字签名
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun verify(data: ByteArray, publicKey: String, sign: String): Boolean {

            val keyBytes = BASE64.decode(publicKey) // 解密由base64编码的公钥
            val keySpec = X509EncodedKeySpec(keyBytes)  // 构造X509EncodedKeySpec对象
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)  // KEY_ALGORITHM 指定的加密算法
            val pubKey = keyFactory.generatePublic(keySpec)   // 取公钥对象

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            signature.initVerify(pubKey)
            signature.update(data)

            return signature.verify(BASE64.decode(sign))
        }

        /**
         * 用私钥解密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun decryptByPrivateKey(data: ByteArray, key: String): ByteArray {
            val keyBytes = BASE64.decode(key)   // 对密钥解密

            val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)   // 取得私钥
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)
            val privateKey = keyFactory.generatePrivate(pkcs8KeySpec)

            // 对数据解密
            val cipher = Cipher.getInstance(keyFactory.algorithm)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            return cipher.doFinal(data)
        }

        /**
         * 用公钥解密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun decryptByPublicKey(data: ByteArray, key: String): ByteArray {
            val keyBytes = BASE64.decode(key)       // 对密钥解密

            // 取得公钥
            val x509KeySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)
            val publicKey = keyFactory.generatePublic(x509KeySpec)

            // 对数据解密
            val cipher = Cipher.getInstance(keyFactory.algorithm)
            cipher.init(Cipher.DECRYPT_MODE, publicKey)

            return cipher.doFinal(data)
        }

        /**
         * 用公钥加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun encryptByPublicKey(data: ByteArray, key: String): ByteArray {
            val keyBytes = BASE64.decode(key)   // 对公钥解密

            // 取得公钥
            val x509KeySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)
            val publicKey = keyFactory.generatePublic(x509KeySpec)

            // 对数据加密
            val cipher = Cipher.getInstance(keyFactory.algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            return cipher.doFinal(data)
        }

        /**
         * 用私钥加密
         *
         * @param data
         * @param key
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun encryptByPrivateKey(data: ByteArray, key: String): ByteArray {

            val keyBytes = BASE64.decode(key)   // 对密钥解密

            // 取得私钥
            val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(Algorithm.RSA.type)
            val privateKey = keyFactory.generatePrivate(pkcs8KeySpec)

            // 对数据加密
            val cipher = Cipher.getInstance(keyFactory.algorithm)
            cipher.init(Cipher.ENCRYPT_MODE, privateKey)

            return cipher.doFinal(data)
        }

        /**
         * 取得私钥
         *
         * @param keyMap
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getPrivateKey(keyMap: Map<String, Any>): String {
            val key = keyMap[PRIVATE_KEY] as Key?

            return BASE64.encodeToString(key!!.encoded)
        }

        /**
         * 取得公钥
         *
         * @param keyMap
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun getPublicKey(keyMap: Map<String, Any>): String {
            val key = keyMap[PUBLIC_KEY] as Key?

            return BASE64.encodeToString(key!!.encoded)
        }

        /**
         * 初始化密钥
         *
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun initKey(): Map<String, Any> {
            val keyPairGen = KeyPairGenerator
                    .getInstance(Algorithm.RSA.type)
            keyPairGen.initialize(1024)

            val keyPair = keyPairGen.generateKeyPair()
            val publicKey = keyPair.public as RSAPublicKey    // 公钥
            val privateKey = keyPair.private as RSAPrivateKey     // 私钥
            val keyMap = HashMap<String, Any>(2)

            keyMap[PUBLIC_KEY] = publicKey
            keyMap[PRIVATE_KEY] = privateKey
            return keyMap
        }

    }

    fun checkNull(text: String?): Boolean {
        return null == text || text.length == 0
    }

    enum class Algorithm private constructor(val type: String) {
        SHA("SHA"),
        MD5("MD5"),
        Hmac_MD5("HmacMD5"),
        Hmac_SHA1("HmacSHA1"),
        Hmac_SHA256("HmacSHA256"),
        Hmac_SHA384("HmacSHA384"),
        Hmac_SHA512("HmacSHA512"),
        DES("DES"),
        RSA("RSA")
    }
}
