package unal.edu.co.reto9

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("resource/d856-btkz.json")
    suspend fun getAcaciasPOIs(
        @Header("X-App-Token") appToken: String,
        @Query("\$limit") limit: Int = 1000,
        @Query("\$where") whereClause: String? = null
    ): Response<List<AcaciasPOI>>
}