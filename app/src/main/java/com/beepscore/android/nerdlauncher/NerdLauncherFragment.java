package com.beepscore.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

        ArrayAdapter<ResolveInfo> adapter = getResolveInfoArrayAdapter(activities);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo)listView.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if (activityInfo == null) {
            return;
        }

        // explicit intent
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        startActivity(intent);
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

    private ArrayAdapter<ResolveInfo> getResolveInfoArrayAdapter(final List<ResolveInfo> activities) {

        ArrayAdapter<ResolveInfo> arrayAdapter = new ArrayAdapter<ResolveInfo>(
                getActivity(), android.R.layout.simple_list_item_1, activities) {

            public View getView(int position, View convertView, ViewGroup parent) {
                PackageManager packageManager = getActivity().getPackageManager();
                View view = super.getView(position, convertView, parent);
                // view is a View and also is a simple_list_item_1, a TextView.
                // Cast to TextView in order to set text
                TextView textView = (TextView)view;
                ResolveInfo resolveInfo = getItem(position);
                textView.setText(resolveInfo.loadLabel(packageManager));
                return view;
            }
        };
        return arrayAdapter;
    }

}
