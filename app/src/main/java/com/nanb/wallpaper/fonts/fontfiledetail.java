package com.nanb.wallpaper.fonts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.fonts.FontFamily;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.nanb.wallpaper.BackgroundHelper;
import com.nanb.wallpaper.R;
import com.nanb.wallpaper.comments;
import com.nanb.wallpaper.displaydataAccoudingtokeyword;
import com.nanb.wallpaper.itemclass;
import com.nanb.wallpaper.itemdisplay;
import com.nanb.wallpaper.similaritemsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class fontfiledetail extends AppCompatActivity {
    private ImageButton back,faveorite;
    private Button download,type,tag;
    private ImageView itemdisplay,userdp;
    LinearLayout commentsection;
    private TextView name,size,totaldownloads,setwallpaper,setlockscreenwallpaper,addcomment,comment,nocomment;
    private String filetype,backgroundurl,url,filename,fileDisplayname,filesize,fileid,filetag,currentuserid,filedownloads,filebackgroundurl;
    private ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;
    private FirebaseAuth mAuth;
    private View popupInputDialogView = null;
    ArrayList<fontsmodelclass> data;
    RecyclerView similiaritemrecycler;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Bitmap bitmap;
    private static final String URL_items = "http://192.168.225.213:8080/NANB_wallpaper/get_Detail.php?filename=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fontfiledetail);
        mAuth = FirebaseAuth.getInstance();
        //currentuserid = mAuth.getCurrentUser().getUid();
        back = findViewById(R.id.backbutton);
        faveorite = findViewById(R.id.likebutton);
        download = findViewById(R.id.download);
        totaldownloads = findViewById(R.id.downloads);
        size = findViewById(R.id.size);
        name = findViewById(R.id.name);
        itemdisplay = findViewById(R.id.item);
        type = findViewById(R.id.type);
        tag = findViewById(R.id.tag);
        addcomment = findViewById(R.id.addcomment);
        comment = findViewById(R.id.comment);
        userdp = findViewById(R.id.userdp);
        nocomment = findViewById(R.id.nocomment);
        commentsection = findViewById(R.id.commentdisplay);
        similiaritemrecycler = findViewById(R.id.similaritemsrecycler);
        layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        similiaritemrecycler.setLayoutManager(layoutManager);
        data = new ArrayList<>();
        // type = intent.getStringExtra("Type");
        url = getIntent().getStringExtra("downloadurl");
        backgroundurl = getIntent().getStringExtra("backgroundurl");
        filename=  getfilename(url);


        backmethod();

        //faveoriteButtonmethod();
        setimagemethod();
        final String URL_Data =  URL_items+filename+"&&type=Fonts";
        //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
        getfiledata(URL_Data);

        mProgressDialog = new ProgressDialog(fontfiledetail.this);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("Downloading Image");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait... till we download the image");
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datareturn = startdownload(url);
                if(datareturn.equals("completed")){
                    moveFile(Environment.getExternalStorageDirectory()+"/Download/",filename,Environment.getExternalStorageDirectory()+"/Wallpaper/Fonts/");
                    adddownloaddatatothedatabase("null",fileid,filetype,filetag,filename,fileDisplayname);
                    //addfonttosettingI();
                }
            }
        });
    }

    private void addfonttosettingI() {

    }

    private void adddownloaddatatothedatabase(String userid, String fileid, String filetype, String filetag, String filename, String fileDisplayname) {
        BackgroundHelper backgroundHelper = new BackgroundHelper(fontfiledetail.this);
        backgroundHelper.execute(fileid,filetype,userid,filetag,filename,fileDisplayname);
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
    private String startdownload(String url){
        String filename = getfilename(url);
        //Toast.makeText(mcontext,url,Toast.LENGTH_LONG).show();
        //File file=new File(Environment.getExternalStorageDirectory().getPath()+"/Wallpaper/Ringtone/");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Downloading.... please wait");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverRoaming(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
        request.setMimeType("*/*");
        DownloadManager manager = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return "completed";


    }
    private void backmethod() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getfilename(String url) {
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();
        return filename;
    }

    private void getsimmilarfile(String filetype,String filetag) {
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getfontsimmiliar.php?Type="+filetype+"&&Tag="+filetag;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject imagedata = jsonArray.getJSONObject(i);
                        fontsmodelclass fontsmodelclass = new fontsmodelclass(imagedata.getString("Type"),imagedata.getString("DisplayName"),imagedata.getString("downloadurl"),imagedata.getString("backgroundurl"));
                        data.add(fontsmodelclass);
                       // Toast.makeText(getApplicationContext(),imagedata.getString("backgroundurl"),Toast.LENGTH_LONG).show();
                    }
                    adapter = new fontssimmiliarAdapter(getApplicationContext(),data);
                    similiaritemrecycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void getfiledata(String url_data) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("id"),Toast.LENGTH_SHORT).show();
                        fileid = jsonObject.getString("id");
                        filetype = jsonObject.getString("Type");
                        filetag = jsonObject.getString("Tag");
                        filesize = jsonObject.getString("size");
                        fileDisplayname = jsonObject.getString("DisplayName");
                        filebackgroundurl = jsonObject.getString("backgroundurl");
                        filedownloads = jsonObject.getString("Total_downloads");

                        //Toast.makeText(getApplicationContext(),fileid,Toast.LENGTH_SHORT).show();

                    }
                    new fontfiledetail.GetImageFromUrl(itemdisplay).execute(filebackgroundurl);
                    getsimmilarfile(filetype,filetag);
                    name.setText(fileDisplayname);
                    size.setText(filesize);
                    tag.setText(filetag);
                    type.setText(filetype);
                    totaldownloads.setText(filedownloads);
                    tag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),filetag,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), displaydataAccoudingtokeyword.class);
                            intent.putExtra("keyword",filetag);
                            startActivity(intent);
                        }
                    });

                    type.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),filetype,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),displaydataAccoudingtokeyword.class);
                            intent.putExtra("keyword",filetype);
                            startActivity(intent);
                        }
                    });
                   // setfavbuttonimage();
                    addcomment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), comments.class);
                            intent.putExtra("filename",filename);
                            intent.putExtra("fileid",fileid);
                            startActivity(intent);
                        }
                    });
                   // getlastcomment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void setimagemethod() {
        new fontfiledetail.GetImageFromUrl(itemdisplay).execute(backgroundurl);
    }
    public class GetImageFromUrl extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        public GetImageFromUrl(ImageView img){
            this.imageView = img;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String stringUrl = url[0];
            bitmap = null;
            InputStream inputStream;
            try {
                inputStream = new java.net.URL(stringUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap){
            super.onPostExecute(bitmap);
            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            imageView.setBackground(ob);
        }
    }
}
