package com.example.tp1appmobil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Database Info
    private static final String DATABASE_NAME = "universityApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_BOOKS = "books";

    // Books Table Columns
    private static final String KEY_BOOK_ID = "id";
    private static final String KEY_BOOK_TITLE = "title";
    private static final String KEY_BOOK_AUTHOR = "author";
    private static final String KEY_BOOK_ISBN = "isbn";
    private static final String KEY_BOOK_STATUS = "status";
    private static final String KEY_BOOK_DESCRIPTION = "description";

    // Singleton instance (optional but recommended)
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS +
                "(" +
                KEY_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define ID as auto-incrementing PK
                KEY_BOOK_TITLE + " TEXT," +
                KEY_BOOK_AUTHOR + " TEXT," +
                KEY_BOOK_ISBN + " TEXT," +
                KEY_BOOK_STATUS + " TEXT," +
                KEY_BOOK_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(CREATE_BOOKS_TABLE);
        Log.i(TAG, "Books table created.");

        // Optional: Pre-populate with some sample data
        addSampleBooks(db);
    }

    // Called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
            onCreate(db);
        }
    }

    // --- Book Table Operations ---

    // Insert a book into the database
    public long addBook(Book book) {
        SQLiteDatabase db = getWritableDatabase();
        long insertedId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_BOOK_TITLE, book.getTitle());
            values.put(KEY_BOOK_AUTHOR, book.getAuthor());
            values.put(KEY_BOOK_ISBN, book.getIsbn());
            values.put(KEY_BOOK_STATUS, book.getStatus());
            values.put(KEY_BOOK_DESCRIPTION, book.getDescription());

            // Note: KEY_BOOK_ID is auto-incrementing, so we don't put it here

            insertedId = db.insertOrThrow(TABLE_BOOKS, null, values);
            db.setTransactionSuccessful();
            Log.i(TAG, "Book added with ID: " + insertedId);
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add book to database", e);
        } finally {
            db.endTransaction();
            // db.close(); // Don't close here if using singleton instance
        }
        return insertedId; // Returns the row ID of the newly inserted row, or -1 if an error occurred
    }

    // Get all books from database
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // Query: SELECT * FROM books ORDER BY title ASC
        String SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s ASC", TABLE_BOOKS, KEY_BOOK_TITLE);

        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(KEY_BOOK_ID);
                    int titleIndex = cursor.getColumnIndex(KEY_BOOK_TITLE);
                    int authorIndex = cursor.getColumnIndex(KEY_BOOK_AUTHOR);
                    int isbnIndex = cursor.getColumnIndex(KEY_BOOK_ISBN);
                    int statusIndex = cursor.getColumnIndex(KEY_BOOK_STATUS);
                    int descIndex = cursor.getColumnIndex(KEY_BOOK_DESCRIPTION);

                    // Check if columns exist before accessing them
                    if (idIndex != -1 && titleIndex != -1 && authorIndex != -1 &&
                            isbnIndex != -1 && statusIndex != -1 && descIndex != -1)
                    {
                        long id = cursor.getLong(idIndex);
                        String title = cursor.getString(titleIndex);
                        String author = cursor.getString(authorIndex);
                        String isbn = cursor.getString(isbnIndex);
                        String status = cursor.getString(statusIndex);
                        String description = cursor.getString(descIndex);

                        Book book = new Book(id, title, author, isbn, status, description);
                        books.add(book);
                    } else {
                        Log.w(TAG, "One or more book columns not found in cursor.");
                    }

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to get books from database", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            // db.close(); // Don't close here if using singleton instance
        }
        Log.i(TAG, "Fetched " + books.size() + " books.");
        return books;
    }

    // Update book status (Example)
    public int updateBookStatus(long bookId, String newStatus) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = 0;

        ContentValues values = new ContentValues();
        values.put(KEY_BOOK_STATUS, newStatus);

        db.beginTransaction();
        try {
            rowsAffected = db.update(TABLE_BOOKS, values, KEY_BOOK_ID + "= ?", new String[]{String.valueOf(bookId)});
            db.setTransactionSuccessful();
            Log.i(TAG, "Updated status for book ID " + bookId + ". Rows affected: " + rowsAffected);
        } catch (Exception e) {
            Log.e(TAG, "Error updating book status", e);
        } finally {
            db.endTransaction();
            // db.close(); // Don't close here if using singleton instance
        }
        return rowsAffected;
    }

    // Delete a book (Example)
    public int deleteBook(long bookId) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = 0;
        db.beginTransaction();
        try {
            rowsDeleted = db.delete(TABLE_BOOKS, KEY_BOOK_ID + "= ?", new String[]{String.valueOf(bookId)});
            db.setTransactionSuccessful();
            Log.i(TAG, "Deleted book ID " + bookId + ". Rows deleted: " + rowsDeleted);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting book", e);
        } finally {
            db.endTransaction();
            // db.close(); // Don't close here if using singleton instance
        }
        return rowsDeleted;
    }


    // --- Helper Methods ---

    // Add some sample books when the database is first created
    private void addSampleBooks(SQLiteDatabase db) {
        // Use the public addBook method logic adapted for direct db access
        Log.i(TAG, "Adding sample books...");
        Book[] sampleBooks = {
                new Book("Clean Code", "Robert C. Martin", "978-0132350884", Book.STATUS_AVAILABLE, "A handbook of agile software craftsmanship."),
                new Book("Effective Java", "Joshua Bloch", "978-0134685991", Book.STATUS_RESERVED, "Best practices for the Java platform."),
                new Book("Design Patterns", "Erich Gamma et al.", "978-0201633610", Book.STATUS_AVAILABLE, "Elements of Reusable Object-Oriented Software."),
                new Book("The Pragmatic Programmer", "Andrew Hunt, David Thomas", "978-0135957059", Book.STATUS_AVAILABLE, "From journeyman to master.")
        };

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Book book : sampleBooks) {
                values.clear(); // Clear values for the next book
                values.put(KEY_BOOK_TITLE, book.getTitle());
                values.put(KEY_BOOK_AUTHOR, book.getAuthor());
                values.put(KEY_BOOK_ISBN, book.getIsbn());
                values.put(KEY_BOOK_STATUS, book.getStatus());
                values.put(KEY_BOOK_DESCRIPTION, book.getDescription());
                db.insert(TABLE_BOOKS, null, values);
            }
            db.setTransactionSuccessful();
            Log.i(TAG, "Sample books added successfully.");
        } catch (Exception e) {
            Log.e(TAG, "Error adding sample books", e);
        } finally {
            db.endTransaction();
        }
    }
}
