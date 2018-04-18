package com.r3tr0.popups.models;

import android.support.annotation.NonNull;

import com.r3tr0.popups.enums.FileMode;

/**
 * Created by r3tr0 on 4/16/18.
 */

public class FileInfo implements Comparable<FileInfo>{
    private String fileName;
    private String path;
    private FileMode fileMode;

    public FileInfo(String fileName, FileMode fileMode) {
        this.fileName = fileName;
        this.fileMode = fileMode;
    }

    public FileInfo(String fileName, String path, FileMode fileMode) {
        this.fileName = fileName;
        this.path = path;
        this.fileMode = fileMode;
    }

    public String getFileName() {
        return fileName;
    }

    public FileMode getFileMode() {
        return fileMode;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(@NonNull FileInfo fileInfo) {
        if (fileInfo.getFileMode() == FileMode.MODE_DIRECTORY && this.fileMode == FileMode.MODE_FILE)
            return 1;
        if (fileInfo.getFileMode() == FileMode.MODE_FILE && this.fileMode == FileMode.MODE_DIRECTORY)
            return -1;

        return 0;


    }
}
