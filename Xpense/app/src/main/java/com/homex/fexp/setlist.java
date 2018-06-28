package com.homex.fexp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class setlist extends AppCompatActivity {
    ListView lview;
    int hour,minute,fhour,fmin,nowhour,nowmin;
    ArrayAdapter<String> adapter;
    EditText upname;
    EditText upemail;
    EditText upphone;
    EditText uppass;
    final DBhandler db = new DBhandler(this);
    Intent i=null;
    String listofset[]={"Set Budget","Notifications"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlist);

        lview=(ListView)findViewById(R.id.list_view);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listofset);
        i = new Intent(this,NotificationService.class);
        final PendingIntent PI = PendingIntent.getService(this, 0, i, 0);
        final AlarmManager am = (AlarmManager)getSystemService(this.ALARM_SERVICE);

        lview.setAdapter(adapter);
        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position)=="Set Budget")
                {
                    AlertDialog.Builder mbuilder=new AlertDialog.Builder(setlist.this);
                    View mview=getLayoutInflater().inflate(R.layout.buddialog,null);
                    final EditText budbox=(EditText)mview.findViewById(R.id.budgetbox);
                    mbuilder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Mydata getData=new Mydata();
                            SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            getData=db.getContact(sharedpref.getString("email",""));
                            getData.budget=Integer.parseInt(budbox.getText().toString());
                            db.updateContact(getData);
                            getData=db.getContact(sharedpref.getString("email",""));
                            dialog.dismiss();
                            Toast.makeText(setlist.this,"Your budget is set to "+getData.budget,Toast.LENGTH_LONG).show();
                        }
                    });
                    mbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mbuilder.setView(mview);
                    AlertDialog dialog=mbuilder.create();
                    dialog.show();
                }
                else if(parent.getItemAtPosition(position)=="Notifications")
                {

                    AlertDialog.Builder mbuilder=new AlertDialog.Builder(setlist.this);
                    View mview=getLayoutInflater().inflate(R.layout.notify,null);
                    final EditText tbox=(EditText)mview.findViewById(R.id.timebox);
                    tbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Calendar c=Calendar.getInstance();
                            hour=c.get(Calendar.HOUR_OF_DAY);
                            minute=c.get(Calendar.MINUTE);
                            TimePickerDialog timePickerDialog = new TimePickerDialog(setlist.this,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            Mydata getData=new Mydata();
                                            SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                            getData=db.getContact(sharedpref.getString("email",""));
                                            nowhour=hourOfDay;
                                            nowmin=minute;
                                            tbox.setText(hourOfDay + ":" + minute);
                                            getData.hour=hourOfDay;
                                            getData.minute=minute;
                                            db.updateContact(getData);
                                            getData=db.getContact(sharedpref.getString("email",""));
                                            Log.e("er",getData.hour+" "+getData.minute);
                                        }
                                    }, hour, minute, false);
                            timePickerDialog.show();
                        }
                    });
                    mbuilder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(setlist.this,"Notifications will be received at "+nowhour+":"+nowmin,Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            java.util.Calendar cur_cal = new GregorianCalendar();
                            cur_cal.setTimeInMillis(System.currentTimeMillis());
                            Mydata getData=new Mydata();
                            SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                            getData=db.getContact(sharedpref.getString("email",""));
                            java.util.Calendar cal = new GregorianCalendar();
                            cal.set(java.util.Calendar.HOUR_OF_DAY, getData.hour);
                            cal.set(java.util.Calendar.MINUTE, getData.minute);
                            am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24*60*60*1000,PI);
                            startService(i);

                        }
                    });
                    mbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    mbuilder.setView(mview);
                    AlertDialog dialog=mbuilder.create();
                    dialog.show();
                }
                /*else if(parent.getItemAtPosition(position)=="Personal Information")
                {
                    Mydata getData=new Mydata();
                    upname=(EditText)findViewById(R.id.cname);
                    upemail=(EditText)findViewById(R.id.cemail);
                    upphone=(EditText)findViewById(R.id.cphone);
                    uppass=(EditText)findViewById(R.id.cpass);
                    SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    getData=db.getContact(sharedpref.getString("email",""));
                    Log.e("e",getData.getEmail());
                    Log.e("n",getData.getName());
                    Log.e("pw",getData.getPassword());
                    Log.e("p",getData.getPhone_number());
                    //upname.setText("khuk");
                    //upemail.setText("jygiu");
                    //uppass.setText("jyhgyu");
                    //upphone.setText("ioiiohjhk");
                    Intent i= new Intent(setlist.this,setting.class);
                    startActivity(i);
                }*/
            }
        });
    }
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i=new Intent(setlist.this,MainActivity.class);
            /*Intent i=new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        startActivity(i);

    }
}
