package com.example.uploadandviewimage.roomdbGhistory;


import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GHistory.class, Gitem.class,Gindeks.class,GStatus.class}, version = 50)
public abstract class AppDatabase extends RoomDatabase {

    public abstract GHistoryDao gHistoryDao();

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbGrainHistory "
                    + " ADD COLUMN image String");
        }
    };

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN image String");
        }
    };

}
