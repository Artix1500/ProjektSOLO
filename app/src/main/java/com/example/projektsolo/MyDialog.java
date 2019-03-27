package com.example.projektsolo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyDialog extends AppCompatDialogFragment {
    private EditText itemTitle;
    private myDialogListener listener;
    DatabaseHelper myDB;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogpopup, null);
        builder.setView(view).setTitle("Add new item")
                             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                             .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String title = itemTitle.getText().toString();
                                        if(title != "")
                                        {
                                            listener.additem(title);
                                        }
                                    }
                                });

        itemTitle = view.findViewById(R.id.newItemTitle);

        return builder.create();

     }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (myDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement myDialogListener!");
        }
    }

    public interface myDialogListener{
        void additem(String title);
    }
}
