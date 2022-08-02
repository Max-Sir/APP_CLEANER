package com.lubuteam.sellsourcecode.supercleaner.screen.listAppSelect;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.AppSelectAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskGetListApp;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppSelectActivity extends BaseActivity {

    private static final String TYPE_DATA = "type data sceen";
    @BindView(R.id.im_back_toolbar)
    ImageView imBack;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.rcv_app)
    RecyclerView rcvApp;
    @BindView(R.id.google_progress)
    GoogleProgressBar mGoogleProgressBar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.id_menu_toolbar)
    ImageView imActionToolbar;

    private List<TaskInfo> lstApp = new ArrayList<>();
    private List<String> lstAppSave = new ArrayList<>();
    private AppSelectAdapter mAppSelectAdapter;

    private TYPE_SCREEN mTypeScreen;

    public enum TYPE_SCREEN implements Serializable {
        IGNORE, GAME_BOOST, WHILE_LIST_VIRUS
    }

    public static void openSelectAppScreen(Context mContext, TYPE_SCREEN mTypeScreen) {
        Intent mIntent = new Intent(mContext, AppSelectActivity.class);
        mIntent.putExtra(TYPE_DATA, mTypeScreen);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_app);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        imBack.setVisibility(View.VISIBLE);
//        imBack.setColorFilter(getResources().getColor(R.color.color_222222), PorterDuff.Mode.SRC_IN);
//        tvToolbar.setTextColor(getResources().getColor(R.color.color_222222));
        imActionToolbar.setVisibility(View.VISIBLE);
        imActionToolbar.setImageResource(R.drawable.ic_check_white_24dp);
//        imActionToolbar.setColorFilter(getResources().getColor(R.color.color_222222), PorterDuff.Mode.SRC_IN);
        /**/
        Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(Toolbox.getProgressDrawableColors(this))
                .build();
        Rect bounds = mGoogleProgressBar.getIndeterminateDrawable().getBounds();
        mGoogleProgressBar.setIndeterminateDrawable(drawable);
        mGoogleProgressBar.getIndeterminateDrawable().setBounds(bounds);
    }

    private void initData() {
        mTypeScreen = (TYPE_SCREEN) getIntent().getSerializableExtra(TYPE_DATA);
        if (mTypeScreen == TYPE_SCREEN.IGNORE) {
            tvToolbar.setText(getString(R.string.app_protect));
            lstAppSave = PreferenceUtils.getListAppIgnore();
            tvContent.setText(getString(R.string.skip_app_title));
        } else if (mTypeScreen == TYPE_SCREEN.GAME_BOOST) {
            tvToolbar.setText(getString(R.string.list_game));
            lstAppSave = PreferenceUtils.getListAppGameBoost();
            tvContent.setText(getString(R.string.select_game_to_boost));
        } else if (mTypeScreen == TYPE_SCREEN.WHILE_LIST_VIRUS) {
            tvToolbar.setText(getString(R.string.app_protect));
            lstAppSave = PreferenceUtils.getListAppWhileVirus();
            tvContent.setText(getString(R.string.skip_app_virus));
        }
        mAppSelectAdapter = new AppSelectAdapter(this, AppSelectAdapter.TYPE_SELECT.CHECK_BOX, lstApp);
        mAppSelectAdapter.setmItemClickListener(position -> {

        });
        rcvApp.setAdapter(mAppSelectAdapter);
        getListAppRunning();
    }

    private void getListAppRunning() {
        new TaskGetListApp(this, lstData -> {
            if (lstApp != null) {
                lstApp.clear();
                lstApp.addAll(lstData);
                if (!lstAppSave.isEmpty())
                    fillterLst();
                mAppSelectAdapter.notifyDataSetChanged();
            }
            rcvApp.setVisibility(View.VISIBLE);
            mGoogleProgressBar.setVisibility(View.GONE);
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fillterLst() {
        for (TaskInfo mTaskInfo : lstApp) {
            for (String pkgNameSave : lstAppSave) {
                if (mTaskInfo.getPackageName().equals(pkgNameSave)) {
                    mTaskInfo.setChceked(true);
                    break;
                }
            }
        }
        Collections.sort(lstApp, (o1, o2) -> Boolean.compare(o2.isChceked(), o1.isChceked()));
    }

    @OnClick({R.id.id_menu_toolbar})
    void clickBack(View mView) {
        switch (mView.getId()) {
            case R.id.id_menu_toolbar:
                List<String> lstSaveNew = new ArrayList<>();
                for (TaskInfo mTaskInfo : lstApp) {
                    if (mTaskInfo.isChceked())
                        lstSaveNew.add(mTaskInfo.getPackageName());
                }
                if (mTypeScreen == TYPE_SCREEN.IGNORE) {
                    PreferenceUtils.setListAppIgnore(lstSaveNew);
                } else if (mTypeScreen == TYPE_SCREEN.GAME_BOOST) {
                    PreferenceUtils.setListAppGameBoost(lstSaveNew);
                } else if (mTypeScreen == TYPE_SCREEN.WHILE_LIST_VIRUS) {
                    PreferenceUtils.setListAppWhileVirus(lstSaveNew);
                }
                finish();
                break;
        }
    }
}
