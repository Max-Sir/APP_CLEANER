package com.lubuteam.sellsourcecode.supercleaner.screen.appManager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.AppManagerAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.ManagerConnect;
import com.lubuteam.sellsourcecode.supercleaner.model.GroupItemAppManager;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.lubuteam.sellsourcecode.supercleaner.widget.AnimatedExpandableListView;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppManagerActivity extends BaseActivity {

    private static final int UNINSTALL_REQUEST_CODE = 1;

    @BindView(R.id.recyclerView)
    AnimatedExpandableListView mRecyclerView;
    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.google_progress)
    GoogleProgressBar mGoogleProgressBar;

    private Handler mHandlerLocal = new Handler(Looper.getMainLooper());
    private List<GroupItemAppManager> mGroupItems = new ArrayList<>();
    private int mGroupPosition;
    private int mChildPosition;
    Runnable runnableLocal;
    private AppManagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_app_manager);
        ButterKnife.bind(this);
        initView();
        initData();
        loadData();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(R.string.app_uninstall));
        /**/
        Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(Toolbox.getProgressDrawableColors(this))
                .build();
        Rect bounds = mGoogleProgressBar.getIndeterminateDrawable().getBounds();
        mGoogleProgressBar.setIndeterminateDrawable(drawable);
        mGoogleProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }

    private void initData() {
        mAdapter = new AppManagerAdapter(this, mGroupItems, new AppManagerAdapter.OnClickItemListener() {
            @Override
            public void onUninstallApp(int groupPosition, int childPosition) {
                mGroupPosition = groupPosition;
                mChildPosition = childPosition;
                ApplicationInfo app = mGroupItems.get(groupPosition).getItems().get(childPosition);
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" + app.packageName));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, UNINSTALL_REQUEST_CODE);
            }

            @Override
            public void onClickItem(int groupPosition, int childPosition) {
                if (mGroupItems.get(groupPosition).getItems().get(childPosition).packageName != null) {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:"
                            + mGroupItems.get(groupPosition).getItems().get(childPosition).packageName));

                    startActivity(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData() {
        mGoogleProgressBar.setVisibility(View.VISIBLE);
        ManagerConnect managerConnect = new ManagerConnect();
        managerConnect.getListManager(this, result -> {
            runnableLocal = () -> {
                if (result.size() != 0) {
                    //set app user
                    GroupItemAppManager groupItemAppManagerUser = new GroupItemAppManager();
                    groupItemAppManagerUser.setTitle(getString(R.string.user_app));
                    groupItemAppManagerUser.setType(GroupItemAppManager.TYPE_USER_APPS);
                    int countUser = 0;
                    List<ApplicationInfo> applicationInfosUser = new ArrayList<>();
                    for (ApplicationInfo applicationInfo : result) {
                        if (!applicationInfo.packageName.equals(getPackageName())
                        ) {
                            countUser++;
                            applicationInfosUser.add(applicationInfo);
                        }
                    }
                    groupItemAppManagerUser.setItems(applicationInfosUser);
                    groupItemAppManagerUser.setTotal(countUser);
                    mGroupItems.add(groupItemAppManagerUser);
                    //set app system
                    GroupItemAppManager groupItemAppManagerSystem = new GroupItemAppManager();
                    groupItemAppManagerSystem.setTitle(getString(R.string.system_app));
                    groupItemAppManagerSystem.setType(GroupItemAppManager.TYPE_SYSTEM_APPS);
                    int countSystem = 0;
                    List<ApplicationInfo> applicationInfosSystem = new ArrayList<>();
                    for (ApplicationInfo applicationInfo : result) {
                        if (!Toolbox.isUserApp(applicationInfo)) {
                            countSystem++;
                            applicationInfosSystem.add(applicationInfo);
                        }
                    }
                    groupItemAppManagerSystem.setItems(applicationInfosSystem);
                    groupItemAppManagerSystem.setTotal(countSystem);
                    mGroupItems.add(groupItemAppManagerSystem);
                    mAdapter.notifyDataSetChanged();
                    if (mRecyclerView.isGroupExpanded(0)) {
                        mRecyclerView.collapseGroupWithAnimation(0);
                    } else {
                        mRecyclerView.expandGroupWithAnimation(0);
                    }
                }
                mGoogleProgressBar.setVisibility(View.GONE);
            };
            mHandlerLocal.postDelayed(runnableLocal, 100);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandlerLocal != null)
            mHandlerLocal.removeCallbacks(runnableLocal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mGroupItems.get(mGroupPosition).getItems().remove(mChildPosition);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
