package com.tiagotibaes.ponto.controller.empresa

import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResponse
import com.tiagotibaes.ponto.controller.response.GenericsResponse
import com.tiagotibaes.ponto.documents.Empresa
import com.tiagotibaes.ponto.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

    @GetMapping("/cnpj/{cnpj}")
    fun buscarPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<GenericsResponse<EmpresaDtoResponse>> {

        val response: GenericsResponse<EmpresaDtoResponse> = GenericsResponse<EmpresaDtoResponse>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null) {
            response.erros.add("Empresa não econtrada para o CNPJ ${cnpj}")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterEmpresaDto(empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterEmpresaDto(empresa: Empresa): EmpresaDtoResponse = EmpresaDtoResponse(empresa.razaoSocial, empresa.cnpj, empresa.id)

}