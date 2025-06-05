package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class IndoorActivity extends AppCompatActivity {
    private EditText pushupsTime, situpsTime, plankTime, squatsTime, jumpingJacksTime;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);

        pushupsTime = findViewById(R.id.pushupsTime);
        situpsTime = findViewById(R.id.situpsTime);
        plankTime = findViewById(R.id.plankTime);
        squatsTime = findViewById(R.id.squatsTime);
        jumpingJacksTime = findViewById(R.id.jumpingJacksTime);
        resultText = findViewById(R.id.indoorResultText);
        Button calculateButton = findViewById(R.id.calculateIndoorButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    private void calculateCalories() {
        // 获取输入的时间（分钟）
        int pushups = getTimeValue(pushupsTime);
        int situps = getTimeValue(situpsTime);
        int plank = getTimeValue(plankTime);
        int squats = getTimeValue(squatsTime);
        int jumpingJacks = getTimeValue(jumpingJacksTime);

        // 计算卡路里（每类运动每分钟消耗的卡路里）
        // 数据来源：https://www.healthline.com/health/fitness-exercise/calories-burned-per-minute
        int pushupsCalories = pushups * 3;     // 俯卧撑每分钟约3卡路里
        int situpsCalories = (int) (situps * 2.5);     // 仰卧起坐每分钟约2.5卡路里
        int plankCalories = plank * 4;         // 平板支撑每分钟约4卡路里
        int squatsCalories = squats * 3.5;     // 深蹲每分钟约3.5卡路里
        int jumpingJacksCalories = jumpingJacks * 8; // 开合跳每分钟约8卡路里

        int totalCalories = pushupsCalories + situpsCalories +
                plankCalories + squatsCalories + jumpingJacksCalories;

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