package org.seniorlaguna.letseat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class ModifyActivity extends AddActivity {

    public static final String KEY_RECIPE_ID = "recipe_id";

    Integer mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipeId = getIntent().getExtras().getInt(KEY_RECIPE_ID);
        mRecipe = mDatabaseHelper.getRecipe(mRecipeId);

        initIngredientsList();
        showRecipe();
    }

    @Override
    protected void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    @Override
    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.am_toolbar);
        toolbar.setTitle(R.string.title_modify_recipe);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initIngredientsList() {

        //no ingredients
        if (mRecipe.mIngredients == null) {
            return;
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.am_ingredients_list);
        String[] ingredients = mRecipe.mIngredients.split("\n");
        EditText editText = null;

        //remove empty fields
        int childCount = linearLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            try {
                editText = (EditText) linearLayout.getChildAt(0);
                linearLayout.removeViewAt(0);
            } catch (ClassCastException e) {
                break;
            }
        }

        //add ingredients
        for (int i = 0; i < ingredients.length; i++) {
            editText = new EditText(this);

            //empty string
            if (ingredients[i].isEmpty()) {
                continue;
            }

            editText.setText(ingredients[i]);
            editText.setHint(String.format("%s %d", getString(R.string.ingredient), i+1));
            linearLayout.addView(editText, i);
        }
    }

    protected void showRecipe() {
        ((EditText) findViewById(R.id.am_name)).setText(mRecipe.mName);
        ((EditText) findViewById(R.id.am_duration)).setText(Integer.toString(mRecipe.mDuration));
        ((CheckBox) findViewById(R.id.am_potatoes)).setChecked(mRecipe.mPotatoes);
        ((CheckBox) findViewById(R.id.am_nuddles)).setChecked(mRecipe.mNuddles);
        ((CheckBox) findViewById(R.id.am_rice)).setChecked(mRecipe.mRice);
        ((CheckBox) findViewById(R.id.am_beef)).setChecked(mRecipe.mBeef);
        ((CheckBox) findViewById(R.id.am_pork)).setChecked(mRecipe.mPork);
        ((CheckBox) findViewById(R.id.am_chicken)).setChecked(mRecipe.mChicken);
        ((CheckBox) findViewById(R.id.am_fish)).setChecked(mRecipe.mFish);
        ((CheckBox) findViewById(R.id.am_vegan)).setChecked(mRecipe.mVegan);
        ((CheckBox) findViewById(R.id.am_vegetarian)).setChecked(mRecipe.mVegetarian);
        ((EditText) findViewById(R.id.am_description_input)).setText(mRecipe.mDescription);
        ((EditText) findViewById(R.id.am_notes_input)).setText(mRecipe.mNotes);

        setDifficulty();
        setCategory();
    }

    protected void setDifficulty() {
        Spinner spinner = (Spinner) findViewById(R.id.am_difficulty);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        spinner.setSelection(adapter.getPosition(mRecipe.mDifficulty));
    }

    protected void addRecipe() {
        mRecipe.mName = ((EditText) findViewById(R.id.am_name)).getText().toString();

        //empty name
        if (mRecipe.mName.isEmpty()) {
            throw new RuntimeException();
        }

        mRecipe.mDuration = Integer.parseInt(((EditText) findViewById(R.id.am_duration)).getText().toString());
        mRecipe.mPotatoes = ((CheckBox) findViewById(R.id.am_potatoes)).isChecked();
        mRecipe.mNuddles = ((CheckBox) findViewById(R.id.am_nuddles)).isChecked();
        mRecipe.mRice = ((CheckBox) findViewById(R.id.am_rice)).isChecked();
        mRecipe.mBeef = ((CheckBox) findViewById(R.id.am_beef)).isChecked();
        mRecipe.mPork = ((CheckBox) findViewById(R.id.am_pork)).isChecked();
        mRecipe.mChicken = ((CheckBox) findViewById(R.id.am_chicken)).isChecked();
        mRecipe.mFish = ((CheckBox) findViewById(R.id.am_fish)).isChecked();
        mRecipe.mVegan = ((CheckBox) findViewById(R.id.am_vegan)).isChecked();
        mRecipe.mVegetarian = ((CheckBox) findViewById(R.id.am_vegetarian)).isChecked();
        mRecipe.mIngredients = readIngredients();
        mRecipe.mDescription = ((EditText) findViewById(R.id.am_description_input)).getText().toString();
        mRecipe.mNotes = ((EditText) findViewById(R.id.am_notes_input)).getText().toString();

        mDatabaseHelper.updateRecipe(mRecipeId, mRecipe);
        Toast.makeText(this, R.string.recipe_modified, Toast.LENGTH_SHORT).show();
        finish();
    }

}
