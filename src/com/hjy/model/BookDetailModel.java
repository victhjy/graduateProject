package com.hjy.model;

import java.io.Serializable;

public class BookDetailModel implements Serializable{

	private String tags;//书本标签

	private String isbn10;//10位ISBN
	private String isbn13;

	private String title;

	private String pages;

	private String author;

	private String price;

	private String binding;

	private String publisher;

	private String pubdate;

	private String summary;

	private String imagePath;
	
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getIsbn10() {
		return isbn10;
	}

	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}

	public String getIsbn13() {
		return isbn13;
	}

	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	
	private String bid;

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}
	
//	private String bookName;
//	private String author;
//	private String press;
//	private String content;
//	private String price;
//	private String count;
//	private String category;
//	public String getCategory() {
//		return category;
//	}
//	public void setCategory(String category) {
//		this.category = category;
//	}
//	public String getBid() {
//		return bid;
//	}
//	public void setBid(String bid) {
//		this.bid = bid;
//	}
//	public String getBookName() {
//		return bookName;
//	}
//	public void setBookName(String bookName) {
//		this.bookName = bookName;
//	}
//	public String getAuthor() {
//		return author;
//	}
//	public void setAuthor(String author) {
//		this.author = author;
//	}
//	public String getPress() {
//		return press;
//	}
//	public void setPress(String press) {
//		this.press = press;
//	}
//	public String getContent() {
//		return content;
//	}
//	public void setContent(String content) {
//		this.content = content;
//	}
//	public String getPrice() {
//		return price;
//	}
//	public void setPrice(String price) {
//		this.price = price;
//	}
//	public String getCount() {
//		return count;
//	}
//	public void setCount(String count) {
//		this.count = count;
//	}
	
}
