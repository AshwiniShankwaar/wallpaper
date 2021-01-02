package com.nanb.wallpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class fav_backgroundhelper extends AsyncTask<String,String,String> {
    Context mcontext;
    ImageButton favorite;

    fav_backgroundhelper(Context ctx,ImageButton fav){
        mcontext = ctx;
        favorite = fav;
    }

    @Override
    protected String doInBackground(String... strings) {
        String id = strings[0];
        String filename = strings[1];
        String userid = strings[2];
        String fileurl = strings[3];
        String filetype = strings[4];
        String displayname = strings[5];

        String setadatindatabase_URL = "http://192.168.225.213:8080/NANB_wallpaper/FavoriteData.php";
        try {
            URL url = new URL(setadatindatabase_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String postdata = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+URLEncoder.encode("filename","UTF-8")+"="+URLEncoder.encode(filename,"UTF-8")+"&"+URLEncoder.encode("UserId","UTF-8")+"="+URLEncoder.encode(userid,"UTF-8")+"&"+URLEncoder.encode("downloadurl","UTF-8")+"="+URLEncoder.encode(fileurl,"UTF-8")+"&"+URLEncoder.encode("filetype","UTF-8")+"="+URLEncoder.encode(filetype,"UTF-8")+"&"+URLEncoder.encode("DisplayName","UTF-8")+"="+URLEncoder.encode(displayname,"UTF-8");
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
        if(s.equals("Favorite")){
            favorite.setImageResource(R.drawable.ic_fav);
            Toast.makeText(mcontext,s,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mcontext,s,Toast.LENGTH_SHORT).show();
            favorite.setImageResource(R.drawable.ic_faveorite);
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
