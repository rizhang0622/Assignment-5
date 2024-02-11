package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LinkActivity extends AppCompatActivity {

    private RecyclerView linksRecyclerView;
    private LinksAdapter linksAdapter;
    private List<LinkItem> linkItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        if (savedInstanceState != null && savedInstanceState.containsKey("linkItems")) {
            linkItems = savedInstanceState.getParcelableArrayList("linkItems");
        } else {
            linkItems = new ArrayList<>();
        }

        linksRecyclerView = findViewById(R.id.linksRecyclerView);
        linksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        linksAdapter = new LinksAdapter(this, linkItems, (item, position) -> {
            // Show edit dialog
            showAddLinkDialog(true, item, position);
        });
        linksRecyclerView.setAdapter(linksAdapter);

        FloatingActionButton addLinkButton = findViewById(R.id.addLinkButton);
        addLinkButton.setOnClickListener(v -> showAddLinkDialog(false, null, -1));

        setupItemTouchHelper();
    }

    private void showAddLinkDialog(boolean isEdit, LinkItem oldItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Link" : "Add Link");

        // Custom layout for the dialog
        final View customLayout = getLayoutInflater().inflate(R.layout.add_link_dialog, null);
        builder.setView(customLayout);

        EditText nameEditText = customLayout.findViewById(R.id.nameEditText);
        EditText urlEditText = customLayout.findViewById(R.id.urlEditText);

        // Pre-populate data if it's an edit
        if (isEdit && oldItem != null) {
            nameEditText.setText(oldItem.getName());
            urlEditText.setText(oldItem.getUrl());
        }

        builder.setPositiveButton(isEdit ? "Update" : "Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString().trim();
                String url = urlEditText.getText().toString().trim();

                if (isEdit) {
                    oldItem.setName(name);
                    oldItem.setUrl(url);
                    linksAdapter.notifyItemChanged(position);
                    showSnackbar(customLayout, "Link updated successfully", "UNDO", oldItem, position, true);
                } else {
                    LinkItem newItem = new LinkItem(name, url);
                    linkItems.add(newItem);
                    linksAdapter.notifyItemInserted(linkItems.size() - 1);
                    showSnackbar(customLayout, "Link added successfully", "UNDO", newItem, linkItems.size() - 1, false);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We don't want move functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                LinkItem removedItem = linkItems.remove(position);
                linksAdapter.notifyItemRemoved(position);
                showSnackbar(linksRecyclerView, "Link deleted", "UNDO", removedItem, position, false);
            }
        }).attachToRecyclerView(linksRecyclerView);
    }

    private void showSnackbar(View view, String message, String actionText, LinkItem linkItem, int position, boolean isEdit) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction(actionText, v -> {
            if (!isEdit) {
                linkItems.add(position, linkItem);
                linksAdapter.notifyItemInserted(position);
            } else {
                // For edit undo, consider the logic to restore previous state
            }
        });
        snackbar.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the list of link items
        outState.putParcelableArrayList("linkItems", new ArrayList<>(linkItems));
    }
}


