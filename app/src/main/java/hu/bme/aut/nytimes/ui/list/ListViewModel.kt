package hu.bme.aut.nytimes.ui.list

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.nytimes.util.Period
import javax.inject.Inject



@HiltViewModel
class ListViewModel @Inject constructor(
    private val listPresenter: ListPresenter
) : RainbowCakeViewModel<ListViewState>(Loading) {

    fun loadFromNetwork(period: Period) = execute {
        try {
            viewState = Loading
            val articles = listPresenter.loadFromNetwork(period)
            viewState = ListLoadedFromNetwork(articles)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        finally {

        }

    }

}