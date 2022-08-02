package com.lubuteam.sellsourcecode.supercleaner.screen.junkfile;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.CleanAdapter;
import com.lubuteam.sellsourcecode.supercleaner.model.ChildItem;
import com.lubuteam.sellsourcecode.supercleaner.model.GroupItem;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.widget.AnimatedExpandableListView;
import com.lubuteam.sellsourcecode.supercleaner.widget.CleanJunkFileView;
import com.lubuteam.sellsourcecode.supercleaner.widget.RotateLoading;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JunkFileActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    AnimatedExpandableListView mRecyclerView;
    @BindView(R.id.tvTotalCache)
    TextView mTvTotalCache;
    @BindView(R.id.tvType)
    TextView mTvType;
    @BindView(R.id.tvNoJunk)
    TextView mTvNoJunk;
    @BindView(R.id.btnCleanUp)
    Button mBtnCleanUp;
    @BindView(R.id.viewLoading)
    View mViewLoading;
    @BindView(R.id.rotateloadingApks)
    RotateLoading mRotateloadingApks;
    @BindView(R.id.rotateloadingCache)
    RotateLoading mRotateloadingCache;
    @BindView(R.id.rotateloadingDownload)
    RotateLoading mRotateloadingDownloadFiles;
    @BindView(R.id.rotateloadingLargeFiles)
    RotateLoading mRotateloadingLargeFiles;
    @BindView(R.id.tv_pkg_name)
    TextView tvPkgName;
    @BindView(R.id.av_progress)
    LottieAnimationView avProgress;
    @BindView(R.id.tv_calculating)
    TextView tvCalculating;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.layout_padding)
    View llToolbar;
    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.cleanJunkFileView)
    CleanJunkFileView mCleanJunkFileView;

    private long mTotalJunk;
    private ArrayList<File> mFileListLarge = new ArrayList<>();
    private ArrayList<GroupItem> mGroupItems = new ArrayList<>();
    private CleanAdapter mAdapter;
    private Boolean flagExit = false;

    public static void startActivityWithData(Context mContext) {
        Intent mIntent = new Intent(mContext, JunkFileActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junkfile);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onBackPressed() {
        if (flagExit) {
            super.onBackPressed();
        }
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(Config.FUNCTION.JUNK_FILES.title));
        llToolbar.setVisibility(View.INVISIBLE);
        YoYo.with(Techniques.Flash).duration(2000).repeat(1000).playOn(tvCalculating);
    }

    private void initData() {
        mAdapter = new CleanAdapter(this, mGroupItems, new CleanAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(int groupPosition) {
                if (mRecyclerView.isGroupExpanded(groupPosition)) {
                    mRecyclerView.collapseGroupWithAnimation(groupPosition);
                } else {
                    mRecyclerView.expandGroupWithAnimation(groupPosition);
                }
            }

            @Override
            public void onSelectItemHeader(int position, boolean isCheck) {
                changeCleanFileHeader(position, isCheck);
            }

            @Override
            public void onSelectItem(int groupPosition, int childPosition, boolean isCheck) {
                changeCleanFileItem(groupPosition, childPosition, isCheck);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        if (mGroupItems.size() == 0) {
            Toolbox.setTextFromSize(0, mTvTotalCache, mTvType);
            mViewLoading.setVisibility(View.VISIBLE);
            startImageLoading();
            getFilesFromDirApkOld();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void changeCleanFileHeader(int position, boolean isCheck) {
        long total = mGroupItems.get(position).getTotal();
        if (isCheck) {
            mTotalJunk += total;
        } else {
            mTotalJunk -= total;
        }
        mGroupItems.get(position).setIsCheck(isCheck);
        for (ChildItem childItem : mGroupItems.get(position).getItems()) {
            childItem.setIsCheck(isCheck);
        }
        mAdapter.notifyDataSetChanged();
        updateSizeTotal();
    }

    private void changeCleanFileItem(int groupPosition, int childPosition, boolean isCheck) {
        long total = mGroupItems.get(groupPosition).getItems().get(childPosition).getCacheSize();
        if (isCheck) {
            mTotalJunk += total;
        } else {
            mTotalJunk -= total;
        }
        mGroupItems.get(groupPosition).getItems().get(childPosition).setIsCheck(isCheck);
        boolean isCheckItem = false;
        for (ChildItem childItem : mGroupItems.get(groupPosition).getItems()) {
            isCheckItem = childItem.isCheck();
            if (!isCheckItem) {
                break;
            }
        }
        if (isCheckItem) {
            mGroupItems.get(groupPosition).setIsCheck(true);
        } else {
            mGroupItems.get(groupPosition).setIsCheck(false);
        }
        mAdapter.notifyDataSetChanged();
        updateSizeTotal();
    }

    private void updateSizeTotal() {
        mTotalJunk = 0;
        for (GroupItem mGroupItem : mGroupItems) {
            for (ChildItem mChildItem : mGroupItem.getItems()) {
                if (mChildItem.isCheck())
                    mTotalJunk += mChildItem.getCacheSize();
            }
        }
        Toolbox.setTextFromSize(mTotalJunk, mTvTotalCache, mTvType);
    }

    private void startImageLoading() {
        mRotateloadingApks.start();
        mRotateloadingCache.start();
        mRotateloadingDownloadFiles.start();
        mRotateloadingLargeFiles.start();
    }

    public void getFilesFromDirApkOld() {
        new ScanApkFiles(result -> {
            if (result != null && result.size() > 0) {
                GroupItem groupItem = new GroupItem();
                groupItem.setTitle(getString(R.string.obsolete_apk));
                groupItem.setIsCheck(true);
                groupItem.setType(GroupItem.TYPE_FILE);
                List<ChildItem> childItems = new ArrayList<>();
                long size = 0;
                for (File currentFile : result) {
                    if (currentFile.getName().endsWith(".apk")) {
                        ChildItem childItem = new ChildItem(currentFile.getName(),
                                currentFile.getName(), ContextCompat.getDrawable(JunkFileActivity.this,
                                R.drawable.ic_android),
                                currentFile.length(), ChildItem.TYPE_APKS,
                                currentFile.getPath(), true);
                        childItems.add(childItem);
                        size += currentFile.length();
                    }
                }
                groupItem.setTotal(size);
                groupItem.setItems(childItems);
                mGroupItems.add(groupItem);
            }
            mRotateloadingApks.stop();
            getCacheFile();
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getCacheFile() {
        new TaskScan((totalSize, result) -> {
            Toolbox.setTextFromSize(totalSize, mTvTotalCache, mTvType);
            if (result.size() != 0) {
                GroupItem groupItem = new GroupItem();
                groupItem.setTitle(getString(R.string.system_cache));
                groupItem.setTotal(totalSize);
                groupItem.setIsCheck(true);
                groupItem.setType(GroupItem.TYPE_CACHE);
                groupItem.setItems(result);
                mGroupItems.add(groupItem);
            }
            mRotateloadingCache.stop();
            getFilesFromDirFileDownload();
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getFilesFromDirFileDownload() {
        new ScanDownLoadFiles(result -> {

            if (result != null && result.length > 0) {
                GroupItem groupItem = new GroupItem();
                groupItem.setTitle(getString(R.string.downloader_files));
                groupItem.setIsCheck(false);
                groupItem.setType(GroupItem.TYPE_FILE);
                List<ChildItem> childItems = new ArrayList<>();
                long size = 0;
                for (File currentFile : result) {
                    size += currentFile.length();
                    ChildItem childItem = new ChildItem(currentFile.getName(),
                            currentFile.getName(), ContextCompat.getDrawable(JunkFileActivity.this,
                            R.drawable.ic_android),
                            currentFile.length(), ChildItem.TYPE_DOWNLOAD_FILE,
                            currentFile.getPath(), false);
                    childItems.add(childItem);
                }
                groupItem.setTotal(size);
                groupItem.setItems(childItems);
                mGroupItems.add(groupItem);
            }
            mRotateloadingDownloadFiles.stop();
            getLargeFile();
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getLargeFile() {
        new ScanLargeFiles(result -> {
            if (result.size() != 0) {
                GroupItem groupItem = new GroupItem();
                groupItem.setTitle(getString(R.string.large_files));
                groupItem.setIsCheck(false);
                groupItem.setType(GroupItem.TYPE_FILE);
                List<ChildItem> childItems = new ArrayList<>();
                long size = 0;
                for (File currentFile : result) {
                    ChildItem childItem = new ChildItem(currentFile.getName(),
                            currentFile.getName(), ContextCompat.getDrawable(JunkFileActivity.this,
                            R.drawable.ic_android),
                            currentFile.length(), ChildItem.TYPE_LARGE_FILES,
                            currentFile.getPath(), false);
                    childItems.add(childItem);
                    size += currentFile.length();
                }
                groupItem.setItems(childItems);
                groupItem.setTotal(size);
                mGroupItems.add(groupItem);
            }
            mRotateloadingLargeFiles.stop();
            updateAdapter();
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateAdapter() {
        updateSizeTotal();
        if (mGroupItems.size() != 0) {
            for (int i = 0; i < mGroupItems.size(); i++) {
                if (mRecyclerView.isGroupExpanded(i)) {
                    mRecyclerView.collapseGroupWithAnimation(i);
                } else {
                    mRecyclerView.expandGroupWithAnimation(i);
                }
            }
            YoYo.with(Techniques.FadeInUp).duration(1000).playOn(mRecyclerView);
            mTvNoJunk.setVisibility(View.GONE);
            mBtnCleanUp.setVisibility(View.VISIBLE);
            llToolbar.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mBtnCleanUp.setVisibility(View.GONE);
            mTvNoJunk.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.no_junk), Toast.LENGTH_LONG).show();
            openScreenResult(Config.FUNCTION.JUNK_FILES);
            finish();
        }
        tvCalculating.setVisibility(View.GONE);
        mViewLoading.setVisibility(View.GONE);
        avProgress.pauseAnimation();
        avProgress.setVisibility(View.INVISIBLE);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mTvTotalCache);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mTvType);
        YoYo.with(Techniques.FadeOut).duration(1000).playOn(tvPkgName);
        flagExit = true;
    }

    public interface OnScanLargeFilesListener {
        void onScanCompleted(List<File> result);
    }

    private class ScanApkFiles extends AsyncTask<Void, String, List<File>> {

        private OnScanApkFilesListener mOnScanLargeFilesListener;

        public ScanApkFiles(OnScanApkFilesListener onActionListener) {
            mOnScanLargeFilesListener = onActionListener;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (!TextUtils.isEmpty((values[0])))
                tvPkgName.setText(values[0]);
        }

        @Override
        protected List<File> doInBackground(Void... params) {
            List<File> filesResult = new ArrayList<>();
            File downloadDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            File[] files = downloadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    publishProgress(file.getName());
                    if (file.getName().endsWith(".apk")) {
                        filesResult.add(file);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return filesResult;
        }

        @Override
        protected void onPostExecute(List<File> result) {
            if (mOnScanLargeFilesListener != null) {
                mOnScanLargeFilesListener.onScanCompleted(result);
            }
        }
    }

    public interface OnScanApkFilesListener {
        void onScanCompleted(List<File> result);
    }

    private class ScanDownLoadFiles extends AsyncTask<Void, String, File[]> {

        private OnScanDownloadFilesListener mOnScanLargeFilesListener;

        public ScanDownLoadFiles(OnScanDownloadFilesListener onActionListener) {
            mOnScanLargeFilesListener = onActionListener;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (!TextUtils.isEmpty(values[0]))
                tvPkgName.setText(values[0]);
        }

        @Override
        protected File[] doInBackground(Void... params) {
            File downloadDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            File[] lst = downloadDir.listFiles();
            if(lst!=null && lst.length!=0){
                for (File mFile : lst) {
                    publishProgress(mFile.getPath());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return lst;
        }

        @Override
        protected void onPostExecute(File[] result) {
            if (mOnScanLargeFilesListener != null) {
                mOnScanLargeFilesListener.onScanCompleted(result);
            }
        }
    }

    public interface OnScanDownloadFilesListener {
        void onScanCompleted(File[] result);
    }

    private class TaskScan extends AsyncTask<Void, String, List<ChildItem>> {

        private OnActionListener mOnActionListener;
        private long mTotalSize;

        public TaskScan(OnActionListener onActionListener) {
            mOnActionListener = onActionListener;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (!TextUtils.isEmpty(values[0]))
                tvPkgName.setText(values[0]);
        }

        @Override
        protected List<ChildItem> doInBackground(Void... params) {
//            List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
            Method mGetPackageSizeInfoMethod = null;
            try {
                mGetPackageSizeInfoMethod = getPackageManager().getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
            List<ChildItem> apps = new ArrayList<>();
            for (ResolveInfo pkg : pkgAppsList) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    StorageStatsManager storageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
                    try {
                        ApplicationInfo mApplicationInfo = getPackageManager().getApplicationInfo(pkg.activityInfo.packageName, 0);
                        StorageStats storageStats = storageStatsManager.queryStatsForUid(mApplicationInfo.storageUuid, mApplicationInfo.uid);
                        long cacheSize = storageStats.getCacheBytes();
                        addPackage(apps, cacheSize, pkg.activityInfo.packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mGetPackageSizeInfoMethod.invoke(getPackageManager(), pkg.activityInfo.packageName,
                                new IPackageStatsObserver.Stub() {
                                    @Override
                                    public void onGetStatsCompleted(PackageStats pStats,
                                                                    boolean succeeded) {
                                        long cacheSize = pStats.cacheSize;
                                        addPackage(apps, cacheSize, pkg.activityInfo.packageName);
                                    }
                                }
                        );
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                publishProgress(pkg.activityInfo.packageName);
            }

            return apps;
        }

        @Override
        protected void onPostExecute(List<ChildItem> result) {
            Collections.sort(result, (o1, o2) -> Long.compare(o2.getCacheSize(), o1.getCacheSize()));
            if (mOnActionListener != null) {
                mOnActionListener.onScanCompleted(mTotalSize, result);
            }
        }

        private void addPackage(List<ChildItem> apps, long cacheSize, String pgkName) {
            try {
                PackageManager packageManager = getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(pgkName, PackageManager.GET_META_DATA);
                if (cacheSize > 1024 * 100 && PreferenceUtils.isCleanCache(pgkName)) {
                    mTotalSize += cacheSize;
                    apps.add(new ChildItem(pgkName,
                            packageManager.getApplicationLabel(info).toString(),
                            info.loadIcon(packageManager),
                            cacheSize, ChildItem.TYPE_CACHE, null, true));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnActionListener {
        void onScanCompleted(long totalSize, List<ChildItem> result);
    }

    private class ScanLargeFiles extends AsyncTask<Void, String, List<File>> {

        private OnScanLargeFilesListener mOnScanLargeFilesListener;

        public ScanLargeFiles(OnScanLargeFilesListener onActionListener) {
            mOnScanLargeFilesListener = onActionListener;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (!TextUtils.isEmpty(values[0]))
                tvPkgName.setText(values[0]);
        }

        @Override
        protected List<File> doInBackground(Void... params) {
            File root = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
            return getfile(root, true);
        }

        @Override
        protected void onPostExecute(List<File> result) {
            if (mOnScanLargeFilesListener != null) {
                mOnScanLargeFilesListener.onScanCompleted(result);
            }
        }

        public ArrayList<File> getfile(File dir, boolean isSleep) {
            File[] listFile = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (File aListFile : listFile) {
                    publishProgress(aListFile.getPath());
                    if (aListFile.isDirectory() && !aListFile.getName().equals(Environment.DIRECTORY_DOWNLOADS)) {
                        getfile(aListFile, false);
                    } else {
                        long fileSizeInBytes = aListFile.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if (fileSizeInMB >= 10 && !aListFile.getName().endsWith(".apk")) {
                            mTotalJunk += aListFile.length();
                            mFileListLarge.add(aListFile);
                        }
                    }
                    if (isSleep)
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
            return mFileListLarge;
        }
    }

    public void cleanUp(List<ChildItem> listItem) {
        mCleanJunkFileView.startAnimationClean(listItem, 100, () -> {
            for (int i = 0; i < listItem.size(); i++) {
                if (i == listItem.size() - 1) {
                    openScreenResult(Config.FUNCTION.JUNK_FILES);
                    finish();
                    return;
                }
                ChildItem mChildItem = listItem.get(i);
                if (mChildItem.getType() == ChildItem.TYPE_CACHE) {
                    PackageManager mPackageManager = getPackageManager();
                    try {
                        Method mGetfreeStorage = getPackageManager().getClass().getMethod(
                                "freeStorage", String.class, IPackageStatsObserver.class);
                        long desiredFreeStorage = 8 * 1024 * 1024 * 1024; // Request for 8GB of free space
                        mGetfreeStorage.invoke(mPackageManager, mChildItem.getPackageName(), desiredFreeStorage, null);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    PreferenceUtils.setTimePkgCleanCache(mChildItem.getPackageName());
                } else {
                    File file = new File(mChildItem.getPath());
                    file.delete();
                    if (file.exists()) {
                        try {
                            file.getCanonicalFile().delete();
                            if (file.exists()) {
                                deleteFile(file.getName());
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.btnCleanUp)
    void clickClean() {
        List<ChildItem> lstChildItems = new ArrayList<>();
        for (GroupItem mGroupItem : mGroupItems) {
            for (ChildItem mChildItem : mGroupItem.getItems()) {
                if (mChildItem.isCheck())
                    lstChildItems.add(mChildItem);
            }
        }
        if (!lstChildItems.isEmpty())
            cleanUp(lstChildItems);
        else
            Toast.makeText(JunkFileActivity.this, getString(R.string.selcet_file_to_clean), Toast.LENGTH_LONG).show();
    }

}
