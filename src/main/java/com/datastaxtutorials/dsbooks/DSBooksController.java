package com.datastaxtutorials.dsbooks;

import com.datastax.astra.client.Collection;
import com.datastax.astra.client.DataAPIClient;
import com.datastax.astra.client.Database;
import com.datastax.astra.client.model.Document;
import com.datastax.astra.client.model.FindIterable;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.datastax.astra.client.model.Filters.eq;

@RequestMapping("/dsbooks")
@RestController
public class DSBooksController {
	
	static final String TOKEN = System.getenv("DB_APPLICATION_TOKEN");
	static final String API_ENDPOINT = System.getenv("DB_API_ENDPOINT");
	
	private Collection<Document> collection;
	
	public DSBooksController() {

		DataAPIClient client = new DataAPIClient(TOKEN);
		Database dbAPI = client.getDatabase(API_ENDPOINT, "default_namespace");
		collection = dbAPI.getCollection("dsbooks");
	}
	
	public Optional<Document> getOneBook(String bookId) {
		return collection.findById(bookId);
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<Optional<Document>> getBook(@PathVariable(value="id") String bookId) {
		
		Optional<Document> book = collection.findById(bookId);		
		
		if (book.isPresent()) {
			return ResponseEntity.ok(book);
		}
		return ResponseEntity.ok(Optional.ofNullable(null));
	}
	
	public FindIterable<Document> getAllBooks() {
		return collection.find();
	}
	
	@GetMapping("/books/")
	public ResponseEntity<FindIterable<Document>> getBooks() {
		
		FindIterable<Document> books = getAllBooks();
		
		return ResponseEntity.ok(books);
	}
	
	public FindIterable<Document> findByAuthor(String author) {
		return collection.find(eq("author",author));
	}
	
	@GetMapping("/albums/author/{author}")
	public ResponseEntity<FindIterable<Document>> getBooksByAuthor(@PathVariable(value="author") String author) {
		
		return ResponseEntity.ok(findByAuthor(author));
	}
	
	public FindIterable<Document> findByTitle(String title) {
		
		return collection.find(eq("title",title));
	}
	
	@GetMapping("/albums/title/{title}")
	public ResponseEntity<FindIterable<Document>> getBookByTitle(@PathVariable(value="title") String title) {
		
		return ResponseEntity.ok(findByTitle(title));
	}
}
