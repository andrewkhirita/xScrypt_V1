package ro.ase.chirita.xscrypt.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ro.ase.chirita.xscrypt.core.MyTypeConverters
import ro.ase.chirita.xscrypt.data.source.local.dao.MedicalIdDao
import ro.ase.chirita.xscrypt.data.source.local.entity.MedicalIdEntity

@TypeConverters(value = [MyTypeConverters::class])
@Database(entities = [MedicalIdEntity::class], version = 1, exportSchema = false)
abstract class MedicalIdDatabase: RoomDatabase() {

    abstract fun getMedicalIdDao(): MedicalIdDao

    companion object {
        const val DB_NAME = "medicalId.db"
    }
}