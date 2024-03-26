package com.tiagotibaes.ponto.services.converters

import com.tiagotibaes.ponto.controller.lancamento.dto.LancamentoDtoResponse
import com.tiagotibaes.ponto.controller.lancamento.dto.LancamentoDtoResquest
import com.tiagotibaes.ponto.documents.Lancamento
import com.tiagotibaes.ponto.enums.TypeEnum
import com.tiagotibaes.ponto.utils.ConverterDates
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult


@Component
class LancamentoConverter(val converterDates: ConverterDates) {

    fun converterDtoParaLancamento(lancamentoDtoRequest: LancamentoDtoResquest, result: BindingResult): Lancamento {

        return Lancamento(  lancamentoDtoRequest.id,
            lancamentoDtoRequest.descricao,
            lancamentoDtoRequest.localizacao,
            TypeEnum.valueOf(lancamentoDtoRequest.tipo!!),
            converterDates.converterStringToDate(lancamentoDtoRequest.data.toString()),
            lancamentoDtoRequest.funcionarioId!!
        )
    }

     fun converterLancamentoParaDto(lancamento: Lancamento) : LancamentoDtoResponse {

        return LancamentoDtoResponse(   lancamento.id,
            lancamento.descricao,
            lancamento.localizacao,
            lancamento.tipo.toString(),
            converterDates.converterDateToString(lancamento.data!!),
            lancamento.funcionarioId
        )
    }

}