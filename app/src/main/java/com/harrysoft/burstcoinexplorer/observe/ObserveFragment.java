package com.harrysoft.burstcoinexplorer.observe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.burst.api.BurstAPIService;
import com.harrysoft.burstcoinexplorer.burst.api.PoccAPIService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ObserveFragment extends Fragment {

    private BurstAPIService burstAPIService;

    private ObservePagerAdapter observePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observe, container, false);

        ViewPager viewPager = view.findViewById(R.id.observe_viewpager);
        observePagerAdapter = setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.observe_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        burstAPIService = new PoccAPIService(getActivity());

        getNetworkStatus();

        return view;
    }

    private void getNetworkStatus(@SuppressWarnings("SameParameterValue") @Nullable ObserveSubFragment sender) {
        burstAPIService.fetchNetworkStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(networkStatus -> observePagerAdapter.onNetworkStatus(networkStatus, sender), throwable -> onError(throwable, sender));
    }

    private void getNetworkStatus() {
        getNetworkStatus(null);
    }

    private void onError(Throwable throwable, @Nullable ObserveSubFragment sender) {
        Toast.makeText(getContext(), R.string.loading_error, Toast.LENGTH_LONG).show();
        throwable.printStackTrace();
        if (sender != null) {
            sender.onRefreshError(throwable);
        }
    }

    private ObservePagerAdapter setupViewPager(ViewPager viewPager) {
        ObservePagerAdapter observePagerAdapter = new ObservePagerAdapter(getChildFragmentManager());

        ObserveStatusFragment statusFragment = new ObserveStatusFragment();
        ObserveVersionsFragment versionsFragment = new ObserveVersionsFragment();
        ObserveBrokenPeersFragment brokenPeersFragment = new ObserveBrokenPeersFragment();

        statusFragment.setUp(getContext(), this::getNetworkStatus);
        versionsFragment.setUp(getContext(), this::getNetworkStatus);
        brokenPeersFragment.setUp(getContext(), this::getNetworkStatus);

        observePagerAdapter.addFragment(statusFragment, getString(R.string.observe_peer_status));
        observePagerAdapter.addFragment(versionsFragment, getString(R.string.observe_peer_versions));
        observePagerAdapter.addFragment(brokenPeersFragment, getString(R.string.observe_peer_versions));
        viewPager.setAdapter(observePagerAdapter);
        return observePagerAdapter;
    }
}