package com.michal.recipes.service;

import java.util.List;

import com.michal.recipes.entity.Recipe;

public interface RecipeService {

	public List<Recipe> getRecipes();

	public void saveRecipe(Recipe recipe);

	public Recipe getRecipe(int recipeId);

	public void deleteRecipe(int recipeId);

	public Recipe updateRecipe(Recipe recipe, int recipeId);
}
