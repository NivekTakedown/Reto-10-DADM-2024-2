package unal.edu.co.reto9

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("resource/d856-btkz.json")
    suspend fun getAcaciasPOIs(
        @Header("X-App-Token") appToken: String
    ): Response<List<AcaciasPOI>>
}