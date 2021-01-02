package com.nanb.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class itemdisplay extends AppCompatActivity {

    private ImageButton back,faveorite;
    private Button download,type,tag;
    private ImageView itemdisplay,userdp;
    LinearLayout commentsection;
    private TextView name,size,totaldownloads,setwallpaper,setlockscreenwallpaper,addcomment,comment,nocomment;
    private String filetype,url,filename,fileDisplayname,filesize,fileid,filetag,currentuserid,filedownloads,filebackgroundurl;
    private ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;
    private FirebaseAuth mAuth;
    private View popupInputDialogView = null;
    ArrayList<itemclass> data;
    RecyclerView similiaritemrecycler;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Bitmap bitmap;
    private static final String URL_items = "http://192.168.225.213:8080/NANB_wallpaper/get_Detail.php?filename=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemdisplay);
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
        url = getIntent().getStringExtra("imageurl");
         filename=  getfilename(url);


        backmethod();

        faveoriteButtonmethod();
        setimagemethod();
        final String URL_Data =  URL_items+filename+"&&type=Wallpapers";
        //Toast.makeText(getApplicationContext(),URL_Data,Toast.LENGTH_SHORT).show();
        getfiledata(URL_Data);

        mProgressDialog = new ProgressDialog(itemdisplay.this);
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
                mMyTask = new DownloadTask()
                        .execute(stringToURL(
                                url
                        ));

            }
        });


    }

    private void getsimmilarfile(String filetype,String filetag) {
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getsimilliaritems.php?Type="+filetype+"&&Tag="+filetag;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject imagedata = jsonArray.getJSONObject(i);
                        itemclass itemclass = new itemclass(imagedata.getString("Type"),imagedata.getString("DisplayName"),imagedata.getString("downloadurl"));
                        data.add(itemclass);

                    }

                    adapter = new similaritemsAdapter(getApplicationContext(),data);
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

    private void getlastcomment() {
        String url = "http://192.168.225.213:8080/NANB_wallpaper/getlattestcomment.php?id="+fileid;
        final String[] userid = new String[1];
        final String[] commenttext = new String[1];

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                         userid[0] = jsonObject.getString("userid");
                         commenttext[0] = jsonObject.getString("comment");

                       // Toast.makeText(getApplicationContext(), userid[0],Toast.LENGTH_SHORT).show();
                        commentsection.setVisibility(View.VISIBLE);
                        nocomment.setVisibility(View.GONE);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid[0]);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("image")){
                                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(userdp);
                                }else{
                                    userdp.setImageResource(R.drawable.profiledp);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        comment.setText(commenttext[0]);
                    }



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

    private void setfavbuttonimage() {
        final String fav_url = "http://192.168.225.213:8080/NANB_wallpaper/getfavdetail.php?id="+fileid+"&&userid="+currentuserid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, fav_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String message = jsonObject.getString("message");
                        //Toast.makeText(getApplicationContext(),fav_url,Toast.LENGTH_SHORT).show();
                        if(message.equals("Favorite")){
                            faveorite.setImageResource(R.drawable.ic_fav);
                        }else{
                            faveorite.setImageResource(R.drawable.ic_faveorite);
                        }

                    }

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

    private void faveoriteButtonmethod() {
        faveorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_backgroundhelper backgroundhelper = new fav_backgroundhelper(getApplicationContext(),faveorite);
                backgroundhelper.execute(fileid,filename,currentuserid,url,filetype,fileDisplayname);
            }
        });
    }

    private void adddownloaddatatothedatabase(String currentuserid) {
        BackgroundHelper backgroundHelper = new BackgroundHelper(this);
        backgroundHelper.execute(fileid,filetype,"null",filetag,filename,fileDisplayname);
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
                    new GetImageFromUrl(itemdisplay).execute(filebackgroundurl);
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
                           Intent intent = new Intent(getApplicationContext(),displaydataAccoudingtokeyword.class);
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
                    setfavbuttonimage();
                    addcomment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),comments.class);
                            intent.putExtra("filename",filename);
                            intent.putExtra("fileid",fileid);
                            startActivity(intent);
                        }
                    });
                    getlastcomment();
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
    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{
        // Before the tasks execution
        protected void onPreExecute(){
            // Display the progress dialog on async task start
            mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL...urls){
            URL url = urls[0];
            HttpURLConnection connection = null;

            try{
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            }catch(IOException e){
                e.printStackTrace();
            }finally{
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result){
            // Hide the progress dialog
            mProgressDialog.dismiss();

            if(result!=null){
               //insert data to the download table in database
                // Save bitmap to internal storage

                final String URL_Data =  URL_items+filename+"&&type=Wallpapers";
                getfiledata(URL_Data);
                Uri imageInternalUri = saveImageToInternalStorage(result);
                // Set the ImageView image from internal storage
                adddownloaddatatothedatabase(currentuserid);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    String uri = imageInternalUri.getPath();
                    Uri imageuri = Uri.fromFile(new File(uri));
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageuri);
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),uri,Toast.LENGTH_SHORT).show();
                /**AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemdisplay.this);

                LayoutInflater layoutInflater = LayoutInflater.from(itemdisplay.this);

                // Inflate the popup dialog from a layout xml file.
                popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_dialog, null);

                // Get user input edittext and button ui controls in the popup dialog.

                alertDialogBuilder.setTitle("Set Image as");
                setwallpaper = popupInputDialogView.findViewById(R.id.setwallaper);
                setlockscreenwallpaper = popupInputDialogView.findViewById(R.id.lockwallpaper);

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //Toast.makeText(getApplicationContext(),"Image downloaded",Toast.LENGTH_SHORT).show();
                setwallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),"set wallpaper"+imageInternalUri,Toast.LENGTH_SHORT).show();
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            String uri = imageInternalUri.getPath();
                            Uri imageuri = Uri.fromFile(new File(uri));
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageuri);
                            wallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

                setlockscreenwallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),"set lock screen wallpaper"+imageInternalUri,Toast.LENGTH_SHORT).show();
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        String uri = imageInternalUri.getPath();
                        Uri imageuri = Uri.fromFile(new File(uri));
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageuri);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });**/
            }else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file =  new File(Environment.getExternalStorageDirectory()+"/Wallpaper");

        // Create a file to save the image
        file = new File(file, filename);

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }
    private String getfilename(String url) {
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();
        return filename;
    }

    private void setimagemethod() {
        Picasso.get().load(url).into(itemdisplay);

    }


    private void backmethod() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
