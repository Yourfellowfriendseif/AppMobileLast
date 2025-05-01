package com.example.tp1appmobil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar; // Use androidx.appcompat.widget.Toolbar

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem; // For handling toolbar back button
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    private static final String TAG = "LibraryActivity";

    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private List<Book> bookList;
    private DatabaseHelper databaseHelper;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Apply the theme before setting content view if needed, though usually done in Manifest
        // setTheme(R.style.Theme_AppMobileHHH);
        setContentView(R.layout.activity_library);

        // --- Initialization ---
        toolbar = findViewById(R.id.toolbar);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        progressBar = findViewById(R.id.progressBarLibrary);
        textViewEmpty = findViewById(R.id.textViewEmptyOrError);

        // Set up the Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.library_title); // Set title from strings.xml
        }


        databaseHelper = DatabaseHelper.getInstance(this); // Get singleton instance
        bookList = new ArrayList<>();

        // --- Setup RecyclerView ---
        // Create adapter with empty list initially
        booksAdapter = new BooksAdapter(this, bookList /*, book -> { /* Handle book click */ //});
                recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBooks.setAdapter(booksAdapter);

        // --- Load Data ---
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        Log.d(TAG, "Loading books from database...");
        progressBar.setVisibility(View.VISIBLE); // Show progress bar
        recyclerViewBooks.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.GONE);

        // Consider running DB query in a background thread for larger datasets
        // For simplicity here, running on main thread. Use AsyncTask, Coroutines (Kotlin), or Threads.
        try {
            List<Book> fetchedBooks = databaseHelper.getAllBooks();
            bookList.clear(); // Clear existing list before adding new data
            bookList.addAll(fetchedBooks);

            progressBar.setVisibility(View.GONE); // Hide progress bar

            if (bookList.isEmpty()) {
                Log.d(TAG, "No books found in database.");
                textViewEmpty.setText(R.string.library_empty); // Set empty message
                textViewEmpty.setVisibility(View.VISIBLE);
                recyclerViewBooks.setVisibility(View.GONE);
            } else {
                Log.d(TAG, "Books loaded successfully: " + bookList.size());
                booksAdapter.updateData(bookList); // Update adapter data
                textViewEmpty.setVisibility(View.GONE);
                recyclerViewBooks.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading books from database", e);
            progressBar.setVisibility(View.GONE);
            textViewEmpty.setText(R.string.library_error_loading); // Define in strings.xml
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewBooks.setVisibility(View.GONE);
        }
    }

    // Handle Toolbar back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Optional: Refresh data when the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        // You might want to reload data if it could have changed while the activity was paused
        // loadBooksFromDatabase(); // Uncomment if needed, but be mindful of performance
    }
}