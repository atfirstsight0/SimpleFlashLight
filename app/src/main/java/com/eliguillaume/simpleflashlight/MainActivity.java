package com.eliguillaume.simpleflashlight;

/*
     Purpose of this application is to demonstrate overriding some Activity life-cycle methods

 */


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First check if device is supporting flashlight or not
        hasFlash = getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {//need to close this open bracket
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //closing the application
                    finish();
                }
            });
            alert.show();
        }
        getCamera(); //need to create this method.

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new

                                                       CompoundButton.OnCheckedChangeListener() {
                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                               if (isChecked) {
                                                                   turnOnFlash();
                                                               } else {
                                                                   turnOffFlash();
                                                               }

                                                           }
                                                       });
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                param = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error: ", e.getMessage());
            }
        }
    }


    //Turning On flash
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || param == null) {
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashOn = true;

            Log.v("SimpleFlashLight", "Flash has been turned on...");
        }
    }

    // Turning Off flash
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || param == null) {
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn = false;
            Log.v("AndroidATC", "Flash has been turned off...");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }


}
