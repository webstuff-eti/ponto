package com.tiagotibaes.ponto.services

import com.tiagotibaes.ponto.documents.Empresa

interface EmpresaService {

    fun buscarPorCnpj(cnpj: String): Empresa?

    fun criar(empresa: Empresa): Empresa

}