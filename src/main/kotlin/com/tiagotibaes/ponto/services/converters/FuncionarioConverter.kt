package com.tiagotibaes.ponto.services.converters


import com.tiagotibaes.ponto.controller.data.DataUtils
import com.tiagotibaes.ponto.controller.data.DataUtilsDto
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoRequest
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoResponse
import com.tiagotibaes.ponto.documents.Funcionario

import org.springframework.stereotype.Component


@Component
class FuncionarioConverter {

    fun converterFuncionarioParaDto(funcionario: Funcionario) =
        FuncionarioDtoResponse( funcionario.id,
                                funcionario.nome,
                                funcionario.email,
                                funcionario.cpf,
                                funcionario.valorHora.toString(),
                                funcionario.empresaId,
                                converterDataUtilsToDataUtilsDto(funcionario.controleHorasFuncionario)
        )

    private fun converterDataUtilsToDataUtilsDto(dataUtils: DataUtils?) =
        DataUtilsDto(
            dataUtils?.qtdHorasTrabalhoDia.toString(),
            dataUtils?.qtdHorasAlmoco.toString()
    )

    private fun converterDataUtilsDtoToDataUtils(dataUtilsDto: DataUtilsDto?) =
        DataUtils(
            dataUtilsDto?.qtdHorasTrabalhoDia?.toFloat(),
            dataUtilsDto?.qtdHorasAlmoco?.toFloat()
    )

    fun converterDtoParaFuncionario(funcionarioDtoRequest: FuncionarioDtoRequest) =
        Funcionario( funcionarioDtoRequest.id,
                     funcionarioDtoRequest.nome,
                     funcionarioDtoRequest.email,
                     funcionarioDtoRequest.cpf,
                     funcionarioDtoRequest.valorHora?.toDouble(),
                     funcionarioDtoRequest.empresaId,
                     converterDataUtilsDtoToDataUtils(funcionarioDtoRequest.controleHorasFuncionario)
    )

}