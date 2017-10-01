package com.example.omar.mycomputer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int GET_EDIT_DATA = 102;
    /***
     * NOTE TO REVIEWER: For some reason, the app wont install on the virtual android device because of an invalid apk (slice_3.apk) when "Instant Run"
     * is enabled. To disable, go to File->Settings->Build, Execution, Deployment->Instant Run->Untick "Enable Instant Run(..)"
     * Once disabled, the app will install and run with no problem.
     */

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    ImageView imgButton;
    TextView computerName;
    Button detailsButton;
    Bundle imageBundle;
    Bundle computerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Elements
        imgButton = (ImageView) findViewById(R.id.imgComputer);
        detailsButton = (Button) findViewById(R.id.btnDetails);
        computerName = (TextView) findViewById(R.id.txtComputerName);

        if (savedInstanceState != null) {
            //Loading bundles that hold data
            computerInfo = savedInstanceState.getBundle("computerInfo");
            imageBundle = savedInstanceState.getBundle("data");
        }

        //Checking if bundle is null so we dont get any crashes
        if (imageBundle != null) {
            SetPicture();
        }

        //Checking if bundle is null so we dont get any crashes
        if (computerInfo != null) {
            SetName();
        }

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePicture();
            }
        });

        detailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                OpenDetailActivity();
            }
        });
    }


    /***
     * This function is intended to abstract away the intent call from the onClickListener for the ImageView.
     * Its a simple function that checks if there is any camera app available for use.
     * If a camera is found, an AlertDialog asks the user if they wish to take a new photo.
     * AlertDialog reference: https://developer.android.com/guide/topics/ui/dialogs.html
     */
    public void TakePicture()
    {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(this, getResources().getText(R.string.noCameraToast), Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)

                .setTitle(getResources().getText(R.string.alertTitle))
                .setMessage(getResources().getText(R.string.alertText))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePicture.resolveActivity(getPackageManager()) != null)
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                    }})

                .setNegativeButton(android.R.string.no, null).show();
    }


    /***
     * This function is intended to abstract away the action of setting a new photo in the ImageView.
     * It is used in onActivityResult() and onCreate()
     */
    public void SetPicture()
    {
        Bitmap imageBitmap = (Bitmap) imageBundle.get("data");
        imgButton.setImageBitmap(imageBitmap);
    }

    //Set name of laptop if relevant
    private void SetName() {
        String name = computerInfo.getString("name");
        computerName.setText(name);
    }


    /***
     * This function simply starts a new activity the first time around with the end goal of the user
     * inputting his or hers computer information.
     * If however the user has already set his computer info in the edit activity, then the null check is passed
     * and the data is transferred to the detail activity where it is made visible for the user.
     */
    public void OpenDetailActivity()
    {
        Intent i = new Intent(getApplicationContext(), DetailActivity.class);

        if(computerInfo != null){
            String name = (String)computerInfo.get("name");
            Boolean laptop = (Boolean)computerInfo.get("laptop");
            int memory = (int)computerInfo.get("memory");
            i.putExtra("name", name);
            i.putExtra("laptop", laptop);
            i.putExtra("memory", memory);
        }

        startActivityForResult(i, GET_EDIT_DATA);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Saving bundle that holds the image and bundle that holds the computer info.
        outState.putBundle("data", imageBundle);
        outState.putBundle("computerInfo", computerInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBundle = extras;
            //Picture taken, set it in the ImageView
            SetPicture();
        }

        if(requestCode == GET_EDIT_DATA && resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            computerInfo = bundle;
            SetName();
        }
    }
}
