package hu.bme.aut.nytimes.model.ui

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "by_line") val byLine: String,
    @ColumnInfo(name = "date_string") val dateString: String,
    @ColumnInfo(name = "img_url") val imgUrl: String,
    @ColumnInfo(name = "period_in_days") var periodInDays: Int? = null
)
