package com.example.indo.downloadretrofitfile.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Downloads implements Parcelable {
    public Downloads(){

    }

    private int progress;
    private int currentFileSize;
    private int totolFileSize;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotolFileSize() {
        return totolFileSize;
    }

    public void setTotolFileSize(int totolFileSize) {
        this.totolFileSize = totolFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(progress);
        dest.writeInt(currentFileSize);
        dest.writeInt(totolFileSize);
    }
    private Downloads(Parcel in){
        progress = in.readInt();
        currentFileSize = in.readInt();
        totolFileSize = in.readInt();
    }
    public static  final Parcelable.Creator<Downloads>CREATOR = new Parcelable.Creator<Downloads>(){
        @Override
        public Downloads createFromParcel(Parcel source) {
            return null;
        }
        public Downloads[] newArray(int size){
            return  new Downloads[size];
        }
    };
}
