package com.app.githubtrending.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.githubtrending.R;
import com.app.githubtrending.databinding.FragmentHostBinding;
import com.app.githubtrending.ui.list.pager.RepoListsPager;
import com.app.githubtrending.ui.navigator.MainNavigator;
import com.app.githubtrending.ui.navigator.SubFragment;
import com.app.githubtrending.ui.navigator.SubNavigator;

public class HostFragment extends Fragment implements SubNavigator {

    private FragmentHostBinding binding;

    private Boolean isLand;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isLand = requireActivity().findViewById(R.id.fragment_container_details) != null;

        FragmentManager fragmentManager = getChildFragmentManager();
        int fragmentsCount = fragmentManager.getFragments().size();

        if (fragmentsCount > 0) {
            navigateOnRotate(fragmentsCount, fragmentManager);
        } else {
            ((MainNavigator) requireActivity()).navigateTo(new RepoListsPager());
        }
    }

    private void navigateOnRotate(int fragmentsCount, FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.getFragments().get(fragmentsCount - 1);

        if (fragment instanceof SubFragment) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragmentManager.executePendingTransactions();

            ((MainNavigator) requireActivity()).subNavigateTo(fragment);
        } else {
            ((MainNavigator) requireActivity()).navigateTo(fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void navigateTo(Fragment fragment) {
        if (isLand) {
            navigate(fragment, R.id.fragment_container_details);
        } else {
             navigate(fragment, R.id.fragment_container_main);
        }
    }

    private void navigate(Fragment fragment, int container) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(container, fragment);
        transaction.setReorderingAllowed(true);
        if (!isLand) transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void back() {
        ((MainNavigator) requireActivity()).back();
    }

    @Override
    public boolean canMoveBack() {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        return fragmentManager.getBackStackEntryCount() > 0;
    }

    @Override
    public void startScreen(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container_main, fragment);
        transaction.setReorderingAllowed(true);

        transaction.commit();
    }
}