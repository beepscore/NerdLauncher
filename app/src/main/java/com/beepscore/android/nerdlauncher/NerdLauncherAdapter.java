package com.beepscore.android.nerdlauncher;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by stevebaker on 11/26/14.
 */
public class NerdLauncherAdapter extends ArrayAdapter<ResolveInfo> {
    private Activity mActivity;

    public NerdLauncherAdapter(Activity activity, List<ResolveInfo> activities) {
        super(activity, 0, activities);
        mActivity = activity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_activity, null);
        }

        ImageView imageView = (ImageView)convertView.findViewById(android.R.id.icon);
        TextView textView = (TextView)convertView.findViewById(R.id.list_item_app_text_view);

        ResolveInfo resolveInfo = getItem(position);
        PackageManager packageManager = mActivity.getPackageManager();

        imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
        textView.setText(resolveInfo.loadLabel(packageManager));
        return convertView;
    }

}

