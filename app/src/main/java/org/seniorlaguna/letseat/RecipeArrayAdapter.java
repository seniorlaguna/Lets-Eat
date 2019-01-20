package org.seniorlaguna.letseat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 10.07.17.
 */

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {

    protected Context mContext;
    protected ArrayList<Recipe> mRecipes;

    public RecipeArrayAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, -1, recipes);
        mContext = context;
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.recipe_list_item, parent, false);
        Recipe recipe = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.recipe_name);
        TextView duration = (TextView) view.findViewById(R.id.recipe_duration);
        TextView difficulty = (TextView) view.findViewById(R.id.recipe_difficulty);

        name.setText(recipe.mName);
        duration.setText(String.format("%s: %d", mContext.getString(R.string.cooking_time),recipe.mDuration));
        difficulty.setText(String.format("%s: %s", mContext.getString(R.string.difficulty), recipe.mDifficulty));

        return view;
    }
}
