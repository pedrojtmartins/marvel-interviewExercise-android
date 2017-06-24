package pedrojtmartins.com.farfetchmarvel.interfaces;

import android.databinding.ObservableArrayList;

import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 22/06/2017
 */

public interface IDetailsCallback {
    MarvelModel.Character getSelectedCharacter();
    ObservableArrayList<Integer> getDetails(MarvelModel.Character character);
}
