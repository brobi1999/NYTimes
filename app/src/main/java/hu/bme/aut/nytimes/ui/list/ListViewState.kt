package hu.bme.aut.nytimes.ui.list

import hu.bme.aut.nytimes.model.ui.Article

data class ListViewState(var articles: List<Article>, var isNetworkAvailable: Boolean)

