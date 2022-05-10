package fastcampus.aop.part3.chapter04.publicApi

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.part3.chapter04.publicApi.dao.HistoryDao
import fastcampus.aop.part3.chapter04.publicApi.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}