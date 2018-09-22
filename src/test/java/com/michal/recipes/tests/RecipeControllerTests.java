package com.michal.recipes.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.michal.recipes.config.AppConfig;
import com.michal.recipes.entity.Recipe;
import com.michal.recipes.tests.util.TestUtil;

@SpringJUnitWebConfig(AppConfig.class)
class RecipeControllerTests {
	
	private static final int RECIPE_ID = 2;
	private static final int NON_EXISTING_RECIPE_ID = 4;
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup(WebApplicationContext wac) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	void shouldReturnAllRecords() throws Exception {
		mockMvc.perform(get("/recipes"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
			   .andExpect(jsonPath("$").isArray());
	}
	
	@Test
	void shouldReturnSecondRecord() throws Exception{
		mockMvc.perform(get("/recipes/{id}", RECIPE_ID).accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
			   .andExpect(status().is2xxSuccessful())
			   .andExpect(jsonPath("$.id").isNumber())
			   .andExpect(jsonPath("$.name").value("Mango chutney"))
			   .andExpect(jsonPath("$.method").isNotEmpty())
			   .andExpect(jsonPath("$.ingredients").exists())
			   .andDo(print());
		
	}
	
	@Test
	void whenEnteringNonExistingRecipeIdShouldThrowException() throws Exception {
		mockMvc.perform(get("/recipes/{id}", NON_EXISTING_RECIPE_ID))
//			   .andDo(print())
			   .andExpect(status().is4xxClientError())
			   .andExpect(jsonPath("$.status", equalTo(404)))
			   .andExpect(jsonPath("$.message").value("Recipe id not found " + NON_EXISTING_RECIPE_ID))
			   .andExpect(jsonPath("$.timestamp").isNotEmpty());
	}
	
	@Test
	void whenEnteringNotANumberShouldThrowException() throws Exception {
		mockMvc.perform(get("/recipes/abc123"))
			   .andExpect(status().is4xxClientError())
			   .andExpect(jsonPath("$.status", equalTo(400)))
			   .andExpect(jsonPath("$.message").exists())
			   .andExpect(jsonPath("$.timestamp").isNotEmpty());
	}
	
	@Test
	@Disabled
	void shouldReturnSortedRecipes() throws Exception{
		mockMvc.perform(get("/recipes"))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$[0].name").value("Easy white bread"))
				.andExpect(jsonPath("$[1].name").value("Mango chutney"))
				.andExpect(jsonPath("$[2].name").value("Smoky aubergine & red pepper salad"));
	}
	
	
	@Test
	@Transactional
	@Disabled
	void shouldAddNewRecipe() throws Exception{
		Recipe recipe = new Recipe();
		
		mockMvc.perform(post("/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.asJsonString(recipe))
				)
		   .andExpect(status().isOk());
	}
	
}
