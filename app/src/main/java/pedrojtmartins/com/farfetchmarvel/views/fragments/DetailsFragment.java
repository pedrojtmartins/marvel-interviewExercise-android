package pedrojtmartins.com.farfetchmarvel.views.fragments;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import pedrojtmartins.com.farfetchmarvel.R;
import pedrojtmartins.com.farfetchmarvel.databinding.DetailsComicItemBinding;
import pedrojtmartins.com.farfetchmarvel.databinding.DetailsEventItemBinding;
import pedrojtmartins.com.farfetchmarvel.databinding.DetailsSeriesItemBinding;
import pedrojtmartins.com.farfetchmarvel.databinding.DetailsStoryItemBinding;
import pedrojtmartins.com.farfetchmarvel.databinding.FragmentDetailsBinding;
import pedrojtmartins.com.farfetchmarvel.interfaces.IDetailsCallback;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;

import static pedrojtmartins.com.farfetchmarvel.settings.Settings.DETAILS_COUNT;

/**
 * Pedro Martins
 * 22/06/2017
 */

public class DetailsFragment extends Fragment {

    private FragmentDetailsBinding binding;
    private IDetailsCallback callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestData();
    }
    private void requestData() {
        final MarvelModel.Character character = callback.getSelectedCharacter();

        // Request more information
        callback.getDetails(character);

        // Load cached information (titles)
        loadComics(character.comics.comicItems);
        loadEvents(character.events.eventItems);
        loadStories(character.stories.storyItems);
        loadSeries(character.series.seriesItems);
    }

    private void loadComics(List<MarvelModel.Comic> comicItems) {
        if (comicItems == null || comicItems.size() == 0)
            return;

        final LayoutInflater inflater = LayoutInflater.from(binding.toInflateComics.getContext());
        for (int i = 0; i < DETAILS_COUNT && i < comicItems.size(); i++) {
            final DetailsComicItemBinding itemBinding = DataBindingUtil.inflate(inflater,
                    R.layout.details_comic_item,
                    binding.toInflateComics,
                    false);

            final MarvelModel.Comic comic = comicItems.get(i);
            itemBinding.setComic(comic);
            comic.loaded.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    loadThumbnail(comic.getThumbnail(), itemBinding.image);
                }
            });

            binding.toInflateComics.addView(itemBinding.getRoot());
        }
    }

    private void loadEvents(List<MarvelModel.Event> eventItems) {
        final LayoutInflater inflater = LayoutInflater.from(binding.toInflateEvents.getContext());
        for (int i = 0; i < DETAILS_COUNT && i < eventItems.size(); i++) {
            final DetailsEventItemBinding itemBinding = DataBindingUtil.inflate(inflater,
                    R.layout.details_event_item,
                    binding.toInflateComics,
                    false);

            final MarvelModel.Event event = eventItems.get(i);
            itemBinding.setEvent(event);

            event.loaded.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    loadThumbnail(event.getThumbnail(), itemBinding.image);
                }
            });

            binding.toInflateEvents.addView(itemBinding.getRoot());
        }
    }

    private void loadStories(List<MarvelModel.Story> storyItems) {
        final LayoutInflater inflater = LayoutInflater.from(binding.toInflateStories.getContext());
        for (int i = 0; i < DETAILS_COUNT && i < storyItems.size(); i++) {
            final DetailsStoryItemBinding itemBinding = DataBindingUtil.inflate(inflater,
                    R.layout.details_story_item,
                    binding.toInflateStories,
                    false);

            final MarvelModel.Story story = storyItems.get(i);
            itemBinding.setStory(story);

            story.loaded.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    loadThumbnail(story.getThumbnail(), itemBinding.image);
                }
            });

            binding.toInflateStories.addView(itemBinding.getRoot());
        }
    }

    private void loadSeries(List<MarvelModel.SeriesItem> seriesItems) {
        final LayoutInflater inflater = LayoutInflater.from(binding.toInflateSeries.getContext());
        for (int i = 0; i < DETAILS_COUNT && i < seriesItems.size(); i++) {
            final DetailsSeriesItemBinding itemBinding = DataBindingUtil.inflate(inflater,
                    R.layout.details_series_item,
                    binding.toInflateSeries,
                    false);

            final MarvelModel.SeriesItem series = seriesItems.get(i);
            itemBinding.setSeries(series);

            series.loaded.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    loadThumbnail(series.getThumbnail(), itemBinding.image);
                }
            });

            binding.toInflateSeries.addView(itemBinding.getRoot());
        }
    }

    private void loadThumbnail(String url, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.mipmap.logo_marvel)
                .into(imageView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (IDetailsCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement IDetailsCallback");
        }
    }
}
