package com.university.library.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.university.library.model.Book;
import com.university.library.model.User;
import com.university.library.repository.UserRepository;
import com.university.library.service.BookService;

@Controller
public class BookController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = { "/books" })
	public String openPage(Model model, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
		String searchString = request.getParameter("search");
		ArrayList<Book> books = BookService.getBookList(searchString);
		model.addAttribute("searchBooks", books);
		ArrayList<String> userBooks = getCurrentUserBookIds();
		model.addAttribute("userBooks", userBooks);
		model.addAttribute("searchString", searchString);
		return "books";
	}

	@RequestMapping(value = { "/placeorder" })
	public String placeOrder(Model model, HttpServletRequest request, HttpServletResponse response) {
		String bookId = request.getParameter("bookid");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getName());
		List<Book> userBooks = user.getBooks();
		if (userBooks == null) {
			userBooks = new ArrayList<Book>();
		}
		Book book = new Book();
		book.setId(bookId);
		userBooks.add(book);
		user.setBooks(userBooks);
		userRepository.save(user);
		return "books :: #" + bookId;
	}

	@RequestMapping(value = { "/cancelorder" })
	public String cancelOrder(Model model, HttpServletRequest request, HttpServletResponse response) {
		String bookId = request.getParameter("bookid");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getName());
		List<Book> userBooks = user.getBooks();
		for (Book book : userBooks) {
			if (book.getId().equals(bookId)) {
				userBooks.remove(book);
				break;
			}
		}

		userRepository.save(user);
		model.addAttribute("userBooks", userBooks);
		return "demands :: #book-orders";
	}

	private ArrayList<String> getCurrentUserBookIds() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUsername(auth.getName());
		List<Book> userBooks = user.getBooks();
		ArrayList<String> userBooksIds = new ArrayList<String>();
		if (userBooks != null) {
			for (Book book : userBooks) {
				userBooksIds.add(book.getId());
			}
		}
		return userBooksIds;
	}
	
}
