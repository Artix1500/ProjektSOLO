package com.example.projektsolo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<DBItem> items;
    private Context mContext;
    private DatabaseHelper myDB;
    AlertDialog dialog;
    EditText editText;

    public RecyclerViewAdapter(MainActivity context, ArrayList<DBItem> items, DatabaseHelper myDB) {
        this.items = items;
        this.myDB = myDB;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.list_item.setText(items.get(i).getName());
        viewHolder.setID(items.get(i).getId());
        viewHolder.setRepeatChkBx(items.get(i).isChecked() == 1);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEdit(viewHolder);
                editText.setText(viewHolder.list_item.getText().toString());
                dialog.show();
            }
        });
    }

    private void initEdit(final ViewHolder viewHolder) {
        dialog = new AlertDialog.Builder(mContext).create();
        editText = new EditText(mContext);
        dialog.setView(editText);
        dialog.setTitle("Edit");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.updateName(editText.getText().toString(),
                        viewHolder.getID(),
                        viewHolder.list_item.getText().toString());
                ((MainActivity) mContext).updateList();

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "REMOVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteName(viewHolder.getID(),
                        viewHolder.list_item.getText().toString());
                ((MainActivity) mContext).updateList();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView list_item;
        RelativeLayout parentLayout;
        Integer id = -1;
        CheckBox repeatChkBx;

        public ViewHolder(View itemView) {
            super(itemView);
            list_item = itemView.findViewById(R.id.list_item);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            repeatChkBx = (CheckBox) itemView.findViewById(R.id.checkBox);
            repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if ( isChecked )
                        myDB.updateState(1,getID());

                    else
                        myDB.updateState(0,getID());


                }
            });
        }

        public void setID(Integer i)
        {
            id = i;
        }

        public Integer getID()
        {
            return id;
        }


        public CheckBox getRepeatChkBx() {
            return repeatChkBx;
        }

        public void setRepeatChkBx(boolean repeatChkBx) {
            this.repeatChkBx.setChecked(repeatChkBx);
        }

    }

}
