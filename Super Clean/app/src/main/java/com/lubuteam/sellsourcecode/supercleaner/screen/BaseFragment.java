package com.lubuteam.sellsourcecode.supercleaner.screen;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lubuteam.sellsourcecode.supercleaner.R;
import com.lubuteam.sellsourcecode.supercleaner.utils.Config;
import com.lubuteam.sellsourcecode.supercleaner.utils.Toolbox;

public class BaseFragment extends Fragment {
    private View loPanel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loPanel = view.findViewById(R.id.layout_padding);
        if (loPanel != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Toolbox.getHeightStatusBar(getActivity()) > 0) {
                loPanel.setPadding(0, Toolbox.getHeightStatusBar(getActivity()), 0, 0);
            }
            Toolbox.setStatusBarHomeTransparent(getActivity());
        }
    }

    public void openScreenFunction(Config.FUNCTION mFunction) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).openScreenFunction(mFunction);
    }

    public void openScreenResult(Config.FUNCTION mFunction) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).openScreenResult(mFunction);
    }

    public void openScreenResult(String packageName) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).openSettingApplication(packageName);
    }

}
