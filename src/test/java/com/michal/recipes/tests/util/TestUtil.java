package com.michal.recipes.tests.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

	private TestUtil() {
	}
	
	public static String asJsonString(final Object object) {
	    try {
	        return new ObjectMapper().writeValueAsString(object);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}  
}
