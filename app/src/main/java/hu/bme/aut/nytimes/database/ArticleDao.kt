package hu.bme.aut.nytimes.database

import androidx.room.*
import hu.bme.aut.nytimes.model.ui.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles : List<Article>)

    @Query("select * from articles where period_in_days = :days")
    suspend fun getAllFromLastDays(days: Int): List<Article>

    @Query("delete from articles where period_in_days = :days")
    suspend fun clearAllFromLastDays(days: Int)

    @Query("select * from articles where id = :id")
    suspend fun getArticleById(id: String): Article
}