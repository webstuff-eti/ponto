package com.tiagotibaes.ponto.controller.lancamento


import com.tiagotibaes.ponto.controller.lancamento.dto.LancamentoDtoResquest
import com.tiagotibaes.ponto.controller.lancamento.dto.LancamentoDtoResponse
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Lancamento

import com.tiagotibaes.ponto.services.LancamentoService
import com.tiagotibaes.ponto.services.converters.LancamentoConverter
import com.tiagotibaes.ponto.services.validates.FuncionarioValidate

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

import javax.validation.Valid


@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController( val lancamentoService: LancamentoService,
                            val funcionarioValidate: FuncionarioValidate,
                            val lancamentoConverter: LancamentoConverter) {

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping //@Valid
    fun adicionar(@Valid @RequestBody lancamentoDtoResquest: LancamentoDtoResquest,
                  result: BindingResult) : ResponseEntity<GenericsResponse<LancamentoDtoResponse>>{

        //FIXME: Validação do DTO
        val response: GenericsResponse<LancamentoDtoResponse> = GenericsResponse<LancamentoDtoResponse>()

        //TODO: Testar
        funcionarioValidate.validarFuncionario(lancamentoDtoResquest, result)

        //FIXME: Tratamento do erro
        if(result.hasErrors()){
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = lancamentoConverter.converterDtoParaLancamento(lancamentoDtoResquest, result)
        val lancamentoCreted: Lancamento = lancamentoService.criar(lancamento)

        response.data = lancamentoConverter.converterLancamentoParaDto(lancamentoCreted)

        //TODO: Retornar Status Code correto = 201 - created
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody lancamentoDtoResquest: LancamentoDtoResquest,
                  result: BindingResult): ResponseEntity<GenericsResponse<LancamentoDtoResponse>>{

        val response: GenericsResponse<LancamentoDtoResponse> = GenericsResponse<LancamentoDtoResponse>()

        //TODO: Testar
        funcionarioValidate.validarFuncionario(lancamentoDtoResquest, result)
        validaLancamentoParaAtualizacao(id, result)

        lancamentoDtoResquest.id = id

        val lancamento: Lancamento = lancamentoConverter.converterDtoParaLancamento(lancamentoDtoResquest, result)

        if (result.hasErrors()) {
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamentoCreted: Lancamento = lancamentoService.criar(lancamento)
        response.data = lancamentoConverter.converterLancamentoParaDto(lancamentoCreted)

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

        response.data = lancamentoConverter.converterLancamentoParaDto(lancamentoSearched)

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
            lancamentos.map { lancamento -> lancamentoConverter.converterLancamentoParaDto (lancamento) }

        response.data = lancamentosDto
        return ResponseEntity.ok(response)
    }


}