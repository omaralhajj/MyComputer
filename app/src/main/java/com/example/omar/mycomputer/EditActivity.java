package com.example.omar.mycomputer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    //public static final int RESULT_OK = 102;

    Button btnOk;
    Button btnCancelEdit;
    Boolean isLaptop = false;
    EditText name;
    EditText memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancelEdit = (Button)findViewById(R.id.btnCancelEdit);
        name = (EditText)findViewById(R.id.editModel);
        memory = (EditText)findViewById(R.id.editMemory);

        if(savedInstanceState != null){
            //do shit
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SaveChanges();
            }
        });

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }


    /***
     *Reference: https://developer.android.com/guide/topics/ui/controls/radiobutton.html
     * The RadioButtons are in the same RadioGroup which make them mutually exclusive.
     * Depending on which RadioButton is ticked, a boolean isLaptop is either set to true or false.
     */
    public void onRadioButtonClicked(View v)
    {
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()) {
            case R.id.radioButtonYes:
                if (checked){
                    isLaptop = true;
                }
                break;
            case R.id.radioButtonNo:
                if (checked){
                    isLaptop = false;
                }
                break;
        }
    }

    /***
     * Simply sends the data back with intents when the user presses OK. It is sent to Detail Activity
     * where then it is immediately sent back to Main Activity
     */
    public void SaveChanges()
    {
        // Checking if EditTexts are empty or not
        // Ref: https://stackoverflow.com/questions/20349522/android-check-if-edittext-is-empty-when-inputtype-is-set-on-number-phone
        String Name = name.getText().toString().trim();
        String Memory = memory.getText().toString().trim();
        if(Name.isEmpty() || Memory.isEmpty())
        {
            Toast.makeText(EditActivity.this, getResources().getText(R.string.empty_textfield_warning), Toast.LENGTH_SHORT).show();
        }
        else {
            Intent data = new Intent();
            data.putExtra("laptop", isLaptop);
            data.putExtra("name", name.getText().toString());
            data.putExtra("memory", Integer.parseInt(memory.getText().toString()));
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
