package ro.ase.chirita.xscrypt.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ro.ase.chirita.xscrypt.data.repository.MedicalNewsRepositoryImpl
import ro.ase.chirita.xscrypt.data.source.remote.MedicalNewsApi
import ro.ase.chirita.xscrypt.domain.repository.MedicalNewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicalNewsModule {

    @Provides
    @Singleton
    fun provideNewsApi(): MedicalNewsApi {
        val httpClient = OkHttpClient.Builder().build()

        val moshi = Moshi.Builder().build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(httpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

        return retrofit.create(MedicalNewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(medicalNewsApi: MedicalNewsApi): MedicalNewsRepository {
        return MedicalNewsRepositoryImpl(medicalNewsApi)
    }
}