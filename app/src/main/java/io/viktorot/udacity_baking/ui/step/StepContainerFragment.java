package io.viktorot.udacity_baking.ui.step;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.ui.details.StepDetailsFragment;
import io.viktorot.udacity_baking.ui.main.MainActivity;

public class StepContainerFragment extends Fragment implements StepListFragment.Callback {

    public static final String TAG = "StepContainerFragment";

    private static final String ARG_RECIPE = "arg_recipe";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.detail_holder)
    View detailsHolder;

    public static StepContainerFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);

        StepContainerFragment fragment = new StepContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_step_container, container, false);
        ButterKnife.bind(this, view);

        toolbar.setNavigationOnClickListener(view1 -> onBackPressed());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_vector);

        Recipe recipe = getData();
        this.toolbar.setTitle(recipe.name);

        return view;
    }

    @Override
    public Recipe getData() {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("arguments must be set");
        }

        Recipe recipe = args.getParcelable(ARG_RECIPE);
        if (recipe == null) {
            throw new IllegalArgumentException("recipe cannot be null");
        }

        return recipe;
    }

    private void onBackPressed() {
        MainActivity.getNavigator(requireActivity()).back();
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void openDetails(Recipe recipe, int index) {
        if (detailsHolder != null) {
            Fragment fragment = getChildFragmentManager().findFragmentByTag(StepDetailsFragment.TAG);
            if (fragment != null && fragment instanceof StepDetailsFragment) {
                ((StepDetailsFragment) fragment).setData(recipe, index);
            } else {
                getChildFragmentManager().beginTransaction()
                        .replace(detailsHolder.getId(), StepDetailsFragment.newInstance(recipe, index), StepDetailsFragment.TAG)
                        .commit();
            }
        } else {
            MainActivity.getNavigator(requireActivity()).navigateToStepDetails(recipe, index);
        }
    }
}
