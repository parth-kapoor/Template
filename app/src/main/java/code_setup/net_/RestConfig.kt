
import code_setup.app_util.Prefs
import code_setup.net_.NetworkConstant
import com.electrovese.kotlindemo.networking.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestConfig() {

    companion object Factory {
        fun create(): ApiInterface {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(3, TimeUnit.MINUTES)
            httpClient.connectTimeout(3, TimeUnit.MINUTES)
            httpClient.writeTimeout(3, TimeUnit.MINUTES)
            httpClient.addInterceptor(logging)
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.BASE_URL)
                .client(httpClient.build())
                .build()

            return retrofit.create<ApiInterface>(ApiInterface::class.java)
        }


        fun createGoogleApi(): ApiInterface {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(3, TimeUnit.MINUTES)
            httpClient.connectTimeout(3, TimeUnit.MINUTES)
            httpClient.writeTimeout(3, TimeUnit.MINUTES)
            httpClient.addInterceptor(logging)
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.Companion.Environment.GOOGLE.url)
                .client(httpClient.build())
                .build()

            return retrofit.create<ApiInterface>(ApiInterface::class.java)
        }
    }


}
