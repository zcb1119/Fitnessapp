package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        dbHelper = new DatabaseHelper(this);

        loadHistoryRecords();
    }

    private void loadHistoryRecords() {
        List<String> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_CALORIES
        };
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_DATE + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
                int calories = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CALORIES));
                records.add(date + " - " + calories + " 卡路里");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // 设置适配器显示记录
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                records
        );
        historyListView.setAdapter(adapter);
    }
}