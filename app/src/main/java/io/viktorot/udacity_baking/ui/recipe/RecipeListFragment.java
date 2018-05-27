package io.viktorot.udacity_baking.ui.recipe;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.viktorot.udacity_baking.BakingApplication;
import io.viktorot.udacity_baking.Prefs;
import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;
import io.viktorot.udacity_baking.ui.main.MainActivity;
import io.viktorot.udacity_baking.ui.util.SpacingItemDecoration;
import io.viktorot.udacity_baking.widget.RecipeWidget;

public class RecipeListFragment extends Fragment implements RecipeAdapter.Callback {

    public static final String TAG = "RecipeListFragment";

    private boolean isTablet = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    private RecipeAdapter adapter;

    private RecipeListViewModel viewModel;

    public static RecipeListFragment newInstance() {
        Bundle args = new Bundle();

        RecipeListFragment fragment = new RecipeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        Recipe fav = Prefs.getFavRecipe(requireContext());
        adapter = new RecipeAdapter(this, requireContext(), fav);

        viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        viewModel.recipes.observe(this, recipes -> {
            if (recipes == null) {
                return;
            }
            onDataLoaded(recipes);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, view);

        toolbar.setTitle(getString(R.string.title_recipe));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView.LayoutManager manager;
        if (!isTablet) {
            manager = new LinearLayoutManager(requireContext());
        } else {
            manager = new GridLayoutManager(requireContext(), 3);
        }

        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new SpacingItemDecoration());
        recycler.setAdapter(adapter);
    }

    private void onDataLoaded(@NonNull List<Recipe> items) {
        adapter.setItems(items);
    }

    @Override
    public void onClick(Recipe recipe) {
        MainActivity.getNavigator(requireActivity()).navigateToStepList(recipe);
    }

    @Override
    public void onSetAsFav(Recipe recipe) {
        Prefs.saveFavRecipe(requireContext(), recipe);
        adapter.onFavUpdated(recipe);
        BakingApplication.updateWidgets(requireContext());
    }
}
