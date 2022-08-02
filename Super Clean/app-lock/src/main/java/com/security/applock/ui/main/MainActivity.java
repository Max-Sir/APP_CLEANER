package com.security.applock.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.ActivityMainLockBinding;
import com.security.applock.databinding.AppBarMainBinding;
import com.security.applock.service.KeepLiveService;
import com.security.applock.ui.BaseActivity;
import com.security.applock.ui.password.PasswordActivity;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;

public class MainActivity extends BaseActivity<ActivityMainLockBinding> {

    private AppBarMainBinding appBarMainBinding;

    private AppBarConfiguration mAppBarConfiguration;
    private boolean isHomeScreen;

    @Override
    protected ActivityMainLockBinding getViewBinding() {
        return ActivityMainLockBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        appBarMainBinding = AppBarMainBinding.bind(binding.appBarMain.getRoot());
        setSupportActionBar(appBarMainBinding.toolbar);
        appBarMainBinding.toolbar.setTitleTextColor(getResources().getColor(android.R.color.transparent));
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_theme, R.id.nav_advanced_protection, R.id.nav_setting)
                .setDrawerLayout(binding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.getHeaderView(0).findViewById(R.id.im_close).setOnClickListener(v -> {
            binding.drawerLayout.close();
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_home) {
                isHomeScreen = true;
            } else {
                isHomeScreen = false;
            }
        });

        try {
            startService(new Intent(this, KeepLiveService.class));
        } catch (Exception e) {

        }
    }

    @Override
    protected void initControl() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!App.getInstace().isForceLockScreen()) {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (PreferencesHelper.isPatternCode()) {
                intent.setAction(Config.ActionIntent.ACTION_CHECK_PATTERN_CODE);
            } else {
                intent.setAction(Config.ActionIntent.ACTION_CHECK_PIN_CODE);
            }
            startActivity(intent);
        }
        App.getInstace().setForceLockScreen(false);
    }

    public void setStageDrawerLayout(boolean isLock) {
        binding.drawerLayout.setDrawerLockMode(isLock ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    protected View getViewPadding() {
        return binding.drawerLayout;
    }

    @Override
    protected TextView getToolbarTitle() {
        if (appBarMainBinding != null) {
            return appBarMainBinding.toolbarTitle;
        }
        return null;
    }

}
