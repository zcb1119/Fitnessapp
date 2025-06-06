package com.example.fitnessapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText dateInput;
    private TextView indoorResult, outdoorResult, totalResult;
    private int indoorCalories = 0, outdoorCalories = 0;
    private DatabaseHelper dbHelper;
    private LinearLayout datePickerLayout;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateInput = findViewById(R.id.dateInput);
        indoorResult = findViewById(R.id.indoorResult);
        outdoorResult = findViewById(R.id.outdoorResult);
        totalResult = findViewById(R.id.totalResult);
        Button indoorButton = findViewById(R.id.indoorButton);
        Button outdoorButton = findViewById(R.id.outdoorButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button historyButton = findViewById(R.id.historyButton);
        datePickerLayout = findViewById(R.id.datePickerLayout);

        dbHelper = new DatabaseHelper(this);
        selectedDate = Calendar.getInstance();
        updateDateInput();

        // 日期选择器
        datePickerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // 室内运动按钮点击事件
        indoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IndoorActivity.class);
                indoorLauncher.launch(intent);
            }
        });

        // 室外运动按钮点击事件
        outdoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OutdoorActivity.class);
                outdoorLauncher.launch(intent);
            }
        });

        // 保存记录按钮点击事件
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecord();
            }
        });

        // 历史记录按钮点击事件
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    // 显示日期选择器
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateInput();
                    }
                },
                year, month, day
        );
        dialog.show();
    }

    // 更新日期输入框显示
    private void updateDateInput() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        dateInput.setText(dateFormat.format(selectedDate.getTime()));
    }

    // 处理室内运动结果
    private ActivityResultLauncher<Intent> indoorLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            indoorCalories = data.getIntExtra("calories", 0);
                            indoorResult.setText("室内运动消耗: " + indoorCalories + " 卡路里");
                            updateTotalResult();
                        }
                    }
                }
            });

    // 处理室外运动结果
    private ActivityResultLauncher<Intent> outdoorLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            outdoorCalories = data.getIntExtra("calories", 0);
                            outdoorResult.setText("室外运动消耗: " + outdoorCalories + " 卡路里");
                            updateTotalResult();
                        }
                    }
                }
            });

    // 更新总消耗显示
    private void updateTotalResult() {
        int total = indoorCalories + outdoorCalories;
        totalResult.setText("总消耗: " + total + " 卡路里");
    }

    // 保存记录到数据库
    private void saveRecord() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = dateFormat.format(selectedDate.getTime());
        int total = indoorCalories + outdoorCalories;

        if (total == 0) {
            Toast.makeText(this, "没有运动数据可保存", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据卡路里判断健身效果并显示提示
        String fitnessTip;
        if (total > 500) {
            fitnessTip = "运动量达标，明天可以休息一下！\uD83D\uDE0A";
        } else if (total >= 300) {
            fitnessTip = "今天是健康的一天！\uD83D\uDE0A\uD83D\uDCAA";
        } else {
            fitnessTip = "运动量小，明天还要继续加油！";
        }
        Toast.makeText(this, fitnessTip, Toast.LENGTH_LONG).show();

        // 保存记录到数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME + " (" +
                DatabaseHelper.COLUMN_DATE + ", " +
                DatabaseHelper.COLUMN_CALORIES + ") VALUES (?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, date);
        stmt.bindLong(2, total);
        stmt.executeInsert();
        db.close();

        Toast.makeText(this, "记录保存成功", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    // 清空输入框和结果
    private void clearFields() {
        indoorCalories = 0;
        outdoorCalories = 0;
        indoorResult.setText("室内运动消耗: 0 卡路里");
        outdoorResult.setText("室外运动消耗: 0 卡路里");
        totalResult.setText("总消耗: 0 卡路里");
    }
}