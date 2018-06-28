package com.homex.fexp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class addexp1 extends AppCompatActivity {
    int exval[]={0,0,0,0,0,0,0,0};
    String mname[]={"January","February","March","April","May","June","July","August","September","October","November","December"};
    final DBhandler exp = new DBhandler(this);
    String monthname[]={"Food","Shopping","Entertainment","Transfer","Travel","Bills","Health","Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexp1);


        setChart();

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.addexpbutton);
        fab.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v)
                {
                    Intent i=new Intent(addexp1.this,add_expense.class);
                    startActivity(i);
                }
        });
    }
    public void setChart()
    {
        int rmonth,ryear,total=0;
        String s;
        TextView cm=(TextView)findViewById(R.id.mon);
        TextView cy=(TextView)findViewById(R.id.year);
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        Calendar cal=Calendar.getInstance();
        rmonth=cal.get(Calendar.MONTH);
        ryear=cal.get(Calendar.YEAR);
        cm.setText(mname[rmonth]);
        cy.setText(ryear+"");
        rmonth+=1;

        if(rmonth<10)
            s=ryear+"-0"+rmonth;
        else
            s=ryear+"-"+rmonth;
        exval[0]=exp.getcategoryexpense(sharedpref.getString("email",""),"Food",s);
        exval[1]=exp.getcategoryexpense(sharedpref.getString("email",""),"Shopping",s);
        exval[2]=exp.getcategoryexpense(sharedpref.getString("email",""),"Entertainment",s);
        exval[3]=exp.getcategoryexpense(sharedpref.getString("email",""),"Transfer",s);
        exval[4]=exp.getcategoryexpense(sharedpref.getString("email",""),"Travel",s);
        exval[5]=exp.getcategoryexpense(sharedpref.getString("email",""),"Bills",s);
        exval[6]=exp.getcategoryexpense(sharedpref.getString("email",""),"Health",s);
        exval[7]=exp.getcategoryexpense(sharedpref.getString("email",""),"Other",s);
        List<PieEntry> pieentries=new ArrayList<>();
        for(int i=0;i<exval.length;i++)
        {   if(exval[i]>0) {
            pieentries.add(new PieEntry(exval[i], monthname[i]));
            total = total + exval[i];
        }}
        PieDataSet dataset=new PieDataSet(pieentries,"");

        dataset.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data=new PieData(dataset);
        data.setValueTextSize(20f);
        PieChart chart=(PieChart)findViewById(R.id.chartp);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setCenterText("Expense:₹"+total);
        chart.setCenterTextSize(22);
        chart.setUsePercentValues(true);
        chart.setData(data);
        chart.animateX(1000);
        chart.invalidate();
    }
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i=new Intent(addexp1.this,MainActivity.class);
            /*Intent i=new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        startActivity(i);

    }
}
