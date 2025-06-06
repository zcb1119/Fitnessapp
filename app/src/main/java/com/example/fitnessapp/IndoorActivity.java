package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndoorActivity extends AppCompatActivity {
    private Spinner exerciseSpinner;
    private EditText durationInput;
    private Button calculateButton;
    private Map<String, Integer> exerciseCaloriesMap; // 运动项目与卡路里对照表
    private List<String> exerciseList;
    private int totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);

        exerciseSpinner = findViewById(R.id.indoorExerciseSpinner);
        durationInput = findViewById(R.id.indoorDurationInput);
        calculateButton = findViewById(R.id.indoorCalculateButton);

        // 初始化室内运动项目和卡路里对照表
        initExerciseData();

        // 设置下拉列表适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, exerciseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

        // 计算按钮点击事件
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    // 初始化室内运动项目数据
    private void initExerciseData() {
        exerciseList = new ArrayList<>();
        exerciseCaloriesMap = new HashMap<>();

        // 添加更多室内运动项目（每小时卡路里消耗）
        exerciseList.add("俯卧撑");
        exerciseCaloriesMap.put("俯卧撑", 300);

        exerciseList.add("仰卧起坐");
        exerciseCaloriesMap.put("仰卧起坐", 250);

        exerciseList.add("平板支撑");
        exerciseCaloriesMap.put("平板支撑", 200);

        exerciseList.add("跳绳");
        exerciseCaloriesMap.put("跳绳", 600);

        exerciseList.add("健身操");
        exerciseCaloriesMap.put("健身操", 400);

        exerciseList.add("瑜伽");
        exerciseCaloriesMap.put("瑜伽", 280);
    }

    // 计算卡路里
    private void calculateCalories() {
        String exercise = exerciseSpinner.getSelectedItem().toString();
        String durationStr = durationInput.getText().toString();

        if (durationStr.isEmpty()) {
            Toast.makeText(this, "请输入运动时间", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr);
            int caloriesPerHour = exerciseCaloriesMap.get(exercise);
            totalCalories = (int) (caloriesPerHour * (duration / 60.0));

            // 返回结果给主活动
            Intent resultIntent = new Intent();
            resultIntent.putExtra("calories", totalCalories);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }
}