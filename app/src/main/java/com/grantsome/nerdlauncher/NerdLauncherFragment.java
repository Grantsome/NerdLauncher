package com.grantsome.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by tom on 2017/4/1.
 */

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private String TAG = "NerdLauncherFragment";

    public static NerdLauncherFragment newInstance(){
        return new NerdLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nerd_launcher,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setAdapter();
        return view;
    }

    private void setAdapter(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,0);
        Collections.sort(resolveInfoList, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(),o2.loadLabel(pm).toString());
            }
        });
        Log.i(TAG,"Found " + resolveInfoList.size() + " activities ");
        mRecyclerView.setAdapter(new ActivityAdapter(resolveInfoList));
    }

    private class ActvivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ResolveInfo mResolveInfo;

        private TextView mTextView;

        private ImageView mImageView;

        public ActvivityHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            mImageView = (ImageView) itemView.findViewById(android.R.id.icon);
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager packageManager = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(packageManager).toString();
            Drawable drawable = mResolveInfo.loadIcon(packageManager);
            mTextView.setText(appName);
            mImageView.setImageDrawable(drawable);
            mTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;
            Log.i(TAG,"The activityInfo.application.packageName " + activityInfo.applicationInfo.packageName);
            Intent intent = new Intent(Intent.ACTION_MAIN).setClassName(activityInfo.applicationInfo.packageName,activityInfo.name);
            Log.i(TAG,"The activityInfo.name " + activityInfo.name);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter{

        private final List<ResolveInfo> mResolveInfoList;

        public ActivityAdapter(List<ResolveInfo> resovlerInfoList){
            mResolveInfoList = resovlerInfoList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.activity_list_item,parent,false);
            ActvivityHolder activityHolder = new ActvivityHolder(view);
            return activityHolder;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ActvivityHolder activityHolder = (ActvivityHolder) holder;
            ResolveInfo resolveInfo = mResolveInfoList.get(position);
            activityHolder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mResolveInfoList.size();
        }
    }

}
