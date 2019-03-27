package com.example.projektsolo;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyDialog.myDialogListener {

    private static final String TAG = "MainActivity";
    private static Context context;
    public ArrayList<DBItem> items = new ArrayList<>();
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DatabaseHelper(this);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        initRecycleView();
        Button addBtn = (Button) findViewById(R.id.showMyDialog);

        addBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                    openDialog();
              }
          }
        );
    }


    private void initRecycleView()
    {
        items = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                Integer id = Integer.parseInt(data.getString(0));
                String name = data.getString(1);
                Integer checked = Integer.parseInt(data.getString(2));
                items.add(new DBItem(id,name,checked));
            }
        }

        Log.d(TAG, "initRecycleView: init");
        RecyclerView recycleview = findViewById(R.id.recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,items, myDB);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this));

    }


    public void updateList()
    {
        initRecycleView();
    }


    private void openDialog() {
        MyDialog mydialog = new MyDialog();
        mydialog.show(getSupportFragmentManager(), "My dialog");
    }


    @Override
    public void additem(String title) {
        myDB.addData(title);
        initRecycleView();
    }


}
