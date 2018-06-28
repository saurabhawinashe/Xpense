package com.homex.fexp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class first extends AppCompatActivity {

    public Button b,b1,b3;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        b=(Button)findViewById(R.id.button);
        b1=(Button)findViewById(R.id.button1);
        //b3=(Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(first.this,Main2Activity.class); //for sign up
                // Intent i=new Intent(MainActivity.this,Camera.class); //for sign up
                startActivity(i);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z=new Intent(first.this,LoginActivity.class);
                startActivity(z);
            }
        });
/*        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y=new Intent(MainActivity.this,Camera.class);
                startActivity(y);
            }
        });
*/

    }
    public void onBackPressed() {

        //super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

}