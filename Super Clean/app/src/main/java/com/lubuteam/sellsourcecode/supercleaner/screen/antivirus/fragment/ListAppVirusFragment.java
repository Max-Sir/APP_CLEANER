package com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.VirusAdapter;
import com.lubuteam.sellsourcecode.supercleaner.model.TaskInfo;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseFragment;
import com.lubuteam.sellsourcecode.supercleaner.screen.antivirus.AntivirusActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ListAppVirusFragment extends BaseFragment {

    @BindView(R.id.im_back_toolbar)
    ImageView imBackToolbar;
    @BindView(R.id.tv_toolbar)
    TextView tvToolbar;
    @BindView(R.id.rcv_app)
    RecyclerView rcvApp;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private VirusAdapter mVirusAdapter;
    private int positionSelect = -1;
    private List<TaskInfo> lstApp = new ArrayList<>();

    public static ListAppVirusFragment getInstance() {
        ListAppVirusFragment mListAppVirusFragment = new ListAppVirusFragment();
        return mListAppVirusFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_list_app_virus, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        imBackToolbar.setVisibility(View.VISIBLE);
        tvToolbar.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        lstApp.clear();
        lstApp.addAll(((AntivirusActivity) getActivity()).getLstAppVirus());
//        tvTitle.setText(String.valueOf(lstApp.size()));
        tvTitle.setText(getString(R.string.app_viruses, String.valueOf(lstApp.size())));

        mVirusAdapter = new VirusAdapter(lstApp, new VirusAdapter.ClickButtonListener() {
            @Override
            public void onClickUninstall(int position) {
                positionSelect = position;
                Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                intent.setData(Uri.parse("package:" + lstApp.get(position).getPackageName()));
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                startActivityForResult(intent, Config.UNINSTALL_REQUEST_CODE);
            }

            @Override
            public void onClickIgnore(int position) {
                List<String> whilteList = PreferenceUtils.getListAppWhileVirus();
                String pkgName = lstApp.get(position).getPackageName();
                if (!whilteList.contains(pkgName)) {
                    whilteList.add(pkgName);
                }
                PreferenceUtils.setListAppWhileVirus(whilteList);
                positionSelect = position;
                updateData();
            }
        });
        rcvApp.setAdapter(mVirusAdapter);

    }

    @OnClick({R.id.im_back_toolbar})
    void click(View mView) {
        switch (mView.getId()) {
            case R.id.im_back_toolbar:
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                updateData();
            }
        }
    }

    public void updateData() {
        ((AntivirusActivity) getActivity()).getLstAppVirus().remove(positionSelect);
        lstApp.remove(positionSelect);
        ((AntivirusActivity) getActivity()).updateData();
        mVirusAdapter.notifyItemRemoved(positionSelect);
        tvTitle.setText(String.valueOf(lstApp.size()));
    }

}
