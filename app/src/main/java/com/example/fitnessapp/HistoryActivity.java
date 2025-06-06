package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.historyListView);
        dbHelper = new DatabaseHelper(this);

        // 加载历史记录（按日期降序排列：最新的在前）
        loadHistoryRecords();
    }

    // 加载历史记录并按日期排序
    private void loadHistoryRecords() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询所有记录并按日期降序排列
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_DATE + " DESC" // 按日期降序排列
        );

        // 设置适配器
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_CALORIES},
                new int[]{android.R.id.text1, android.R.id.text2},
                0
        );

        historyListView.setAdapter(adapter);
    }
}