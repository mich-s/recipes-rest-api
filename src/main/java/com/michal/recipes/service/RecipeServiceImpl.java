package com.michal.recipes.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.michal.recipes.entity.Recipe;
import com.michal.recipes.repository.RecipeDAO;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeDAO recipeDAO;
	
	@Override
	@Transactional
	public List<Recipe> getRecipes() {
		return recipeDAO.getRecipes();
	}

	@Override
	@Transactional
	public void saveRecipe(Recipe recipe) {
		recipeDAO.saveRecipe(recipe);
	}

	@Override
	@Transactional
	public Recipe getRecipe(int recipeId) {
		return recipeDAO.getRecipe(recipeId);
	}

	@Override
	@Transactional
	public void deleteRecipe(int recipeId) {
		recipeDAO.deleteRecipe(recipeId);
		
	}

	@Override
	@Transactional
	public Recipe updateRecipe(Recipe recipe, int recipeId) {
		return recipeDAO.updateRecipe(recipe, recipeId);
	}

	
}
