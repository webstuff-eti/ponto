package com.tiagotibaes.ponto.controller

import com.tiagotibaes.ponto.controller.dto.request.LancamentoDtoResquest
import com.tiagotibaes.ponto.controller.dto.response.LancamentoDtoResponse
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Funcionario
import com.tiagotibaes.ponto.documents.Lancamento
import com.tiagotibaes.ponto.enums.TypeEnum
import com.tiagotibaes.ponto.services.FuncionarioService
import com.tiagotibaes.ponto.services.LancamentoService

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.text.SimpleDateFormat
import java.util.*

//import javax.validation.Valid



@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController( val lancamentoService: LancamentoService,
                            val funcionarioService: FuncionarioService) {

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping //@Valid
    fun adicionar(@RequestBody lancamentoDtoResquest: LancamentoDtoResquest,
                  result: BindingResult) : ResponseEntity<GenericsResponse<LancamentoDtoResponse>>{

        //FIXME: Validação do DTO
        val response: GenericsResponse<LancamentoDtoResponse> = GenericsResponse<LancamentoDtoResponse>()

        //FIXME: Validação do funcionário
        validarFuncionario(lancamentoDtoResquest, result)

        //FIXME: Tratamento do erro
        if(result.hasErrors()){
//            for(erro in result.allErrors) response.erros.add(erro.defaultMessage)
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDtoResquest, result)

        val lancamentoCreted: Lancamento = lancamentoService.criar(lancamento)

        response.data = converterLancamentoParaDto(lancamentoCreted)

        return ResponseEntity.ok(response)
    }


    //FIXME: Método para validar Funcionário
    private fun validarFuncionario(lancamentoDtoResquest: LancamentoDtoResquest, result: BindingResult){
        if(lancamentoDtoResquest.funcionarioId == null){
            result.addError(ObjectError("funcionário", "Funcionário não informado"))
            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDtoResquest.funcionarioId)
        if(funcionario == null){
            result.addError(ObjectError("funcionário", "Funcionário não encontrado. ID inexistente"))
        }
    }



    //FIXME: Métodos para converter Dtos
    private fun converterDtoParaLancamento(lancamentoDtoRequest: LancamentoDtoResquest,
                                           result: BindingResult): Lancamento {

        //TODO: Trecho abaixo será utilizado somente para operação de UPDATE
//        if (lancamentoDtoRequest.id != null) {
//            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDtoRequest.id!!)
//            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
//        }

        val dataLancamento: Date? = lancamentoDtoRequest.data?.let { converterStringToDate(it) }

        return Lancamento(  lancamentoDtoRequest.id,
            lancamentoDtoRequest.descricao,
            lancamentoDtoRequest.localizacao,
            TypeEnum.valueOf(lancamentoDtoRequest.tipo!!),
            dataLancamento,
            lancamentoDtoRequest.funcionarioId!!
        )

    }

    private fun converterLancamentoParaDto(lancamento: Lancamento) : LancamentoDtoResponse {

        val dataLancamento: String? = lancamento.data?.let {converterDateToString(it)}

        return LancamentoDtoResponse(   lancamento.id,
            lancamento.descricao,
            lancamento.localizacao,
            lancamento.tipo.toString(),
            dataLancamento,
            lancamento.funcionarioId
        )

    }

    //TODO: Mover para o pacote Utils e criar uma classe DataConverterUtils
    private fun converterStringToDate(testeData: String) : Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: Date = formatter.parse(testeData)

        return dateConverted;
    }


    //TODO: Mover para o pacote Utils e criar uma classe DataConverterUtils
    private fun converterDateToString(testeData: Date) : String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: String = formatter.format(testeData)

        return dateConverted;
    }



}