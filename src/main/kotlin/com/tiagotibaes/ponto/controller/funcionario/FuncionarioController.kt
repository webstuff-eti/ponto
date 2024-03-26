package com.tiagotibaes.ponto.controller.funcionario

import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResponse
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoRequest
import com.tiagotibaes.ponto.controller.funcionario.dto.FuncionarioDtoResponse
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Funcionario
import com.tiagotibaes.ponto.services.FuncionarioService
import com.tiagotibaes.ponto.services.converters.FuncionarioConverter
import com.tiagotibaes.ponto.services.validates.FuncionarioValidate
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api/funcionario")
class FuncionarioController(val funcionarioService: FuncionarioService, val funcionarioConverter: FuncionarioConverter, val funcionarioValidate: FuncionarioValidate) {

    @PostMapping
    fun salvar(@Valid @RequestBody funcionarioDtoRequest: FuncionarioDtoRequest, result: BindingResult) :
            ResponseEntity<GenericsResponse<FuncionarioDtoResponse>> {

        //FIXME: Validação do DTO
        val response: GenericsResponse<FuncionarioDtoResponse> = GenericsResponse<FuncionarioDtoResponse>()

        funcionarioValidate.validarDadoFuncionario(funcionarioDtoRequest, result)

        //FIXME: Tratamento do erro
        if(result.hasErrors()){
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val func: Funcionario = funcionarioConverter.converterDtoParaFuncionario(funcionarioDtoRequest)

        val funcionarioCreated: Funcionario = funcionarioService.criar(func)

        response.data = funcionarioConverter.converterFuncionarioParaDto(funcionarioCreated)

        return ResponseEntity.ok(response)

    }
}