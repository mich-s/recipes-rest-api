package com.michal.recipes.exception;

public class RecipeNotFoundException extends RuntimeException {

	public RecipeNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RecipeNotFoundException(String arg0) {
		super(arg0);
	}

	public RecipeNotFoundException(Throwable arg0) {
		super(arg0);
	}

	
	
}
