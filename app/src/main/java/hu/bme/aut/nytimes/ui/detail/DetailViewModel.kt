package hu.bme.aut.nytimes.ui.detail

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.nytimes.interactor.ArticleInteractor
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val articleInteractor: ArticleInteractor
) : RainbowCakeViewModel<DetailViewState>(
    DetailViewState(
        article = null,
        isLoading = false,
        isError = false,
        isInitial = true,
        isNetworkLost = false
    )
) {

    fun loadArticle(id: String) = execute {
        try {
            viewState = viewState.copy(isInitial = false, isLoading = true, isError = false)
            val article = articleInteractor.loadArticle(id)
            viewState = viewState.copy(article = article, isLoading = false)
        }
        catch (e: Exception){
            e.printStackTrace()
            viewState = viewState.copy(isError = true, isLoading = false)
        }


    }

    fun networkLost() {
        viewState = viewState.copy(isNetworkLost = true)
    }

    fun networkAvailable() {
        viewState = viewState.copy(isNetworkLost = false)
    }

}