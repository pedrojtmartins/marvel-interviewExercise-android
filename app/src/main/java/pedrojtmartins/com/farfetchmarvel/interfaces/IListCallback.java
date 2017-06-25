package pedrojtmartins.com.farfetchmarvel.interfaces;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;

import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 21/06/2017
 */

public interface IListCallback{
    ObservableArrayList<MarvelModel.Character> getItems();

    void loadMoreCharacters();
    void onItemClick(MarvelModel.Character item);
    void onItemLongClick(MarvelModel.Character item);

    MainStatus getMainListBindable();

    void previousPage();
    void nextPage();
}
