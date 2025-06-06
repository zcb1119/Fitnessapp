package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndoorActivity extends AppCompatActivity {
    private LinearLayout exerciseContainer;
    private Button calculateButton;
    private Map<String, Integer> exerciseCaloriesMap;
    private Map<String, EditText> durationInputs;
    private List<String> exerciseList;
    private int totalCalories = 0;
    private LinearLayout resultContainer;
    private TextView resultDetails, totalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);

        exerciseContainer = findViewById(R.id.indoorExerciseContainer);
        calculateButton = findViewById(R.id.indoorCalculateButton);
        resultContainer = findViewById(R.id.indoorResultContainer);
        resultDetails = findViewById(R.id.indoorResultDetails);
        totalResult = findViewById(R.id.indoorTotalResult);
        durationInputs = new HashMap<>();


        initExerciseData();


        generateExerciseInputs();


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

        // 添加室内运动项目（每小时卡路里消耗）
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

    // 动态生成运动项目输入区域
    private void generateExerciseInputs() {
        for (String exercise : exerciseList) {
            View exerciseItem = getLayoutInflater().inflate(R.layout.exercise_item, null);
            TextView nameTextView = exerciseItem.findViewById(R.id.exerciseName);
            nameTextView.setText(exercise);

            EditText durationInput = exerciseItem.findViewById(R.id.exerciseDuration);
            durationInputs.put(exercise, durationInput);

            exerciseContainer.addView(exerciseItem);
        }
    }

    // 计算卡路里
    private void calculateCalories() {
        totalCalories = 0;
        boolean hasInput = false;
        StringBuilder detailsBuilder = new StringBuilder();

        for (String exercise : exerciseList) {
            EditText input = durationInputs.get(exercise);
            String durationStr = input.getText().toString();

            if (!TextUtils.isEmpty(durationStr)) {
                hasInput = true;
                try {
                    int duration = Integer.parseInt(durationStr);
                    int caloriesPerHour = exerciseCaloriesMap.get(exercise);
                    int calories = (int) (caloriesPerHour * (duration / 60.0));

                    totalCalories += calories;
                    detailsBuilder.append(exercise)
                            .append(": ")
                            .append(duration)
                            .append("分钟 → ")
                            .append(calories)
                            .append("卡路里\n");
                } catch (NumberFormatException e) {
                    // 忽略无效输入
                }
            }
        }

        if (!hasInput) {
            resultContainer.setVisibility(View.GONE);
            Toast.makeText(this, "请输入运动时间", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示结果
        resultDetails.setText(detailsBuilder.toString());
        totalResult.setText("总计: " + totalCalories + " 卡路里");
        resultContainer.setVisibility(View.VISIBLE);

        // 返回结果给主活动
        Intent resultIntent = new Intent();
        resultIntent.putExtra("calories", totalCalories);
        setResult(RESULT_OK, resultIntent);
    }
}