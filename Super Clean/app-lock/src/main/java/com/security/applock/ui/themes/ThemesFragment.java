package com.security.applock.ui.themes;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.security.applock.R;
import com.security.applock.databinding.FragmentThemesLockBinding;
import com.security.applock.ui.BaseFragment;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;


public class ThemesFragment extends BaseFragment<FragmentThemesLockBinding> {
    private ThemesAdapter themesAdapter;

    @Override
    protected void initView() {
        themesAdapter = new ThemesAdapter(getContext(), Config.LIST_THEME(),
                PreferencesHelper.getInt(PreferencesHelper.SETTING_THEME, R.drawable.bg_theme_defatult));
        binding.rcvTheme.setAdapter(themesAdapter);
    }

    @Override
    protected void initControl() {
        binding.tvSave.setOnClickListener(v -> saveTheme());
    }

    private void saveTheme() {
        PreferencesHelper.putInt(PreferencesHelper.SETTING_THEME, themesAdapter.getItemSelected());
        toast(getResources().getString(R.string.save_ok));
    }

    @Override
    protected FragmentThemesLockBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentThemesLockBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.themes;
    }
}

