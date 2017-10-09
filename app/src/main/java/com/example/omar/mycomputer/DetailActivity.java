package com.example.omar.mycomputer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    Button btnCancel;
    Button btnEdit;
    TextView txtLaptop;
    TextView txtModel;
    TextView txtMemory;
    Bundle computerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //UI Elements
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnEdit = (Button)findViewById(R.id.btnEdit);

        /***
         * Getting intent from MainActivity and checking if its empty or not.
         * If not empty, SetComputerInfo is called, which sets the TextViews
         * to display whatever data input from earlier in EditActivity.
         */

        if (savedInstanceState != null) {
            computerInfo = savedInstanceState.getBundle("computerInfo");
        }

        Intent intentData = getIntent();
        computerInfo = intentData.getExtras();
        if(computerInfo != null) {
            SetComputerInfo();
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenEditActivity();
            }
        });
    }

    /***
     * Opens edit activity, and if relevant, also sends computer info to edit activity
     */
    public void OpenEditActivity()
    {
        Intent i = new Intent(this, EditActivity.class);

        if (computerInfo != null) {
            String name = (String) computerInfo.get("name");
            Boolean laptop = (Boolean) computerInfo.get("laptop");
            int memory = (int) computerInfo.get("memory");
            i.putExtra("name", name);
            i.putExtra("laptop", laptop);
            i.putExtra("memory", memory);
        }

        startActivityForResult(i, DataKeys.GET_EDIT_DATA);
    }

    /***
     * Updates the UI with the new computer info
     */
    private void SetComputerInfo()
    {
        txtLaptop = (TextView)findViewById(R.id.txtLaptop);
        txtModel = (TextView)findViewById(R.id.txtModel);
        txtMemory = (TextView)findViewById(R.id.txtRam);

        String name = computerInfo.getString("name");
        Boolean laptop = computerInfo.getBoolean("laptop");
        int memory = computerInfo.getInt("memory");

        if(laptop == true)
            txtLaptop.setText(R.string.string_yes);
        else
            txtLaptop.setText(R.string.string_no);

        txtModel.setText(name);
        txtMemory.setText(String.valueOf(memory));
        txtMemory.append(" GB");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Simply passes the data back from edit activity to main activity
        if(requestCode == DataKeys.GET_EDIT_DATA && resultCode == RESULT_OK)
        {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("computerInfo", computerInfo);
    }
}
