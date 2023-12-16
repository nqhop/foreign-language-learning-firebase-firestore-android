package com.example.quizapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.adapters.ResultMultipleAdapter;
import com.example.quizapp.models.MultipleChoiseResult;
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

public class multipleChoiceTestActivity extends AppCompatActivity {

    TextView starTesting, lengthOfVocabEditText;
    EditText numberOfQuestionEditText;
    int numberOfQuestion, maxNumberOfQuestion;
    Boolean EnglishIsSelected = true;
    Dialog dialog, dialogQuestion;
    RadioGroup radioGroup;
    TopicLibrary topicLibrary = new TopicLibrary("vegatables", "AaWZ5yEnedL7al8jRhH9", "001", 7, new User("H 2023", "hop@gmail.com", "AaWZ5yEnedL7al8jRhH9"));
    ArrayList<Vocab2> vocabList = new ArrayList<>();
    ArrayList<String> correctOption = new ArrayList<>();
    ArrayList<String> userOption = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice_test);

        getMyIntent();
        dialog = new Dialog(this, R.style.FullScreenDialogTheme);
        dialog.setContentView(R.layout.multiple_choice_test_setting);
        lengthOfVocabEditText = dialog.findViewById(R.id.textView35);
        lengthOfVocabEditText.setText("Tối đa " +maxNumberOfQuestion);
        dialog.show();

        numberOfQuestionEditText = dialog.findViewById(R.id.editTextNumber2);
        radioGroup = dialog.findViewById(R.id.radioGroup);
        starTesting = dialog.findViewById(R.id.textView29);

        LinearLayout closeMultipleSetting = dialog.findViewById(R.id.closeMultipleSetting);
        closeMultipleSetting.setOnClickListener(v->{
            startActivity(new Intent(this, LearningActivity.class));
        });


        starTesting.setOnClickListener(v -> {
            String tem = String.valueOf(numberOfQuestionEditText.getText());
            numberOfQuestion = tem.length() <= 0 ? vocabList.size() : Integer.parseInt(tem);
            if(numberOfQuestion > maxNumberOfQuestion)
                numberOfQuestion = maxNumberOfQuestion;

            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = dialog.findViewById(selectedId);
            EnglishIsSelected = (radioButton.getId() == R.id.radio_en) ? true : false;
            Log.d("multipleChoiceTestActivity", String.valueOf(numberOfQuestion));
            Log.d("multipleChoiceTestActivity", String.valueOf(EnglishIsSelected));
            dialog.dismiss();
            loopQuestion(0);
        });

