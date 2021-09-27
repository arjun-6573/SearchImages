package com.example.searchimages.di

import android.content.Context
import com.example.searchimages.external.remote.MyApi
import com.example.searchimages.external.remote.RequestInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import com.example.searchimages.BuildConfig
import com.example.searchimages.data.datasources.RemoteDataSourceImpl
import com.example.searchimages.data.mappers.DataMapper
import com.example.searchimages.data.repositories.SearchRepositoryImpl
import com.example.searchimages.domain.datasources.RemoteDataSource
import com.example.searchimages.domain.repositories.SearchRepository
import com.example.searchimages.domain.usecases.SearchImageUseCase
import com.example.searchimages.ui.mappers.UIDataMapper
import com.example.searchimages.utils.dispatcher.MyDispatchers
import com.example.searchimages.utils.dispatcher.MyDispatchersImpl
import com.example.searchimages.utils.espresso.EspressoIdleResourceImpl
import com.example.searchimages.utils.espresso.EspressoIdleResources
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Cache
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor {
            Timber.tag("OK-HTTP").i(it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpCache(@ApplicationContext context: Context): Cache {
//        2 MB cache file
        val file = File(context.cacheDir, "offline_cache")
        return Cache(file, (2 * 1024 * 1024))
    }

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        interceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestInterceptor(context))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .cache(cache)
            .build()

    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): MyApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().run {
                create(MyApi::class.java)
            }
    }

    @Singleton
    @Provides
    fun provideDispatcher(): MyDispatchers {
        return MyDispatchersImpl()
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        myApi: MyApi,
        dataMapper: DataMapper,
        dispatchers: MyDispatchers
    ): RemoteDataSource {
        return RemoteDataSourceImpl(myApi, dispatchers, dataMapper)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(remoteDataSource: RemoteDataSource): SearchRepository {
        return SearchRepositoryImpl(remoteDataSource)
    }


    @Singleton
    @Provides
    fun provideDataMapper(): DataMapper {
        return DataMapper()
    }

    @Singleton
    @Provides
    fun provideUIDataMapper(): UIDataMapper {
        return UIDataMapper()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideSearchImageUseCase(repository: SearchRepository): SearchImageUseCase {
        return SearchImageUseCase(repository)
    }

}

@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    @Provides
    @Singleton
    fun bindEspressoIdleResources(): EspressoIdleResources {
        return EspressoIdleResourceImpl()
    }
}