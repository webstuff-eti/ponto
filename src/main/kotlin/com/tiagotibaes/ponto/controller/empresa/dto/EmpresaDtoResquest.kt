package com.tiagotibaes.ponto.controller.empresa.dto

import javax.validation.constraints.NotEmpty

data class EmpresaDtoResquest (

    @get:NotEmpty(message = "Situação Cadastral não pode ser vazia.")
    val situacaoCadastral: String,

    @get:NotEmpty(message = "Razão Social não pode ser vazia.")
    val razaoSocial: String,

    @get:NotEmpty(message = "CNPJ não pode ser vazia.")
    val cnpj: String,
    val id: String? = null
)