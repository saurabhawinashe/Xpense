package com.homex.fexp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class mybills extends AppCompatActivity {
    ArrayList<String> imagePaths;
    private static final int PERMISSION_CAMERA_CODE = 3;
    static final int REQUEST_IMAGE_CAPTURE=1;
    private File output=null;
    private String file_name;
    String foldname;
    final DBhandler db = new DBhandler(this);

    FloatingActionButton clickimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybills);
        Mydata getData=new Mydata();
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        getData=db.getContact(sharedpref.getString("email",""));
        foldname=getData.phone_number;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        imagePaths =getFilePaths();
        Log.e("size",imagePaths.size()+"");
        if(imagePaths.size()==0){
            TextView noimgtxt=(TextView)findViewById(R.id.noimg);
            noimgtxt.setText("No Bills in your wallet");
        }
        else {
            ListView ilistview = (ListView) findViewById(R.id.imagelist);

            CustomAdapter cadapter = new CustomAdapter();
            ilistview.setAdapter(cadapter);
            ilistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder mbuilder=new AlertDialog.Builder(mybills.this);
                    View mview=getLayoutInflater().inflate(R.layout.imgviewing,null);
                    ImageView imgv=(ImageView)mview.findViewById(R.id.imaview);
                    imgv.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(position)));
                    mbuilder.setView(mview);
                    AlertDialog dialog=mbuilder.create();
                    dialog.show();
                }
            });
        }
        clickimg=(FloatingActionButton) findViewById(R.id.addbills);
        clickimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermissionCamera())
                    {
                        // Code for above or equal 23 API Oriented Device
                        // Your Permission granted already .Do next code
                    } else {
                        requestPermissionCamera(); // Code for permission
                    }
                }
                else
                {

                    // Code for Below 23 API Oriented Device
                    // Do next code
                }
                launchCamera(v);
            }


        });
    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imagePaths.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, final ViewGroup parent) {
            view=getLayoutInflater().inflate(R.layout.customimages,null);
            ImageView imview=(ImageView)view.findViewById(R.id.custimg);
            TextView imd=(TextView)view.findViewById(R.id.imgdate);
            TextView imt=(TextView)view.findViewById(R.id.imgtime);
            Log.e("no",imagePaths.get(position));
            String arr[]=imagePaths.get(position).split("_");
            imview.setImageBitmap(decodeSampledBitmapFromFile(imagePaths.get(position),
            640,640));
            imd.setText(arr[1]);
            imt.setText(arr[2]);

            return view;
        }
    }
    public  ArrayList<String> getFilePaths()

    {

        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + "/Xpense/"+foldname);

        // check for directory
        if (directory.isDirectory())
        {
            // getting list of file paths
            File[] listFiles = directory.listFiles();

            // Check for count
            if (listFiles.length > 0)
            {

                for (int i = 0; i < listFiles.length; i++)
                {

                    String filePath = listFiles[i].getAbsolutePath();
                    if(filePath.endsWith(".jpg")||filePath.endsWith(".png")||filePath.endsWith(".jpeg"))
                    filePaths.add(filePath);
                }
            }
        }
        return filePaths;
    }

    private boolean checkPermissionCamera() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("camlol", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("camlol", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void launchCamera(View view)
    {   Mydata getData=new Mydata();
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        getData=db.getContact(sharedpref.getString("email",""));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir= Environment.getExternalStoragePublicDirectory("Xpense");
        File picsdir = new File(dir,getData.phone_number);
        Calendar currentTime = Calendar.getInstance();
        file_name = "_"+currentTime.get(Calendar.YEAR)+"-"+currentTime.get(Calendar.MONTH)+"-"+currentTime.get(Calendar.DAY_OF_MONTH)+"_"+currentTime.get(Calendar.HOUR_OF_DAY)+":"+currentTime.get(Calendar.MINUTE)+":"+currentTime.get(Calendar.SECOND)+"_";
        Log.i("camlol",file_name);
        output = new File(picsdir, file_name+".jpeg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        Log.i("camlol","hua0");
        Toast.makeText(getApplicationContext(),"Lauching....",Toast.LENGTH_SHORT).show();
        try{
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        }
        catch(Exception e){
            Log.i("camlol", e.getMessage(),e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("camlol","hua");
        if(requestCode==REQUEST_IMAGE_CAPTURE)
        {
            Intent i=new Intent(mybills.this,mybills.class);

            i.setDataAndType(Uri.fromFile(output), "image/jpeg");
            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(),"not In application",Toast.LENGTH_SHORT).show();
        }

    }
    public static Bitmap decodeSampledBitmapFromFile(String filename,
                                                     int reqWidth, int reqHeight) {
// First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options
                options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);
// Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
// Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
// BEGIN_INCLUDE (calculate_sample_size)
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
// END_INCLUDE (calculate_sample_size)
    }
    public void onBackPressed() {
            //super.onBackPressed();
        Intent i=new Intent(mybills.this,MainActivity.class);
            /*Intent i=new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
           startActivity(i);

    }
}
