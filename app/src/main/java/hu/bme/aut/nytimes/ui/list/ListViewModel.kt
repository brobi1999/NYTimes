package hu.bme.aut.nytimes.ui.list

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.nytimes.interactor.ArticleInteractor
import hu.bme.aut.nytimes.util.FailedToLoad
import hu.bme.aut.nytimes.util.NetworkLost
import hu.bme.aut.nytimes.util.Period
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val articleInteractor: ArticleInteractor
) : RainbowCakeViewModel<ListViewState>(
    initialState =
    ListViewState(
        articles = mutableListOf(),
        isLoading = false,
        isInitial = true,
        period = Period.LAST_DAY
    )
) {

    fun load() = execute {
        if(viewState.isInitial)
            viewState.isInitial = false
        try {
            var dbArticles = articleInteractor.loadFromDatabase(viewState.period)
            if(dbArticles.isEmpty()){
                viewState = viewState.copy(isLoading = true)
                articleInteractor.refreshDatabaseFromNetwork(viewState.period)
                dbArticles = articleInteractor.loadFromDatabase(viewState.period)
            }
            else{
                viewState = viewState.copy(articles = dbArticles, isLoading = true)
                articleInteractor.refreshDatabaseFromNetwork(viewState.period)
                dbArticles = articleInteractor.loadFromDatabase(viewState.period)
            }
            viewState = viewState.copy(articles = dbArticles)
        }
        catch (e: Exception){
            e.printStackTrace()
            postEvent(FailedToLoad)
        }
        finally {
            viewState = viewState.copy(isLoading = false)
        }
    }

    fun setPeriod(period: Period){
        viewState = viewState.copy(period = period)
    }


    fun networkLost(){
        postEvent(NetworkLost)
    }

}