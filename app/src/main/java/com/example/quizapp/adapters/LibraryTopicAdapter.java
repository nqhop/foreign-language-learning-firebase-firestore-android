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
import com.example.quizapp.models.TopicLibrary;

import java.util.ArrayList;

public class LibraryTopicAdapter extends RecyclerView.Adapter<LibraryTopicAdapter.MyviewHolder>{
     private Context context;
     private ArrayList<TopicLibrary> topicLibraries;

    public LibraryTopicAdapter(Context context, ArrayList<TopicLibrary> topicLibraries) {
        this.context = context;
        this.topicLibraries = topicLibraries;
    }

    @NonNull
    @Override
    public LibraryTopicAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_view, parent, false);
        return new LibraryTopicAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.topicName.setText(topicLibraries.get(position).getName());
        holder.countVocab.setText(""+topicLibraries.get(position).getsizeOfVocabList() + " Thuật ngữ");
        holder.userName.setText(topicLibraries.get(position).getUser().getName());
    }

    @Override
    public int getItemCount() {
        return topicLibraries.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{

        TextView topicName, countVocab, userName;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            topicName = itemView.findViewById(R.id.topicName);
            countVocab = itemView.findViewById(R.id.terminologyCount);
            userName = itemView.findViewById(R.id.textView17);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "intent to vocab", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
