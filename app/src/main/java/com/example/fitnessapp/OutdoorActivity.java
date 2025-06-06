package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutdoorActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    private Button calculateButton;
    private Map<String, Integer> exerciseCaloriesMap; // 运动项目与卡路里对照表
    private Map<String, EditText> durationInputs; // 运动项目对应的输入框
    private List<String> exerciseList;
    private int totalCalories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);

        exerciseContainer = findViewById(R.id.outdoorExerciseContainer);
        calculateButton = findViewById(R.id.outdoorCalculateButton);
        durationInputs = new HashMap<>();

        // 初始化室外运动项目和卡路里对照表
        initExerciseData();

        // 动态生成运动项目输入区域
        generateExerciseInputs();

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

    // 动态生成运动项目输入区域
    private void generateExerciseInputs() {
        for (String exercise : exerciseList) {
            // 加载运动项目布局
            View exerciseItem = getLayoutInflater().inflate(R.layout.exercise_item, null);

            // 设置运动名称
            TextView nameTextView = exerciseItem.findViewById(R.id.exerciseName);
            nameTextView.setText(exercise);

            // 获取输入框并保存到映射
            EditText durationInput = exerciseItem.findViewById(R.id.exerciseDuration);
            durationInputs.put(exercise, durationInput);

            // 添加文本变化监听器
            durationInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    // 可以在这里实时计算每项运动的卡路里
                }
            });

            // 添加到容器
            exerciseContainer.addView(exerciseItem);
        }
    }

    // 计算卡路里
    private void calculateCalories() {
        totalCalories = 0;
        boolean hasInput = false;

        for (String exercise : exerciseList) {
            EditText input = durationInputs.get(exercise);
            String durationStr = input.getText().toString();

            if (!durationStr.isEmpty()) {
                hasInput = true;
                try {
                    int duration = Integer.parseInt(durationStr);
                    int caloriesPerHour = exerciseCaloriesMap.get(exercise);
                    totalCalories += (int) (caloriesPerHour * (duration / 60.0));
                } catch (NumberFormatException e) {
                    // 忽略无效输入
                }
            }
        }

        if (!hasInput) {
            // 没有输入任何运动时间
            setResult(RESULT_CANCELED);
        } else {
            // 返回结果给主活动
            Intent resultIntent = new Intent();
            resultIntent.putExtra("calories", totalCalories);
            setResult(RESULT_OK, resultIntent);
        }
        finish();
    }
}