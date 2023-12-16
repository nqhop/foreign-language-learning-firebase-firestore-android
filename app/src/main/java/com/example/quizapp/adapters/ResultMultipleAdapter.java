package com.example.quizapp.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.MultipleChoiseResult;

import java.util.ArrayList;
import java.util.Locale;

public class ResultMultipleAdapter extends RecyclerView.Adapter<ResultMultipleAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<MultipleChoiseResult> multipleChoiseResultArrayList;
    TextToSpeech toSpeech, toSpeechVietNam;

    public ResultMultipleAdapter(Context context, ArrayList<MultipleChoiseResult> multipleChoiseResultArrayList) {
        this.context = context;
        this.multipleChoiseResultArrayList = multipleChoiseResultArrayList;
    }

    @NonNull
    @Override
    public ResultMultipleAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.multiple_choice_test_result_item, parent,false);
        return new ResultMultipleAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        boolean EnglishIsSelected = multipleChoiseResultArrayList.get(position).isEnglishIsSelected();
        toSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });
        toSpeechVietNam = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    toSpeechVietNam.setLanguage(Locale.forLanguageTag("vi-VN"));
                }
            }
        });

        String correctOption = multipleChoiseResultArrayList.get(position).getCorrectOption();
        String wrongOption = multipleChoiseResultArrayList.get(position).getWrongOption();
        String question = multipleChoiseResultArrayList.get(position).getQuestion();
        holder.vocabQuestion.setText(question);
        holder.correctOption.setText(correctOption);
        if(correctOption == wrongOption){
            holder.wrongOptionLinearLayout.setVisibility(View.INVISIBLE);
        }else {
            holder.wrongOption.setText(wrongOption);
        }

        holder.speechvocabQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EnglishIsSelected){
                    toSpeech.speak(question, toSpeech.QUEUE_FLUSH, null);
                }else {
                    toSpeechVietNam.speak(question, toSpeechVietNam.QUEUE_FLUSH, null);
                }
            }
        });
        holder.speechvocabCorrectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EnglishIsSelected){
                    toSpeechVietNam.speak(correctOption, toSpeechVietNam.QUEUE_FLUSH, null);
                }else {
                    toSpeech.speak(correctOption, toSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        holder.speechvocabwrongOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EnglishIsSelected){
                    toSpeechVietNam.speak(wrongOption, toSpeechVietNam.QUEUE_FLUSH, null);
                }else {
                    toSpeech.speak(wrongOption, toSpeech.QUEUE_FLUSH, null);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return multipleChoiseResultArrayList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView vocabQuestion, correctOption, wrongOption;
        LinearLayout wrongOptionLinearLayout;
        ImageView speechvocabQuestion, speechvocabCorrectOption, speechvocabwrongOption;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            vocabQuestion = itemView.findViewById(R.id.vocabQuestion);
            correctOption = itemView.findViewById(R.id.textView38);
            wrongOption = itemView.findViewById(R.id.textView39);
            wrongOptionLinearLayout = itemView.findViewById(R.id.wrongOptionLinearLayout);
            speechvocabQuestion = itemView.findViewById(R.id.imageView20);
            speechvocabCorrectOption = itemView.findViewById(R.id.imageView21);
            speechvocabwrongOption = itemView.findViewById(R.id.imageView22);
        }
    }
}
