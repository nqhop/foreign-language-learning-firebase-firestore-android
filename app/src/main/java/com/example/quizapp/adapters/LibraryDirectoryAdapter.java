package com.example.quizapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.models.TopicDirectory;

import java.util.ArrayList;

public class LibraryDirectoryAdapter extends RecyclerView.Adapter<LibraryDirectoryAdapter.MyviewHolder> {

    private Context context;
    private ArrayList<TopicDirectory> dictionaries;

    public LibraryDirectoryAdapter(Context context, ArrayList<TopicDirectory> dictionaries) {
        this.context = context;
        this.dictionaries = dictionaries;
    }

    @NonNull
    @Override
    public LibraryDirectoryAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.directory_view, parent, false);
        return new LibraryDirectoryAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.directoryName.setText(dictionaries.get(position).getName());
        holder.userName.setText(dictionaries.get(position).getUser().getName());
    }

    @Override
    public int getItemCount() {
        return dictionaries.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder{
        TextView directoryName, userName;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            directoryName = itemView.findViewById(R.id.textView12);
            userName = itemView.findViewById(R.id.textView17);
        }
    }
}
