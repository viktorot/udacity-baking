package io.viktorot.udacity_baking.ui.details;

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

public class StepDetailsFragment extends Fragment {

    public static final String TAG = "StepDetailsFragment";

    private static final String ARG_RECIPE = "arg_recipe";
    private static final String ARG_INDEX = "arg_index";

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

    private ConstraintSet originalConstraints = new ConstraintSet();
    private ConstraintSet fullscreenConstraints = new ConstraintSet();

    private StepDetailsFragment.Callback callback;

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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            restoreConstraints();
        } else {
            setFullscreenConstraints();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAgent = Util.getUserAgent(requireContext().getApplicationContext(), "imbakingmom");

        viewModel = ViewModelProviders.of(this).get(StepDetailsViewModel.class);
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

        Bundle args = getArguments();
//        if (args == null) {
//            throw new IllegalArgumentException("arguments must be set");
//        }
//
//        Recipe recipe = args.getParcelable(ARG_RECIPE);
//        if (recipe == null) {
//            throw new IllegalArgumentException("recipe cannot be null");
//        }
//
//        int index = args.getInt(ARG_INDEX, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (StepDetailsFragment.Callback) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);

        ButterKnife.bind(this, view);

        toolbar.setNavigationOnClickListener(view1 -> onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_vector);

        originalConstraints.clone(root);

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
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        super.onStop();
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

    private void onDataChanged(@NonNull Recipe recipe) {
    }

    private void onStepChanged(@NonNull Step step) {
        toolbar.setTitle(step.shortDescription);
        tvDescription.setText(step.description);

        if (!TextUtils.isEmpty(step.videoURL)) {
            showPlayer();
            showNoVideoLabel(false);

            setupPlayer(step.videoURL);
        } else {
            showNoVideoLabel();
            showPlayer(false);
        }
    }

    public void setFullscreenConstraints() {
        fullscreenConstraints.clone(originalConstraints);

        fullscreenConstraints.setVisibility(toolbar.getId(), ConstraintSet.GONE);
        fullscreenConstraints.setVisibility(tvDescription.getId(), ConstraintSet.GONE);
        fullscreenConstraints.setVisibility(R.id.prev, ConstraintSet.GONE);
        fullscreenConstraints.setVisibility(R.id.next, ConstraintSet.GONE);

        fullscreenConstraints.connect(container.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        fullscreenConstraints.connect(container.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        fullscreenConstraints.connect(container.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        fullscreenConstraints.connect(container.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        fullscreenConstraints.setDimensionRatio(container.getId(), null);

        fullscreenConstraints.applyTo(root);
    }

    public void restoreConstraints() {
        originalConstraints.applyTo(root);
    }

    private void setupPlayer(String url) {
        releasePlayer();

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(requireContext().getApplicationContext(), userAgent);
        MediaSource source = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));

        initPlayer();

        player.prepare(source, true, true);
        player.setPlayWhenReady(true);
        playerView.setPlayer(player);
    }

    private void initPlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(requireContext(), trackSelector);
        }
    }

    private void releasePlayer() {
        if (player == null) {
            return;
        }
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
        callback.close();
    }

    interface Callback {
        void close();
    }
}
