package com.example.product;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImage extends AsyncTask<String,Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap=null;
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream in;
        try {
            url=new URL(strings[0]);

            httpURLConnection=(HttpURLConnection) url.openConnection();

            in=httpURLConnection.getInputStream();

            bitmap= BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}


