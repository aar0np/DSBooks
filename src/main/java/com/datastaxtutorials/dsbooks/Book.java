package com.datastaxtutorials.dsbooks;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
	
	@JsonProperty("_id")
	private UUID id;
	private String title;
	private String author;
	private String image;
	private String publisher;
	@JsonProperty("amazon_link")
	private String amazonLink;
	private String isbn;
	
//  "_id": "98d26a5a-fe89-49f6-926a-5afe8919f61e",
//  "title": "Code with Java 21",
//  "author": "Aaron Ploetz",
//  "image": "java_book_cover.png",
//  "publisher": "BPB",
//  "amazon_link": "https://www.amazon.com/dp/B0CS3KZTQ9/ref=sr_1_3",
//  "isbn": "978-9355519993"
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getAmazonLink() {
		return amazonLink;
	}
	
	public void setAmazonLink(String amazonLink) {
		this.amazonLink = amazonLink;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}	
}
