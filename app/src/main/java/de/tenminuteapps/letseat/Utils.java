package de.tenminuteapps.letseat;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static Charset CHAR_ENCODING = StandardCharsets.UTF_8;

    protected static void showKeyboard(Context context, View someView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(someView, 0);
    }

    protected static void hideKeyboard(Context context, View someView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(someView.getWindowToken(), 0);
    }

    protected static void setFocusOn(Activity activity, int resourceId) {
        activity.findViewById(resourceId).requestFocus();
    }

    protected static String recipeToString(Recipe recipe, Context context) throws IOException{
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        jsonWriter.beginObject();
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_NAME).value(recipe.mName);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_DURATION).value(recipe.mDuration);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_DIFFICULTY).value(recipe.mDifficulty);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_CATEGORY).value(context.getString(R.string.no_category));
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_POTATOES).value(recipe.mPotatoes);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_NUDDLES).value(recipe.mNuddles);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_RICE).value(recipe.mRice);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_BEEF).value(recipe.mBeef);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_PORK).value(recipe.mPork);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_CHICKEN).value(recipe.mChicken);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_FISH).value(recipe.mFish);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_VEGAN).value(recipe.mVegan);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN).value(recipe.mVegetarian);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_INGREDIENTS).value(recipe.mIngredients);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_DESCRIPTION).value(recipe.mDescription);
        jsonWriter.name(DatabaseStructure.TableRecipe.COLOMN_NOTES).value(recipe.mNotes);
        jsonWriter.endObject();

        String json = stringWriter.toString();
        byte[] base64 = Base64.encode(json.getBytes(CHAR_ENCODING), Base64.DEFAULT);
        return new String(base64, CHAR_ENCODING);
    }

    protected static Recipe stringToRecipe(String string) throws IOException {
        byte[] base64 = Base64.decode(string, Base64.DEFAULT);
        String json = new String(base64, CHAR_ENCODING);

        StringReader stringReader = new StringReader(json);
        JsonReader jsonReader = new JsonReader(stringReader);
        Recipe recipe = new Recipe();

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            switch (name) {

                case DatabaseStructure.TableRecipe.COLOMN_NAME:
                    recipe.mName = jsonReader.nextString();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_DURATION:
                    recipe.mDuration = jsonReader.nextInt();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_DIFFICULTY:
                    recipe.mDifficulty = jsonReader.nextString();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_CATEGORY:
                    recipe.mCategory = jsonReader.nextString();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_POTATOES:
                    recipe.mPotatoes = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_NUDDLES:
                    recipe.mNuddles = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_RICE:
                    recipe.mRice = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_BEEF:
                    recipe.mBeef = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_PORK:
                    recipe.mPork = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_CHICKEN:
                    recipe.mChicken = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_FISH:
                    recipe.mFish = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_VEGAN:
                    recipe.mVegan = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN:
                    recipe.mVegetarian = jsonReader.nextBoolean();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_INGREDIENTS:
                    recipe.mIngredients = jsonReader.nextString();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_DESCRIPTION:
                    recipe.mDescription = jsonReader.nextString();
                    break;

                case DatabaseStructure.TableRecipe.COLOMN_NOTES:
                    recipe.mNotes = jsonReader.nextString();
                    break;
            }
        }

        return recipe;
    }

}
