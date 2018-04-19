package com.r3tr0.popups.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.r3tr0.popups.R;
import com.r3tr0.popups.adapters.FilesRecyclerAdapter;
import com.r3tr0.popups.enums.FileMode;
import com.r3tr0.popups.interfaces.OnItemClickListener;
import com.r3tr0.popups.models.FileInfo;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by r3tr0 on 4/16/18.
 */

public class FileDialog{

    private int depth = 0;

    private AlertDialog dialog;
    private Context context;

    private ImageView forward;
    private ImageView backward;

    Button selectButton;

    private TextView pathTextView;
    private TextView selectedFileTextView;

    private RecyclerView filesRecyclerView;
    private FilesRecyclerAdapter adapter;

    private File currentFile;
    private File selectedFile;

    OnDialogFinishListener onDialogFinishListener;

    public FileDialog(final Context context) {
        this.context = context;
        View view = ((LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.file_dialog_layout, null);

        dialog = new AlertDialog.Builder(context).setView(view).create();

        adapter = new FilesRecyclerAdapter(context);

        filesRecyclerView = view.findViewById(R.id.filesRecyclerView);
        filesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        filesRecyclerView.setAdapter(adapter);

        getFilesList(depth);
        selectedFile = null;


        forward = view.findViewById(R.id.forwardImage);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backward = view.findViewById(R.id.backImage);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (depth > 0) {
                    currentFile = currentFile.getParentFile();
                    getFilesList(--depth);
                    pathTextView.setText(currentFile.getAbsolutePath());
                    selectedFileTextView.setText("Selected file : none");
                }
            }
        });

        pathTextView = view.findViewById(R.id.pathTextView);
        selectedFileTextView = view.findViewById(R.id.selectedFileTextView);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (currentFile != null){
                    if (adapter.getSelectedFileInfo().getFileMode() != FileMode.MODE_FILE) {
                        currentFile = new File(adapter.getSelectedFileInfo().getPath());
                        getFilesList(++depth);
                        pathTextView.setText(currentFile.getAbsolutePath());
                        selectedFileTextView.setText("Selected file : none");
                    }

                    else {
                        selectedFile = new File(adapter.getSelectedFileInfo().getPath());
                        selectedFileTextView.setText("Selected file : " + selectedFile.getName());
                        Log.e("selected file", selectedFile.getName());
                    }
                }

                else {
                    currentFile = new File(adapter.getDirectoryInfo(position).getPath());
                    getFilesList(++depth);
                    pathTextView.setText(currentFile.getAbsolutePath());
                }
            }
        });

        selectButton = view.findViewById(R.id.selectButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getSelectedFileInfo() == null || adapter.getSelectedFileInfo().getFileMode() != FileMode.MODE_FILE) {
                    AlertDialog tempDialog = new AlertDialog.Builder(FileDialog.this.context)
                            .setTitle("error")
                            .setMessage("You didn't select anything!")
                            .setNeutralButton("Ok", null).create();

                    tempDialog.show();
                }
                else {
                    dialog.dismiss();
                }
            }
        });
    }

    public void setOnDialogFinishListener(OnDialogFinishListener onDialogFinishListener) {
        this.onDialogFinishListener = onDialogFinishListener;
    }

    private void getFilesList(int depth){
        ArrayList<FileInfo> fileInfos = new ArrayList<>();

        if (depth > 0){
            File[] files = currentFile.listFiles();
            for(File file1 : files){
                if (file1.isDirectory()) fileInfos.add(new FileInfo(file1.getName(), file1.getAbsolutePath(), FileMode.MODE_DIRECTORY));
                else fileInfos.add(new FileInfo(file1.getName(),file1.getAbsolutePath(), FileMode.MODE_FILE));
            }

        }

        else {
            final String secondaryStorage = System.getenv("SECONDARY_STORAGE");
            File secondary;

            if (secondaryStorage != null) {
                secondary = new File(secondaryStorage);
                fileInfos.add(new FileInfo(secondary.getName(), secondary.getAbsolutePath(), FileMode.MODE_INTERNAL_STORAGE));
            }

            secondary = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            fileInfos.add(new FileInfo(secondary.getName(),secondary.getAbsolutePath(), FileMode.MODE_SD));
        }

        adapter.replaceList(fileInfos);

    }

    public void showDialog() {
        dialog.show();
        if (onDialogFinishListener != null)
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    onDialogFinishListener.onDismiss((adapter.getSelectedFileInfo() != null) ? new File(adapter.getSelectedFileInfo().getPath()) : null);
                }
            });
    }

    public interface OnDialogFinishListener{
        void onDismiss(File current);
    }
}
