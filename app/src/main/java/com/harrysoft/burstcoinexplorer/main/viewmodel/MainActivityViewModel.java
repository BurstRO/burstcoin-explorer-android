package com.harrysoft.burstcoinexplorer.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harry1453.burst.explorer.entity.SearchResult;
import com.harry1453.burst.explorer.service.BurstBlockchainService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    private final BurstBlockchainService burstBlockchainService;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<SearchResult> searchResult = new MutableLiveData<>();

    MainActivityViewModel(BurstBlockchainService burstBlockchainService) {
        this.burstBlockchainService = burstBlockchainService;
    }

    public void search(String query) {
        compositeDisposable.add(burstBlockchainService.determineSearchRequestType(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchResult::postValue, t -> {}));
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }

    public LiveData<SearchResult> getSearchResult() { return searchResult; }
}
