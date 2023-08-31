package ro.ase.chirita.xscrypt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ro.ase.chirita.xscrypt.data.repository.WalletRepositoryImpl
import ro.ase.chirita.xscrypt.domain.repository.WalletRepository

@Module
@InstallIn(ViewModelComponent::class)
object WalletModule {
    @Provides
    fun provideWalletRepository(walletRepositoryImpl: WalletRepositoryImpl): WalletRepository {
        return walletRepositoryImpl
    }

}