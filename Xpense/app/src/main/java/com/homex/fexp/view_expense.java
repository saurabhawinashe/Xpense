package com.homex.fexp;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class view_expense extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<list_item> itemlist;
    final DBhandler exp = new DBhandler(this);

    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);
        barChart = (BarChart)findViewById(R.id.bargraph);
        Calendar cal=Calendar.getInstance();
        SharedPreferences sharedpref=getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-01")));
        entries.add(new BarEntry(2f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-02")));
        entries.add(new BarEntry(3f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-03")));
        entries.add(new BarEntry(4f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-04")));
        entries.add(new BarEntry(5f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-05")));
        entries.add(new BarEntry(6f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-06")));
        entries.add(new BarEntry(7f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-07")));
        entries.add(new BarEntry(8f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-08")));
        entries.add(new BarEntry(9f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-09")));
        entries.add(new BarEntry(10f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-10")));
        entries.add(new BarEntry(11f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-11")));
        entries.add(new BarEntry(12f, exp.getmonthexpense(sharedpref.getString("email",""),cal.get(Calendar.YEAR)+"-12")));
        BarDataSet depenses = new BarDataSet(entries, "Expenses");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add((IBarDataSet) depenses);
        BarData Data = new BarData(dataSets);
        barChart.setData(Data);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(false);
        barChart.animateXY(3000, 3000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.getDescription().setText("Monthwise Expenses");
        barChart.getDescription().setPosition(850,1000);
        barChart.getDescription().setTextColor(ColorTemplate.getHoloBlue());
        barChart.getDescription().setTextSize(21);
        depenses.setColors(ColorTemplate.COLORFUL_COLORS);

        recyclerView=(RecyclerView)findViewById(R.id.rview);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<AddExpense> contacts = exp.getallexpenses(sharedpref.getString("email",""));
        itemlist=new ArrayList<>();
        for (AddExpense cn : contacts) {
            list_item l=new list_item(cn.getDescription().toString(),cn.getAmount()+"",cn.getCategory().toString(),cn.getDate().toString());
            itemlist.add(l);
        }
        if(itemlist.size()==0)
        {
            TextView noexptxt=(TextView)findViewById(R.id.noexp);
            noexptxt.setText("No Expenses");
        }
        else {
            adapter = new MyAdapdter(itemlist, this);
            recyclerView.setAdapter(adapter);
        }}
}
class list_item {
    private String description,amount,category,date;

    public list_item(String description, String amount, String category, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
class MyAdapdter extends RecyclerView.Adapter<MyAdapdter.ViewHolder>{
    private List<list_item> litems;
    private Context context;
    public MyAdapdter(List<list_item> litems,Context context) {
        this.litems=litems;
        this.context=context;
    }

    @Override
    public MyAdapdter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapdter.ViewHolder holder, int position) {
        list_item lviewitems=litems.get(position);

        holder.rdate.setText(lviewitems.getDate());
        holder.ramt.setText(lviewitems.getAmount());
        holder.rdesc.setText(lviewitems.getDescription());
        holder.rcat.setText(lviewitems.getCategory());
    }

    @Override
    public int getItemCount() {
        return litems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView rdesc,rdate,ramt,rcat;

        public ViewHolder(View itemView) {
            super(itemView);
            rdesc=(TextView)itemView.findViewById(R.id.rdesc);
            rdate=(TextView)itemView.findViewById(R.id.rdate);
            rcat=(TextView)itemView.findViewById(R.id.rcate);
            ramt=(TextView)itemView.findViewById(R.id.ramt);
        }
    }
}