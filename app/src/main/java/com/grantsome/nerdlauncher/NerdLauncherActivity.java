package com.grantsome.nerdlauncher;

import android.support.v4.app.Fragment;

public class NerdLauncherActivity extends SuperFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }

}
