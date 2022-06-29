package hu.bme.aut.nytimes.ui.list

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.nytimes.model.domain.MostViewedResponse
import hu.bme.aut.nytimes.model.domain.Results
import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.network.NYTimesRepo
import hu.bme.aut.nytimes.util.Period
import javax.inject.Inject

class ListPresenter @Inject constructor(
    private val nyTimesRepo: NYTimesRepo
) {
    suspend fun loadFromNetwork(period: Period): List<Article> = withIOContext{
        val days = periodToDays(period)
        val response = nyTimesRepo.getMostViewedList(days)
        convertMostViewedResponseToArticleList(response)
    }

    private fun periodToDays(period: Period): Int{
        return when(period){
            Period.LAST_DAY -> 1
            Period.LAST_7_DAYS -> 7
            Period.LAST_30_DAYS -> 30
        }
    }

    private fun convertMostViewedResponseToArticleList(response: MostViewedResponse): List<Article>{
        val articleList = mutableListOf<Article>()
        for(result in response.results){
            val article = convertDomainResultsToArticle(result)
            article?.let {
                articleList.add(article)
            }
        }
        return articleList
    }

    private fun convertDomainResultsToArticle(result: Results): Article? {
        if (result.uri == null)
            return null
        var smallImgUrl = ""
        var mediumImgUrl = ""
        var largeImgUrl = ""
        if(result.media.size != 0){
            val media = result.media[0]
            if(media.mediaMetadata.size == 3){
                smallImgUrl = media.mediaMetadata[0].url ?: ""
                mediumImgUrl = media.mediaMetadata[1].url ?: ""
                largeImgUrl = media.mediaMetadata[2].url ?: ""
            }
        }

        return Article(
            id = result.uri.toString(),
            title = result.title.toString(),
            byLine = result.byline.toString(),
            dateString = result.publishedDate.toString(),
            smallImgUrl = smallImgUrl,
            mediumImgUrl = mediumImgUrl,
            largeImgUrl = largeImgUrl
        )
    }
}