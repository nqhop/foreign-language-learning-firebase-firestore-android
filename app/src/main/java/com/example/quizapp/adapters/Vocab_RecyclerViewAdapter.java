package com.example.quizapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.Vocab;

import java.util.ArrayList;

public class Vocab_RecyclerViewAdapter extends RecyclerView.Adapter<Vocab_RecyclerViewAdapter.MyviewHolder> {

    Context context;
    ArrayList<Vocab> vocabs;

    public Vocab_RecyclerViewAdapter(Context context, ArrayList<Vocab> vocabs) {
        this.context = context;
        this.vocabs = vocabs;
    }

    @NonNull
    @Override
    public Vocab_RecyclerViewAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.table_view, parent, false);
        return new Vocab_RecyclerViewAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vocab_RecyclerViewAdapter.MyviewHolder holder, int position) {
        holder.textView.setText(vocabs.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return vocabs.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView textView, cound;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView10);
            cound = itemView.findViewById(R.id.textView13);
            itemView.setOnClickListener(v -> {
            });
        }

    }

}
