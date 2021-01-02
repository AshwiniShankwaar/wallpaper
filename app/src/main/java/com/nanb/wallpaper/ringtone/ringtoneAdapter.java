package com.nanb.wallpaper.ringtone;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ringtoneAdapter extends RecyclerView.Adapter<ringtoneviwholder> {

    Context mcontext;
    ArrayList<itemclass> data;
    boolean isplaying = false;
    private ProgressDialog mProgressDialog;
    private AsyncTask mMyTask;
    private View popupInputDialogView = null;
    String rintoneurl;
    String URL_items = "http://192.168.225.213:8080/NANB_wallpaper/get_Detail.php?filename=",fileid,filetype,filetag,filesize,filedisplayname,filetotaldownloads;
    FirebaseAuth mAuth;
    public String filename;
    TextView setasringtone,setasalarm,setasnotification;
    ArrayList<fildedata> fildata;
    public ringtoneAdapter(Context mcontext, ArrayList<itemclass> data) {
        this.mcontext = mcontext;
        this.data = data;
    }

    MediaPlayer mplayer;
    @NonNull
    @Override
    public ringtoneviwholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ringtone_item_layout, parent, false);
        ringtoneviwholder ringtoneviwholder = new ringtoneviwholder(v);
        return ringtoneviwholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ringtoneviwholder holder, int position) {
        itemclass itemclass = data.get(position);
        rintoneurl = itemclass.getDownloadurl();
        filename = getfilename(rintoneurl);
        String finalurl = URL_items+filename+"&&type=Ringtones";
        getfiledata(holder,finalurl);
        holder.downloadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemclass itemclass = data.get(position);
                String ringtoneurl = itemclass.getDownloadurl();
                filename = getfilename(ringtoneurl);
                String datareturn = startdownload(ringtoneurl);
                if(datareturn.equals("completed")){
                    moveFile(Environment.getExternalStorageDirectory()+"/Download/",filename,Environment.getExternalStorageDirectory()+"/Wallpaper/ringtone/");
                    fildedata filedata = fildata.get(position);
                    fileid = filedata.getId();
                    filetype = filedata.getType();
                    filetag = filedata.getTag();
                    filesize = filedata.getSize();
                    filedisplayname = filedata.getDisplayName();
                    filetotaldownloads = filedata.getTotal_downloads();
                    adddownloaddatatothedatabase("null",fileid,filetype,filetag,filename,filedisplayname);
                //showdilogmethod(filename);
                }



            }
        });
        holder.pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mplayer!=null){
                    if(mplayer.isPlaying()){
                        mplayer.stop();
                        isplaying = false;
                        holder.pausebutton.setVisibility(View.GONE);
                        //mplayer.reset();
                        //mplayer.release();
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemclass itemclass = data.get(position);
                String ringtoneurl = itemclass.getDownloadurl();
                if(mplayer!=null){
                    mplayer.reset();
                }
                isplaying = true;
                mplayer = new MediaPlayer();
                mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mplayer.setDataSource(ringtoneurl);
                    mplayer.prepare();
                    mplayer.start();
                    holder.pausebutton.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //Toast.makeText(mcontext,finalurl,Toast.LENGTH_LONG).show();

    }

    private void showdilogmethod(String filenamedata) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcontext);

        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_ringtone, null);

        // Get user input edittext and button ui controls in the popup dialog.

        alertDialogBuilder.setTitle("Set Image as");
        setasalarm = popupInputDialogView.findViewById(R.id.addtoalarm);
        setasringtone = popupInputDialogView.findViewById(R.id.addtoringtone);
        setasnotification = popupInputDialogView.findViewById(R.id.addtonotification);


        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);

        // Create AlertDialog and show.
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        //Toast.makeText(getApplicationContext(),"Image downloaded",Toast.LENGTH_SHORT).show();
        setasalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mcontext,"check",Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Download/"+filenamedata);
                File file =new File(String.valueOf(uri));
                if(file.exists()){
                    try {
                        if (checkSystemWritePermission()) {
                            RingtoneManager.setActualDefaultRingtoneUri(mcontext, RingtoneManager.TYPE_ALARM, uri);
                            Toast.makeText(mcontext, "Set as alarm successfully ", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mcontext, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.i("alarm",e.toString());
                        Toast.makeText(mcontext, "unable to set as alarm ", Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                }else{
                    Toast.makeText(mcontext,"not availabe",Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        });
        setasringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mcontext,"check",Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Download/"+filenamedata);
                File file =new File(String.valueOf(uri));
                if(file.exists()){
                    try {
                        if (checkSystemWritePermission()) {
                            RingtoneManager.setActualDefaultRingtoneUri(mcontext, RingtoneManager.TYPE_RINGTONE, uri);
                            Toast.makeText(mcontext, "Set as Ringtone successfully ", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mcontext, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.i("alarm",e.toString());
                        Toast.makeText(mcontext, "unable to set as ringtone ", Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                }else{
                    Toast.makeText(mcontext,"not availabe",Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        });
        setasnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mcontext,"check",Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Download/"+filenamedata);
                File file =new File(String.valueOf(uri));
                if(file.exists()){
                    try {
                        if (checkSystemWritePermission()) {
                            RingtoneManager.setActualDefaultRingtoneUri(mcontext, RingtoneManager.TYPE_NOTIFICATION, uri);
                            Toast.makeText(mcontext, "Set as Notification successfully ", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mcontext, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.i("alarm",e.toString());
                        Toast.makeText(mcontext, "unable to set as notification ", Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                }else{
                    Toast.makeText(mcontext,"not availabe",Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        });

    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(mcontext))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + mcontext.getPackageName()));
            mcontext.startActivity(intent);
        }
    }
    private void getfiledata(ringtoneviwholder holder,String finalurl) {

        mAuth = FirebaseAuth.getInstance();
        fildata = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(mcontext,response,Toast.LENGTH_SHORT).show();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        //Toast.makeText(getApplicationContext(),jsonObject.getString("id"),Toast.LENGTH_SHORT).show();
                        //create class to extract the data from the array then put it in filds
                        fildedata filedata = new fildedata(jsonObject.getString("id"),jsonObject.getString("Type"),jsonObject.getString("Tag"),jsonObject.getString("size"),jsonObject.getString("DisplayName"),jsonObject.getString("Total_downloads"));

                       //Toast.makeText(mcontext,filedata.getId()+" "+ filedata.getType()+" "+filedata.getTag()+" "+filedata.getSize()+" "+filedata.getDisplayName()+" "+filedata.getTotal_downloads(),Toast.LENGTH_LONG).show();
                       fildata.add(filedata);

                        holder.name.setText(filedata.getDisplayName());
                        holder.size.setText(filedata.getSize());
                        holder.tag.setText(filedata.getTag());
                        holder.totaldownloads.setText(filedata.getTotal_downloads());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mcontext,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(mcontext).add(stringRequest);
    }

    private void adddownloaddatatothedatabase(String uid, String fileid, String filetype, String filetag, String filename, String fileDisplayname) {
        BackgroundHelper backgroundHelper = new BackgroundHelper(mcontext);
        backgroundHelper.execute(fileid,filetype,uid,filetag,filename,fileDisplayname);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private String getfilename(String url) {
        Uri uri = Uri.parse(url);
        String filename = uri.getLastPathSegment();
        return filename;
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
        DownloadManager manager = (DownloadManager) mcontext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return "completed";


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
}



class ringtoneviwholder extends RecyclerView.ViewHolder{

    CircleImageView pausebutton;
    TextView name,size,tag,totaldownloads;
    ImageView image;
    ImageButton downloadbutton;
    public ringtoneviwholder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.music);
        pausebutton = itemView.findViewById(R.id.pause);
        name = itemView.findViewById(R.id.name);
        downloadbutton=itemView.findViewById(R.id.download);
        size = itemView.findViewById(R.id.size);
        tag = itemView.findViewById(R.id.tag);
        totaldownloads = itemView.findViewById(R.id.totaldownload);
    }
}