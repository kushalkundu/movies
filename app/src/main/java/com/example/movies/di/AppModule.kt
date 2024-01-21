package com.example.movies.di

import android.content.Context
import androidx.room.Room
import com.example.movies.network.ApiInterface
import com.example.movies.network.URL
import com.example.movies.network.repository.MovieRepository
import com.example.movies.network.repositoryImpl.MoviesRepositoryImpl
import com.example.movies.network.useCases.GenreListUseCase
import com.example.movies.network.useCases.MovieDetailUseCase
import com.example.movies.network.useCases.MovieListUseCase
import com.example.movies.network.useCases.MoviesUseCase
import com.example.movies.room.AppDatabase
import com.example.movies.room.daoImpl.GenreRepo
import com.example.movies.room.daoImpl.MovieDetailRepo
import com.example.movies.room.daoImpl.MovieListRepo
import com.example.movies.utils.Constants
import com.example.movies.utils.Constants.ACCEPT
import com.example.movies.utils.Constants.APPLICATION_JSON
import com.example.movies.utils.Constants.AUTHORIZATION
import com.example.movies.utils.Constants.TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // For Retrofit Instance
    @Provides
    @Singleton
    fun providesRetrofitInstance(@Named("WithOutCaching") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(URL.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // For Retrofit Client
    @Provides
    @Singleton
    @Named("WithOutCaching")
    fun provideOkHttpClientWithOut(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader(
                        AUTHORIZATION,
                        TOKEN
                    )
                    .addHeader(ACCEPT, APPLICATION_JSON)
                    .build()
                chain.proceed(newRequest)
            })
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )

            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    //For Api Interface
    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }


    // For Repository Implementation
    @Provides
    @Singleton
    fun provideMoviesRepositoryImpl(
        apiInterface: ApiInterface,
        movieListRepo: MovieListRepo
    ): MovieRepository {
        return MoviesRepositoryImpl(apiInterface, movieListRepo)
    }

    // For UseCases
    @Provides
    @Singleton
    fun providesMoviesUseCase(
        movieRepository: MovieRepository,
        movieDetailRepo: MovieDetailRepo
    ): MoviesUseCase {
        return MoviesUseCase(
            MovieListUseCase(movieRepository = movieRepository),
            GenreListUseCase(movieRepository = movieRepository),
            MovieDetailUseCase(
                movieRepository = movieRepository, movieDetailRepo = movieDetailRepo
            )
        )
    }

    // For Database Instance
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    // For DB Genre Repository
    @Singleton
    @Provides
    fun providesGenreRepo(database: AppDatabase): GenreRepo {
        return GenreRepo(database)
    }

    // For DB Movie List Repository
    @Singleton
    @Provides
    fun providesMovieListRepo(database: AppDatabase): MovieListRepo {
        return MovieListRepo(database)
    }

    // For DB Movie Detail Repository
    @Singleton
    @Provides
    fun providesMovieDetailRepo(database: AppDatabase): MovieDetailRepo {
        return MovieDetailRepo(database)
    }

}