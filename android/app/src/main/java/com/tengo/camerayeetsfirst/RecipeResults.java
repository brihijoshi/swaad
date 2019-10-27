package com.tengo.camerayeetsfirst;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_results);
        RecyclerView recyclerView = findViewById(R.id.recipes_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        RecipeAdapter adapter = new RecipeAdapter();
        Recipe[] recipes = (Recipe[]) getIntent().getSerializableExtra("recipes");
        adapter.setData(Arrays.asList(recipes));
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }

    private static class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        protected List<Recipe> mRecipes = new ArrayList<>();

        public void setData(List<Recipe> recipes) {
            mRecipes.clear();
            mRecipes.addAll(recipes);
            notifyDataSetChanged();
        }

        public void clearData() {
            mRecipes.clear();
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
            return new RecipeHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RecipeHolder recipeHolder = (RecipeHolder) holder;
            recipeHolder.mTitle.setText(mRecipes.get(position).title);
            recipeHolder.setRecipeUrl(mRecipes.get(position).source_url);
            Picasso.get().load(mRecipes.get(position).image_url).into(recipeHolder.mImage);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        private static class RecipeHolder extends RecyclerView.ViewHolder {
            private TextView mTitle;
            private ImageView mImage;
            private String mRecipeUrl;

            public RecipeHolder(final View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.recipe_title);
                mImage = (ImageView) itemView.findViewById(R.id.recipe_image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), RecipeViewer.class);
                        intent.putExtra("url", mRecipeUrl);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }

            private void setRecipeUrl(String recipeUrl) {
                mRecipeUrl = recipeUrl;
            }
        }
    }
}
