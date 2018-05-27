package io.viktorot.udacity_baking.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.main.MainActivity;
import timber.log.Timber;

public class StepDetailsFragment extends Fragment {

    public static final String TAG = "StepDetailsFragment";

    private static final String ARG_RECIPE = "arg_recipe";
    private static final String ARG_INDEX = "arg_index";
    private static final String ARG_PLAYING = "arg_playing";
    private static final String ARG_POSITION = "arg_position";

    @BindView(R.id.root)
    ConstraintLayout root;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.container)
    View container;

    @BindView(R.id.player_view)
    PlayerView playerView;

    @BindView(R.id.desc)
    TextView tvDescription;

    @BindView(R.id.no_video)
    TextView tvNoVideo;

    private String userAgent;
    private final BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    private final DefaultTrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
    private SimpleExoPlayer player;

    private final Observer<Step> stepObserver = step -> {
        if (step == null) {
            return;
        }
        onStepChanged(step);
    };

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

        userAgent = Util.getUserAgent(requireContext().getApplicationContext(), "imbakingmom");

        viewModel = ViewModelProviders.of(this).get(StepDetailsViewModel.class);

        Bundle args;
        if (savedInstanceState == null) {
            args = getArguments();
        } else {
            args = savedInstanceState;

            viewModel.playing = args.getBoolean(ARG_PLAYING, true);
            viewModel.position = args.getLong(ARG_POSITION, 0);
            viewModel.restore = true;
        }

        if (args == null) {
            throw new IllegalArgumentException("arguments must be set");
        }

        Recipe recipe = args.getParcelable(ARG_RECIPE);
        if (recipe == null) {
            throw new IllegalArgumentException("recipe cannot be null");
        }

        int index = args.getInt(ARG_INDEX, 0);
        setData(recipe, index);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            MainActivity.clearFullscreen(requireActivity());
        } else {
            MainActivity.setFullscreen(requireActivity());
        }
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


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23) {
            initPlayer();
        }
        viewModel.step.observe(this, stepObserver);
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        viewModel.step.removeObserver(stepObserver);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        MainActivity.clearFullscreen(requireActivity());
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Recipe recipe = viewModel.getRecipe();
        outState.putParcelable(ARG_RECIPE, recipe);

        int index = viewModel.getIndex();
        if (index > -1) {
            outState.putInt(ARG_INDEX, index);
        }

        if (player != null) {
            long position = player.getCurrentPosition();
            boolean playing = player.getPlayWhenReady();

            outState.putBoolean(ARG_PLAYING, playing);
            outState.putLong(ARG_POSITION, position);
        }
    }

    @OnClick(R.id.prev)
    void onPrevClick() {
        viewModel.goToPrevStep();
    }

    @OnClick(R.id.next)
    void onNextClick() {
        viewModel.goToNextStep();
    }

    public void setData(Recipe recipe, int index) {
        viewModel.setData(recipe, index);
    }

    private void onStepChanged(@NonNull Step step) {
        toolbar.setTitle(step.shortDescription);
        tvDescription.setText(step.description);

        if (!TextUtils.isEmpty(step.videoURL)) {
            showPlayer();
            showNoVideoLabel(false);
            playUrl(step.videoURL);
        } else {
            showNoVideoLabel();
            showPlayer(false);
            player.stop();
        }
    }

    private void playUrl(String url) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(requireContext().getApplicationContext(), userAgent);
        MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));

        player.prepare(source, true, true);

        if (viewModel.restore) {
            player.seekTo(viewModel.position);
            player.setPlayWhenReady(viewModel.playing);
            viewModel.restore = false;
        } else {
            player.setPlayWhenReady(true);
        }

        playerView.setPlayer(player);
    }

    private void initPlayer() {
        if (player != null) {
            return;
        }

        player = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector);
    }

    private void releasePlayer() {
        if (player == null) {
            return;
        }

        viewModel.playing = player.getPlayWhenReady();
        viewModel.position = player.getContentPosition();
        viewModel.restore = true;

        player.stop();
        player.release();
        player = null;
    }

    private void showPlayer() {
        showPlayer(true);
    }

    private void showPlayer(boolean show) {
        if (show) {
            playerView.setVisibility(View.VISIBLE);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }

    private void showNoVideoLabel() {
        showNoVideoLabel(true);
    }

    private void showNoVideoLabel(boolean show) {
        if (show) {
            tvNoVideo.setVisibility(View.VISIBLE);
        } else {
            tvNoVideo.setVisibility(View.GONE);
        }
    }

    private void onBackPressed() {
        MainActivity.getNavigator(requireActivity()).back();
    }
}
