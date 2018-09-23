package com.michal.recipes.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.michal.recipes.config.AppConfig;
import com.michal.recipes.entity.Recipe;
import com.michal.recipes.repository.RecipeDAO;

@SpringJUnitWebConfig(AppConfig.class)
@Transactional
@TestPropertySource("classpath:persistence-h2.properties")
public class RecipeJpaIntegrationTests extends AbstractTransactionalJUnit4SpringContextTests {

	private static int RECIPE_ID = 1;
	
	@Autowired
	private RecipeDAO repo;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Recipe r1;
	private Recipe r2;
	
	public RecipeJpaIntegrationTests() {
		r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		r2 = new Recipe.RecipeBuilder().name("n2").method("m2").ingredients("i2").build();
	}
	
	
	@Test
	void shouldReturnAllRecipes() {
		repo.saveRecipe(r1);
		repo.saveRecipe(r2);
		
		assertThat(countRowsInTable("recipe"), equalTo(2));
		
		// Manual flush is required to avoid false positive in test - via docs
		sessionFactory.getCurrentSession().flush();
	}

	@Test
	void shouldReturnRecipeByGivenId() {
		repo.saveRecipe(r1);
		
		Recipe recipe = repo.getRecipe(RECIPE_ID);
		
		assertAll(
				() -> assertEquals("n1", recipe.getName()),
				() -> assertEquals("m1", recipe.getMethod()),
				() -> assertEquals("i1", recipe.getIngredients())
		);
		
		sessionFactory.getCurrentSession().flush();
	}
	
	
	@Test
	void shouldUpdateRecipe() {
		repo.saveRecipe(r1);
		repo.saveRecipe(r2);
		
		Recipe updatedRecipe = repo.updateRecipe(r2, RECIPE_ID);
		
		assertAll(
				() -> assertEquals("n2", updatedRecipe.getName()),
				() -> assertEquals("m2", updatedRecipe.getMethod()),
				() -> assertEquals("i2", updatedRecipe.getIngredients())
		);	
		
		sessionFactory.getCurrentSession().flush();
	}
	
	
	@Test
	void shouldDeleteRecipe() {
		repo.saveRecipe(r1);
		
		assertFalse(repo.getRecipes().isEmpty());
		
		repo.deleteRecipe(r1.getId());
		
		assertTrue(repo.getRecipes().isEmpty());
		
		sessionFactory.getCurrentSession().flush();
	}
	
}
