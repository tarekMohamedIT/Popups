package com.r3tr0.popups.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.r3tr0.popups.R;
import com.r3tr0.popups.enums.FileMode;
import com.r3tr0.popups.interfaces.OnItemClickListener;
import com.r3tr0.popups.models.FileInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by r3tr0 on 4/16/18.
 */

public class FilesRecyclerAdapter extends RecyclerView.Adapter<FilesRecyclerAdapter.FilesViewHolder> {
    ArrayList<FileInfo> filesList;
    ArrayList<FileInfo> directoriesList;
    Context context;

    FileInfo selectedFileInfo;

    OnItemClickListener onItemClickListener;

    public FilesRecyclerAdapter(Context context) {
        this.context = context;
        this.filesList = new ArrayList<>();
        this.directoriesList = new ArrayList<>();
    }

    public FilesRecyclerAdapter(Context context, ArrayList<FileInfo> fileInfoList) {
        this.filesList = new ArrayList<>();
        this.directoriesList = new ArrayList<>();

        for (FileInfo info : fileInfoList){
            if (info.getFileMode() == FileMode.MODE_FILE) this.filesList.add(info);
            else this.directoriesList.add(info);
        }
        this.context = context;
    }

    public ArrayList<FileInfo> getFilesList() {
        return filesList;
    }

    public ArrayList<FileInfo> getDirectoriesList() {
        return directoriesList;
    }

    public FileInfo getSelectedFileInfo() {
        return selectedFileInfo;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(FileInfo info){
        this.selectedFileInfo = null;
        if (info.getFileMode() == FileMode.MODE_FILE){
            this.filesList.add(info);
            notifyItemInserted(filesList.size() - 1 + directoriesList.size());
        }
        else{
            this.directoriesList.add(info);
            notifyItemInserted(directoriesList.size() - 1);
        }

    }

    public void addAll(Collection<FileInfo> fileInfos){
        this.selectedFileInfo = null;
        for (FileInfo info : fileInfos){
            if (info.getFileMode() == FileMode.MODE_FILE){
                this.filesList.add(info);
            }
            else{
                this.directoriesList.add(info);
            }
        }
        notifyDataSetChanged();
    }

    public void replaceList(Collection<FileInfo> fileInfos){
        this.directoriesList.clear();
        this.filesList.clear();
        this.selectedFileInfo = null;

        for (FileInfo info : fileInfos){
            if (info.getFileMode() == FileMode.MODE_FILE){
                this.filesList.add(info);
            }
            else{
                this.directoriesList.add(info);
            }
        }

        notifyDataSetChanged();
    }

    public FileInfo getFileInfo(int position){
        return this.filesList.get(position);
    }

    public FileInfo getDirectoryInfo(int position){
        return this.directoriesList.get(position);
    }

    public void clearAll(){
        this.directoriesList.clear();
        this.filesList.clear();
        this.selectedFileInfo = null;
        notifyDataSetChanged();
    }

    @Override
    public FilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new FilesViewHolder(inflater.inflate(R.layout.file_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(FilesViewHolder holder, final int pos) {

        final Integer position = pos;

        if (position < directoriesList.size()){
            if (directoriesList.get(pos).getFileMode() == FileMode.MODE_INTERNAL_STORAGE){
                holder.fileImageView.setImageResource(R.drawable.ic_mobile);
                holder.fileTextView.setText("Internal");
            }

            else if (directoriesList.get(pos).getFileMode() == FileMode.MODE_SD){
                holder.fileImageView.setImageResource(R.drawable.ic_sd_card);
                holder.fileTextView.setText("SD Card");
            }

            else {
                holder.fileImageView.setImageResource(R.drawable.ic_directory);
                holder.fileTextView.setText(directoriesList.get(position).getFileName());
            }
        }

        else {
            holder.fileImageView.setImageResource(R.drawable.ic_file);
            holder.fileTextView.setText(filesList.get(position - directoriesList.size()).getFileName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < directoriesList.size()) {
                    selectedFileInfo = directoriesList.get(position);
                }

                else {
                    selectedFileInfo = filesList.get(position - directoriesList.size());
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return filesList.size() + directoriesList.size();
    }

    class FilesViewHolder extends RecyclerView.ViewHolder{
        ImageView fileImageView;
        TextView fileTextView;
        public FilesViewHolder(View itemView) {
            super(itemView);

            fileImageView = itemView.findViewById(R.id.fileImageView);
            fileTextView = itemView.findViewById(R.id.fileTextView);
        }
    }
}
