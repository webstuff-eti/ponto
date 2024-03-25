package com.tiagotibaes.ponto.controller.empresa.dto

data class EmpresaDtoResponse (
    val razaoSocial: String,
    val cnpj: String,
    val id: String? = null
)