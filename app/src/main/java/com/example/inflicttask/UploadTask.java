package com.example.inflicttask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class UploadTask extends AsyncTask {

    private DbxClientV2 dbxClient;
    private Bitmap bitmap;
    UploadInterface uploadInterface;
    UploadTask(DbxClientV2 dbxClient, Bitmap bitmap, Context context,UploadInterface uploadInterface) {
        this.dbxClient = dbxClient;
        this.bitmap = bitmap;
        this.uploadInterface = uploadInterface;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            dbxClient.files().uploadBuilder("/" +"Image_"+ Calendar.getInstance().getTimeInMillis()+".png")
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(bs);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        uploadInterface.uploadCompleted();
    }


}

