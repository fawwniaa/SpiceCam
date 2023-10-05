package com.trobeyco.spicecamv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CardArticleActivity extends AppCompatActivity {

    private TextView link_berita_tv, isi_berita_tv, judul_berita_tv;
    private CardView card_berita;
    private ImageView gambar_detail_berita, arrow_back_news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_article);

        Log.d("Debug", "CardArticleActivity onCreate");

        link_berita_tv = (TextView) findViewById(R.id.link_berita_tv);
        isi_berita_tv = (TextView) findViewById(R.id.isi_berita_tv);
        judul_berita_tv = (TextView) findViewById(R.id.judul_berita_tv);
        card_berita = (CardView) findViewById(R.id.card_berita);
        gambar_detail_berita = (ImageView) findViewById(R.id.spice_news_pic);


        // nampilin judul berita
        // Retrieve the selected title and the collection name
        String selectedTitle = getIntent().getStringExtra("selectedTitle");
        String collectionName = getIntent().getStringExtra("collectionName");

        Log.d("Debug", "Selected Title: " + selectedTitle);
        Log.d("Debug", "Collection Name: " + collectionName);

        if (selectedTitle != null && collectionName != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference newsCollection = db.collection(collectionName);
            Query query = newsCollection.whereEqualTo("judul_berita", selectedTitle);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get the fields you need from the document and display them in your activity
                        String isi_berita = document.getString("isi_berita");
                        String link_berita = document.getString("link_berita");
                        String judul_berita = document.getString("judul_berita");
                        String img = document.getString("imageResourceName");


                        // Update TextViews
                        judul_berita_tv.setText(judul_berita);
                        String formattedIsiBerita = isi_berita.replace("\\n", "\n");
                        isi_berita_tv.setText(formattedIsiBerita);
                        // link_berita : i want user click this textView, a web browser will appears accoridng to the url in the link_berita data
                        if (link_berita != null && !link_berita.isEmpty()){
//                           link_berita_tv.setText("Klik");
                            link_berita_tv.setOnClickListener(v ->{
                                Uri webpage = Uri.parse(link_berita);
                                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                if (intent.resolveActivity(getPackageManager()) != null){
                                    startActivity(intent);
                                }
                            });
                        }
                        // img : img are in my drawable folder, but the imageResourceName are inside the firebase,
                        // how do i display the image?
                        int drawableResourceId = getResources().getIdentifier(
                                img, "drawable", getPackageName()

                        );
                        gambar_detail_berita.setImageResource(drawableResourceId);

                    }
                } else {
                    // Handle the error
                }
            });
        }

        // tombol arrow back news
        arrow_back_news = (ImageView) findViewById(R.id.arrow_back_news);
        arrow_back_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(CardArticleActivity.this, MainScreen.class);
                startActivity(a);
                finish();
            }
        });






    }
}