package pedrojtmartins.com.farfetchmarvel.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;

import pedrojtmartins.com.farfetchmarvel.BR;

/**
 * Pedro Martins
 * 24/06/2017
 */

public class MainStatus extends BaseObservable {
    @Bindable
    private boolean loadingCharacters;
    public boolean isLoadingCharacters() {
        return loadingCharacters;
    }
    public void setLoadingCharacters(boolean loadingCharacters) {
        this.loadingCharacters = loadingCharacters;
        notifyPropertyChanged(BR.loadingCharacters);
    }

    @Bindable
    private int availablePages;
    public String getAvailablePages() {
        return String.valueOf(availablePages);
    }
    public void setAvailablePages(int availablePages) {
        this.availablePages = availablePages;
        notifyPropertyChanged(BR.availablePages);
    }

    private ObservableInt currPage;
    public ObservableInt getCurrPageObservable() {
        return currPage;
    }
    public int getCurrPage() {
        return currPage.get();
    }
    @Bindable
    public String getCurrPageString() {
        return String.valueOf(currPage.get() + 1);
    }
    public void setCurrPage(int currPage) {
        this.currPage.set(currPage);
        notifyPropertyChanged(BR.currPageString);
    }

    public MainStatus() {
        currPage = new ObservableInt(-1);
    }
}
