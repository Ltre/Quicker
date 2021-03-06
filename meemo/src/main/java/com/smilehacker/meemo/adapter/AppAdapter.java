package com.smilehacker.meemo.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilehacker.meemo.R;
import com.smilehacker.meemo.data.model.AppInfo;
import com.smilehacker.meemo.fragment.DialFragment;
import com.smilehacker.meemo.plugin.image.AsyncIconLoader;
import com.smilehacker.meemo.util.PackageHelper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by kleist on 14-4-2.
 */
public class AppAdapter extends BaseAdapter {

    private List<AppInfo> mAppInfos;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private PackageHelper mPackageHelper;
    private PackageManager mPackageManager;
    private AsyncIconLoader mAsyncIconLoader;

    private DialFragment mFragment;

    public AppAdapter(Context context, DialFragment dialFragment, List<AppInfo> appInfos) {
        mAppInfos = appInfos;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPackageManager = context.getPackageManager();
        mPackageHelper = new PackageHelper(context);
        mFragment = dialFragment;
        mAsyncIconLoader = new AsyncIconLoader(context);
    }

    public void refreshApps(List<AppInfo> appInfos) {
        mAppInfos.clear();
        mAppInfos.addAll(appInfos);
        notifyDataSetChanged();
    }

    public void updateApps(List<AppInfo> appInfos) {
        for (Iterator<AppInfo> iterator = mAppInfos.iterator(); iterator.hasNext();) {
            AppInfo appInfo = iterator.next();
            if (!appInfos.contains(appInfo)) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAppInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return mAppInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_app, viewGroup, false);
            holder = new ViewHolder();
            holder.tvAppName = (TextView) view.findViewById(R.id.tv_name);
            holder.ivAppIcon = (ImageView) view.findViewById(R.id.iv_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppInfo appInfo = mAppInfos.get(i);

        holder.tvAppName.setText(appInfo.appName);
        mAsyncIconLoader.loadBitmap(appInfo.packageName, holder.ivAppIcon);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mFragment.openAppAndHideDialer(appInfo);
            }
        });


        return view;
    }

    private static class ViewHolder {
        public TextView tvAppName;
        public ImageView ivAppIcon;
    }
}
