package lt.vaidas.argyledemo.links

import retrofit2.http.GET
import retrofit2.http.Query

interface LinksService {
    @GET("search/link-items")
    suspend fun fetchLinks(@Query("limit") limit: Int, @Query("q") query: String?): LinksResponse
}
