package com.michal.recipes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.michal.recipes.entity.Recipe;
import com.michal.recipes.exception.RecipeNotFoundException;
import com.michal.recipes.service.RecipeService;

@RestController
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	
	@GetMapping("/recipes")
	public List<Recipe> getRecipes() {
		return recipeService.getRecipes();
	}
	
	@GetMapping("/recipes/{recipeId}")
	public Recipe getRecipe(@PathVariable int recipeId) {
		Recipe recipe = recipeService.getRecipe(recipeId);
		if (recipe == null) {
			throw new RecipeNotFoundException("Recipe id not found " + recipeId);
		}
		
		return recipe;
	}
	
	@PostMapping("/recipes")
	public Recipe saveRecipe(@RequestBody Recipe recipe) {
		recipe.setId(0);
		recipeService.saveRecipe(recipe);
		return recipe;
	}
	
	@PutMapping("/recipes/{recipeId}")
	public Recipe updateRecipe(@RequestBody Recipe recipe, @PathVariable int recipeId) {
		Recipe recipeFromDb = recipeService.getRecipe(recipeId);
		if (recipeFromDb == null) {
			throw new RecipeNotFoundException("Recipe id not found " + recipeId);
		}
		recipeService.updateRecipe(recipe, recipeId);
		return recipe;				
	}
	
	@DeleteMapping("/recipes/{recipeId}")
	public void deleteRecipe(@PathVariable int recipeId) {
		Recipe recipe = recipeService.getRecipe(recipeId);
		if (recipe == null) {
			throw new RecipeNotFoundException("Recipe id not found " + recipeId);
		}
		recipeService.deleteRecipe(recipeId);
	}
	
	
	
	
}
