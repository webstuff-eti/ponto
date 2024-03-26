package com.tiagotibaes.ponto.utils

import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ConverterDates {

    //TODO: Mover para o pacote Utils e criar uma classe DataConverterUtils
     fun converterStringToDate(testeData: String) : Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: Date = formatter.parse(testeData)

        return dateConverted;
    }


    //TODO: Mover para o pacote Utils e criar uma classe DataConverterUtils
     fun converterDateToString(testeData: Date) : String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: String = formatter.format(testeData)

        return dateConverted;
    }
}