package org.seniorlaguna.letseat;

import android.provider.BaseColumns;

/**
 * Created by user on 08.07.17.
 */

public final class DatabaseStructure {

    private DatabaseStructure() {}

    public static final class TableRecipe implements BaseColumns {
        public static final String TABLE_NAME = "Recipe";

        public static final String COLOMN_NAME = "Name";
        public static final String COLOMN_DURATION = "Duration";
        public static final String COLOMN_DIFFICULTY = "Difficulty";
        public static final String COLOMN_CATEGORY = "Category";
        public static final String COLOMN_POTATOES = "Potatoes";
        public static final String COLOMN_NUDDLES = "Nuddles";
        public static final String COLOMN_RICE = "Rice";
        public static final String COLOMN_BEEF = "Beef";
        public static final String COLOMN_PORK = "Pork";
        public static final String COLOMN_CHICKEN = "Chicken";
        public static final String COLOMN_FISH = "Fish";
        public static final String COLOMN_VEGAN = "Vegan";
        public static final String COLOMN_VEGETARIAN = "Vegetarian";
        public static final String COLOMN_INGREDIENTS = "Ingredients";
        public static final String COLOMN_DESCRIPTION = "Description";
        public static final String COLOMN_NOTES = "Notes";

        public static final String SQL_CREATE_TABLE = String.format(

                "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s TEXT," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT" +
                ")"

        , TABLE_NAME, _ID, COLOMN_NAME, COLOMN_DURATION
        , COLOMN_DIFFICULTY, COLOMN_CATEGORY, COLOMN_POTATOES, COLOMN_NUDDLES
        , COLOMN_RICE, COLOMN_BEEF, COLOMN_PORK, COLOMN_CHICKEN
        , COLOMN_FISH, COLOMN_VEGAN, COLOMN_VEGETARIAN, COLOMN_INGREDIENTS
        , COLOMN_DESCRIPTION, COLOMN_NOTES);

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class TableCategory implements BaseColumns {
        public static final String TABLE_NAME = "Category";

        public static final String COLOMN_NAME = "Name";

        public static final String SQL_CREATE_TABLE = String.format(

                "CREATE TABLE %s (" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT" +
                ")"

                , TABLE_NAME, _ID, COLOMN_NAME);

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
