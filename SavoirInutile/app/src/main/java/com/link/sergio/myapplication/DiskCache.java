package com.link.sergio.myapplication;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class DiskCache
{
    private static final String TAG = DiskCache.class.getSimpleName();

    protected File rootDir;

    public DiskCache(Context context, String cacheFolder)
    {
        rootDir = context.getDir(cacheFolder, Context.MODE_PRIVATE);
    }


    public synchronized boolean saveData(byte[] data, String filename)
    {
        try
        {
            File itemFile = getFile(filename, true);

            FileOutputStream fos = new FileOutputStream(itemFile);
            fos.write(data);
            fos.close();
            Log.i(TAG, "File written to disk (" + itemFile.getAbsolutePath() + ")");

            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to save  " + filename, e);
            return false;
        }
    }

    public synchronized boolean saveText(String text, String filename)
    {
        try
        {
            return saveData(text.getBytes("UTF-8"), filename);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e(TAG, "Impossible to save  " + filename, e);
            return true;
        }
    }

    public synchronized File getFile(String filename, boolean isForCreation)
    {
        return getFile(filename, isForCreation, true);
    }

    public synchronized File getFile(String filename, boolean isForCreation, boolean isNameHashed)
    {
        File f = new File(rootDir, filename);

        if (isForCreation && !f.getParentFile().exists())
        {
            f.getParentFile().mkdirs();
        }

        return f;
    }

    public synchronized String getData(String filename, boolean isNameHashed)
    {
        File f = getFile(filename, false, isNameHashed);
        String content = null;

        if (f.exists())
        {
            try
            {
                InputStream is = new FileInputStream(f);
                content = Utils.getTextFromStream(is);
            }
            catch (Exception e)
            {
                Log.e(TAG, "Error while opening to open data file in cache :" + filename, e);
            }
        }
        else
        {
            Log.e(TAG, "Binary file does not exist in cache :" + filename);
        }

        return content;
    }

    public synchronized String getText(String filename)
    {
        File f = getFile(filename, false);
        String content = null;

        if (f != null && f.exists())
        {
            try
            {
                InputStream is = new FileInputStream(f);

                content = Utils.getTextFromStream(is);
            }
            catch (Exception e)
            {
                Log.e(TAG, "Error while opening to open text file in cache :" + filename, e);
            }
        }
        else
        {
            Log.e(TAG, "Text file does not exist in cache :" + filename);
        }

        return content;
    }
}
