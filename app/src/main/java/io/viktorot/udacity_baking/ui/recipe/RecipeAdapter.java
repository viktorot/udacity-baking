package io.viktorot.udacity_baking.ui.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.viktorot.udacity_baking.R;
import io.viktorot.udacity_baking.data.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    @NonNull
    private RecipeAdapter.Callback callback;

    @NonNull
    private Context context;

    private Recipe fav = null;

    private ArrayList<Recipe> items = new ArrayList<>();

    public RecipeAdapter(@NonNull RecipeAdapter.Callback callback, @NonNull Context context, @Nullable Recipe fav) {
        this.callback = callback;
        this.context = context;
        this.fav = fav;
    }

    public void setItems(List<Recipe> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void onFavUpdated(Recipe recipe) {
        this.fav = recipe;
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

        holder.btnFav.setOnClickListener(view1 -> {
            if (holder.data == null) {
                return;
            }
            // TODO
            callback.onSetAsFav(holder.data);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        Recipe item = items.get(position);
        holder.data = item;
        holder.tvTitle.setText(item.name);

        if (!TextUtils.isEmpty(item.image)) {
            Picasso.with(context)
                    .load(item.image)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.img);
        } else {
            holder.img.setBackgroundResource(R.drawable.placeholder);
        }

        if (fav == null) {
            holder.btnFav.setText("[fav]");
        } else {
            if (fav.id == item.id) {
                holder.btnFav.setText("[unfav]");
            } else {
                holder.btnFav.setText("[fav]");
            }
        }
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
        final ImageView img;
        final Button btnFav;

        ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tvTitle = itemView.findViewById(R.id.title);
            img = itemView.findViewById(R.id.image);
            btnFav = itemView.findViewById(R.id.fav);
        }
    }

    interface Callback {
        void onClick(Recipe recipe);
        void onSetAsFav(Recipe recipe);
    }
}
