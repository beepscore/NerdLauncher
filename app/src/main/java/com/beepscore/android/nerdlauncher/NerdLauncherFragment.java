package com.beepscore.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by stevebaker on 11/25/14.
 */
public class NerdLauncherFragment extends ListFragment {
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

        sortActivities(activities);

        ArrayAdapter<ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
                getActivity(), android.R.layout.simple_list_item_1, activities) {

            public View getView(int position, View convertView, ViewGroup parent) {
                PackageManager packageManager = getActivity().getPackageManager();
                View view = super.getView(position, convertView, parent);
                // view will be a simple_list_item_1, a TextView.
                // Cast in order to set its text
                TextView textView = (TextView)view;
                ResolveInfo resolveInfo = getItem(position);
                textView.setText(resolveInfo.loadLabel(packageManager));
                return view;
            }
        };

        setListAdapter(adapter);
    }

    private void sortActivities(List<ResolveInfo> activities) {
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            public int compare(ResolveInfo resolveInfoA, ResolveInfo resolveInfoB) {
                PackageManager packageManager = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        resolveInfoA.loadLabel(packageManager).toString(),
                        resolveInfoB.loadLabel(packageManager).toString());
            }
        });
    }

}
