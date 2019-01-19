package de.tenminuteapps.letseat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper mDatabaseHelper;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        mDatabaseHelper = new DatabaseHelper(this);
        initToolbar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.import_from_base64_button:
                importRecipeFromBase64();
                break;

            case R.id.import_from_chefkoch_button:
                importRecipeFromChefkoch();
                break;
        }
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.import_toolbar);
        mToolbar.setTitle(getString(R.string.title_import_recipe));
        setSupportActionBar(mToolbar);
    }

    protected void importRecipeFromBase64() {

        try {
            EditText editText = (EditText) findViewById(R.id.import_edittext_base64);
            String text = editText.getText().toString();

            String[] parts = text.split("\n\n");

            //try to find the encoded recipe
            for (String part : parts) {
                try {
                    //parse recipe out of text
                    Recipe recipe = Utils.stringToRecipe(part);
                    int id = (int) mDatabaseHelper.addRecipe(recipe);

                    //show recipe
                    Intent intent = new Intent(this, ModifyActivity.class);
                    intent.putExtra(ModifyActivity.KEY_RECIPE_ID, id);
                    startActivity(intent);
                    return;
                } catch (Exception e) {}

            }

            Toast.makeText(this, R.string.import_error, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {}

    }

    protected void importRecipeFromChefkoch() {
        Toast.makeText(this, R.string.apologize, Toast.LENGTH_SHORT).show();
    }
}
