package com.example.quizapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.Topic;

import java.util.ArrayList;

public class Topic_RecyclerViewAdapter extends RecyclerView.Adapter<Topic_RecyclerViewAdapter.MyviewHolder> {

    Context context;
    ArrayList<Topic> topics;

    public Topic_RecyclerViewAdapter(Context context, ArrayList<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public Topic_RecyclerViewAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view, parent, false);
        return new Topic_RecyclerViewAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Topic_RecyclerViewAdapter.MyviewHolder holder, int position) {
        holder.textView.setText(topics.get(position).getName());
//        Toast.makeText(context, topics.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView10);
        }
    }
}
