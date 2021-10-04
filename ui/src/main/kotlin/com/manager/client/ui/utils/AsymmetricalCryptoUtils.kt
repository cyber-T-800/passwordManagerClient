package com.example.manager.utils

import java.security.*
import java.security.interfaces.RSAPrivateCrtKey
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

object AsymmetricalCryptoUtils{

    fun generateCryptoKeys(size : Int) : KeyPair{
        val result : KeyPair
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(size)
        result = generator.genKeyPair()

        return result
    }
    fun publicKeyFromPrivate(privateKey: PrivateKey) : PublicKey{
        val result : PublicKey
        val privateCtrKey = privateKey as RSAPrivateCrtKey
        val publicKeySpecs = RSAPublicKeySpec(privateCtrKey.modulus, privateCtrKey.publicExponent)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePublic(publicKeySpecs)

        return result
    }

    fun privateKeyToBytes(privateKey: PrivateKey) : ByteArray{
        return privateKey.encoded
    }
    fun privateKeyFromBytes(byteArray: ByteArray) : PrivateKey{
        val result : PrivateKey

        val keySpec : EncodedKeySpec = PKCS8EncodedKeySpec(byteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePrivate(keySpec)

        return result
    }
    fun publicKeyToBytes(publicKey: PublicKey) : ByteArray{
        return publicKey.encoded
    }
    fun publicKeyFromBytes(byteArray: ByteArray) : PublicKey{
        val result : PublicKey

        val keySpec : EncodedKeySpec = X509EncodedKeySpec(byteArray)
        val keyFactory = KeyFactory.getInstance("RSA")
        result = keyFactory.generatePublic(keySpec)

        return result
    }

    fun initializeEncryptCipher(privateKey: PrivateKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.ENCRYPT_MODE, privateKey)
        return result
    }
    fun initializeEncryptCipher(publicKey: PublicKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.ENCRYPT_MODE, publicKey)
        return result
    }
    fun initializeDecryptCipher(privateKey: PrivateKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.DECRYPT_MODE, privateKey)
        return result
    }
    fun initializeDecryptCipher(publicKey: PublicKey) : Cipher{
        val result : Cipher = Cipher.getInstance("RSA")
        result.init(Cipher.DECRYPT_MODE, publicKey)
        return result
    }

    fun encryptMessage(cipher : Cipher, message : ByteArray) : ByteArray{
        return cipher.doFinal(message)
    }
    fun encryptMessage(publicKey: PublicKey, message: ByteArray) : ByteArray{
        return encryptMessage(initializeEncryptCipher(publicKey), message)
    }
    fun encryptMessage(privateKey: PrivateKey, message: ByteArray) : ByteArray{
        return encryptMessage(initializeEncryptCipher(privateKey), message)
    }
    fun encryptMessage(cipher : Cipher, message : String) : ByteArray{
        return cipher.doFinal(message.toByteArray())
    }
    fun encryptMessage(publicKey: PublicKey, message: String) : ByteArray{
        return encryptMessage(initializeEncryptCipher(publicKey), message)
    }
    fun encryptMessage(privateKey: PrivateKey, message: String) : ByteArray{
        return encryptMessage(initializeEncryptCipher(privateKey), message)
    }

    fun encryptMessageToBase64(cipher : Cipher, message : ByteArray) : String{
        return Base64.getEncoder().encodeToString(cipher.doFinal(message))
    }
    fun encryptMessageToBase64(publicKey: PublicKey, message: ByteArray) : String{
        return encryptMessageToBase64(initializeEncryptCipher(publicKey), message)
    }
    fun encryptMessageToBase64(privateKey: PrivateKey, message: ByteArray) : String{
        return encryptMessageToBase64(initializeEncryptCipher(privateKey), message)
    }
    fun encryptMessageToBase64(cipher : Cipher, message : String) : String{
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.toByteArray()))
    }
    fun encryptMessageToBase64(publicKey: PublicKey, message: String) : String{
        return encryptMessageToBase64(initializeEncryptCipher(publicKey), message)
    }
    fun encryptMessageToBase64(privateKey: PrivateKey, message: String) : String{
        return encryptMessageToBase64(initializeEncryptCipher(privateKey), message)
    }


    fun decryptMessage(cipher : Cipher, message : ByteArray) : ByteArray{
        return cipher.doFinal(message)
    }
    fun decryptMessage(publicKey: PublicKey, message : ByteArray) : ByteArray{
        return decryptMessage(initializeDecryptCipher(publicKey), message)
    }
    fun decryptMessage(privateKey: PrivateKey, message : ByteArray) : ByteArray{
        return decryptMessage(initializeDecryptCipher(privateKey), message)
    }
    fun decryptMessage(cipher : Cipher, messageInBase64 : String) : ByteArray{
        return cipher.doFinal(Base64.getDecoder().decode(messageInBase64))
    }
    fun decryptMessage(publicKey: PublicKey, messageInBase64 : String) : ByteArray{
        return decryptMessage(initializeDecryptCipher(publicKey), messageInBase64)
    }
    fun decryptMessage(privateKey: PrivateKey, messageInBase64 : String) : ByteArray{
        return decryptMessage(initializeDecryptCipher(privateKey), messageInBase64)
    }

    fun decryptMessageToString(cipher : Cipher, message : ByteArray) : String{
        return String(cipher.doFinal(message))
    }
    fun decryptMessageToString(publicKey: PublicKey, message : ByteArray) : String{
        return decryptMessageToString(initializeDecryptCipher(publicKey), message)
    }
    fun decryptMessageToString(privateKey: PrivateKey, message : ByteArray) : String{
        return decryptMessageToString(initializeDecryptCipher(privateKey), message)
    }

    fun decryptMessageToString(cipher : Cipher, messageInBase64 : String) : String{
        return String(cipher.doFinal(Base64.getDecoder().decode(messageInBase64)))
    }
    fun decryptMessageToString(publicKey: PublicKey, messageInBase64 : String) : String{
        return decryptMessageToString(initializeDecryptCipher(publicKey), messageInBase64)
    }
    fun decryptMessageToString(privateKey: PrivateKey, messageInBase64 : String) : String{
        return decryptMessageToString(initializeDecryptCipher(privateKey), messageInBase64)
    }
}

