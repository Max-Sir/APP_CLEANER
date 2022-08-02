package com.lubuteam.sellsourcecode.supercleaner.screen.main.home;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.AdmobHelp;
import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.adapter.FunctionAdapter;
import com.lubuteam.sellsourcecode.supercleaner.data.TotalMemoryStorageTask;
import com.lubuteam.sellsourcecode.supercleaner.data.TotalRamTask;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.ObserverInterface;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.ObserverUtils;
import com.lubuteam.sellsourcecode.supercleaner.listener.ObserverPartener.eventModel.EvbOnResumeAct;
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseFragment;
import com.lubuteam.sellsourcecode.supercleaner.screen.main.MainActivity;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.widget.HorizontalProgressView;
//import com.lubuteam.sellsourcecode.supercleaner.widget.circularprogressindicator.CircularProgressIndicator;

//import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHome extends BaseFragment implements FunctionAdapter.ClickItemListener, ObserverInterface {

    @BindView(R.id.rcv_home_horizontal)
    RecyclerView rcvHorizontal;
    @BindView(R.id.rcv_home_vertical)
    RecyclerView rcvVertical;
//    @BindView(R.id.prg_storage_used)
//    CircularProgressIndicator prgStorageUsed;
//    @BindView(R.id.prg_memory_used)
//    CircularProgressIndicator prgMemoryUsed;

    @BindView(R.id.prg_memory_used)
    HorizontalProgressView prgMemoryUsed;
    @BindView(R.id.prg_storage_used)
    HorizontalProgressView prgStorageUsed;

//    @BindView(R.id.tv_memory_used)
//    TextView tvMemoryUsed;
//    @BindView(R.id.tv_storage_used)
//    TextView tvStorageUsed;

    private FunctionAdapter mFunctionAdapterHorizontal;
    private FunctionAdapter mFunctionAdapterVertical;

    public static FragmentHome getInstance() {
        FragmentHome mFragmentHome = new FragmentHome();
        Bundle mBundle = new Bundle();
        mFragmentHome.setArguments(mBundle);
        return mFragmentHome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home_new, container, false);
        ButterKnife.bind(this, mView);
        ObserverUtils.getInstance().registerObserver(this);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAds();
        initView();
        initData();
        initControl();
    }

    public void loadAds() {
        try {
            if (((MainActivity) getActivity()).isLoadAdsNative()) {
                new Handler().postDelayed(() -> {
                    try {
                        AdmobHelp.getInstance().loadNativeFragment(getActivity(), getView());
                    } catch (Exception e) {

                    }
                }, 1000);
            }
        } catch (Exception e) {

        }
    }

    private void initView() {
//        prgStorageUsed.setMaxProgress(100);
//        prgMemoryUsed.setMaxProgress(100);

        int randomMemnory = new Random().nextInt(20) + 30;
        prgMemoryUsed.setProgress(randomMemnory);
//        tvMemoryUsed.setText(String.valueOf(randomMemnory));
//        prgMemoryUsed.setCurrentProgress(randomMemnory);

        int randomStorage = new Random().nextInt(20);
        prgStorageUsed.setProgress(randomStorage);
//        tvStorageUsed.setText(String.valueOf(randomStorage));
//        prgStorageUsed.setCurrentProgress(randomStorage);
    }

    private void initData() {
        mFunctionAdapterHorizontal = new FunctionAdapter(Config.LST_HOME_HORIZONTAL, Config.TYPE_DISPLAY_ADAPTER.HORIZOLTAL);
        rcvHorizontal.setAdapter(mFunctionAdapterHorizontal);

        mFunctionAdapterVertical = new FunctionAdapter(Config.LST_HOME_VERTICAL, Config.TYPE_DISPLAY_ADAPTER.VERTICAL);
        rcvVertical.setAdapter(mFunctionAdapterVertical);

        // separator
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator_horizontal);
        if (dividerDrawable != null) {
            divider.setDrawable(dividerDrawable);
            rcvVertical.addItemDecoration(divider);
        }

        getDataRamMemory();
    }

    public void getDataRamMemory() {
        new TotalRamTask((useRam, totalRam) -> {
            if (prgMemoryUsed != null)
//                prgMemoryUsed.setCurrentProgress(0);
                prgMemoryUsed.setProgress(0);
            float progress = (float) useRam / (float) totalRam;
//            if (tvMemoryUsed != null && prgMemoryUsed != null) {
            if (prgMemoryUsed != null) {
//                tvMemoryUsed.setText(String.valueOf((int) (progress * 100)));
//                prgMemoryUsed.setCurrentProgress((int) (progress * 100));
                prgMemoryUsed.setProgress((int) (progress * 100));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new TotalMemoryStorageTask((useMemory, totalMemory) -> {
            if (prgStorageUsed != null)
//                prgStorageUsed.setCurrentProgress(0);
                prgStorageUsed.setProgress(0);
            float progress = (float) useMemory / (float) totalMemory;
//            if (tvStorageUsed != null && prgStorageUsed != null) {
            if (prgStorageUsed != null) {
//                tvStorageUsed.setText(String.valueOf((int) (progress * 100)));
//                prgStorageUsed.setCurrentProgress((int) (progress * 100));
                prgStorageUsed.setProgress((int) (progress * 100));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initControl() {
        mFunctionAdapterHorizontal.setmClickItemListener(this);
        mFunctionAdapterVertical.setmClickItemListener(this);
    }

    @Override
    public void itemSelected(Config.FUNCTION mFunction) {
        openScreenFunction(mFunction);
    }

    @Override
    public void notifyAction(Object action) {
        try {
            if (action instanceof EvbOnResumeAct) {
                getDataRamMemory();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObserverUtils.getInstance().removeObserver(this::notifyAction);
    }
}
