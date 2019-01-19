package de.tenminuteapps.letseat;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper mDatabaseHelper;
    AlertDialog mAlertDialog;
    ArrayList<CharSequence> mCategories;
    ArrayList<CharSequence> mToRemoveCategories = new ArrayList<>();
    Recipe mRecipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify);
        mDatabaseHelper = new DatabaseHelper(this);

        initToolbar();
        initDifficultySpinner();
        initCategorySpinner();
        initIngredientsList();
    }

    @Override
    protected void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.am_toolbar);
        toolbar.setTitle(R.string.title_add_recipe);
        setSupportActionBar(toolbar);
    }


    protected void initDifficultySpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.am_difficulty);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mRecipe.mDifficulty = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    protected void initCategorySpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.am_category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItem().toString().equals(getString(R.string.add_category_entry))) {
                    showAddCategoryFragment();
                }
                else if (spinner.getSelectedItem().toString().equals(getString(R.string.remove_category_entry))){
                    showRemoveCategoryFragment();
                }
                else {
                    mRecipe.mCategory = spinner.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mCategories = getAllCategories();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, mCategories);
        spinner.setAdapter(adapter);
    }

    protected void setCategory() {
        Spinner spinner = (Spinner) findViewById(R.id.am_category);
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        spinner.setSelection(adapter.getPosition(mRecipe.mCategory));
    }

    protected void initIngredientsList() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.am_ingredients_list);
        EditText editText = null;

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            editText = (EditText) linearLayout.getChildAt(i);
            editText.setHint(String.format("%s %d", getString(R.string.ingredient), i + 1));
        }
    }

    protected ArrayList<CharSequence> getAllCategories() {
        ArrayList<CharSequence> categories = mDatabaseHelper.getAllCategories();
        categories.add(getString(R.string.add_category_entry));
        categories.add(getString(R.string.remove_category_entry));

        return categories;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.am_minus:
                removeIngredientField();
                break;

            case R.id.am_plus:
                addIngredientField();
                break;
        }
    }

    protected void addIngredientField() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.am_ingredients_list);

        int children = linearLayout.getChildCount();

        EditText editText = new EditText(this);
        editText.setHint(String.format("%s %d", getString(R.string.ingredient), children + 1));
        linearLayout.addView(editText);
    }

    protected void removeIngredientField() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.am_ingredients_list);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            EditText editText = (EditText) linearLayout.getChildAt(0);
            String ingredient = editText.getText().toString();
            if (ingredient.isEmpty()) {
                linearLayout.removeViewAt(i);
                initIngredientsList();
                break;
            }
        }
    }

    protected void showAddCategoryFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_category);
        builder.setCancelable(true);
        builder.setView(R.layout.fragment_add_category);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addCategory();
                setCategory();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setCategory();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setCategory();
            }
        });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    protected void showRemoveCategoryFragment() {

        String selection = String.format("%s NOT LIKE ?", DatabaseStructure.TableCategory.COLOMN_NAME);
        String[] selectionArgs = new String[] {getString(R.string.no_category)};
        final ArrayList<CharSequence> categories = mDatabaseHelper.getAllCategories(selection, selectionArgs);

        mToRemoveCategories.clear();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remove_category);
        builder.setCancelable(true);

        builder.setMultiChoiceItems(categories.toArray(new CharSequence[] {}), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean checked) {
                if (checked) {
                    mToRemoveCategories.add(categories.get(i));
                }
                else {
                    mToRemoveCategories.remove(categories.get(i));
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeCategories();
            }
        });

        builder.create().show();
    }

    protected void addCategory() {
        EditText editText = (EditText) mAlertDialog.findViewById(R.id.fragment_add_category);
        String category = editText.getText().toString();

        //empty category
        if (category.isEmpty()) {
            return;
        }

        if (mDatabaseHelper.addCategory(category)) {
            int index = mCategories.size() - 2;
            mCategories.add(index, category);
            Spinner spinner = (Spinner) findViewById(R.id.am_category);
            ((ArrayAdapter<CharSequence>) spinner.getAdapter()).notifyDataSetChanged();
            mRecipe.mCategory = category;
        }
    }

    protected void removeCategories() {
        for (CharSequence charSequence : mToRemoveCategories) {
            mDatabaseHelper.deleteCategory(charSequence.toString());
        }
        initCategorySpinner();
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

        mDatabaseHelper.addRecipe(mRecipe);
        Toast.makeText(this, R.string.recipe_added, Toast.LENGTH_SHORT).show();
        finish();
    }

    protected String readIngredients() {
        StringBuilder stringBuilder = new StringBuilder();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.am_ingredients_list);
        EditText editText = null;
        String ingredient = null;

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            editText = (EditText) linearLayout.getChildAt(i);
            ingredient = editText.getText().toString();

            //field is empty
            if (ingredient.isEmpty()) {
                continue;
            }

            //not the first ingredient
            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append("\n");
            }

            stringBuilder.append(ingredient);

        }

        return stringBuilder.toString();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_menu_save:
                try {
                    addRecipe();
                } catch (Exception e) {
                    Toast.makeText(this, R.string.recipe_added_error, Toast.LENGTH_SHORT).show();
                }
        }

        return super.onOptionsItemSelected(item);
    }

}
