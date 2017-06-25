package pedrojtmartins.com.farfetchmarvel.viewmodels;

import android.databinding.ObservableArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import pedrojtmartins.com.farfetchmarvel.settings.Settings;
import pedrojtmartins.com.farfetchmarvel.sharedPreferences.SharedPreferencesManager;

import static org.mockito.Mockito.mock;

/**
 * Pedro Martins
 * 25/06/2017
 */

public class MainViewModelTest {

    private MainViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        SharedPreferencesManager mockedSPManager = mock(SharedPreferencesManager.class);
        viewModel = new MainViewModel(mockedSPManager);
    }

    @Test
    public void previousPage_shouldGoBack() throws Exception {
        viewModel.mainStatus.setCurrPage(2);
        viewModel.previousPage();

        Assert.assertEquals(1, viewModel.mainStatus.getCurrPage());
    }

    @Test
    public void previousPage_shouldNotGoBelow0() throws Exception {
        viewModel.mainStatus.setCurrPage(0);
        viewModel.previousPage();

        Assert.assertEquals(0, viewModel.mainStatus.getCurrPage());
    }

    @Test
    public void nextPage_shouldGoForward() throws Exception {
        ObservableArrayList<MarvelModel.Character> list = viewModel.getCharacters();
        for (int i = 0; i < Settings.PAGINATION_ITEMS_COUNT + 1; i++)
            list.add(mock(MarvelModel.Character.class));

        viewModel.mainStatus.setCurrPage(0);
        viewModel.mainStatus.setAvailablePages(2);

        viewModel.nextPage();

        Assert.assertEquals(1, viewModel.mainStatus.getCurrPage());
    }

    @Test
    public void nextPage_shouldNotGoForwardIfListIsNotEnough() throws Exception {
        ObservableArrayList<MarvelModel.Character> list = viewModel.getCharacters();
        for (int i = 0; i < Settings.PAGINATION_ITEMS_COUNT; i++)
            list.add(mock(MarvelModel.Character.class));

        viewModel.mainStatus.setCurrPage(0);
        viewModel.mainStatus.setAvailablePages(2);

        viewModel.nextPage();

        Assert.assertEquals(0, viewModel.mainStatus.getCurrPage());
    }

    @Test
    public void nextPage_shouldNotGoForwardIfTotalPagesNotEnough() throws Exception {
        ObservableArrayList<MarvelModel.Character> list = viewModel.getCharacters();
        for (int i = 0; i < Settings.PAGINATION_ITEMS_COUNT + 1; i++)
            list.add(mock(MarvelModel.Character.class));

        viewModel.mainStatus.setCurrPage(0);
        viewModel.mainStatus.setAvailablePages(1);

        viewModel.nextPage();

        Assert.assertEquals(0, viewModel.mainStatus.getCurrPage());
    }
}