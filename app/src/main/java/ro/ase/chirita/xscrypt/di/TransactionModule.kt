package ro.ase.chirita.xscrypt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ro.ase.chirita.xscrypt.data.repository.TransactionRepositoryImpl
import ro.ase.chirita.xscrypt.domain.repository.TransactionRepository

@Module
@InstallIn(ViewModelComponent::class)
object TransactionModule {

    @Provides
    fun provideTransactionRepository(trasanctionRepository: TransactionRepositoryImpl): TransactionRepository {
        return trasanctionRepository
    }
}