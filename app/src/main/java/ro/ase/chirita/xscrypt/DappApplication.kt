package ro.ase.chirita.xscrypt

import android.app.Application
import com.elrond.erdkotlin.ErdSdk
import dagger.hilt.android.HiltAndroidApp
import okhttp3.logging.HttpLoggingInterceptor

@HiltAndroidApp
class DappApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ErdSdk.elrondHttpClientBuilder.apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }

}