package com.nanb.wallpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundHelper extends AsyncTask<String,String,String> {
    Context mcontext;
    public BackgroundHelper(Context ctx){
        mcontext = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String id = strings[0];
        String type = strings[1];
        String downloadedBy = strings[2];
        String tag = strings[3];
        String filename = strings[4];
        String DisplayName = strings[5];

        //Toast.makeText(mcontext,id+" "+type+" "+ downloadedBy+" "+tag+" "+filename+" "+DisplayName,Toast.LENGTH_SHORT).show();
        String setadatindatabase_URL = "http://192.168.225.213:8080/NANB_wallpaper/downloadData.php";
        try {
            URL url = new URL(setadatindatabase_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String postdata = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+URLEncoder.encode("type","UTF-8")+"="+URLEncoder.encode(type,"UTF-8")+"&"+URLEncoder.encode("tag","UTF-8")+"="+URLEncoder.encode(tag,"UTF-8")+"&"+URLEncoder.encode("filename","UTF-8")+"="+URLEncoder.encode(filename,"UTF-8")+"&"+URLEncoder.encode("displayname","UTF-8")+"="+URLEncoder.encode(DisplayName,"UTF-8")+"&"+URLEncoder.encode("downloadBy","UTF-8")+"="+URLEncoder.encode(downloadedBy,"UTF-8");
            bufferedWriter.write(postdata);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line;
            while ((line = bufferedReader.readLine())!=null){
                result+=line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Toast.makeText(mcontext,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
