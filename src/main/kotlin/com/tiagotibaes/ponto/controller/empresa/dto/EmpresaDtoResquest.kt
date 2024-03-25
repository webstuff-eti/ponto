package com.tiagotibaes.ponto.controller.empresa.dto

data class EmpresaDtoResquest (
    val razaoSocial: String,
    val cnpj: String,
    val id: String? = null
)