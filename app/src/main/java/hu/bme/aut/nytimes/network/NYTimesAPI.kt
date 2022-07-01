package hu.bme.aut.nytimes.network

import hu.bme.aut.nytimes.model.domain.MostViewedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NYTimesAPI {

    @GET("/svc/mostpopular/v2/viewed/{days}.json")
    suspend fun getMostViewedArticleList(
        @Path("days") days: Int,
    ) : Response<MostViewedResponse>

}
