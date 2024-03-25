package com.tiagotibaes.ponto.controller.lancamento


import com.tiagotibaes.ponto.controller.lancamento.dto.req.LancamentoDtoResquest
import com.tiagotibaes.ponto.controller.lancamento.dto.resp.LancamentoDtoResponse
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Funcionario
import com.tiagotibaes.ponto.documents.Lancamento
import com.tiagotibaes.ponto.enums.TypeEnum
import com.tiagotibaes.ponto.services.FuncionarioService
import com.tiagotibaes.ponto.services.LancamentoService

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.text.SimpleDateFormat
import java.util.*

import javax.validation.Valid



@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController( val lancamentoService: LancamentoService,
                            val funcionarioService: FuncionarioService) {

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping //@Valid
    fun adicionar(@Valid @RequestBody lancamentoDtoResquest: LancamentoDtoResquest,
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

        //TODO: Retornar Status Code correto = 201 - created
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody lancamentoDtoResquest: LancamentoDtoResquest,
                  result: BindingResult): ResponseEntity<GenericsResponse<LancamentoDtoResponse>>{

        val response: GenericsResponse<LancamentoDtoResponse> = GenericsResponse<LancamentoDtoResponse>()

        validarFuncionario(lancamentoDtoResquest, result)
        validaLancamentoParaAtualizacao(id, result)

        lancamentoDtoResquest.id = id

        val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDtoResquest, result)

        if (result.hasErrors()) {
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamentoCreted: Lancamento = lancamentoService.criar(lancamento)
        response.data =   converterLancamentoParaDto(lancamentoCreted)

        return ResponseEntity.ok(response)
    }



    @DeleteMapping(value = ["/{id}"])
    //@PreAuthorize("hasAnyRole('ADMIN')")
    fun remover(@PathVariable("id") id: String): ResponseEntity<GenericsResponse<String>> {

        val response: GenericsResponse<String> = GenericsResponse<String>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)
        return ResponseEntity.ok(GenericsResponse<String>())
    }


    //TODO: Trecho abaixo será utilizado somente para operação de UPDATE - TESTAR
    private fun validaLancamentoParaAtualizacao(id: String, result: BindingResult) {
        var lanc: Lancamento? = null

        if (id != null) {
            lanc = lancamentoService.buscarPorId(id!!)
        }
        if (lanc == null) {
            return result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }
    }


    @GetMapping("/{id}")
    fun listarPorId(@PathVariable("id") id: String) : ResponseEntity<GenericsResponse<LancamentoDtoResponse>> {

        //FIXME: Validação do DTO
        val response: GenericsResponse<LancamentoDtoResponse> = GenericsResponse<LancamentoDtoResponse>()

        val lancamentoSearched: Lancamento? = lancamentoService.buscarPorId(id)

        if(lancamentoSearched == null){
            response.erros.add("Lançamento encontrado para o id = $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoParaDto(lancamentoSearched)


        return ResponseEntity.ok(response)
    }


    @GetMapping("/funcionario/{funcionarioId}")
    fun listarPorFuncionarioId(@PathVariable("funcionarioId") funcionarioId: String,
                               @RequestParam(value = "pag", defaultValue = "0") pag: Int,
                               @RequestParam(value = "ord", defaultValue = "id") ord: String,
                               @RequestParam(value = "dir", defaultValue = "DESC") dir: String):
            ResponseEntity<GenericsResponse<Page<LancamentoDtoResponse>>> {

        val response: GenericsResponse<Page<LancamentoDtoResponse>> = GenericsResponse<Page<LancamentoDtoResponse>>()

        val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lancamentos: Page<Lancamento> =
            lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

        val lancamentosDto: Page<LancamentoDtoResponse> =
            lancamentos.map { lancamento -> converterLancamentoParaDto (lancamento) }

        response.data = lancamentosDto
        return ResponseEntity.ok(response)
    }





    //FIXME: Métodos para validações

    //FIXME: Métodos para validar Funcionário
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