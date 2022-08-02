package com.lubuteam.sellsourcecode.supercleaner.screen.antivirus;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.data.TaskScanAntivirus;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseActivity;
import com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.fragment.ListAppDangerousFragment;
import com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.fragment.ListAppVirusFragment;
import com.lubuteam.sellsourcecode.supercleaner.service.NotificationUtil;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.widget.AntivirusScanView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AntivirusActivity extends BaseActivity {

    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.antivirusScanView)
    AntivirusScanView mAntivirusScanView;
    @BindView(R.id.tv_number_virus)
    TextView tvVirusNumber;
    @BindView(R.id.tv_number_dangerous)
    TextView tvDangerousNumber;
//    @BindView(R.id.ll_background)
//    View llBackground;
    @BindView(R.id.tv_total_issues)
    TextView tvTotalIssues;
    @BindView(R.id.ll_virus)
    View llVirus;
    @BindView(R.id.ll_dangerous)
    View llDangerous;
    @BindView(R.id.id_menu_toolbar)
    ImageView imMenuToolbar;

    private boolean isCanback = true;
    private List<TaskInfo> lstAppDangerous = new ArrayList<>();
    private List<TaskInfo> lstAppVirus = new ArrayList<>();
    private int startResolve = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        ButterKnife.bind(this);
        initView();
        initData();
        
    }

    public List<TaskInfo> getLstAppVirus() {
        return lstAppVirus;
    }

    public List<TaskInfo> getLstAppDangerous() {
        return lstAppDangerous;
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setText(getString(R.string.security));
    }

    private void initData() {
        mAntivirusScanView.setVisibility(View.VISIBLE);
        mAntivirusScanView.startAnimationScan();
        isCanback = false;
        new TaskScanAntivirus(this, 200, new TaskScanAntivirus.OnTaskListListener() {
            @Override
            public void OnResult(List<TaskInfo> lstDangerous, List<TaskInfo> lstVirus) {
                YoYo.with(Techniques.FadeOut).duration(1000).playOn(mAntivirusScanView);
                lstAppDangerous.clear();
                lstAppDangerous.addAll(lstDangerous);
                lstAppVirus.clear();
                lstAppVirus.addAll(lstVirus);
//                lstAppVirus.addAll(lstDangerous); // for tests
                updateData();
                tvToolbar.setVisibility(View.INVISIBLE);
                isCanback = true;
                mAntivirusScanView.stopAnimationScan();
                imMenuToolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onProgress(String appName, String virusName, String dangerousSize, String progress) {
                mAntivirusScanView.setContent(appName);
                mAntivirusScanView.setProgress(Integer.parseInt(progress));
//                if (virusName != null && !virusName.isEmpty()) {
//                    mAntivirusScanView.showBgVirus();
//                } else if (dangerousSize != null && !dangerousSize.isEmpty() && Integer.parseInt(dangerousSize) != 0) {
//                    mAntivirusScanView.showBgDangerous();
//                }
            }

        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick({R.id.tv_check_virus, R.id.tv_check_dangerous, R.id.tv_skip_dangerous, R.id.tv_resolve_all, R.id.id_menu_toolbar})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.id_menu_toolbar:
                PopupMenu popupMenu = new PopupMenu(this, imMenuToolbar);
                popupMenu.getMenuInflater().inflate(R.menu.scan_virus_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    openWhileListVirusSceen();
                    return true;
                });
                popupMenu.show();
                break;
            case R.id.tv_check_virus:
                addFragmentWithTag(ListAppVirusFragment.getInstance(), ListAppDangerousFragment.class.getName());
                break;
            case R.id.tv_check_dangerous:
                addFragmentWithTag(ListAppDangerousFragment.getInstance(() -> {
                    lstAppDangerous.clear();
                    updateData();
                }), ListAppDangerousFragment.class.getName());
                break;
            case R.id.tv_skip_dangerous:
                lstAppDangerous.clear();
                updateData();
                break;
            case R.id.tv_resolve_all:
                if (!lstAppVirus.isEmpty()) {
                    startResolve = lstAppVirus.size() - 1;
                    uninstall(startResolve);
                } else {
                    openScreenResult(Config.FUNCTION.ANTIVIRUS);
                    finish();
                }
                break;
        }
    }

    public void updateData() {
        llVirus.setVisibility(lstAppVirus.isEmpty() ? View.GONE : View.VISIBLE);
        llDangerous.setVisibility(lstAppDangerous.isEmpty() ? View.GONE : View.VISIBLE);
        tvVirusNumber.setText(String.valueOf(lstAppVirus.size()));
        tvDangerousNumber.setText(String.valueOf(lstAppDangerous.size()));
        int total = lstAppVirus.size() + lstAppDangerous.size();
//        if (lstAppVirus.isEmpty()) {
//            llBackground.setBackgroundColor(getResources().getColor(R.color.color_ffa800));
//        } else {
//            llBackground.setBackgroundColor(getResources().getColor(R.color.color_f66051));
//        }
        tvTotalIssues.setText(getString(R.string.issues_found, String.valueOf(total)));
        if (total == 0) {
            openScreenResult(Config.FUNCTION.ANTIVIRUS);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (isCanback)
            super.onBackPressed();
    }

    public void uninstall(int position) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + lstAppVirus.get(position).getPackageName()));
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        startActivityForResult(intent, Config.UNINSTALL_REQUEST_CODE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.UNINSTALL_REQUEST_CODE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                lstAppVirus.remove(startResolve);
                updateData();
                startResolve--;
                if (startResolve >= 0)
                    uninstall(startResolve);
            }
        }
    }
}
