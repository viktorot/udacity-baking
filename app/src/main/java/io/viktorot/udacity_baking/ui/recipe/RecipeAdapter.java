package io.viktorot.udacity_baking.ui.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    @NonNull
    RecipeAdapter.Callback callback;

    private ArrayList<Recipe> items = new ArrayList<>();

    public RecipeAdapter(@NonNull RecipeAdapter.Callback callback) {
        this.callback = callback;
    }

    public void setItems(List<Recipe> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);

        ViewHolder holder =  new ViewHolder(view);
        holder.root.setOnClickListener(view1 -> {
            if (holder.data == null) {
                return;
            }
            callback.onClick(holder.data);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe item = items.get(position);
        holder.data = item;
        holder.tvTitle.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        Recipe data;

        final View root;
        final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tvTitle = itemView.findViewById(R.id.title);
        }
    }

    interface Callback {
        void onClick(Recipe recipe);
    }
}
