package ro.ase.chirita.xscrypt.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ro.ase.chirita.xscrypt.domain.model.MedicalNewsResponse

interface MedicalNewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): MedicalNewsResponse
}