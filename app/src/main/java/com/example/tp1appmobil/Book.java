package com.example.tp1appmobil;
public class Book {
    private long id; // Use long for SQLite ID
    private String title;
    private String author;
    private String isbn;
    private String status; // e.g., "Available", "Reserved"
    private String description;
    // Add other fields like coverImageUrl if needed

    // Constants for Status
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_RESERVED = "Reserved";

    // Constructor
    public Book(long id, String title, String author, String isbn, String status, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.description = description;
    }

    // Constructor without ID (for inserting new books)
    public Book(String title, String author, String isbn, String status, String description) {
        this(-1, title, author, isbn, status, description); // Use -1 or 0 to indicate no ID yet
    }

    // --- Getters ---
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    // --- Setters ---
    // ID is usually set by the database or constructor, so a setter might not be needed
    // public void setId(long id) { this.id = id; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}