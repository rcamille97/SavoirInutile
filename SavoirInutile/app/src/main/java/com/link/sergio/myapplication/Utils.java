package com.link.sergio.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class Utils
{
    // PUT IN GRADLE THIS DEPENDENCY
    // implementation "commons-io:commons-io:+"

    public static String getTextFromStream(InputStream is)
    {
        try
        {
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, Charset.forName("UTF-8"));
            return writer.toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean isStringValid(String string){
        return string != null && !string.isEmpty();
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
