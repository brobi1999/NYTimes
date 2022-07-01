package hu.bme.aut.nytimes.network

import hu.bme.aut.nytimes.model.domain.MostViewedResponse
import javax.inject.Inject

class NYTimesRepo @Inject constructor() {

    private val api = RetrofitHelper.getInstance().create(NYTimesAPI::class.java)

    suspend fun getMostViewedList(days: Int): MostViewedResponse{
        val response = api.getMostViewedArticleList(days)
        if(response.isSuccessful)
            return response.body()!!
        else
            throw RuntimeException("Retrofit call failed! " + response.raw())
    }



}