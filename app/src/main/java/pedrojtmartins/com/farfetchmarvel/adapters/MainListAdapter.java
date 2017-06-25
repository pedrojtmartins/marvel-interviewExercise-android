package pedrojtmartins.com.farfetchmarvel.adapters;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import pedrojtmartins.com.farfetchmarvel.R;
import pedrojtmartins.com.farfetchmarvel.databinding.CharacterItemBinding;
import pedrojtmartins.com.farfetchmarvel.interfaces.IItemInteraction;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

/**
 * Pedro Martins
 * 20/06/2017
 */

public class MainListAdapter extends ObservableAdapter<MarvelModel.Character> {

    private IItemInteraction<MarvelModel.Character> callback;

    public MainListAdapter(ObservableArrayList<MarvelModel.Character> items, IItemInteraction<MarvelModel.Character> callback, ObservableInt currPage) {
        super(items, currPage);
        this.callback = callback;
    }

    @Override
    public ObservableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CharacterItemBinding itemBinding = CharacterItemBinding.inflate(layoutInflater, parent, false);
        return new ObservableAdapter.ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ObservableAdapter.ViewHolder holder, int position) {
        final MarvelModel.Character currCharacter = getItem(position);
        CharacterItemBinding binding = (CharacterItemBinding) holder.binding;
        binding.setModel(currCharacter);

        // Add interaction listeners
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(currCharacter);
            }
        });
        binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callback.onItemLongClick(currCharacter);
                return true;
            }
        });

        // Load thumbnails
        MarvelModel.Thumbnail thumbnail = currCharacter.thumbnail;
        String thumbnailUrl = thumbnail.path + "." + thumbnail.extension;
        Glide.with(binding.getRoot().getContext())
                .load(thumbnailUrl)
                .placeholder(R.mipmap.logo_marvel)
                .into(binding.image);
    }
}


