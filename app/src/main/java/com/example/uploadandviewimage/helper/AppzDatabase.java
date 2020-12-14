package com.example.uploadandviewimage.helper;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Type.class}, version = 2)
public abstract class AppzDatabase extends RoomDatabase {
    public abstract TypeDAO typeDAO();

    @VisibleForTesting
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN created_at String");
        }
    };
    @VisibleForTesting
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN modifed_at String");
        }
    };


}
