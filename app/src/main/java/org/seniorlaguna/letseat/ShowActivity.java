package org.seniorlaguna.letseat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ShowActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    Integer mRecipeId;
    Recipe mRecipe;
    Menu mMenu;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mDatabaseHelper = new DatabaseHelper(this);
        mRecipeId = getIntent().getExtras().getInt(ModifyActivity.KEY_RECIPE_ID);
        mRecipe = mDatabaseHelper.getRecipe(mRecipeId);

        initToolbar();
        showRecipe();
    }

    @Override
    protected void onResume() {
        mRecipe = mDatabaseHelper.getRecipe(mRecipeId);
        initToolbar();
        showRecipe();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.show_toolbar);
        mToolbar.setTitle(mRecipe.mName);
        setSupportActionBar(mToolbar);
    }

    protected void showRecipe() {
        ((TextView) findViewById(R.id.show_recipe_duration)).setText(String.format("%s: %d min", getString(R.string.cooking_time), mRecipe.mDuration));
        ((TextView) findViewById(R.id.show_recipe_category)).setText(String.format("%s: %s", getString(R.string.category), mRecipe.mCategory));
        ((TextView) findViewById(R.id.show_recipe_difficulty)).setText(String.format("%s: %s", getString(R.string.difficulty), mRecipe.mDifficulty));

        showMainComponents();
        showIngredients();

        ((TextView) findViewById(R.id.show_recipe_description)).setText(mRecipe.mDescription);
        ((TextView) findViewById(R.id.show_recipe_notes)).setText(mRecipe.mNotes);
    }

    protected void showMainComponents() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.show_main_components_list);
        linearLayout.removeAllViews();

        if (mRecipe.mPotatoes) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.potatoes));
            linearLayout.addView(textView);
        }

        if (mRecipe.mRice) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.rice));
            linearLayout.addView(textView);
        }

        if (mRecipe.mNuddles) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.nuddles));
            linearLayout.addView(textView);
        }

        if (mRecipe.mBeef) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.beef));
            linearLayout.addView(textView);
        }

        if (mRecipe.mPork) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.pork));
            linearLayout.addView(textView);
        }

        if (mRecipe.mChicken) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.chicken));
            linearLayout.addView(textView);
        }

        if (mRecipe.mFish) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.fish));
            linearLayout.addView(textView);
        }

        if (mRecipe.mVegan) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.vegan));
            linearLayout.addView(textView);
        }

        if (mRecipe.mVegetarian) {
            TextView textView = new TextView(this);
            textView.setText(getString(R.string.vegetarian));
            linearLayout.addView(textView);
        }
    }

    protected void showIngredients() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.show_ingredients_list);
        linearLayout.removeAllViews();
        String[] ingredients = mRecipe.mIngredients.split("\n");

        for (String ingredient : ingredients) {
            TextView textView = new TextView(this);
            textView.setText(ingredient);
            linearLayout.addView(textView);
        }
    }

    protected void askForConfirmation() {
        mMenu.clear();
        getMenuInflater().inflate(R.menu.show_menu_delete, mMenu);
        mToolbar.setTitle(R.string.sure);
    }

    protected void deleteRecipe() {
        mDatabaseHelper.deleteRecipe(mRecipeId);
        Toast.makeText(this, R.string.recipe_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void shareRecipe() {
        try {
            String recipeEncoded = Utils.recipeToString(mRecipe, this);
            String msgText = String.format(getString(R.string.share_text), mRecipe.mName, recipeEncoded, getString(R.string.market_url));



            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msgText);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } catch (IOException e) {}
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.show_menu_delete:
                askForConfirmation();
                break;

            case R.id.show_menu_delete_no:
                initToolbar();
                break;

            case R.id.show_menu_delete_yes:
                deleteRecipe();
                break;

            case R.id.show_menu_edit:
                Intent intent = new Intent(this, ModifyActivity.class);
                intent.putExtra(ModifyActivity.KEY_RECIPE_ID, mRecipe.mId);
                startActivity(intent);
                break;

            case R.id.show_menu_share:
                shareRecipe();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
