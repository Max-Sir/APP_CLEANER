package com.security.applock.ui.kidZone;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.security.applock.R;
import com.security.applock.adapter.AppSelectAdapter;
import com.security.applock.data.AppLockRepository;
import com.security.applock.databinding.FragmentKidzoneBinding;
import com.security.applock.model.TaskInfo;
import com.security.applock.service.AntiTheftService;
import com.security.applock.service.BackgroundManager;
import com.security.applock.ui.BaseFragment;
import com.security.applock.utils.Config;
import com.security.applock.utils.PreferencesHelper;
import com.security.applock.utils.Toolbox;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class KidZoneFragment extends BaseFragment<FragmentKidzoneBinding> {

    private AppSelectAdapter mAppSelectAdapterThirdParty;
    private List<TaskInfo> lstThirdParty = new ArrayList<>();
    private List<TaskInfo> lstThirdPartySearch = new ArrayList<>();
    private List<TaskInfo> lstThirdPartyStack = new ArrayList<>();
    private boolean enableLock;
    private MenuItem menuItem;
    private String textSearch = "";
    private Handler handler = new Handler();
    private Runnable runnable = () -> {
        onSearch(textSearch);
    };

    private int pageNumber = 0;
    private int pageSize = 15;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView() {
        enableLock = PreferencesHelper.getBoolean(PreferencesHelper.ENABLE_KIDZONE, false);
        pageNumber = 0;
        pageSize = 15;
        Drawable drawable = new ChromeFloatingCirclesDrawable.Builder(getActivity())
                .colors(Toolbox.getProgressDrawableColors(getActivity()))
                .build();
        Rect bounds = binding.googleProgress.getIndeterminateDrawable().getBounds();
        binding.googleProgress.setIndeterminateDrawable(drawable);
        binding.googleProgress.getIndeterminateDrawable().setBounds(bounds);

        mAppSelectAdapterThirdParty = new AppSelectAdapter(getActivity(), lstThirdPartyStack);
        binding.rcvThirdParty.setAdapter(mAppSelectAdapterThirdParty);
        Disposable disposable = AppLockRepository.fetchInstalledAppList(getContext(), data -> {
            if (!data.isEmpty()) {
                lstThirdParty.clear();
                lstThirdParty.addAll(data);
                lstThirdPartySearch.clear();
                lstThirdPartySearch.addAll(data);
                loadApplication();
            }
            binding.llProgress.setVisibility(View.GONE);
        });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater mMenuInflater) {
        mMenuInflater.inflate(R.menu.menu_kidzone, menu);
        menuItem = menu.findItem(R.id.action_save);
        menuItem.setIcon(getResources().getDrawable(enableLock ? R.drawable.ic_lock_white : R.drawable.ic_unlock));
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                actionSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                actionSearch(newText);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                if (getViewTitle() != null) {
                    getViewTitle().setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (getViewTitle() != null) {
                    getViewTitle().setVisibility(View.VISIBLE);
                }
                // Write your code here
                return true;
            }
        });
    }

    private void actionSearch(String textSearch) {
        this.textSearch = textSearch;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 500);
    }

    private void onSearch(String query) {
        lstThirdPartySearch.clear();
        for (TaskInfo taskInfo : lstThirdParty) {
            if (taskInfo.getTitle().toLowerCase().contains(query.toLowerCase())) {
                lstThirdPartySearch.add(taskInfo);
            }
        }
        pageNumber = 0;
        pageSize = 15;
        lstThirdPartyStack.clear();
        if (lstThirdPartySearch.size() < pageSize)
            lstThirdPartyStack.addAll(lstThirdPartySearch);
        mAppSelectAdapterThirdParty.notifyDataSetChanged();
        loadApplication();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (enableLock) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.stop_applock))
                        .setNegativeButton(R.string.ok, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            setStateAppLock(false);
                        })
                        .setPositiveButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
            } else {
                getBaseActivity().askPermissionUsageSetting(() -> {
                    getBaseActivity().checkdrawPermission(() -> {
                        setStateAppLock(true);
                        return null;
                    });
                    return null;
                });

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStateAppLock(boolean turnOn) {
        enableLock = turnOn;
        menuItem.setIcon(getResources().getDrawable(turnOn ? R.drawable.ic_lock_white : R.drawable.ic_unlock));
        PreferencesHelper.putBoolean(PreferencesHelper.ENABLE_KIDZONE, turnOn);
        if (turnOn) {
            if (BackgroundManager.getInstance(getActivity()).canStartService()) {
                BackgroundManager.getInstance(getBaseActivity()).startService(AntiTheftService.class);
            }
        } else {
            Intent intent = new Intent(getContext(), AntiTheftService.class);
            intent.setAction(Config.ActionIntent.ACTION_STOP_SERVICE);
            getActivity().startService(intent);
        }
    }

    @Override
    protected void initControl() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                receiverNewApp, new IntentFilter(Config.ActionIntent.ACTION_NEW_APP_RECEIVER));

        binding.rcvThirdParty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    pageNumber++;
                    loadApplication();
                }
            }
        });

        mAppSelectAdapterThirdParty.setItemClickListener(() -> {
            List<String> lstAppSave = PreferencesHelper.getListAppKidZone();
            lstAppSave.clear();
            for (TaskInfo mTaskInfo : lstThirdPartySearch) {
                if (mTaskInfo.isChceked())
                    lstAppSave.add(mTaskInfo.getPackageName());
            }
            PreferencesHelper.setListAppKidZone(lstAppSave);
        });
    }

    private void loadApplication() {
        if (pageSize * (pageNumber + 1) > lstThirdPartySearch.size()) return;
        if (lstThirdPartySearch.size() > 0) {
            for (int i = pageNumber * pageSize; i < pageSize * (pageNumber + 1); i++) {
                lstThirdPartyStack.add(lstThirdPartySearch.get(i));
                mAppSelectAdapterThirdParty.notifyItemChanged(i);
            }
        }
    }

    @Override
    protected FragmentKidzoneBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentKidzoneBinding.inflate(inflater, container, false);
    }

    @Override
    protected int getTitleFragment() {
        return R.string.app_name_empty;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiverNewApp);
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private BroadcastReceiver receiverNewApp = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initView();
        }
    };

}