package com.michal.recipes.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.michal.recipes.controller.RecipeController;
import com.michal.recipes.entity.Recipe;
import com.michal.recipes.exception.RecipeExceptionHandler;
import com.michal.recipes.service.RecipeService;
import com.michal.recipes.tests.util.TestUtil;

/*Use '@ExtendWith(MockitoExtension.class' or 'MockitoAnnotations.initMocks(this);' in the setup() method 
 * to initialize mocks*/
//@ExtendWith(MockitoExtension.class)
class RecipeControllerStandaloneSetupTests {
	
	/*Use @Mock annotation or 'recipeService = mock(RecipeService.class);' in the setup() method*/
	@Mock
	private RecipeService recipeService;
	
	@InjectMocks
	private RecipeController recipeController;
	
	private MockMvc mockMvc;
	
//	@Mock
//	private RecipeExceptionHandler recipeExceptionHandler;
	
	
	@BeforeEach
	void setup() {
//		recipeService = mock(RecipeService.class);
		MockitoAnnotations.initMocks(this);
		/* or -> MockMvcBuilders.standaloneSetup(new RecipeController(recipeService) (...) */
		mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
								 .setControllerAdvice(new RecipeExceptionHandler())
								 .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
//								 .alwaysExpect(status().isOk())
								 .build();
	}
	
	
	@Test
	void shouldGetAllRecipes() throws Exception{
		Recipe r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		Recipe r2 = new Recipe.RecipeBuilder().name("n2").method("m2").ingredients("i2").build();
		List<Recipe> recipes = Arrays.asList(r1, r2);
		
		given(recipeService.getRecipes()).willReturn(recipes);
		
		mockMvc.perform(get("/recipes"))
			   .andExpect(jsonPath("$", hasSize(2)))
			   .andExpect(jsonPath("$[0].name", is("n1")))
			   .andExpect(jsonPath("$[0].method", is("m1")))
			   .andExpect(jsonPath("$[0].ingredients", is("i1")))
			   .andExpect(jsonPath("$[1].name").value("n2"))
			   .andExpect(jsonPath("$[1].method").value("m2"))
			   .andExpect(jsonPath("$[1].ingredients").value("i2"));
		
		verify(recipeService, times(1)).getRecipes();
		verifyNoMoreInteractions(recipeService);
	}
	
	@Test
	void shouldGetRecipeById() throws Exception {
		Recipe r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		
		when(recipeService.getRecipe(1)).thenReturn(r1);
		
		mockMvc.perform(get("/recipes/{id}", 1))
		   .andExpect(jsonPath("$.name").value("n1"))
		   .andExpect(jsonPath("$.method").value("m1"))
		   .andExpect(jsonPath("$.ingredients").value("i1"));
		   
		verify(recipeService, times(1)).getRecipe(1);
		verifyNoMoreInteractions(recipeService);
	}
	
	@Test
	void shouldDelegateExceptionToRecipeExceptionHandlerWhenRecipeIsNull() throws Exception{
		when(recipeService.getRecipe(1)).thenReturn(null);
		
		mockMvc.perform(get("/recipes/{id}", 1))
		   .andExpect(status().isNotFound())
		   .andExpect(content().string(containsString("Recipe id not found 1")))
		   .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		   .andDo(print());
		
		verify(recipeService, times(1)).getRecipe(1);
		verifyNoMoreInteractions(recipeService);
	}
	
	
	@Test
	void shouldCreateNewRecipe() throws Exception {
		Recipe r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		String recipeJson = TestUtil.asJsonString(r1);
		
		doNothing().when(recipeService).saveRecipe(r1);
		
		mockMvc.perform(post("/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(recipeJson)
			).andDo(print());
		
		verify(recipeService, times(1)).saveRecipe(r1);
		verifyNoMoreInteractions(recipeService);
	}
	
	@Test
	void shouldUpdateExistingRecipeByGivenId() throws Exception {
		Recipe r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		String recipeJson = TestUtil.asJsonString(r1);
		
		when(recipeService.getRecipe(1)).thenReturn(r1);

		mockMvc.perform(put("/recipes/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(recipeJson)
			);
		
		verify(recipeService, times(1)).getRecipe(1);
		verify(recipeService, times(1)).updateRecipe(r1, 1);
		verifyNoMoreInteractions(recipeService);
	}
	
	@Test
	void shouldDeleteExistingRecipeByGivenId() throws Exception {
		Recipe r1 = new Recipe.RecipeBuilder().name("n1").method("m1").ingredients("i1").build();
		
		when(recipeService.getRecipe(1)).thenReturn(r1);
		doNothing().when(recipeService).deleteRecipe(1);
		
		mockMvc.perform(delete("/recipes/{id}", 1))
				.andExpect(status().isOk());
		
		verify(recipeService, times(1)).getRecipe(1);
		verify(recipeService, times(1)).deleteRecipe(1);
		verifyNoMoreInteractions(recipeService);
	}

}
