package io.viktorot.udacity_baking.ui.step;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Ingredient;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.data.Step;
import io.viktorot.udacity_baking.ui.util.SpacingItemDecoration;

public class StepListFragment extends Fragment {

    public static final String TAG = "StepListFragment";

    private static final String ARG_RECIPE = "arg_recipe";

    private StepListViewModel viewModel;

    @BindView(R.id.ingredients)
    TextView tvIngredients;

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
        recycler.addItemDecoration(new SpacingItemDecoration());
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    private void onDataChanged(@NonNull Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        sb.append("<strong>").append("[Ingredients]").append("</strong><br/><br/>");
        for (Ingredient ing: recipe.ingredients) {
            sb.append("&#8226; ").append(ing.name).append("<br/>");
        }

        this.tvIngredients.setText(Html.fromHtml(sb.toString()));
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
