package com.tiagotibaes.ponto.services.converters

import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResponse
import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResquest
import com.tiagotibaes.ponto.documents.Empresa
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult

@Component
class EmpresaConverter {

    public fun converterDtoParaEmpresa(empresaDtoResquest: EmpresaDtoResquest, result: BindingResult) : Empresa =
        Empresa( empresaDtoResquest.id, empresaDtoResquest.razaoSocial, empresaDtoResquest.cnpj, empresaDtoResquest.situacaoCadastral)

    public fun converterEmpresaDto(empresa: Empresa): EmpresaDtoResponse = EmpresaDtoResponse(empresa.situacaoCadastral, empresa.razaoSocial, empresa.cnpj, empresa.id)
}