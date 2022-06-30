package hu.bme.aut.nytimes.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.nytimes.database.ArticleDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideArticleDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ArticleDatabase::class.java,
        "article-db"
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideYourDao(db: ArticleDatabase) = db.articleDao()
}