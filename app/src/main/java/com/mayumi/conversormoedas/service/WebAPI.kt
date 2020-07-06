package com.mayumi.conversormoedas.service

import com.mayumi.conversormoedas.model.Moedas
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WebAPI {
    @GET("latest")
    suspend fun getMoedas(@Query("base") moedaAtual: String): Response<Moedas>
}


