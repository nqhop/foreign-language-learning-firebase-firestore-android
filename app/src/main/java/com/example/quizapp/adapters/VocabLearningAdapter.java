package com.example.quizapp.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.activities.LearningActivity;
import com.example.quizapp.models.TopicLibrary;
import com.example.quizapp.models.Vocab2;

import java.util.ArrayList;
import java.util.Locale;

public class VocabLearningAdapter extends RecyclerView.Adapter<VocabLearningAdapter.MyviewHolder> {

    private Context context;
    private ArrayList<Vocab2> vocabList;
    TextToSpeech toSpeech;

    public VocabLearningAdapter(Context context, ArrayList<Vocab2> vocabList) {
        this.context = context;
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabLearningAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vocab_view, parent, false);
        return new VocabLearningAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        String vocabWord = vocabList.get(position).getWord();
        toSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    toSpeech.setLanguage(Locale.UK);
                }
            }
        });

        holder.word.setText(vocabList.get(position).getWord());
        holder.meaning.setText(vocabList.get(position).getMeaning());
        holder.speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(vocabWord, toSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView word, meaning;
        ImageView speech;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.textView19);
            meaning = itemView.findViewById(R.id.textView20);
            speech = itemView.findViewById(R.id.imageView12);
        }
    }
}