//        fexData();
//        createTheDialogTest();

    }

    private void getMyIntent() {
        Intent intent = getIntent();
        ArrayList<Vocab2> vocabListExtra = intent.getParcelableArrayListExtra("vocabListExtra");
        maxNumberOfQuestion = intent.getIntExtra("lengthOfList", 0);
        if(vocabListExtra != null){
            vocabList = vocabListExtra;
        }
    }

    private void loopQuestion(int index){
        if(index == numberOfQuestion){
            Log.d("multipleChoiceTestActivity", "correctOption: " + correctOption.toString());
            Log.d("multipleChoiceTestActivity", "userOption: " + userOption.toString());
            resultActivity();
            return;
        }
        Log.d("multipleChoiceTestActivity","size: " + vocabList.size());
        List<Integer> indexList = randomFrom0ToNExcludeM(vocabList.size(), index);
        correctOption.add(EnglishIsSelected ? vocabList.get(index).getMeaning() : vocabList.get(index).getWord());
        Log.d("multipleChoiceTestActivity", indexList.toString());
        createTheDialogTest(index, indexList);
    }

    private void resultActivity() {
        int countCorrectOption = 0;
        ArrayList<MultipleChoiseResult> resultsList = new ArrayList<>();

        for(int i = 0; i < numberOfQuestion; i++){
            Log.d("resultActivity", "resultActivity " + correctOption.get(i) + ", " + userOption.get(i));
            resultsList.add(new MultipleChoiseResult(EnglishIsSelected, EnglishIsSelected ? vocabList.get(i).getWord() : vocabList.get(i).getMeaning(), correctOption.get(i), userOption.get(i)));
            if(correctOption.get(i) == userOption.get(i))
                countCorrectOption++;
        }

        Dialog resultDialog = new Dialog(this, R.style.FullScreenDialogTheme);
        resultDialog.setContentView(R.layout.multiple_choice_test_result);
        TextView correctTextView = resultDialog.findViewById(R.id.textView32);
        TextView wrongTextVIew = resultDialog.findViewById(R.id.textView33);
        RecyclerView resultMultipleOptionTest = resultDialog.findViewById(R.id.multipleChoiseResultRecyclerView);
        ResultMultipleAdapter adapter = new ResultMultipleAdapter(this, resultsList);
        resultMultipleOptionTest.setAdapter(adapter);
        resultMultipleOptionTest.setLayoutManager(new LinearLayoutManager(this));

        LinearLayout closeMutipleChoiseResult = resultDialog.findViewById(R.id.closeMutipleChoiseResult);
        closeMutipleChoiseResult.setOnClickListener(v -> {
            finish();
        });


        correctTextView.setText(Integer.toString(countCorrectOption));
        wrongTextVIew.setText(Integer.toString(numberOfQuestion - countCorrectOption));

        resultDialog.show();
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
//        if(dialogQuestion != null && dialogQuestion.isShowing()){
//            dialogQuestion.dismiss();
//        }
        dialogQuestion = new Dialog(this, R.style.FullScreenDialogThemeWithActionBar);
        dialogQuestion.setContentView(R.layout.multiple_choice_test_question);
        TextView question = dialogQuestion.findViewById(R.id.textView26);
        question.setText(EnglishIsSelected ? vocabList.get(index).getWord() : vocabList.get(index).getMeaning());

        RadioGroup radioGroupChoise = dialogQuestion.findViewById(R.id.radioGroupChoise);
        RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
        radioButton1 = dialogQuestion.findViewById(R.id.radio_a);
        radioButton2 = dialogQuestion.findViewById(R.id.radio_b);
        radioButton3 = dialogQuestion.findViewById(R.id.radio_c);
        radioButton4 = dialogQuestion.findViewById(R.id.radio_d);

        radioButton1.setText(EnglishIsSelected ? vocabList.get((Integer) indexList.get(0)).getMeaning() : vocabList.get((Integer) indexList.get(0)).getWord());
        radioButton2.setText(EnglishIsSelected ? vocabList.get((Integer) indexList.get(1)).getMeaning() : vocabList.get((Integer) indexList.get(1)).getWord());
        radioButton3.setText(EnglishIsSelected ? vocabList.get((Integer) indexList.get(2)).getMeaning() : vocabList.get((Integer) indexList.get(2)).getWord());
        radioButton4.setText(EnglishIsSelected ? vocabList.get((Integer) indexList.get(3)).getMeaning() : vocabList.get((Integer) indexList.get(3)).getWord());
//        radioButton2.setText(vocabList.get((Integer) indexList.get(1)).getMeaning());
//        radioButton3.setText(vocabList.get((Integer) indexList.get(2)).getMeaning());
//        radioButton4.setText(vocabList.get((Integer) indexList.get(3)).getMeaning());


        int nextIndex = index+1;
        radioGroupChoise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = dialogQuestion.findViewById(checkedId);
                if (radioButton != null && radioButton.isChecked()) {
                    String selectedOption = radioButton.getText().toString();
                    Log.d("multipleChoiceTestActivity", "selectedOption: " + selectedOption);
                    userOption.add(selectedOption);
                    Log.d("multipleChoiceTestActivity", "nextIndex: " + nextIndex);
                    loopQuestion(nextIndex);
                }
            }
        });

        dialogQuestion.show();
    }


}