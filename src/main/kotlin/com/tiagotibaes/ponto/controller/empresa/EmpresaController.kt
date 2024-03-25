package com.tiagotibaes.ponto.controller.empresa


import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResponse
import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResquest
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Empresa
import com.tiagotibaes.ponto.services.EmpresaService
import com.tiagotibaes.ponto.services.converters.EmpresaConverter
import com.tiagotibaes.ponto.services.validates.EmpresaValidate

import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService, val empresaConverter: EmpresaConverter, val empresaValidate: EmpresaValidate) {


    @PostMapping
    fun salvar(@Valid @RequestBody empresaDtoResquest: EmpresaDtoResquest, result: BindingResult) :
            ResponseEntity<GenericsResponse<EmpresaDtoResponse>>{

        //FIXME: Validação do DTO
        val response: GenericsResponse<EmpresaDtoResponse> = GenericsResponse<EmpresaDtoResponse>()

        //FIXME: Validação do funcionário
        empresaValidate.validarEmpresa(empresaDtoResquest, result)

        //FIXME: Tratamento do erro
        if(result.hasErrors()){
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val empresa: Empresa = empresaConverter.converterDtoParaEmpresa(empresaDtoResquest, result)

        val empresaCreated: Empresa = empresaService.criar(empresa)

        response.data = empresaConverter.converterEmpresaDto(empresa)

        //TODO: Retornar Status Code correto = 201 - created
        return ResponseEntity.ok (response)
    }

    @GetMapping("/cnpj/{cnpj}")
    fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<GenericsResponse<EmpresaDtoResponse>> {

        val response: GenericsResponse<EmpresaDtoResponse> = GenericsResponse<EmpresaDtoResponse>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null) {
            response.erros.add("Empresa não econtrada para o CNPJ ${cnpj}")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = empresaConverter.converterEmpresaDto(empresa)
        return ResponseEntity.ok(response)
    }

}