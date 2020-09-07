package code_setup.app_util.di

import com.electrovese.kotlindemo.networking.ApiInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by arischoice on 20/1/2019.
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun providesEndpoints(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)
}