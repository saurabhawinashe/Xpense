package com.homex.fexp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class add_expense extends AppCompatActivity {
    ImageView cimage;
    static final int dialog_id=0;
    int cyear,cmon,cday;
    final DBhandler exp = new DBhandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        final EditText descbox=(EditText)findViewById(R.id.desc);
        final EditText amtbox=(EditText)findViewById(R.id.amt);
        final EditText catbox=(EditText)findViewById(R.id.ecategory);
        final EditText datebox=(EditText)findViewById(R.id.dat);
        final Calendar cal=Calendar.getInstance();
        final SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        cyear=cal.get(Calendar.YEAR);
        cmon=cal.get(Calendar.MONTH);
        cday=cal.get(Calendar.DAY_OF_MONTH);
        showDialogOnButtonClick();
        defineimg();
        Button catbtn=(Button)findViewById(R.id.saveexp);
        catbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exp.addexpense(new AddExpense(sharedpref.getString("email",""),catbox.getText().toString(),descbox.getText().toString(),Integer.parseInt(amtbox.getText().toString()), datebox.getText().toString()));
                Intent i= new Intent(add_expense.this,addexp1.class);
                startActivity(i);
                Toast.makeText(add_expense.this, "Added!", Toast.LENGTH_SHORT).show();
            }
        });

        }

    public void showDialogOnButtonClick()
    {
        cimage=(ImageView)findViewById(R.id.calimage);
        cimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(dialog_id);
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        if(id==dialog_id)
            return new DatePickerDialog(this,dpickerListener,cyear,cmon,cday);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
           cyear=year;
            cmon=month+1;
            cday=dayOfMonth;
            String cmonc,cdayc;
            EditText dvar=(EditText)findViewById(R.id.dat);
            if(cmon<10)
                cmonc="0"+Integer.toString(cmon);
            else
                cmonc=Integer.toString(cmon);
            if(cday<10)
                cdayc="0"+Integer.toString(cday);
            else
                cdayc=Integer.toString(cday);
            dvar.setText(Integer.toString(cyear)+"-"+cmonc+"-"+cdayc);
        }
    };

    public void defineimg()
    {
        findViewById(R.id.foodimg).setOnClickListener(imageclickListener);
        findViewById(R.id.entimg).setOnClickListener(imageclickListener);
        findViewById(R.id.shopimg).setOnClickListener(imageclickListener);
        findViewById(R.id.travelimg).setOnClickListener(imageclickListener);
        findViewById(R.id.transferimg).setOnClickListener(imageclickListener);
        findViewById(R.id.healthimg).setOnClickListener(imageclickListener);
        findViewById(R.id.billsimg).setOnClickListener(imageclickListener);
        findViewById(R.id.otherimg).setOnClickListener(imageclickListener);
    }
    private View.OnClickListener imageclickListener=new View.OnClickListener(){
        public void onClick(View view)
        {
            EditText xtext=(EditText)findViewById(R.id.ecategory);
           switch(view.getId())
           {
               case R.id.foodimg:xtext.setText("Food");
                                 break;
               case R.id.entimg:xtext.setText("Entertainment");
                   break;
               case R.id.shopimg:xtext.setText("Shopping");
                   break;
               case R.id.transferimg:xtext.setText("Transfer");
                   break;
               case R.id.travelimg:xtext.setText("Travel");
                   break;
               case R.id.healthimg:xtext.setText("Health");
                   break;
               case R.id.billsimg:xtext.setText("Bills");
                   break;
               case R.id.otherimg:xtext.setText("Other");
                   break;
           }
        }
    };




}