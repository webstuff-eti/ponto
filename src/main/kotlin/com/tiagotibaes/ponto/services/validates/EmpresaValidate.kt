package com.tiagotibaes.ponto.services.validates

import com.tiagotibaes.ponto.controller.empresa.dto.EmpresaDtoResquest
import com.tiagotibaes.ponto.enums.StatusSituacaoCadastral
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

@Component
class EmpresaValidate {

    public fun validarEmpresa(empresaDtoResquest: EmpresaDtoResquest, result: BindingResult): BindingResult {

        if(empresaDtoResquest.razaoSocial == null){
            result.addError(ObjectError("empresa", "Razão Social não informado"))
        } else if(empresaDtoResquest.cnpj == null){
            result.addError(ObjectError("empresa", "CNPJ não informado"))
        } else if(empresaDtoResquest.situacaoCadastral == null){
            result.addError(ObjectError("empresa", "Situação Cadastral não informado"))
        }

        val statusCadastral: Boolean = validaValoresSituacaoCadastral(empresaDtoResquest)
        if(  empresaDtoResquest.situacaoCadastral != null && !statusCadastral ){
            result.addError(ObjectError("empresa", "Situação Cadastral Inexistente"))
        }

        return result
    }

    private fun validaValoresSituacaoCadastral(empresaDtoResquest: EmpresaDtoResquest) : Boolean {

        return  ( StatusSituacaoCadastral.NULA.toString().equals( empresaDtoResquest.situacaoCadastral ) )  ||
                ( StatusSituacaoCadastral.ATIVA.toString().equals(empresaDtoResquest.situacaoCadastral) )   ||
                ( StatusSituacaoCadastral.INAPTA.toString().equals(empresaDtoResquest.situacaoCadastral) )  ||
                ( StatusSituacaoCadastral.BAIXADA.toString().equals(empresaDtoResquest.situacaoCadastral) ) ||
                ( StatusSituacaoCadastral.SUSPENSA.toString().equals( empresaDtoResquest.situacaoCadastral) )
    }
}