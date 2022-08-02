package com.security.applock.ui.forgotpassword;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.security.applock.App;
import com.security.applock.R;
import com.security.applock.databinding.ActivityPasswordBinding;
import com.security.applock.ui.BaseActivity;
import com.security.applock.utils.SystemUtil;

public class ForgotPasswordActivity extends BaseActivity<ActivityPasswordBinding> {

    @Override
    protected ActivityPasswordBinding getViewBinding() {
        return ActivityPasswordBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolbar.setTitleTextColor(getResources().getColor(android.R.color.transparent));
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);
        graph.setStartDestination(R.id.nav_question_confirm);
        navController.setGraph(graph);
    }

    @Override
    protected void initControl() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected View getViewPadding() {
        return binding.container;
    }
}
