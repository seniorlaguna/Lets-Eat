package org.seniorlaguna.letseat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    DatabaseHelper mDatabaseHelper;
    RecipeArrayAdapter mAdapter;

    Menu mMenu;
    Toolbar mToolbar;
    CharSequence mSelectedFilter = null;

    String[] mProjection = null;
    String mSelection = null;
    String[] mSelectionArgs = null;
    String mOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mDatabaseHelper = new DatabaseHelper(this);

        initToolbar();
        showList(mProjection, mSelection, mSelectionArgs, mOrder);
        mSelectedFilter = getString(R.string.no_category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList(mProjection, mSelection, mSelectionArgs, mOrder);
    }

    @Override
    protected void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.list_toolbar);
        mToolbar.setTitle(R.string.my_recipes);
        setSupportActionBar(mToolbar);
    }

    protected void showList(String[] projection, String selection, String[] selectionArgs, String order) {
        ArrayList<Recipe> recipes = mDatabaseHelper.getAllRecipes(projection, selection, selectionArgs, order);
        ListView listView = (ListView) findViewById(R.id.my_recipes_list);
        mAdapter = new RecipeArrayAdapter(this, recipes);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    protected void sortFromAToZ() {
        if (mOrder != null && mOrder.contains(DatabaseHelper.ASCENDING)) {
            mOrder = String.format("%s %s", DatabaseStructure.TableRecipe.COLOMN_NAME, DatabaseHelper.DESCENDING);
        }
        else {
            mOrder = String.format("%s %s", DatabaseStructure.TableRecipe.COLOMN_NAME, DatabaseHelper.ASCENDING);
        }
        showList(mProjection, mSelection, mSelectionArgs, mOrder);
    }

    protected void filterCategory(String sortOrder) {
        String filter = mSelectedFilter.toString();
        mToolbar.setTitle(filter);

        //cases

        if (filter.equals(getString(R.string.potatoes))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_POTATOES);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.nuddles))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_NUDDLES);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.rice))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_RICE);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.beef))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_BEEF);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.pork))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_PORK);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.chicken))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_CHICKEN);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.fish))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_FISH);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.vegan))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_VEGAN);
            mSelectionArgs = new String[] {"1"};
        }

        else if (filter.equals(getString(R.string.vegetarian))) {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN);
            mSelectionArgs = new String[] {"1"};
        }

        else {
            mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_CATEGORY);
            mSelectionArgs = new String[]{filter};
        }

        mOrder = String.format("%s %s", DatabaseStructure.TableRecipe.COLOMN_NAME, sortOrder);
        showList(mProjection, mSelection, mSelectionArgs, mOrder);
    }

    protected void openSearchBar() {
        mMenu.clear();
        getMenuInflater().inflate(R.menu.list_menu_search, mMenu);
        mToolbar.setTitle("");
        getLayoutInflater().inflate(R.layout.fragment_search_bar, mToolbar);

        EditText editText = (EditText) findViewById(R.id.list_search_query);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //bring focus on edittext and show keyboard
        editText.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
    }

    protected void closeSearchBar() {
        mToolbar.removeViewAt(1);
        initToolbar();
        mToolbar.inflateMenu(R.menu.list_menu);

        //reset sql search params
        mSelection = null;
        mSelectionArgs = null;
        showList(mProjection, mSelection, mSelectionArgs, mOrder);

        //hide keyboard
        Utils.hideKeyboard(this, mToolbar);
    }

    protected void search(String query) {
        mSelection = String.format("%s LIKE ?", DatabaseStructure.TableRecipe.COLOMN_NAME);
        mSelectionArgs = new String[] {"%" + query + "%"};
        showList(mProjection, mSelection, mSelectionArgs, mOrder);
    }

    protected void showFilterFragment() {
        final ArrayList<CharSequence> filters = mDatabaseHelper.getAllCategories();
        filters.add(getString(R.string.potatoes));
        filters.add(getString(R.string.rice));
        filters.add(getString(R.string.nuddles));
        filters.add(getString(R.string.beef));
        filters.add(getString(R.string.pork));
        filters.add(getString(R.string.chicken));
        filters.add(getString(R.string.fish));
        filters.add(getString(R.string.vegan));
        filters.add(getString(R.string.vegetarian));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.filter);
        builder.setCancelable(true);
        builder.setSingleChoiceItems(filters.toArray(new CharSequence[] {}), filters.indexOf(mSelectedFilter), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSelectedFilter = filters.get(i);
            }
        });

        builder.setPositiveButton(R.string.ascending, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filterCategory(DatabaseHelper.ASCENDING);
            }
        });

        builder.setNegativeButton(R.string.descending, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filterCategory(DatabaseHelper.DESCENDING);
            }
        });

        builder.create().show();
    }

    @Override
    public void onBackPressed() {

        if (mProjection == null && mSelection == null && mSelectionArgs == null && mOrder == null) {
            finish();
        }
        else {
            mProjection = null;
            mSelection = null;
            mSelectionArgs = null;
            mOrder = null;
            showList(mProjection, mSelection, mSelectionArgs, mOrder);
            mToolbar.setTitle(R.string.my_recipes);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Recipe recipe = mAdapter.getItem(i);

        Intent intent = new Intent(this, ShowActivity.class);
        intent.putExtra(ModifyActivity.KEY_RECIPE_ID, recipe.mId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.list_from_a_to_z:
                sortFromAToZ();
                break;

            case R.id.list_sort_by_category:
                showFilterFragment();
                break;

            case R.id.list_search:
                openSearchBar();
                break;

            case R.id.list_stop_search:
                closeSearchBar();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
