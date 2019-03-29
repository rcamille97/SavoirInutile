package com.link.sergio.myapplication;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ContentManager
{
    private static final String TAG       = ContentManager.class.getSimpleName();
    private static final String DATA_JSON = "data_json";
    private String mFilename= "monCache";

    private static ContentManager   mInstance;
    private        List<SavoirItem> itemsList;
    private        DiskCache        dataCache;


    public interface DataListener
    {
        void notifyRetrieved(List<SavoirItem> savoirItem);
        void notifyNotRetrieved();
    }

    public static ContentManager getInstance(Context c)
    {
        if (mInstance == null)
            mInstance = new ContentManager(c);

        return mInstance;
    }

    private static List<SavoirItem> retrieveDataFromJson(String jsonString)
    {
        List<SavoirItem> itemsList = new ArrayList<>();
        try
        {
            JSONObject jsonItems = new JSONObject(jsonString);
            JSONArray itemsArray = jsonItems.getJSONArray("items");
            int nbPlaces = itemsArray.length();
            for (int i = 0; i < nbPlaces; i++)
            {
                itemsList.add(new SavoirItem(itemsArray.getJSONObject(i)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return itemsList;
    }

    private ContentManager(Context context)
    {
        dataCache = new DiskCache(context, "items");
    }


    public void getRequestData(DataListener listener)
    {
        new SavoirItemsAsynctask(listener).execute();
    }


    public class SavoirItemsAsynctask extends AsyncTask<Void, Void, List<SavoirItem> >
    {

        private WeakReference<DataListener> mListenerRef;

        public SavoirItemsAsynctask(DataListener listener)
        {
            mListenerRef = new WeakReference<>(listener);
        }

        @Override
        protected List<SavoirItem> doInBackground(Void... voids)
        {
            List<SavoirItem> itemsList = new ArrayList<>();
            URL urlObject = null;
            try {
                //CHECK IF CACHE
                String monCache= dataCache.getText(mFilename);
                if( monCache != null && !monCache.isEmpty() && !Utils.isNetworkAvailable(MainActivity.getContext())){
                    return retrieveDataFromJson(dataCache.getText(mFilename));
                }
                else {
                    Log.i(TAG, "ici");
                    //urlObject = new URL("https://serginho.goodbarber.com/front/get_items/939101/26903013/?local=1");
                    urlObject = new URL("https://serginho.goodbarber.com/front/get_items/939101/26902416/?local=1");
                    HttpURLConnection conn = (HttpURLConnection) urlObject.openConnection();
                    conn.setReadTimeout(7000);
                    conn.setConnectTimeout(7000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    Log.i(TAG, "là");
                    int status = conn.getResponseCode();
                    if (status != 200) {
                        //notifyItemsNotRetrieved();
                        return null;
                    }
                    InputStream is = conn.getInputStream();
                    String jsonAsString = Utils.getTextFromStream(is);
                    if (Utils.isStringValid(jsonAsString)) {
                        //SAVE JSON IN CACHE
                        dataCache.saveText(jsonAsString, mFilename);
                        JSONObject responseJSON = new JSONObject(jsonAsString);
                        JSONArray itemsArray = responseJSON.optJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            itemsList.add(new SavoirItem(itemsArray.optJSONObject(i)));
                        }

                        return itemsList;
                    }
                    Log.i(TAG, jsonAsString);
                }
            }
            catch(Exception e)
            {
                Log.i(TAG, "c'est cassé");
            }
            return null;

        }

        @Override
        protected void onPostExecute(List<SavoirItem>  savoirItem)
        {
            super.onPostExecute(savoirItem);

            if (mListenerRef.get() != null)
            {
                if (savoirItem != null)
                {
                    mListenerRef.get().notifyRetrieved(savoirItem);
                }
                else
                {
                    mListenerRef.get().notifyNotRetrieved();
                }
            }
        }
    }
}

