package hu.bme.aut.nytimes.ui.list

import hu.bme.aut.nytimes.model.ui.Article

sealed class ListViewState

object Loading : ListViewState()

data class ListLoadedFromNetwork(val articles: List<Article>): ListViewState()
