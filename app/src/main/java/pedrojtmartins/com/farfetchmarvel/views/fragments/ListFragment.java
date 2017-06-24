package pedrojtmartins.com.farfetchmarvel.views.fragments;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pedrojtmartins.com.farfetchmarvel.R;
import pedrojtmartins.com.farfetchmarvel.adapters.MainListAdapter;
import pedrojtmartins.com.farfetchmarvel.databinding.FragmentListBinding;
import pedrojtmartins.com.farfetchmarvel.interfaces.IItemInteraction;
import pedrojtmartins.com.farfetchmarvel.interfaces.IListCallback;
import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 21/06/2017
 */

public class ListFragment extends Fragment implements IItemInteraction<MarvelModel.Character> {

    private FragmentListBinding binding;
    private IListCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        binding.nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.nextPage();
            }
        });
        binding.previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.previousPage();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainStatus status = callback.getMainListBindable();
        binding.setData(status);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(new MainListAdapter(callback.getItems(), this, status.getCurrPageObservable()));

        status.getCurrPageObservable().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (IListCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IListCallback");
        }
    }

    @Override
    public void onItemClick(MarvelModel.Character item) {
        callback.onItemClick(item);
    }
}
