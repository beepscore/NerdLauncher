package com.beepscore.android.nerdlauncher;

import android.support.v4.app.Fragment;


public class NerdLauncherActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new NerdLauncherFragment();
    }
}
