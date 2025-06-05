package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class OutdoorActivity extends AppCompatActivity {
    private EditText runningTime, cyclingTime, swimmingTime, hikingTime, walkingTime;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);

        runningTime = findViewById(R.id.runningTime);
        cyclingTime = findViewById(R.id.cyclingTime);
        swimmingTime = findViewById(R.id.swimmingTime);
        hikingTime = findViewById(R.id.hikingTime);
        walkingTime = findViewById(R.id.walkingTime);
        resultText = findViewById(R.id.outdoorResultText);
        Button calculateButton = findViewById(R.id.calculateOutdoorButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    private void calculateCalories() {
        // 获取输入的时间（分钟）
        int running = getTimeValue(runningTime);
        int cycling = getTimeValue(cyclingTime);
        int swimming = getTimeValue(swimmingTime);
        int hiking = getTimeValue(hikingTime);
        int walking = getTimeValue(walkingTime);

        // 计算卡路里（每类运动每分钟消耗的卡路里）
        // 数据来源：https://www.healthline.com/health/fitness-exercise/calories-burned-per-minute
        int runningCalories = running * 10;     // 跑步每分钟约10卡路里
        int cyclingCalories = cycling * 7;      // 骑自行车每分钟约7卡路里
        int swimmingCalories = swimming * 12;   // 游泳每分钟约12卡路里
        int hikingCalories = hiking * 5;        // 徒步每分钟约5卡路里
        int walkingCalories = walking * 3;      // 散步每分钟约3卡路里

        int totalCalories = runningCalories + cyclingCalories +
                swimmingCalories + hikingCalories + walkingCalories;

        // 显示结果
        resultText.setText("总消耗: " + totalCalories + " 卡路里");

        // 返回结果给主活动
        Intent resultIntent = new Intent();
        resultIntent.putExtra("calories", totalCalories);
        setResult(RESULT_OK, resultIntent);
    }

    private int getTimeValue(EditText editText) {
        String text = editText.getText().toString().trim();
        return TextUtils.isEmpty(text) ? 0 : Integer.parseInt(text);
    }
}