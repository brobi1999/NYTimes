package hu.bme.aut.nytimes.model.ui

data class Article(
    val id: String,
    val title: String,
    val byLine: String,
    val dateString: String,
    val smallImgUrl: String,
    val mediumImgUrl: String,
    val largeImgUrl: String
)
