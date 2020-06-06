package com.mayumi.conversormoedas.service

import com.mayumi.conversormoedas.model.Moedas
import retrofit2.Response
import retrofit2.http.GET

interface WebAPI {
    @GET("latest")
    suspend fun getMoedas(): Response<Moedas>
}