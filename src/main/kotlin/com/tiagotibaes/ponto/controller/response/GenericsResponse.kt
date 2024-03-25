package com.tiagotibaes.ponto.controller.response

data class GenericsResponse <T> (
    val erros: ArrayList<String> = arrayListOf(),
    var data: T? = null
)