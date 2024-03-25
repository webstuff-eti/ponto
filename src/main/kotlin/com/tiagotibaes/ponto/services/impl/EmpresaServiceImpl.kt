package com.tiagotibaes.ponto.services.impl

import com.tiagotibaes.ponto.documents.Empresa
import com.tiagotibaes.ponto.repositories.EmpresaRepository
import com.tiagotibaes.ponto.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {


    override fun buscarPorCnpj(cnpj: String): Empresa? {
        return empresaRepository.findByCnpj(cnpj)
    }

    override fun criar(empresa: Empresa): Empresa = empresaRepository.save(empresa)

}