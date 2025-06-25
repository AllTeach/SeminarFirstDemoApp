package com.example.seminarfirstdemoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.graphics.Paint;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FruitActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<FruitItem> fruitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fruit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });






        setUpRecyclerView();
        setUpFloatingActionButton();


    }

    private void setUpFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_fruit);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inflate the custom dialog layout
                View dialogView = LayoutInflater.from(FruitActivity.this).inflate(R.layout.dialog_add_fruit, null);

                EditText nameInput = dialogView.findViewById(R.id.edit_fruit_name);
                EditText descInput = dialogView.findViewById(R.id.edit_fruit_desc);
                // Set a default image for the fruit
                // only default image is set - later we read from gallery
                // or from camera

                ImageView imgFruit = dialogView.findViewById(R.id.img_fruit);
                imgFruit.setImageResource(R.drawable.mango);
                imgFruit.setTag(R.drawable.mango); // Store resource ID

                AlertDialog.Builder builder = new AlertDialog.Builder(FruitActivity.this);
                builder.setTitle("Add New Fruit")
                        .setView(dialogView)
                        .setPositiveButton("Add", (dialog, which) -> {
                            String name = nameInput.getText().toString();
                            String description = descInput.getText().toString();
                            // Add a new fruit item to the list
                            int imageResource = (int) imgFruit.getTag(); // Get the image resource ID
                            fruitList.add(new FruitItem(imageResource, name, description));
                            myAdapter.notifyItemInserted(fruitList.size() - 1);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setCancelable(false);

                builder.show();

            }
        });
    }

    private void setUpRecyclerView() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Sample fruit data
        fruitList = new ArrayList<>();
        fruitList.add(new FruitItem(R.drawable.apple, "Apple", "Rich in fiber and vitamin C."));
        fruitList.add(new FruitItem(R.drawable.banana, "Banana", "Great source of potassium."));
        fruitList.add(new FruitItem(R.drawable.orange, "Orange", "Loaded with vitamin C."));
        fruitList.add(new FruitItem(R.drawable.strawberry, "Strawberry", "Full of antioxidants."));
        fruitList.add(new FruitItem(R.drawable.watermelon, "Watermelon", "Very refreshing and hydrating."));
        // Add more fruits as desired

        myAdapter = new MyAdapter(fruitList);
        recyclerView.setAdapter(myAdapter);

        // use ItemTouchHelper for drag and drop or swipe actions if needed
        // Example here includes:
        // ItemTouchHelper for swipe-to-delete
        // and Drag and drop to move item
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, // Enable drag directions
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // Enable swipe left and right
        ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();
                // Swap items and notify adapter
                Collections.swap(fruitList, fromPos, toPos);
                myAdapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FruitItem fruitItem = fruitList.get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    myAdapter.notifyItemChanged(position);


            //        showAlertDialog(position, fruitItem);
                 //   showDeleteConfirmationDialog(position, fruitItem);
               //   showUndoSnackbar(position, fruitItem);
                    // Swipe left: delete
                      //  fruitList.remove(position);
                 //        myAdapter.notifyItemRemoved(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // Swipe right
                    shareItem(fruitItem);
                    myAdapter.notifyItemChanged(position);

                }





            }

/*
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();

                if (dX < 0) { // Swiping left
                    paint.setColor(Color.RED);
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                } else if (dX > 0) { // Swiping right
                    paint.setColor(Color.GREEN);
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(),
                            (float) itemView.getLeft() + dX, (float) itemView.getBottom(), paint);
                }
            }
 */

        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showAlertDialog(int position, FruitItem fruitItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Our alert dialog")
                    .setMessage("Just an example, please confirm to delete")
                    .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    fruitList.remove(fruitItem);
                                    myAdapter.notifyItemRemoved(position);


                                }
                            })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).
                    setNeutralButton("Maybe", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });



            AlertDialog dialog = builder.create();

            dialog.show();


    }

    private void showUndoSnackbar(int position, FruitItem fruitItem) {
        // show a Snackbar for undo action
                 Snackbar.make(recyclerView, "Item removed", Snackbar.LENGTH_LONG)
                         .setAction("UNDO Fruit Removal", v -> {
                             fruitList.add(position, fruitItem);
                             myAdapter.notifyItemInserted(position);
                         })
                         .show();
    }

    private void shareItem(FruitItem fruitItem) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this fruit: " + fruitItem.getName());
        startActivity(Intent.createChooser(shareIntent, "Share via"));


    }

    private void showDeleteConfirmationDialog(int position, FruitItem fruitItem) {

        // Optional: Set item click listener if needed
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FruitActivity.this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this fruit?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    fruitList.remove(position);
                    myAdapter.notifyItemRemoved(position);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    myAdapter.notifyItemChanged(position); // Restore item if canceled
                });
        alertDialog.setCancelable(false);
        alertDialog.show();


    }
}
