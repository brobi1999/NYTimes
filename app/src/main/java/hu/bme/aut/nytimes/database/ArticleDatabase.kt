package hu.bme.aut.nytimes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.nytimes.model.ui.Article

@Database(
    entities = [Article::class], // Tell the database the entries will hold data of this type
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}