package com.example.uploadandviewimage.roomdbGhistory;


import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GHistory.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GHistoryDao gHistoryDao();

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN created_at String");
        }
    };
}
