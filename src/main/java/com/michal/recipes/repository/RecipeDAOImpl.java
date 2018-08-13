package com.michal.recipes.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.michal.recipes.entity.Recipe;

@Repository
public class RecipeDAOImpl implements RecipeDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Recipe> getRecipes() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<Recipe> query = currentSession.createQuery("from Recipe order by name", Recipe.class);
		List<Recipe> recipes = query.getResultList();
		
		return recipes;
	}
	
	@Override
	public Recipe getRecipe(int recipeId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Recipe recipe = currentSession.get(Recipe.class, recipeId);
		return recipe;
	}


	@Override
	public void saveRecipe(Recipe recipe) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.saveOrUpdate(recipe);
	}

	@Override
	public void deleteRecipe(int recipeId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("delete from Recipe where id=:recipeId");
		query.setParameter("recipeId", recipeId);
		query.executeUpdate();
	}

	@Override
	public Recipe updateRecipe(Recipe recipe, int recipeId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery("update Recipe set name=:recipeName, "
																+ "method=:methodName, "
																+ "ingredients=:ingredientsName "
																+ "where id=:recipeId");
		query.setParameter("recipeName", recipe.getName());
		query.setParameter("methodName", recipe.getMethod());
		query.setParameter("ingredientsName", recipe.getIngredients());
		query.setParameter("recipeId", recipeId);
		query.executeUpdate();
		return recipe;
	}


	

	
	
}
