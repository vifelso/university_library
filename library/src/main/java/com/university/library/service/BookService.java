package com.university.library.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.library.model.Book;

public class BookService {

	private static String searchURL = "http://localhost:8080/book/search/findBooks?search=";

	public static ArrayList<Book> getBookList(String searchString) throws JsonProcessingException, IOException {
		ArrayList<Book> books = new ArrayList<Book>();
		if (searchString != null && !"".equals(searchString)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode root = mapper.readTree(new URL(searchURL + searchString));
			books = mapper.readValue(root.get("_embedded").get("Book").traverse(),
					new TypeReference<ArrayList<Book>>() {});
		}
		return books;
	}
}
