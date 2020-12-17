package com.example.uploadandviewimage.helper;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GrainTypeData.class}, version = 4)
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
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN type_percent double");
        }
    };
    @VisibleForTesting
    public static final Migration MIGRATION_4_5 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN type_percent double");
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN type_value double" );
        }
    };

    @VisibleForTesting
    public static final Migration MIGRATION_5_6 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN nama_szie STRING");
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN size_value double" );
            database.execSQL("ALTER TABLE tbarang " + " ADD COLUMN type_value double" );
        }
    };


}
