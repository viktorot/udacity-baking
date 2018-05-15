package io.viktorot.udacity_baking.ui.details;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.main.MainActivity;

public class StepDetailsFragment extends Fragment {

    public static final String TAG = "StepDetailsFragment";

    private static final String ARG_RECIPE = "arg_recipe";
    private static final String ARG_INDEX = "arg_index";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.player_view)
    PlayerView playerView;

    @BindView(R.id.desc)
    TextView tvDescription;

    private StepDetailsViewModel viewModel;

    public static StepDetailsFragment newInstance(@NonNull Recipe recipe, int index) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);
        args.putInt(ARG_INDEX, index);

        StepDetailsFragment fragment = new StepDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("arguments must be set");
        }

        Recipe recipe = args.getParcelable(ARG_RECIPE);
        if (recipe == null) {
            throw new IllegalArgumentException("recipe cannot be null");
        }

        int index = args.getInt(ARG_INDEX, 0);

        viewModel = ViewModelProviders.of(this).get(StepDetailsViewModel.class);
        viewModel.setData(recipe, index);

        viewModel.recipe.observe(this, data -> {
            if (data == null) {
                return;
            }
            onDataChanged(data);
        });
        viewModel.step.observe(this, data -> {
            if (data == null) {
                return;
            }
            onStepChanged(data);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, view);

        toolbar.setNavigationOnClickListener(view1 -> onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_vector);

        return view;
    }

    @OnClick(R.id.prev)
    void onPrevClick() {
        viewModel.goToPrevStep();
    }

    @OnClick(R.id.next)
    void onNextClick() {
        viewModel.goToNextStep();
    }

    private void onDataChanged(@NonNull Recipe recipe) {
    }

    private void onStepChanged(@NonNull Step step) {
        toolbar.setTitle(step.shortDescription);
        tvDescription.setText(step.description);

        if (!TextUtils.isEmpty(step.videoURL)) {
            setupPlayer(step.videoURL);
        }
    }

    private void setupPlayer(String url) {
        String userAgent = Util.getUserAgent(requireContext().getApplicationContext(), "marjan");
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(requireContext().getApplicationContext(), userAgent);

        Uri uri = Uri.parse(url);
        MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector);
        player.prepare(source, true, true);

        playerView.setPlayer(player);
    }

    private void onBackPressed() {
        MainActivity.getNavigator(requireActivity()).back();
    }
}
