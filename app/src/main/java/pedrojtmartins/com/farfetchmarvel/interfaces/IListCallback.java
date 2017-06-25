package pedrojtmartins.com.farfetchmarvel.interfaces;

import android.databinding.ObservableArrayList;

import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 21/06/2017
 */

public interface IListCallback{
    ObservableArrayList<MarvelModel.Character> getItems();
    MainStatus getMainListBindable();

    void onItemClick(MarvelModel.Character item);
    void onItemLongClick(MarvelModel.Character item);

    void previousPage();
    void nextPage();
}
