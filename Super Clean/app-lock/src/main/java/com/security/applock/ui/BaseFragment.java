package com.security.applock.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewbinding.ViewBinding;

import com.security.applock.ui.kidZone.KidZoneFragment;
import com.security.applock.ui.main.MainActivity;
import com.security.applock.utils.Toolbox;
import com.security.applock.utils.ViewUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseFragment<B extends ViewBinding> extends Fragment {

    protected B binding;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        Toolbox.hideSoftKeyboard(getActivity());
        ViewUtils.setupUI(binding.getRoot(), getActivity());
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitleFragment(getString(getTitleFragment()));
        initView();
        initControl();
    }

    protected abstract void initView();

    protected abstract void initControl();

    protected abstract B getViewBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract int getTitleFragment();

    protected void setTitleFragment(String title) {
        if (getBaseActivity() != null)
            getBaseActivity().setTitleToolbar(title);

    }

    protected TextView getViewTitle() {
        if (getBaseActivity() != null){
            return getBaseActivity().getToolbarTitle();
        }
        return null;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        Toolbox.hideSoftKeyboard(getActivity());
    }

    public void toast(String content) {
        if (getBaseActivity() != null)
            getBaseActivity().toast(content);
    }

    public void navigate(int id) {
        navigate(id, null);
    }

    public void backPress() {
        if (getBaseActivity() != null)
            getBaseActivity().onBackPressed();
    }

    public void navigate(int id, Bundle bundle) {
        Navigation.findNavController(getView()).navigate(
                id,
                bundle,
                null,
                null);
    }

    public void navigateUp() {
        Navigation.findNavController(getView()).navigateUp();
    }

    public void setStageDrawerLayout(boolean isLock) {
        if (getBaseActivity() != null && getBaseActivity() instanceof MainActivity)
            ((MainActivity) getBaseActivity()).setStageDrawerLayout(isLock);
    }

    public void setTitleToolbar(String title) {
        if (getBaseActivity() != null)
            getBaseActivity().setTitleToolbar(title);
    }

}
