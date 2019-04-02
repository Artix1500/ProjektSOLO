package com.example.projektsolo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements MyDialog.myDialogListener {

    private static final String TAG = "MainActivity";
    private static Context context;
    public ArrayList<DBItem> items = new ArrayList<>();
    private DatabaseHelper myDB;
    private Button btnShowSetting;
    private Button addBtn;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        addBtn = (Button) findViewById(R.id.showMyDialog);
        btnShowSetting = (Button) findViewById(R.id.ShowSetting);

        initRecycleView();
        addListenersToButtons();

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = createSharedPreferenceListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
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

        boolean sort = sharedPreferences.getBoolean("Sort_alphabetically",false);
        if(sort)
        {
            items.sort(new Comparator<DBItem>() {
                @Override
                public int compare(DBItem o1, DBItem o2) {
                    return o1.getName().compareToIgnoreCase( o2.getName() );
                }
            });
        }
        RecyclerView recycleview = findViewById(R.id.recycle_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,items, myDB);
        recycleview.setAdapter(adapter);
        recycleview.setLayoutManager(new LinearLayoutManager(this));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateList()
    {
        initRecycleView();
    }


    private void openDialog() {
        MyDialog mydialog = new MyDialog();
        mydialog.show(getSupportFragmentManager(), "My dialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void additem(String title) {
        myDB.addData(title);
        initRecycleView();
    }

    private SharedPreferences.OnSharedPreferenceChangeListener createSharedPreferenceListener() {
        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                initRecycleView();
            }
        };
    }

    private void addListenersToButtons() {
        btnShowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  openDialog();
              }
          }
        );
    }

}
