package ro.ase.chirita.xscrypt.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ro.ase.chirita.xscrypt.data.repository.MedicalIdRepositoryImpl
import ro.ase.chirita.xscrypt.data.source.MedicalIdDatabase
import ro.ase.chirita.xscrypt.data.source.local.dao.MedicalIdDao
import ro.ase.chirita.xscrypt.domain.repository.MedicalIdRepository

@Module
@InstallIn(SingletonComponent::class)
object MedicalIdDatabaseModule {

    @Provides
    fun provideMedicalIdDao(database: MedicalIdDatabase): MedicalIdDao {
        return database.getMedicalIdDao()
    }

    @Provides
    internal fun provideMedicalIdDatabase(@ApplicationContext context: Context): MedicalIdDatabase {
        return Room.databaseBuilder(context, MedicalIdDatabase::class.java, MedicalIdDatabase.DB_NAME).build()
    }

    @Provides
    fun provideMedicalIdRepository(
        appDatabase: MedicalIdDatabase,
    ): MedicalIdRepository {
        return MedicalIdRepositoryImpl(appDatabase)
    }
}