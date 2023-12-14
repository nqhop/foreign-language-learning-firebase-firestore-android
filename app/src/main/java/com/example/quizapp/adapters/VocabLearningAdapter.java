package com.example.quizapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizapp.R;
import com.example.quizapp.models.Vocab2;
import java.util.ArrayList;
import java.util.Locale;

public class VocabLearningAdapter extends RecyclerView.Adapter<VocabLearningAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Vocab2> vocabList;
    private TextToSpeech toSpeech;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Vocab2 vocab);
    }

    public VocabLearningAdapter(Context context, ArrayList<Vocab2> vocabList) {
        this.context = context;
        this.vocabList = vocabList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vocab_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String vocabWord = vocabList.get(position).getWord();

        toSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                toSpeech.setLanguage(Locale.UK);
            }
        });

        holder.word.setText(vocabList.get(position).getWord());
        holder.meaning.setText(vocabList.get(position).getMeaning());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(vocabList.get(position));
            }
        });

        holder.speech.setOnClickListener(v -> {
            toSpeech.speak(vocabWord, TextToSpeech.QUEUE_FLUSH, null);
        });
    }

    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView word, meaning;
        ImageView speech;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.textView19);
            meaning = itemView.findViewById(R.id.textView20);
            speech = itemView.findViewById(R.id.imageView12);
        }
    }
}
