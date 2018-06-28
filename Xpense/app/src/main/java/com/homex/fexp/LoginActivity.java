package com.homex.fexp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn3=(Button)findViewById(R.id.button3);
        final DBhandler db = new DBhandler(this);
        final TextView ert=(TextView)findViewById(R.id.error_text);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mydata getdata=new Mydata();
                EditText ed1 = (EditText) findViewById(R.id.emaillog);
                EditText ed2 = (EditText) findViewById(R.id.passlog);
                SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedpref.edit();
                editor.putString("email",ed1.getText().toString());
                editor.apply();
                try {
                    getdata = db.getContact(ed1.getText().toString());
                    Log.d("msg",ed2.getText().toString());
                    Log.d("msg",ed1.getText().toString());
                    Log.d("msg",getdata.password);
                    if(getdata.password.equals(ed2.getText().toString())) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        ert.setText("Invalid Login!");

                    }
                }
                catch(Exception e)
                {
                    ert.setText("Invalid Email ID!");
                }

            }
        });
    }
}
