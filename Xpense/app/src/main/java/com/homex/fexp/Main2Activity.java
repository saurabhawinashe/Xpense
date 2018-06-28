package com.homex.fexp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final DBhandler db = new DBhandler(this);
        Button bt = (Button) findViewById(R.id.signbtn);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed2 = (EditText) findViewById(R.id.namebox);
                EditText ed1 = (EditText) findViewById(R.id.emailbox);
                EditText ed4 = (EditText) findViewById(R.id.passbox);
                EditText ed3 = (EditText) findViewById(R.id.phonebox);
                String rootDirectory = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(rootDirectory + "/Xpense/"+ed3.getText().toString());
                boolean s = myDir.mkdirs();
                Log.e("Name: ",ed2.getText().toString());
                db.addContact(new Mydata(ed1.getText().toString(), ed4.getText().toString(), ed2.getText().toString(), ed3.getText().toString(),1000000000,22,0));
                List<Mydata> contacts = db.getAllContacts();

                for (Mydata cn : contacts) {
                    String log = "Name: " + cn.getName() + " ,Password: " +
                            cn.getPassword() + "Email:" + cn.getEmail() + "Phone:" + cn.getPhone_number();
                    Log.d("Name: ", log);

                }

                SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedpref.edit();
                editor.putString("email",ed1.getText().toString());
                editor.apply();
                Intent i = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(i);

            }
        });



    }

}
