package com.lubuteam.sellsourcecode.supercleaner.screen.main.tool;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.lubuteam.sellsourcecode.supercleaner.screen.BaseFragment;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTool extends BaseFragment implements FunctionAdapter.ClickItemListener {

    @BindView(R.id.rcv_clean_boost)
    RecyclerView rcvCleanBoost;
    @BindView(R.id.rcv_security)
    RecyclerView rcvSecurity;

    private FunctionAdapter mFunctionCleanBoost;
    private FunctionAdapter mFunctionSecurity;


    public static FragmentTool getInstance() {
        FragmentTool mFragmentTool = new FragmentTool();
        Bundle mBundle = new Bundle();
        mFragmentTool.setArguments(mBundle);
        return mFragmentTool;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AdmobHelp.getInstance().loadBannerFragment(getActivity(), view);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_tool_new, container, false);
        ButterKnife.bind(this, mView);
        initView();
        initData();
        initControl();
        return mView;
    }

    private void initView() {

    }

    private void initData() {
        mFunctionCleanBoost = new FunctionAdapter(Config.LST_TOOL_CLEAN_BOOST, Config.TYPE_DISPLAY_ADAPTER.VERTICAL);
        rcvCleanBoost.setAdapter(mFunctionCleanBoost);

        mFunctionSecurity = new FunctionAdapter(Config.LST_TOOL_SECURITY, Config.TYPE_DISPLAY_ADAPTER.VERTICAL);
        rcvSecurity.setAdapter(mFunctionSecurity);

        // separator
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        Drawable dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.separator_horizontal);
        if (dividerDrawable != null) {
            divider.setDrawable(dividerDrawable);
            rcvCleanBoost.addItemDecoration(divider);
            rcvSecurity.addItemDecoration(divider);
        }
    }

    private void initControl() {
        mFunctionCleanBoost.setmClickItemListener(this);
        mFunctionSecurity.setmClickItemListener(this);
    }

    @Override
    public void itemSelected(Config.FUNCTION mFunction) {
        openScreenFunction(mFunction);
    }
}
