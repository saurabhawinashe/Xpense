package com.homex.fexp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;*/

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final DBhandler db = new DBhandler(this);
    private static final int PERMISSION_REQUEST_CODE = 1;
    ArrayList<String> imagePaths;
    String foldname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Mydata getData=new Mydata();
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        getData=db.getContact(sharedpref.getString("email",""));
        foldname=getData.phone_number;
        imagePaths =getFilePaths();
       /* GraphView gview=(GraphView)findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series=new LineGraphSeries<>(getDataPoint());
        gview.addSeries(series);

*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.i("camlol","hiii");
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                Log.i("camlol","hiii");
                requestPermission(); // Code for permission
            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        String amt,rexp;
        List<AddExpense> contacts = db.getallexpenses(sharedpref.getString("email",""));
        if(contacts.size()==0)
        {TextView ermsg=(TextView)findViewById(R.id.errormsg);
            ermsg.setText("No Recent Expense");

        }
        else{
        TextView rdes=(TextView)findViewById(R.id.rdes);
            TextView sym=(TextView)findViewById(R.id.sym);
        TextView rcat=(TextView)findViewById(R.id.rcat);
        TextView ramt=(TextView)findViewById(R.id.ramt);
        TextView rdat=(TextView)findViewById(R.id.rdat);
        rdes.setText(contacts.get(0).description);
            sym.setText("₹");
        rcat.setText(contacts.get(0).category);
        amt=""+contacts.get(0).amount;
        ramt.setText(amt);
        rdat.setText(contacts.get(0).date);
        }
        int rmon,ryear;
        String s;
        Calendar cal=Calendar.getInstance();
        rmon=cal.get(Calendar.MONTH)+1;
        ryear=cal.get(Calendar.YEAR);
        if(rmon<10)
            s=ryear+"-0"+rmon;
        else
            s=ryear+"-"+rmon;
        TextView rtxt=(TextView)findViewById(R.id.rexpval);
        rexp=""+db.getmonthexpense(sharedpref.getString("email",""),s);
        rtxt.setText(rexp);
        if(imagePaths.size()>=1) {
            ImageView im1 = (ImageView) findViewById(R.id.rimg1);
            im1.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(imagePaths.size() - 1)));

            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
                    View mview = getLayoutInflater().inflate(R.layout.imgviewing, null);
                    ImageView imgv = (ImageView) mview.findViewById(R.id.imaview);
                    imgv.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(imagePaths.size() - 1)));
                    mbuilder.setView(mview);
                    AlertDialog dialog = mbuilder.create();
                    dialog.show();

                }
            });
        }
        if(imagePaths.size()>=2) {
            ImageView im2 = (ImageView) findViewById(R.id.rimg2);
            im2.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(imagePaths.size() - 2)));
            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
                    View mview = getLayoutInflater().inflate(R.layout.imgviewing, null);
                    ImageView imgv = (ImageView) mview.findViewById(R.id.imaview);
                    imgv.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(imagePaths.size() - 2)));
                    mbuilder.setView(mview);
                    AlertDialog dialog = mbuilder.create();
                    dialog.show();

                }
            });
        }


        Button ebtn=(Button)findViewById(R.id.emailbtn);
        ebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rmon,ryear;
                String s;
                Calendar cal=Calendar.getInstance();
                rmon=cal.get(Calendar.MONTH)+1;
                ryear=cal.get(Calendar.YEAR);
                if(rmon<10)
                    s=ryear+"-0"+rmon;
                else
                    s=ryear+"-"+rmon;
                List<AddExpense> contactList = new ArrayList<AddExpense>();
                List<AddExpense> explist = new ArrayList<AddExpense>();
                SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                contactList=db.getallexpenses(sharedpref.getString("email",""));
                for (AddExpense cn : contactList) {
                    String[] data = cn.getDate().split("-");
                    String dob = data[0]+"-"+data[1];
                    if(s.equals(dob)){
                        explist.add(cn);
                    }
                }
                String html="<html><head><style>table,th,td{ border: 1px solid black;}</style></head><body><table><tr><th>Date</th><th>Category</th><th>Description</th><th>Amount</th></tr>";
                for (AddExpense cn : explist) {
                    html+="<tr><td>"+cn.getDate()+"</td><td>"+cn.getCategory()+"</td><td>"+cn.getDescription()+"</td><td>"+cn.getAmount()+"</td></tr>";
                }
                html+="</table></body></html>";
                try {
                    Toast.makeText(MainActivity.this,"Sending Mail...",Toast.LENGTH_SHORT).show();
                    GMailSender sender = new GMailSender("xpenseman@gmail.com","xpenseman123");
                    Log.i("SendMail", "huaaa");
                    sender.sendMail("Monthly Statement",
                            html,
                            "xpenseman@gmail.com",sharedpref.getString("email",""));
                    Log.i("SendMail", "111huaaa");
                    Toast.makeText(MainActivity.this,"Mail sent!",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(MainActivity.this,"Error sending mail!",Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    /*private DataPoint[] getDataPoint(){
       DataPoint[] dpoint=new DataPoint[]{
         new DataPoint(0,1),
               new DataPoint(1,0.5),

        new DataPoint(2,3),
               new DataPoint(3,10),
        new DataPoint(4,5),
               new DataPoint(5,1),
        new DataPoint(6,7)
       };
       return dpoint;

    }*/

    public boolean onCreateOptionsMenu(Menu menu){
        Mydata getdata=new Mydata();
        TextView navname=(TextView)findViewById(R.id.user_name);
        TextView navemail=(TextView)findViewById(R.id.e_mail);
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        getdata=db.getContact(sharedpref.getString("email",""));
        navname.setText(getdata.getName());
        navemail.setText(sharedpref.getString("email",""));
        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent i=new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent i= new Intent(this,addexp1.class);
            startActivity(i);
        } else if (id == R.id.nav_cal) {
            Intent i= new Intent(this,view_expense.class);
            startActivity(i);
        } else if (id == R.id.nav_bills) {

            Intent i= new Intent(this,mybills.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent i= new Intent(this,setlist.class);
            startActivity(i);

        }
        else if(id == R.id.nav_logout)
        {
            Intent i= new Intent(this,first.class);
            startActivity(i);
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        //if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          //  Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        //} else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        //}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            switch (requestCode) {
                case PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.e("camlol", "Permission Granted, Now you can use local drive .");
                    } else {
                        Log.e("camlol", "Permission Denied, You cannot use local drive .");
                    }
                    break;
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
}
