package com.example.quotes_uas.data.api

import com.example.quotes_uas.data.local.QuotesEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("quotes")
    suspend fun getAllQuotes(): Response<List<QuotesEntity>>

    @POST("quotes")
    suspend fun addQuote(@Body quote: QuoteRequest): Response<QuotesEntity>

    @DELETE("quotes/{id}")
    suspend fun deleteQuote(@Path("id") id: String): Response<ResponseBody>
}