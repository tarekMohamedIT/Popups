package com.r3tr0.popups;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.r3tr0.popups.adapters.FilesRecyclerAdapter;
import com.r3tr0.popups.dialogs.FileDialog;
import com.r3tr0.popups.enums.FileMode;
import com.r3tr0.popups.models.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FileDialog dialog;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new FileDialog(MainActivity.this);

        Button button = findViewById(R.id.showDialogButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.showDialog();
            }
        });

        textView = findViewById(R.id.textView);

        dialog.setOnDialogFinishListener(new FileDialog.OnDialogFinishListener() {
            @Override
            public void onDismiss(File current) {
                try {
                    if (current != null)
                        textView.setText(readFile(current));
                    else
                        textView.setText("no text here!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuilder whole = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            whole.append(line).append("\n");

        return whole.toString();
    }
}
