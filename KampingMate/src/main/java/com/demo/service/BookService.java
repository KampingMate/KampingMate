package com.demo.service;

import org.springframework.data.domain.Page;

import com.demo.domain.Book;

public interface BookService {
	
	public void insertBook(Book vo);
	
	public void updateBook(Book vo);
	
	public void deleteBook(Book vo);
	
	// 예약출력
	public Page<Book> getAllBook(int bookseq, int page, int size, int id); 
	
	public Book getBook(int bookseq); 
	
	//캠핑장명으로 검색
	public Page<Book> getBookByCamping(int bookseq, int page, int size, String campingname);
	
}
