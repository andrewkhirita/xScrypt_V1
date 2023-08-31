package ro.ase.chirita.xscrypt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ro.ase.chirita.xscrypt.data.repository.KeyWalletRepositoryImpl
import ro.ase.chirita.xscrypt.domain.repository.KeyWalletRepository

@Module
@InstallIn(SingletonComponent::class)
object KeyWalletModule {

    @Provides
    fun provideKey(): KeyWalletRepository =
        KeyWalletRepositoryImpl()
}