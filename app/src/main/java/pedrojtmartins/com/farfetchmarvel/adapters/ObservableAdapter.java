package pedrojtmartins.com.farfetchmarvel.adapters;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import static pedrojtmartins.com.farfetchmarvel.settings.Settings.PAGINATION_ITEMS_COUNT;

/**
 * Pedro Martins
 * 21/06/2017
 */

abstract class ObservableAdapter<T> extends RecyclerView.Adapter<ObservableAdapter.ViewHolder> {

    private int pageOffset = 0; // Page displaying
    private final ObservableArrayList<T> items;

    ObservableAdapter(ObservableArrayList<T> items) {
        this.items = items;
        this.items.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> ts) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> ts, int i, int i1, int i2) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> ts, int i, int i1) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;

        int itemsCount = 0;
        int startingItem = pageOffset * PAGINATION_ITEMS_COUNT;
        if (items.size() > startingItem) {
            itemsCount = items.size() - startingItem;
        }

        if (itemsCount > PAGINATION_ITEMS_COUNT)
            return PAGINATION_ITEMS_COUNT;

        return itemsCount;
    }

    T getItem(int pos) {
        if (items == null)
            return null;

        int position = pageOffset * PAGINATION_ITEMS_COUNT + pos;
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

    public int getPageOffset() {
        return pageOffset;
    }

    /**
     * Updates page offset. If storyItems already cached reloads storyItems.
     * Otherwise data binding must update the ui when storyItems are available.
     *
     * @return true if no further action required, false if characters not cached
     */
    public boolean nextPage() {
        pageOffset++;

        // Do we already have the storyItems for this page cached?
        if (getItem(0) != null) {
            notifyDataSetChanged();
            return true;
        } else {
            // Do nothing. the storyItems are not ready yet
            // The binding will take care of the update
            //// TODO: 22/06/2017 check what happens if we scroll in this time
            return false;
        }
    }

    /**
     * Updates page offset and reloads storyItems if possible
     * In case it is already at the first page nothing happens.
     */
    public void previousPage() {
        if (pageOffset <= 0)
            return;

        pageOffset--;
        notifyDataSetChanged();
    }

    public void resetOffsetOnly() {
        pageOffset = 0;
    }
}