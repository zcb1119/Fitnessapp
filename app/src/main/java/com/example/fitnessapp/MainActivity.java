package com.example.fitnessapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText dateInput;
    private TextView indoorResult, outdoorResult, totalResult;
    private int indoorCalories = 0;
    private int outdoorCalories = 0;
    private Calendar selectedDate;
    private DatabaseHelper dbHelper;

    private static final int INDOOR_REQUEST_CODE = 1;
    private static final int OUTDOOR_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        dateInput = findViewById(R.id.dateInput);
        indoorResult = findViewById(R.id.indoorResult);
        outdoorResult = findViewById(R.id.outdoorResult);
        totalResult = findViewById(R.id.totalResult);
        dbHelper = new DatabaseHelper(this);
        selectedDate = Calendar.getInstance();

        updateDateDisplay();
        initButtons();
        initDatePicker();
    }

    private void initButtons() {
        // 室内运动按钮
        Button indoorButton = findViewById(R.id.indoorButton);
        indoorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IndoorActivity.class);
            startActivityForResult(intent, INDOOR_REQUEST_CODE);
        });

        // 室外运动按钮
        Button outdoorButton = findViewById(R.id.outdoorButton);
        outdoorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OutdoorActivity.class);
            startActivityForResult(intent, OUTDOOR_REQUEST_CODE);
        });

        // 保存记录按钮
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveRecord());

        // 历史记录按钮
        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // 健康知识按钮
        Button healthTipsButton = findViewById(R.id.healthTipsButton);
        healthTipsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HealthTipsActivity.class);
            startActivity(intent);
        });
    }

    private void initDatePicker() {
        findViewById(R.id.datePickerLayout).setOnClickListener(v -> {
            // 创建带有确认按钮的日期选择器
            DatePickerDialog dialog = new DatePickerDialog(
                    MainActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog, // 使用系统默认对话框主题
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateDisplay();
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );

            // 显示对话框
            dialog.show();
            dialog.setTitle("选择日期");
        });
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        dateInput.setText(dateFormat.format(selectedDate.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == INDOOR_REQUEST_CODE && data != null) {
                indoorCalories = data.getIntExtra("calories", 0);
                indoorResult.setText("室内运动消耗: " + indoorCalories + " 卡路里");
            } else if (requestCode == OUTDOOR_REQUEST_CODE && data != null) {
                outdoorCalories = data.getIntExtra("calories", 0);
                outdoorResult.setText("室外运动消耗: " + outdoorCalories + " 卡路里");
            }
            updateTotalResult();
        }
    }

    private void updateTotalResult() {
        int total = indoorCalories + outdoorCalories;
        totalResult.setText("总消耗: " + total + " 卡路里");

        // 根据卡路里总量给出健康提示
        showCalorieTips(total);
    }

    private void showCalorieTips(int calories) {
        String tips;
        if (calories < 300) {
            tips = "今日运动量有些少哦！继续加油\uD83D\uDE0A";
        } else if (calories >= 300 && calories <= 500) {
            tips = "今日运动量适中，继续保持！\uD83C\uDFC3 \uD83C\uDFCB\uFE0F\u200D♂\uFE0F \uD83C\uDFCA\u200D♀\uFE0F \uD83D\uDEB4";
        } else {
            tips = "今日运动量充足，恭喜达成目标！注意运动后拉伸补充水分\uD83C\uDF1F";
        }

        Toast.makeText(this, tips, Toast.LENGTH_LONG).show();
    }

    private void saveRecord() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = dateFormat.format(selectedDate.getTime());
        int total = indoorCalories + outdoorCalories;

        if (total == 0) {
            Toast.makeText(this, "没有记录任何运动", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存到数据库
        long rowId = dbHelper.insertRecord(date, total);
        if (rowId != -1) {
            Toast.makeText(this, "记录已保存", Toast.LENGTH_SHORT).show();
            // 重置结果
            indoorCalories = 0;
            outdoorCalories = 0;
            updateTotalResult();
            indoorResult.setText("室内运动消耗: 0 卡路里");
            outdoorResult.setText("室外运动消耗: 0 卡路里");
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}