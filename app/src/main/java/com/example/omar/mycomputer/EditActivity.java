package com.example.omar.mycomputer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    Button btnOk;
    Button btnCancelEdit;
    EditText Name;
    EditText Memory;
    Boolean isLaptop;
    Bundle computerInfo;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //UI Elements
        btnOk = (Button)findViewById(R.id.btnOk);
        btnCancelEdit = (Button)findViewById(R.id.btnCancelEdit);
        Name = (EditText) findViewById(R.id.editModel);
        Memory = (EditText) findViewById(R.id.editMemory);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if(savedInstanceState != null){
            computerInfo = savedInstanceState.getBundle("computerInfo");
        }

        //Receiving intent with data and checking if null
        Intent intentData = getIntent();
        computerInfo = intentData.getExtras();
        if (computerInfo != null) {
            SetComputerInfo();
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
                finish();
            }
        });
    }


    /***
     * Reference: https://developer.android.com/guide/topics/ui/controls/radiobutton.html
     * The RadioButtons are in the same RadioGroup which make them mutually exclusive.
     * Depending on which RadioButton is ticked, a boolean isLaptop is either set to true or false.
     * The boolean is then sent with an intent back to main activity
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
     * Updates the UI with the new computer info
     */
    public void SetComputerInfo() {
        String name = computerInfo.getString("name");
        isLaptop = computerInfo.getBoolean("laptop");
        int memory = computerInfo.getInt("memory");

        Name.setText(name);
        Memory.setText(String.valueOf(memory));

        if (isLaptop == true)
            radioGroup.check(R.id.radioButtonYes);
        else
            radioGroup.check(R.id.radioButtonNo);
    }

    /***
     * Simply sends the data back with intents when the user presses OK. It is sent to Detail Activity
     * where then it is immediately sent back to Main Activity
     */
    public void SaveChanges()
    {
        // Checking if EditTexts are empty or not and if a RadioButton is checked in the RadioGroup
        // Ref: https://stackoverflow.com/questions/20349522/android-check-if-edittext-is-empty-when-inputtype-is-set-on-number-phone
        //      https://stackoverflow.com/questions/24992936/how-to-check-if-a-radiobutton-is-checked-in-a-radiogroup-in-android
        String name = Name.getText().toString().trim();
        String memory = Memory.getText().toString().trim();

        if (name.isEmpty() || memory.isEmpty())//If any textfield is empty
        {
            if (radioGroup.getCheckedRadioButtonId() == -1) {  //If no radiobutton is ticked
                Toast.makeText(this, getResources().getText(R.string.empty_text_and_radiogroup), Toast.LENGTH_SHORT).show(); //Alert user that field is empty and radiogroup is not ticked
            }

            else
                Toast.makeText(EditActivity.this, getResources().getText(R.string.empty_textfield_warning), Toast.LENGTH_SHORT).show(); //Alert user that fields are empty
        }

        else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, getResources().getText(R.string.empty_radiogroup), Toast.LENGTH_SHORT).show(); //Alert user that radiogroup is not ticked
        }

        else {
            Intent data = new Intent();
            data.putExtra("laptop", isLaptop);
            data.putExtra("name", Name.getText().toString());
            data.putExtra("memory", Integer.parseInt(Memory.getText().toString()));
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
