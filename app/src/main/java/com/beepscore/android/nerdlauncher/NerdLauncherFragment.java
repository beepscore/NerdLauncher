package com.beepscore.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
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

        NerdLauncherAdapter adapter = new NerdLauncherAdapter(activities);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Fragments in inflate their view in onCreateView, not in onCreate
        return inflater.inflate(R.layout.list_item_activity, parent, false);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private class NerdLauncherAdapter extends ArrayAdapter<ResolveInfo> {

        public NerdLauncherAdapter(List<ResolveInfo> activities) {
            super(getActivity(), 0, activities);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_activity, null);
            }

            ImageView imageView = (ImageView)convertView.findViewById(android.R.id.icon);
            TextView textView = (TextView)convertView.findViewById(R.id.list_item_app_text_view);

            ResolveInfo resolveInfo = getItem(position);
            PackageManager packageManager = getActivity().getPackageManager();

            imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
            textView.setText(resolveInfo.loadLabel(packageManager));
            return convertView;
        }
    }

}
