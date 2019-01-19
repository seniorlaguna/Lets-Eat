package de.tenminuteapps.letseat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "letseat.db";
    public static final Integer DB_VERSION = 1;

    public static final String DESCENDING = "DESC";
    public static final String ASCENDING = "ASC";

    protected Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseStructure.TableRecipe.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseStructure.TableCategory.SQL_CREATE_TABLE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseStructure.TableCategory.COLOMN_NAME, mContext.getString(R.string.no_category));
        sqLiteDatabase.insert(DatabaseStructure.TableCategory.TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long addRecipe(Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //make first letter in name uppercase
        Character character = recipe.mName.charAt(0);
        character = Character.toUpperCase(character);
        recipe.mName = character.toString() + recipe.mName.subSequence(1, recipe.mName.length());

        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NAME, recipe.mName);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DURATION, recipe.mDuration);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DIFFICULTY, recipe.mDifficulty);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_CATEGORY, recipe.mCategory);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_POTATOES, recipe.mPotatoes);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NUDDLES, recipe.mNuddles);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_RICE, recipe.mRice);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_BEEF, recipe.mBeef);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_PORK, recipe.mPork);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_CHICKEN, recipe.mChicken);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_FISH, recipe.mFish);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_VEGAN, recipe.mVegan);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN, recipe.mVegetarian);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_INGREDIENTS, recipe.mIngredients);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DESCRIPTION, recipe.mDescription);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NOTES, recipe.mNotes);

        return sqLiteDatabase.insert(DatabaseStructure.TableRecipe.TABLE_NAME, null, contentValues);
    }

    public void updateRecipe(int id, Recipe recipe) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String whereClause = DatabaseStructure.TableRecipe._ID + " LIKE ?";
        String[] whereArgs = {Integer.toString(id)};

        //make first letter in name uppercase
        Character character = recipe.mName.charAt(0);
        character = Character.toUpperCase(character);
        recipe.mName = character.toString() + recipe.mName.subSequence(1, recipe.mName.length());

        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NAME, recipe.mName);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DURATION, recipe.mDuration);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DIFFICULTY, recipe.mDifficulty);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_CATEGORY, recipe.mCategory);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_POTATOES, recipe.mPotatoes);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NUDDLES, recipe.mNuddles);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_RICE, recipe.mRice);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_BEEF, recipe.mBeef);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_PORK, recipe.mPork);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_CHICKEN, recipe.mChicken);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_FISH, recipe.mFish);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_VEGAN, recipe.mVegan);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN, recipe.mVegetarian);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_INGREDIENTS, recipe.mIngredients);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_DESCRIPTION, recipe.mDescription);
        contentValues.put(DatabaseStructure.TableRecipe.COLOMN_NOTES, recipe.mNotes);

        sqLiteDatabase.update(DatabaseStructure.TableRecipe.TABLE_NAME, contentValues, whereClause, whereArgs);
    }

    public void deleteRecipe(int id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = DatabaseStructure.TableRecipe._ID + " LIKE ?";
        String[] whereArgs = {Integer.toString(id)};

        sqLiteDatabase.delete(DatabaseStructure.TableRecipe.TABLE_NAME, whereClause, whereArgs);
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String whereClause = DatabaseStructure.TableRecipe._ID + " LIKE ?";
        String[] whereArgs = {Integer.toString(id)};

        Cursor cursor = sqLiteDatabase.query(DatabaseStructure.TableRecipe.TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        if (cursor.moveToNext()) {
            Recipe recipe = readCursorRecipe(cursor);

            return recipe;
        }
        cursor.close();

        return null;
    }

    public ArrayList<Recipe> getAllRecipes(String[] projection, String selection, String[] selectionArgs, String order) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DatabaseStructure.TableRecipe.TABLE_NAME, projection, selection, selectionArgs, null, null, order);
        ArrayList<Recipe> recipes = new ArrayList<>();

        while (cursor.moveToNext()) {
            recipes.add(readCursorRecipe(cursor));
        }
        cursor.close();

        return recipes;
    }

    public boolean addCategory(String name) {

        if (containsCategory(name)) {
            return false;
        }

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseStructure.TableCategory.COLOMN_NAME, name);

        sqLiteDatabase.insert(DatabaseStructure.TableCategory.TABLE_NAME, null, contentValues);
        return true;
    }

    public void deleteCategory(String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = DatabaseStructure.TableCategory.COLOMN_NAME + " LIKE ?";
        String[] whereArgs = {category};

        sqLiteDatabase.delete(DatabaseStructure.TableCategory.TABLE_NAME, whereClause, whereArgs);

        whereClause = DatabaseStructure.TableRecipe.COLOMN_CATEGORY + " LIKE ?";
        ArrayList<Recipe> recipes = getAllRecipes(null, whereClause, whereArgs, null);
        for (Recipe recipe : recipes) {
            recipe.mCategory = mContext.getString(R.string.no_category);
            updateRecipe(recipe.mId, recipe);
        }
    }

    public ArrayList<CharSequence> getAllCategories(String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DatabaseStructure.TableCategory.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        ArrayList<CharSequence> categories = new ArrayList<>();

        while (cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableCategory.COLOMN_NAME)));
        }
        cursor.close();

        return categories;
    }

    public ArrayList<CharSequence> getAllCategories() {
        return getAllCategories(null, null);
    }

    public void clearRecipeTable() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DatabaseStructure.TableRecipe.SQL_DELETE_ENTRIES);
    }

    public void clearCategoryTable() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DatabaseStructure.TableCategory.SQL_DELETE_ENTRIES);
    }

    private Recipe readCursorRecipe(Cursor cursor) {
        Recipe recipe = new Recipe();

        recipe.mId = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe._ID));
        recipe.mName = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_NAME));
        recipe.mDuration = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_DURATION));
        recipe.mDifficulty = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_DIFFICULTY));
        recipe.mCategory = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_CATEGORY));
        recipe.mPotatoes = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_POTATOES)) == 1;
        recipe.mNuddles = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_NUDDLES)) == 1;
        recipe.mRice = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_RICE)) == 1;
        recipe.mBeef = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_BEEF)) == 1;
        recipe.mPork = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_PORK)) == 1;
        recipe.mChicken = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_CHICKEN)) == 1;
        recipe.mFish = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_FISH)) == 1;
        recipe.mVegan = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_VEGAN)) == 1;
        recipe.mVegetarian = cursor.getInt(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_VEGETARIAN)) == 1;
        recipe.mIngredients = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_INGREDIENTS));
        recipe.mDescription = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_DESCRIPTION));
        recipe.mNotes = cursor.getString(cursor.getColumnIndex(DatabaseStructure.TableRecipe.COLOMN_NOTES));

        return recipe;
    }

    private boolean containsCategory(String category) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String selection = String.format("%s LIKE ?", DatabaseStructure.TableCategory.COLOMN_NAME);
        String[] selectionArgs = new String[] {category};

        Cursor cursor = sqLiteDatabase.query(DatabaseStructure.TableCategory.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        return cursor.moveToNext();
    }
}
