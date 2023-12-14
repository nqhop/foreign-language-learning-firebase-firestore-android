package com.example.quizapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.User;
import com.example.quizapp.models.Vocab2;
import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class  multipleChoiceTestActivity extends AppCompatActivity {

    TextView starTesting;
    EditText numberOfQuestionEditText;
    int numberOfQuestion;
    Boolean EnglishIsSelected = true;
    Dialog dialog;
    RadioGroup radioGroup;
    TopicLibrary topicLibrary = new TopicLibrary("vegatables", "AaWZ5yEnedL7al8jRhH9", "001", 7, new User("H 2023", "hop@gmail.com", "AaWZ5yEnedL7al8jRhH9"));
    ArrayList<Vocab2> vocabList = new ArrayList<>();
    ArrayList<Integer> correctOption = new ArrayList<>();
    ArrayList<Integer> userOption = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_test);
        dialog = new Dialog(this, R.style.FullScreenDialogTheme);
        dialog.setContentView(R.layout.multiple_choice_test_setting);
        dialog.show();

        numberOfQuestionEditText = dialog.findViewById(R.id.editTextNumber2);
        radioGroup = dialog.findViewById(R.id.radioGroup);
        starTesting = dialog.findViewById(R.id.textView29);
        starTesting.setOnClickListener(v -> {
            String tem = String.valueOf(numberOfQuestionEditText.getText());
            numberOfQuestion = tem.length() <= 0 ? vocabList.size() : Integer.parseInt(tem);

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = dialog.findViewById(selectedId);
            EnglishIsSelected = (radioButton.getId() == R.id.radio_en) ? true : false;
            Log.d("multipleChoiceTestActivity", String.valueOf(numberOfQuestion));
            Log.d("multipleChoiceTestActivity", String.valueOf(EnglishIsSelected));
            dialog.dismiss();
            loopQuestion();
        });

        fexData();
//        createTheDialogTest();

    }

    private void loopQuestion(){
        Log.d("multipleChoiceTestActivity","size: " + vocabList.size());
        for (int i =0; i < vocabList.size(); i++){
            List<Integer> indexList = randomFrom0ToNExcludeM(vocabList.size(), i);
            Log.d("multipleChoiceTestActivity", indexList.toString());
            createTheDialogTest(i, indexList);

            return;
        }
    }
    public static List randomFrom0ToNExcludeM(int n, int m){
        int count = 3;
        if(count >= n){
            count = n - 1;
        }
        Random random = new Random();
        Set<Integer> randomNumbers = new HashSet<>();

        while (randomNumbers.size() < count) {
            int randomNumber = random.nextInt(n);
            if (randomNumber != m) {
                randomNumbers.add(randomNumber);
            }
        }
        randomNumbers.add(m);
        List<Integer> shuffledList = new ArrayList<>(randomNumbers);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }
    private void fexData(){

        vocabList.add(new Vocab2("Apple", "qủa táo", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("Avocado", "qủa bơ", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("Strawberry", "dâu tây", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("watermelon", "dưa hấu", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("mango", "trái xoài", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("guava", "trái ổi", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("banana", "trái chuối", "new", Timestamp.now(), Timestamp.now(), false));
        vocabList.add(new Vocab2("coconut", "trái dừa", "new", Timestamp.now(), Timestamp.now(), false));
    }
    private void createTheDialogTest(int index, List indexList) {
        Dialog dialogQuestion = new Dialog(this, R.style.FullScreenDialogThemeWithActionBar);
        dialogQuestion.setContentView(R.layout.multiple_choice_test_question);
        TextView question = dialogQuestion.findViewById(R.id.textView26);
        question.setText(vocabList.get(index).getWord());

        RadioGroup radioGroupChoise = dialogQuestion.findViewById(R.id.radioGroupChoise);
        RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
        radioButton1 = dialogQuestion.findViewById(R.id.radio_a);
        radioButton2 = dialogQuestion.findViewById(R.id.radio_b);
        radioButton3 = dialogQuestion.findViewById(R.id.radio_c);
        radioButton4 = dialogQuestion.findViewById(R.id.radio_d);

        radioButton1.setText(vocabList.get((Integer) indexList.get(0)).getMeaning());
        radioButton2.setText(vocabList.get((Integer) indexList.get(1)).getMeaning());
        radioButton3.setText(vocabList.get((Integer) indexList.get(2)).getMeaning());
        radioButton4.setText(vocabList.get((Integer) indexList.get(3)).getMeaning());


        radioGroupChoise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = dialogQuestion.findViewById(checkedId);
                if (radioButton != null && radioButton.isChecked()) {
                    String selectedOption = radioButton.getText().toString();
                    Log.d("multipleChoiceTestActivity", "selectedOption: " + selectedOption);
                }
            }
        });

        dialogQuestion.show();
    }


}