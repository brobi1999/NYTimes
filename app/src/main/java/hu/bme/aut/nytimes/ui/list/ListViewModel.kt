package hu.bme.aut.nytimes.ui.list

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.nytimes.interactor.ArticleInteractor
import hu.bme.aut.nytimes.util.FinishLoading
import hu.bme.aut.nytimes.util.Period
import hu.bme.aut.nytimes.util.StartLoading
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val articleInteractor: ArticleInteractor
) : RainbowCakeViewModel<ListViewState>(initialState = ListViewState(articles = mutableListOf(), isNetworkAvailable = true)) {

    fun load(period: Period) = execute {
        try {
            var dbArticles = articleInteractor.loadFromDatabase(period)
            if(dbArticles.isEmpty()){
                postEvent(StartLoading)
                articleInteractor.refreshDatabaseFromNetwork(period)
                dbArticles = articleInteractor.loadFromDatabase(period)
            }
            else{
                viewState = viewState.copy(articles = dbArticles)
                postEvent(StartLoading)
                articleInteractor.refreshDatabaseFromNetwork(period)
                dbArticles = articleInteractor.loadFromDatabase(period)
            }
            viewState = viewState.copy(articles = dbArticles)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        finally {
            postEvent(FinishLoading)
        }
    }


    fun networkAvailable() {
        viewState = viewState.copy(isNetworkAvailable = true)
    }

    fun networkLost(){
        viewState = viewState.copy(isNetworkAvailable = false)
        //TODO - post event - snackbar
    }

}