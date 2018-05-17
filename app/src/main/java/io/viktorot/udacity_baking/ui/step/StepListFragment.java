package io.viktorot.udacity_baking.ui.step;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;

public class StepListFragment extends Fragment {

    public static final String TAG = "StepListFragment";

    private static final String ARG_RECIPE = "arg_recipe";

    private StepListViewModel viewModel;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private StepAdapter adapter;

    public static StepListFragment newInstance(Recipe recipe) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_RECIPE, recipe);

        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle args = getArguments();
//        if (args == null) {
//            throw new IllegalArgumentException("arguments must be set");
//        }
//
//        Recipe recipe = args.getParcelable(ARG_RECIPE);
//        if (recipe == null) {
//            throw new IllegalArgumentException("recipe cannot be null");
//        }

        adapter = new StepAdapter(this::onClick, requireContext());

        viewModel = ViewModelProviders.of(this).get(StepListViewModel.class);

        viewModel.recipe.observe(this, data -> {
            if (data == null) {
                return;
            }
            onDataChanged(data);
        });

        Recipe recipe = getCallback().getData();
        viewModel.setData(recipe);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, view);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    private void onDataChanged(@NonNull Recipe recipe) {
        this.adapter.setItems(recipe.steps);
    }

    private void onClick(@NonNull Step step) {
        Recipe recipe = viewModel.getData();
        int index = recipe.steps.indexOf(step);
        getCallback().openDetails(recipe, index);
    }

    private void onBackPressed() {
        getCallback().back();
    }

    private StepListFragment.Callback getCallback() {
        return ((Callback)getParentFragment());
    }

    interface Callback {
        Recipe getData();

        void back();
        void openDetails(Recipe recipe, int index);
    }
}
