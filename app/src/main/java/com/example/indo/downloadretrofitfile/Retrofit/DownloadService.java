package com.example.indo.downloadretrofitfile.Retrofit;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.indo.downloadretrofitfile.MainActivity;
import com.example.indo.downloadretrofitfile.Model.Downloads;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class DownloadService extends IntentService {

    public DownloadService() {
        super("Download Database");
    }

    private NotificationCompat.Builder notifiactionBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;


    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notifiactionBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.support.graphics.drawable.animated.R.drawable.notification_icon_background)
                .setContentTitle("Download Db")
                .setAutoCancel(false)
                .setContentText("Downloading File");
        notificationManager.notify(0, notifiactionBuilder.build());
        initDownload();
    }
    private void initDownload(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://downoad.learn2Crack.com/")
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<ResponseBody> request = retrofitInterface.downloadFile();
    }
    private void downloadFileSize(ResponseBody body) throws  IOException{

        int count;
        byte data[] = new byte[1024 * 4];
        long filesize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024* 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.zip");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1){

            total += count;
            totalFileSize = (int) (filesize/(Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100)/ filesize);

            long currentTime = System.currentTimeMillis() - startTime;

            Downloads downloads = new Downloads();
            downloads.setTotolFileSize(totalFileSize);
            if (currentTime > 1000 * timeCount){
                downloads.setCurrentFileSize((int) current);
                downloads.setProgress(progress);
                sendNotification(downloads);
                timeCount++;
            }
            output.write(data, 0, count);
        }
        onDowloadComplet();
        output.flush();
        output.close();
        bis.close();
    }
    private void sendNotification(Downloads downloads){
        sendIntent(downloads);
        notifiactionBuilder.setProgress(100, downloads.getProgress(), false);
        notifiactionBuilder.setContentText("Downloading file"+ downloads.getCurrentFileSize()+ "/"+ totalFileSize);
        notificationManager.notify(0, notifiactionBuilder.build());
    }
    private void sendIntent(Downloads downloads){
        Intent intent = new Intent(MainActivity.MESSAGE_PROGRESS);
        intent.putExtra("download", downloads);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }
    private void onDowloadComplet(){

        Downloads downloads = new Downloads();
        downloads.setProgress(100);
        sendIntent(downloads);

        notificationManager.cancel(0);
        notifiactionBuilder.setProgress(0,0,false);
        notifiactionBuilder.setContentText("File Downloaded");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.cancel(0);
    }
}


















