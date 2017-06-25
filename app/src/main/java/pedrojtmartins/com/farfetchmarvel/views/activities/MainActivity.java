package pedrojtmartins.com.farfetchmarvel.views.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import pedrojtmartins.com.farfetchmarvel.R;
import pedrojtmartins.com.farfetchmarvel.databinding.ActivityMainBinding;
import pedrojtmartins.com.farfetchmarvel.interfaces.IDetailsCallback;
import pedrojtmartins.com.farfetchmarvel.interfaces.IListCallback;
import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import pedrojtmartins.com.farfetchmarvel.sharedPreferences.SharedPreferencesManager;
import pedrojtmartins.com.farfetchmarvel.viewmodels.MainViewModel;
import pedrojtmartins.com.farfetchmarvel.views.fragments.DetailsFragment;
import pedrojtmartins.com.farfetchmarvel.views.fragments.ListFragment;

public class MainActivity extends Activity implements IListCallback, IDetailsCallback {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private Fragment filteredFragment;
    private String lastFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences(SharedPreferencesManager.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferencesManager spManager = new SharedPreferencesManager(sp);
        viewModel = new MainViewModel(spManager);
        viewModel.initialize();

        registerObservables();

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
                viewModel.loadFilteredCharacters(null); // Clears the filter
                filteredFragment = null;
                getFragmentManager().popBackStack();
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

            //Emulator bug? The same intent is being called multiple times
            if (filter == null || filter.equals(lastFilter))
                return;

            lastFilter = filter;
            viewModel.loadFilteredCharacters(filter);

            if (filteredFragment == null) {
                // If this is the first search instantiate a new list fragment
                filteredFragment = new ListFragment();
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, filteredFragment)
                        .commit();
            }
        }
    }

    private void initializeListFragment() {
        Fragment listFragment = new ListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment)
                .commit();
    }

    private void registerObservables() {
        viewModel.apiCallResult.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Toast.makeText(binding.getRoot().getContext(), R.string.apiError, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public ObservableArrayList<MarvelModel.Character> getItems() {
        return viewModel.getCharacters();
    }

    @Override
    public void onItemClick(MarvelModel.Character character) {
        viewModel.onCharacterSelected(character);

        Fragment detailsFragment = new DetailsFragment();
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(
                        R.animator.fade_in,
                        R.animator.fade_out,
                        R.animator.fade_in,
                        R.animator.fade_out)
                .replace(R.id.fragment_container, detailsFragment)
                .commit();
    }
    @Override
    public void onItemLongClick(MarvelModel.Character character) {
        viewModel.onCharacterLongPressed(character);
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
    public void getDetails(MarvelModel.Character character) {
        viewModel.getDetails(character);
    }
}
