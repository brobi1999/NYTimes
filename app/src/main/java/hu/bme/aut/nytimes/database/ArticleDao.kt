package hu.bme.aut.nytimes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hu.bme.aut.nytimes.model.ui.Article
import hu.bme.aut.nytimes.util.Period

@Dao
interface ArticleDao {

    @Insert
    suspend fun insertAll(articles : List<Article>)

    @Query("select * from articles where period_in_days = :days")
    suspend fun getAllFromLastDays(days: Int): List<Article>
}