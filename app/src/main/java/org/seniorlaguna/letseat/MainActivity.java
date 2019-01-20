package org.seniorlaguna.letseat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void addNewRecipe() {
        startActivity(new Intent(this, AddActivity.class));
    }

    protected void showRandomRecipe() {
        mDatabaseHelper = new DatabaseHelper(this);
        ArrayList<Recipe> recipes = mDatabaseHelper.getAllRecipes(null, null, null, null);

        //no recipes
        if (recipes.isEmpty()) {
            Toast.makeText(this, R.string.no_recipe, Toast.LENGTH_SHORT).show();
            return;
        }

        //pick random recipe
        Collections.shuffle(recipes);
        Recipe recipe = recipes.get(0);

        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra(ModifyActivity.KEY_RECIPE_ID, recipe.mId);
        startActivity(intent);
    }

    protected void showMyRecipes() {
        startActivity(new Intent(this, ListActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.main_my_recipes:
                showMyRecipes();
                break;

            case R.id.main_random_recipe:
                showRandomRecipe();
                break;

            case R.id.main_add_recipe:
                addNewRecipe();
                break;

            case R.id.main_import_recipe:
                startActivity(new Intent(this, ImportActivity.class));
                break;

        }
    }
}
