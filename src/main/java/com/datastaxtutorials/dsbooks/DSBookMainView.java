package com.datastaxtutorials.dsbooks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.datastax.astra.client.model.Document;
import com.datastax.astra.client.model.FindIterable;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route("")
public class DSBookMainView extends VerticalLayout {

	private static final long serialVersionUID = -7995326895715889791L;

	private ListBox<String> allTitles = new ListBox<>();
	private Image bookImage = new Image();
	private Paragraph author = new Paragraph();
	private Paragraph isbn = new Paragraph();
	private Paragraph link = new Paragraph();
	private H3 title = new H3();
	
	private DSBooksController controller;
	
	private Map<String,String> books;
	
	public DSBookMainView() {
		
		controller = new DSBooksController();

		add(new H1("Welcome to DSBooks"));

		books = new HashMap<String,String>();
		loadBooks();
		allTitles.getStyle().set("font", "10px");
		allTitles.getStyle().set("border", "2px solid Black");
		allTitles.addValueChangeListener(click -> {
			if (allTitles.getValue() != null) {
				String id = books.get(allTitles.getValue());
				viewBook(id);
			}
		});
		
		HorizontalLayout controlDataLayout = new HorizontalLayout();
		controlDataLayout.add(allTitles, bookComponents());
		
		add(controlDataLayout);
	}

	private void loadBooks() {
	
		FindIterable<Document> docs = controller.getAllBooks();
		
		for (Document book : docs) {
			String title = book.getString("title"); 
			books.put(title, book.getString("_id"));
		}
		
		allTitles.setItems(books.keySet());
	}
	
	private VerticalLayout bookComponents() {
		VerticalLayout returnVal = new VerticalLayout();
		HorizontalLayout bookImageAndData = new HorizontalLayout();
		VerticalLayout authorData = new VerticalLayout();
		
		bookImage.setHeight("100px");
		bookImage.getStyle().set("border", "1px solid Black");
		
		authorData.add(author);
		authorData.add(isbn);
		
		bookImageAndData.add(bookImage, authorData);
		returnVal.add(title, bookImageAndData);
		
		return returnVal;
	}
	
	private void viewBook(String id) {
		
		Optional<Document> book = controller.getOneBook(id);
		
		if (book.isPresent()) {
			title.setText(book.get().getString("title"));
			author.setText("author(s): " + book.get().getString("author"));
			isbn.setText("isbn: " + book.get().getString("isbn"));
			link.setText(book.get().getString("amazon_link"));
			
			String filename = book.get().getString("image");
			bookImage.setSrc(new StreamResource(filename, () -> getClass().getResourceAsStream("/images/" + book.get().getString("image"))));
		}
	}
}
