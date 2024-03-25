package com.tiagotibaes.ponto.services.impl

import com.tiagotibaes.ponto.documents.Lancamento
import com.tiagotibaes.ponto.repositories.LancamentoRepository
import com.tiagotibaes.ponto.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LancamentoServiceImpl(val lancamentoRepository: LancamentoRepository) :  LancamentoService {

    override fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest): Page<Lancamento> {
        return lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    override fun buscarPorId(id: String): Lancamento? {
        return lancamentoRepository.findById(id).get()
    }

    override fun criar(lancamento: Lancamento) = lancamentoRepository.save(lancamento)

    override fun remover(id: String) = lancamentoRepository.deleteById(id)


}