package com.tiagotibaes.ponto.services

import com.tiagotibaes.ponto.documents.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

interface LancamentoService {

    fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest): Page<Lancamento>

    fun buscarPorId(id: String): Lancamento?

    fun criar(lancamento: Lancamento): Lancamento

    fun remover(id: String)
}