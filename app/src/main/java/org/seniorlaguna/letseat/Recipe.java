package org.seniorlaguna.letseat;

/**
 * Created by user on 08.07.17.
 */

public class Recipe {

    public Integer mId;
    public String mName;
    public Integer mDuration;
    public String mDifficulty;
    public String mCategory;
    public Boolean mPotatoes;
    public Boolean mNuddles;
    public Boolean mRice;
    public Boolean mBeef;
    public Boolean mPork;
    public Boolean mChicken;
    public Boolean mFish;
    public Boolean mVegan;
    public Boolean mVegetarian;
    public String mIngredients;
    public String mDescription;
    public String mNotes;

    public Recipe() {}

    public Recipe(String name, Integer duration, String difficulty, String category, Boolean potatoes,
                  Boolean nuddles, Boolean rice, Boolean beef, Boolean pork,
                  Boolean chicken, Boolean fish, Boolean vegan, Boolean vegetarian,
                  String ingredients, String description, String notes) {

        mName = name;
        mDuration = duration;
        mDifficulty = difficulty;
        mCategory = category;
        mPotatoes = potatoes;
        mNuddles = nuddles;
        mRice = rice;
        mBeef = beef;
        mPork = pork;
        mChicken = chicken;
        mFish = fish;
        mVegan = vegan;
        mVegetarian = vegetarian;
        mIngredients = ingredients;
        mDescription = description;
        mNotes = notes;

    }

}
