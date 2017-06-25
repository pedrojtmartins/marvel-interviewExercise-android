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

    public static final int NORMAL_LIST = 0;
    public static final int FILTERED_LIST = 1;
    public int currentList;

    // Will be used to display a loading animation
    @Bindable
    private boolean loadingCharacters;
    public boolean isLoadingCharacters() {
        return loadingCharacters;
    }
    public void setLoadingCharacters(boolean loadingCharacters) {
        this.loadingCharacters = loadingCharacters;
        notifyPropertyChanged(BR.loadingCharacters);
    }

    // Will be used to display the total amount of pages available
    @Bindable
    private int availablePages;
    private int availableFilteredPages;
    public int getAvailablePagesInt() {
        return currentList == NORMAL_LIST ? availablePages : availableFilteredPages;
    }
    public String getAvailablePages() {
        return String.valueOf(getAvailablePagesInt());
    }
    public void setAvailablePages(int availablePages) {
        if (currentList == NORMAL_LIST)
            this.availablePages = availablePages;
        else
            this.availableFilteredPages = availablePages;

        notifyPropertyChanged(BR.availablePages);
    }

    // Will be used to display the current active page
    // and calculate what to show in the pagination system
    @Bindable
    private ObservableInt currPage;
    @Bindable
    private ObservableInt currFilteredPage;
    public ObservableInt getCurrPageObservable() {
        if (currentList == NORMAL_LIST)
            return currPage;
        else
            return currFilteredPage;
    }
    public int getCurrPage() {
        if (currentList == NORMAL_LIST)
            return currPage.get();
        else
            return currFilteredPage.get();
    }
    @Bindable
    public String getCurrPageString() {
        return String.valueOf(getCurrPage() + 1);
    }
    public void setCurrPage(int currPage) {
        if (currentList == NORMAL_LIST)
            this.currPage.set(currPage);
        else
            this.currFilteredPage.set(currPage);

        notifyPropertyChanged(BR.currPageString);
    }


    /**
     * Resets filter status
     */
    public void resetFilterStatus() {
        currentList = MainStatus.NORMAL_LIST;
        availableFilteredPages = 0;
        currFilteredPage.set(-1);
    }

    public MainStatus() {
        currentList = NORMAL_LIST;
        currPage = new ObservableInt(-1);
        currFilteredPage = new ObservableInt(-1);
    }
}
