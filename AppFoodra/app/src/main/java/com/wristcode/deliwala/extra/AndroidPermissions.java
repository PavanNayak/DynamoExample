package com.wristcode.deliwala.extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class AndroidPermissions
{

    /**
     * Id to identify a locations permission request.
     */
    public static final int REQUEST_LOCATION = 0;

    /**
     * Permissions required to get locations
     */
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static AndroidPermissions instance;

    public static AndroidPermissions getInstance() {
        if (instance == null) {
            instance = new AndroidPermissions();
        }
        return instance;
    }

    public boolean checkLocationPermission(Context thisActivity) {
        if (ActivityCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(thisActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }


    public void displayLocationPermissionAlert(Activity thisActivity) {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(thisActivity, PERMISSIONS_LOCATION, REQUEST_LOCATION);
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults)
    {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void displayAlert(final Activity context, final int position)
    {
        Toast.makeText(context, "Turn on the GPS in your device!!!", Toast.LENGTH_SHORT).show();
    }
}