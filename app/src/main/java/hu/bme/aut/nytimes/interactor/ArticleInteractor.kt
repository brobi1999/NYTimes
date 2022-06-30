package hu.bme.aut.nytimes.interactor

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.nytimes.database.ArticleDao
import hu.bme.aut.nytimes.model.domain.MostViewedResponse
import hu.bme.aut.nytimes.model.domain.Results
import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.network.NYTimesRepo
import hu.bme.aut.nytimes.util.Period
import hu.bme.aut.nytimes.util.periodToDays
import javax.inject.Inject

class ArticleInteractor @Inject constructor(
    private val articleDao: ArticleDao,
    private val nyTimesRepo: NYTimesRepo
) {

    private suspend fun load(period: Period): List<Article> = withIOContext{
        val days = periodToDays(period)
        val response = nyTimesRepo.getMostViewedList(days)
        val articles = convertMostViewedResponseToArticleList(response)
        for(article in articles)
            article.periodInDays = days
        articles
    }

    suspend fun loadFromDatabase(period: Period): List<Article> {
        return articleDao.getAllFromLastDays(periodToDays(period))
    }

    suspend fun refreshDatabaseFromNetwork(period: Period) {
        val networkArticles = load(period)
        articleDao.insertAll(networkArticles)
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

        var imgUrl = ""
        if(result.media.size != 0){
            val media = result.media[0]
            if(media.mediaMetadata.size == 3){
                imgUrl = media.mediaMetadata[2].url ?: ""
            }
        }

        return Article(
            id = result.uri.toString(),
            url = result.url.toString(),
            title = result.title.toString(),
            byLine = result.byline.toString(),
            dateString = result.publishedDate.toString(),
            imgUrl = imgUrl
        )
    }
}