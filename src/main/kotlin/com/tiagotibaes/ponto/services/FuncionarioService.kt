package com.tiagotibaes.ponto.services

import com.tiagotibaes.ponto.documents.Funcionario

interface FuncionarioService {

    fun criar(funcionario: Funcionario): Funcionario

    fun buscarPorCpf(cpf: String): Funcionario?

    fun buscarPorEmail(email: String): Funcionario?

    fun buscarPorId(id: String): Funcionario?
}