package pedrojtmartins.com.farfetchmarvel.interfaces;

import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 22/06/2017
 */

public interface IDetailsCallback {
    MarvelModel.Character getSelectedCharacter();
    void getDetails(MarvelModel.Character character);
}
