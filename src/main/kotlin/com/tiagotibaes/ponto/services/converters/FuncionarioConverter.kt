package com.tiagotibaes.ponto.services.converters


import com.tiagotibaes.ponto.controller.data.DataUtils
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoRequest
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoResponse
import com.tiagotibaes.ponto.documents.Funcionario

import org.springframework.stereotype.Component


@Component
class FuncionarioConverter {

    fun converterFuncionarioParaDto(funcionario: Funcionario): FuncionarioDtoResponse {

        val funcionarioDtoResponse: FuncionarioDtoResponse = FuncionarioDtoResponse( funcionario.id,
                                                                                     funcionario.nome,
                                                                                     funcionario.email, "",
                                                                                     funcionario.cpf,
                                                                                     funcionario.valorHora.toString()
        )
        funcionarioDtoResponse.empresaId =  funcionario.empresaId
        funcionarioDtoResponse.controleHorasFuncionario = converterControl(funcionario)

        return funcionarioDtoResponse
    }


    private fun converterControl(funcionario: Funcionario) = DataUtils(
            funcionario.controleHorasFuncionario?.qtdHorasAlmoco,
        funcionario.controleHorasFuncionario?.qtdHorasTrabalhoDia
    )


    fun converterDtoParaFuncionario(funcionarioDtoRequest: FuncionarioDtoRequest) : Funcionario{
        return Funcionario( funcionarioDtoRequest.id,
                            funcionarioDtoRequest.nome,
                            funcionarioDtoRequest.email,
                            funcionarioDtoRequest.cpf,
                            funcionarioDtoRequest.valorHora?.toDouble(),
                            funcionarioDtoRequest.empresaId,
                            funcionarioDtoRequest.controleHorasFuncionario
        )
    }


}