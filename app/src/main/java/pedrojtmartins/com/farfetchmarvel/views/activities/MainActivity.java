package pedrojtmartins.com.farfetchmarvel.views.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import pedrojtmartins.com.farfetchmarvel.R;
import pedrojtmartins.com.farfetchmarvel.databinding.ActivityMainBinding;
import pedrojtmartins.com.farfetchmarvel.interfaces.IDetailsCallback;
import pedrojtmartins.com.farfetchmarvel.interfaces.IListCallback;
import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import pedrojtmartins.com.farfetchmarvel.viewmodels.MainViewModel;
import pedrojtmartins.com.farfetchmarvel.views.fragments.DetailsFragment;
import pedrojtmartins.com.farfetchmarvel.views.fragments.ListFragment;

public class MainActivity extends Activity implements IListCallback, IDetailsCallback {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new MainViewModel();
        viewModel.initialize();

        initializeListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                viewModel.loadFilteredCharacters(null);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // This will be called when a new search string is typed
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String filter = intent.getStringExtra(SearchManager.QUERY);
            viewModel.loadFilteredCharacters(filter);
        }
    }

    private void initializeListFragment() {
        Fragment listFragment = new ListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .commit();
    }

    @Override
    public ObservableArrayList<MarvelModel.Character> getItems() {
        return viewModel.getCharacters();
    }

    public void loadMoreCharacters() {
        viewModel.loadMoreCharacters();
    }

    @Override
    public void onItemClick(MarvelModel.Character character) {
        viewModel.onCharacterSelected(character);
        Fragment detailsFragment = new DetailsFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public MainStatus getMainListBindable() {
        return viewModel.mainStatus;
    }

    @Override
    public void previousPage() {
        viewModel.previousPage();
    }
    @Override
    public void nextPage() {
        viewModel.nextPage();
    }


    @Override
    public MarvelModel.Character getSelectedCharacter() {
        return viewModel.getSelectedCharacter();
    }
    @Override
    public ObservableArrayList getDetails(MarvelModel.Character character) {
        return viewModel.getDetails(character);
    }
}
