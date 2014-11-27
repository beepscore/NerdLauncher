package com.beepscore.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

/**
 * Created by stevebaker on 11/25/14.
 */
public class NerdLauncherFragment extends Fragment {
    public static final String TAG = "NerdLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Implicit intent.
        // On the device every app's AndroidManifest.xml that contains an activity with
        // <action android:name="android.intent.action.MAIN" />
        // <category android:name="android.intent.category.LAUNCHER" />
        // will respond to this intent.
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(startupIntent, 0);

        Log.d(TAG, "I've found " + Integer.toString(activities.size()) + " activities");
    }
}
