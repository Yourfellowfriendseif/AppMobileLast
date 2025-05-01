package com.example.tp1appmobil;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private List<Book> bookList;
    private Context context;
    // Optional: Add an interface for click handling
    // private OnItemClickListener listener;

    // public interface OnItemClickListener { void onItemClick(Book book); }

    public BooksAdapter(Context context, List<Book> bookList /*, OnItemClickListener listener */) {
        this.context = context;
        this.bookList = bookList;
        // this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (list_item_book.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the book at the current position
        Book currentBook = bookList.get(position);
        // Bind the data to the ViewHolder's views
        holder.bind(currentBook);
    }

    @Override
    public int getItemCount() {
        return bookList == null ? 0 : bookList.size();
    }

    // Method to update the list data and refresh the adapter
    public void updateData(List<Book> newBookList) {
        this.bookList.clear();
        if (newBookList != null) {
            this.bookList.addAll(newBookList);
        }
        notifyDataSetChanged(); // Notify adapter about data change
        Log.d("BooksAdapter", "Data updated. New size: " + getItemCount());
    }


    // --- ViewHolder Class ---
    class BookViewHolder extends RecyclerView.ViewHolder {
        // Declare the views from list_item_book.xml
        TextView textViewBookTitle;
        TextView textViewBookAuthor;
        TextView textViewBookStatus;
        // TextView textViewBookIsbn; // If you added it

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the views by their ID
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewBookAuthor = itemView.findViewById(R.id.textViewBookAuthor);
            textViewBookStatus = itemView.findViewById(R.id.textViewBookStatus);
            // textViewBookIsbn = itemView.findViewById(R.id.textViewBookIsbn);



            // Set click listener if needed
            /*
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(bookList.get(position));
                }
            });
            */
        }

        // Helper method to bind data to the views
        void bind(final Book book) {
            textViewBookTitle.setText(book.getTitle());
            textViewBookAuthor.setText(book.getAuthor());
            textViewBookStatus.setText(book.getStatus());
            // textViewBookIsbn.setText("ISBN: " + book.getIsbn()); // Example if ISBN is shown

            // Set status background and text color dynamically
            if (Book.STATUS_AVAILABLE.equalsIgnoreCase(book.getStatus())) {
                textViewBookStatus.setBackgroundResource(R.drawable.status_background_available); // Use your drawable
                textViewBookStatus.setTextColor(ContextCompat.getColor(context, R.color.black)); // Example text color
            } else if (Book.STATUS_RESERVED.equalsIgnoreCase(book.getStatus())) {
                textViewBookStatus.setBackgroundResource(R.drawable.status_background_reserved); // Use your drawable
                textViewBookStatus.setTextColor(ContextCompat.getColor(context, R.color.black)); // Example text color
            } else {
                // Default style if status is unknown
                textViewBookStatus.setBackgroundResource(R.drawable.status_background_default); // Create a default drawable
                textViewBookStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }
    }
}