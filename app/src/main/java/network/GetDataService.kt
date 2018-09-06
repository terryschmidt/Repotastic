package network

import model.EntityListWrapper
import model.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetDataService {
    //@GET("/api/amiibo/?language=figure")
    @GET("search/users?")
    fun getEntityListWrapper(@Query("q") q: String,
                             @Query("access_token") accessToken: String,
                             @Query("page") page: Int): Call<EntityListWrapper>

    @GET("users/{name}/repos")
    fun getReposByName(@Path("name") name: String,
                       @Query("page") page: Int): Call<List<Repo>>
}