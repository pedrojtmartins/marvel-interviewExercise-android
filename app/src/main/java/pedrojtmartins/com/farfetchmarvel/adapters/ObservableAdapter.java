package pedrojtmartins.com.farfetchmarvel.adapters;

import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import static pedrojtmartins.com.farfetchmarvel.settings.Settings.PAGINATION_ITEMS_COUNT;

/**
 * Pedro Martins
 * 21/06/2017
 */

abstract class ObservableAdapter<T> extends RecyclerView.Adapter<ObservableAdapter.ViewHolder> {

    private ObservableInt currPage; // Page displaying
    private final ObservableArrayList<T> items;

    ObservableAdapter(ObservableArrayList<T> items, ObservableInt currPage) {
        this.items = items;

        this.currPage = currPage;
        this.currPage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null || currPage.get() < 0)
            return 0;

        int itemsCount = 0;
        int startingItem = currPage.get() * PAGINATION_ITEMS_COUNT;
        if (items.size() > startingItem) {
            itemsCount = items.size() - startingItem;
        }

        if (itemsCount > PAGINATION_ITEMS_COUNT)
            return PAGINATION_ITEMS_COUNT;

        return itemsCount;
    }

    T getItem(int pos) {
        if (items == null || currPage.get() < 0)
            return null;

        int position = currPage.get() * PAGINATION_ITEMS_COUNT + pos;
        if (items.size() > position)
            return items.get(position);

        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        ViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            binding = dataBinding;
        }
    }
}