package com.michal.recipes.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.michal.recipes.config.AppConfig;
import com.michal.recipes.entity.Recipe;
import com.michal.recipes.tests.util.TestUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= AppConfig.class)
@WebAppConfiguration
public class RecipeControllerDifferentConfigTests {

	private static int DELETE_RECIPE_ID = 2;
	private static int UPDATE_RECIPE_ID = 3;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
//									.alwaysDo(print())
									.build();
	}
	
	@Test
	@Transactional
	void shouldCreateNewRecipe() throws Exception {
		Recipe recipe = new  Recipe.RecipeBuilder()
									.name("test name")
									.method("test method")
									.ingredients("test ingredients")
									.build();
		
		String recipeJson = TestUtil.asJsonString(recipe);
		
		mockMvc.perform(post("/recipes")
					.contentType(MediaType.APPLICATION_JSON)
					.content(recipeJson)
					)
			   .andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	void shouldUpdateExistingRecipeByGivenId() throws Exception {
		Recipe recipe = new  Recipe.RecipeBuilder()
									.name("test name")
									.method("test method")
									.ingredients("test ingredients")
									.build();
		String recipeJson = TestUtil.asJsonString(recipe);
		
		mockMvc.perform(put("/recipes/{id}", UPDATE_RECIPE_ID)
					.contentType(MediaType.APPLICATION_JSON)
					.content(recipeJson)
					)
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	@Transactional
	void shouldDeleteRecipe() throws Exception {
		mockMvc.perform(delete("/recipes/{id}", DELETE_RECIPE_ID))
				.andExpect(status().isOk());
	}
	
	
}
