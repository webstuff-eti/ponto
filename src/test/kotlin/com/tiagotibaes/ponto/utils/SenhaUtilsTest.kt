package com.tiagotibaes.ponto.utils

import com.mongodb.assertions.Assertions

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test

class SenhaUtilsTest {

    //given
    private val senhaMocada = "123456"
    private val bCryptEncoder = BCryptPasswordEncoder()

    @Test
    fun testeGerarHashSenha(){

        //when
        val hash = SenhaUtils().gerarBcrypt(senhaMocada)

        //then
        Assertions.assertTrue(bCryptEncoder.matches(senhaMocada, hash))

    }
}