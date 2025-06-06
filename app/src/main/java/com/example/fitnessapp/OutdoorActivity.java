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

public class OutdoorActivity extends AppCompatActivity {
    private Spinner exerciseSpinner;
    private EditText durationInput;
    private Button calculateButton;
    private Map<String, Integer> exerciseCaloriesMap; // 运动项目与卡路里对照表
    private List<String> exerciseList;
    private int totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);

        exerciseSpinner = findViewById(R.id.outdoorExerciseSpinner);
        durationInput = findViewById(R.id.outdoorDurationInput);
        calculateButton = findViewById(R.id.outdoorCalculateButton);

        // 初始化室外运动项目和卡路里对照表
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

    // 初始化室外运动项目数据
    private void initExerciseData() {
        exerciseList = new ArrayList<>();
        exerciseCaloriesMap = new HashMap<>();

        // 添加更多室外运动项目（每小时卡路里消耗）
        exerciseList.add("慢跑");
        exerciseCaloriesMap.put("慢跑", 500);

        exerciseList.add("快跑");
        exerciseCaloriesMap.put("快跑", 700);

        exerciseList.add("骑自行车");
        exerciseCaloriesMap.put("骑自行车", 400);

        exerciseList.add("游泳");
        exerciseCaloriesMap.put("游泳", 650);

        exerciseList.add("打篮球");
        exerciseCaloriesMap.put("打篮球", 550);

        exerciseList.add("爬山");
        exerciseCaloriesMap.put("爬山", 520);
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