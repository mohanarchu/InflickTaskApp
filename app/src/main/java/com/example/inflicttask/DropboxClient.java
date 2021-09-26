package com.example.inflicttask;
import android.content.Context;
import android.content.SharedPreferences;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Arrays;
import java.util.Locale;
public class DropboxClient {
    static String KEY = "com.inflict.task";
    static String ACCESS_TOKEN = "access-token";
    public static DbxClientV2 getClient(String ACCESS_TOKEN) {
        DbxRequestConfig config = new DbxRequestConfig("InflictTask/1.0.0", Locale.getDefault().toString());
        return new DbxClientV2(config, ACCESS_TOKEN);
    }


    public static void loginDropbox(Context context) {
        String clientIdentifier = "InflictTask/1.0.0";
        DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(clientIdentifier);
        Auth.startOAuth2PKCE(context, context.getString(R.string.APP_KEY), dbxRequestConfig, Arrays.asList("account_info.read", "files.content.write"));

    }

    public static String retrieveAccessToken(Context context) {
        String accessToken = Auth.getOAuth2Token();
        if (accessToken != null) {
            SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            prefs.edit().putString(ACCESS_TOKEN, accessToken).apply();
            return  accessToken;
        }
        return null;
    }
    public static String getAccessToken(Context context) {
        SharedPreferences prefs =context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return prefs.getString(ACCESS_TOKEN, "");
    }
}