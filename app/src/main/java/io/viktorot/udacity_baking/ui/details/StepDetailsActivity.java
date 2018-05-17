package io.viktorot.udacity_baking.ui.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;

public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.Callback {

    private static final String ARG_RECIPE = "arg_recipe";
    private static final String ARG_INDEX = "arg_index";

    public static Intent getIntent(@NonNull Context context, @NonNull Recipe recipe, int index) {
        Intent intent = new Intent(context, StepDetailsActivity.class);
        intent.putExtra(ARG_RECIPE, recipe);
        intent.putExtra(ARG_INDEX, index);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        Bundle args = getIntent().getExtras();
        if (args == null) {
            throw new IllegalArgumentException("arguments must be set");
        }

        Recipe recipe = args.getParcelable(ARG_RECIPE);
        if (recipe == null) {
            throw new IllegalArgumentException("recipe cannot be null");
        }

        int index = args.getInt(ARG_INDEX, 0);

        StepDetailsFragment frag = getDetailsFragment();
        frag.setData(recipe, index);
    }

    private StepDetailsFragment getDetailsFragment() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        return (StepDetailsFragment) frag;
    }

    @Override
    public void close() {
        finish();
    }
}
