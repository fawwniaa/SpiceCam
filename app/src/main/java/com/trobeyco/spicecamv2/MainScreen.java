package com.trobeyco.spicecamv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.trobeyco.spicecamv2.Adapter.spiceIdentifyAdapter;
import com.trobeyco.spicecamv2.Adapter.spiceNewsAdapter;
import com.trobeyco.spicecamv2.Domain.spiceIdentifyDomain;
import com.trobeyco.spicecamv2.Domain.spiceNewsDomain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainScreen extends BaseActivity {


    // REQUEST CODE 10 GALLERY BUTTON
    // REQUEST CODE 11 camera permission
    // REQUEST CODE 12 open camera and capture
    private String[] categories; //Declare the array to store class labels
    private CardView galleryBtn, snapBtn;
    private Bitmap bitmap;
    private RecyclerView.Adapter adapterSpiceList;
    private RecyclerView recyclerView2, recyclerView;
    private spiceNewsAdapter adapter1;
    private spiceIdentifyAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen); // Replace with your layout XML file
        // Add your activity initialization code here

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Widget Initialized and assigned
        galleryBtn = (CardView) findViewById(R.id.idButtonGallery);
        snapBtn = (CardView) findViewById(R.id.idButtonSnap);

        //permission for camera and gallery
        getPermission();

        fetchNewsInformation();
        fetchIdentifyInformation();

        // Gallery Button Logic
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        // Open the camera, and user can capture
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

    }

    // Declare the items ArrayList at class level

    private ArrayList<spiceNewsDomain> newsItems = new ArrayList<>();
    private ArrayList<spiceNewsDomain> identifyItems = new ArrayList<>();
    // Method biar cardnya swipeswipe
//    private void initRecyclerView() {
//        fetchNewsInformation();
//
//    }

    // Method buat retrieve dataset news dan identify di firestore
    private void fetchNewsInformation(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // collection name = spice_news
        CollectionReference spiceCollection = db.collection("spice_news");
        spiceCollection.get().addOnSuccessListener(querySnapshot -> {
            ArrayList<spiceNewsDomain> newsItems = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot){
                String judul_berita = document.getString("judul_berita");
                String sumber_berita = document.getString("sumber_berita");
                String imageResourceName = document.getString("imageResourceName");

                spiceNewsDomain newsItem = new spiceNewsDomain(judul_berita,sumber_berita, imageResourceName);
                newsItems.add(newsItem);
            }
            RecyclerView recyclerView = findViewById(R.id.view1);
            adapter1 = new spiceNewsAdapter(newsItems);
            recyclerView.setAdapter(adapter1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        }).addOnFailureListener(e -> {
            // handle failure
        });

    }
    private void fetchIdentifyInformation(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // collection name = spice_identify
        CollectionReference spiceCollection = db.collection("spice_identify");
        spiceCollection.get().addOnSuccessListener(querySnapshot -> {
            ArrayList<spiceIdentifyDomain> identifyItems = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot){
                String judul_berita = document.getString("judul_berita");
                String sumber_berita = document.getString("sumber_berita");
                String imageResourceName = document.getString("imageResourceName");

                spiceIdentifyDomain identifyItem = new spiceIdentifyDomain(judul_berita,sumber_berita, imageResourceName);
                identifyItems.add(identifyItem);
            }
            RecyclerView recyclerView2 = findViewById(R.id.view2);
            adapter2 = new spiceIdentifyAdapter(identifyItems);
            recyclerView2.setAdapter(adapter2);
            recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        }).addOnFailureListener(e -> {
            // handle failure
        });

    }


    // Method to set up RecyclerView with the fetched data
//    private void setupRecyclerView(){
//        //Find the recyclerView from layout
//        RecyclerView recyclerView = findViewById(R.id.view1);
//        RecyclerView recyclerView2 = findViewById(R.id.view2);
//
//
//        // Set up horizontal linear layout manager for view1 and view2
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView2.setLayoutManager(layoutManager2);
//
//
//        // Set the appropriate adapter for each RecyclerView
//        if (adapter1 != null) {
//            recyclerView.setAdapter(adapter1);
//        }
//        if (adapter2 != null) {
//            recyclerView2.setAdapter(adapter2);
//        }
//
//    }

    //display the result to the imageview in other activity -> activity_spice_result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==10){
            if (data != null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    //Pass the bitmap to the other activity
                    Intent intent = new Intent(this, SpiceResultsActivity.class);
                    intent.putExtra("imageUri", uri.toString());
                    startActivity(intent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else if (requestCode== 12){
            bitmap = (Bitmap) data.getExtras().get("data");

            // Convert the bitmap to a uri using MediaStore
            Uri imageUri = convertBitmapToUri(bitmap);

            // Pass the bitmap to the other activity
            Intent intent = new Intent(this, SpiceResultsActivity.class);
            intent.putExtra("imageUri", imageUri.toString());
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Uri convertBitmapToUri(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Title",
                null
        );
        return Uri.parse(path);
    }
}
