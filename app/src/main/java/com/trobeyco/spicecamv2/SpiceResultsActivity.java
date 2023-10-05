package com.trobeyco.spicecamv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

//glide
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trobeyco.spicecamv2.ml.Model01Finetune;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SpiceResultsActivity extends BaseActivity {

    private ImageView gambar_hasil, arrow_back_results, galleryBtn2, snapBtn2;
    private Bitmap bitmap;
    private Button scanBtn;
    private String[] categories;
    private TextView hasil_rempah_txt, latin_rempah,warna_rempah,bentuk_rempah, bau_rempah, funfact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spice_results);

        //Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //widget initialized
        gambar_hasil = (ImageView) findViewById(R.id.gambar_hasil);
        arrow_back_results = (ImageView) findViewById(R.id.arrow_back_results);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        hasil_rempah_txt = (TextView) findViewById(R.id.idDetectedSpice);
        latin_rempah = (TextView) findViewById(R.id.latin_rempah);
        warna_rempah = (TextView) findViewById(R.id.warna_rempah);
        bentuk_rempah = (TextView) findViewById(R.id.bentuk_rempah);
        bau_rempah = (TextView) findViewById(R.id.bau_rempah);
        funfact = (TextView) findViewById(R.id.funfact);
        galleryBtn2 = (ImageView) findViewById(R.id.galleryBtn2);
        snapBtn2 = (ImageView) findViewById(R.id.snapBtn2);


        // Read class labels from labels.txt and store them in the 'categories' array
        try {
            InputStream inputStream = getAssets().open("labels.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            ArrayList<String> categoryList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                categoryList.add(line.trim()); // Convert to lowercase here
            }

            //Convert array list to a string array
            categories = categoryList.toArray(new String[0]);
            //Close the buffered reader
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }


        // Arrow Back
        arrow_back_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SpiceResultsActivity.this, MainScreen.class);
                startActivity(intent);
                finish();
            }
        });

        // Display image
        // Retrieve the passed bitmap
        String imageUriString = getIntent().getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);

        // Set the received bitmap to the image view
        ImageView gambar_hasil = findViewById(R.id.gambar_hasil);
        Glide.with(this)
                .load(imageUri)
                .into(gambar_hasil);

        //Scan Button
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert displayed image (Uri) to Bitmap
                Bitmap bitmap = null;
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                } catch (IOException e){
                    e.printStackTrace();
                }

                // Resize the bitmap to 224x224 pixels
                if (bitmap != null) {
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);

                    // Perform Classification
                    try {
                        Model01Finetune model = Model01Finetune.newInstance(getApplicationContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                        // Load the resized bitmap into tensorImage
                        tensorImage.load(resizedBitmap);
                        ByteBuffer byteBuffer = tensorImage.getBuffer();

                        inputFeature0.loadBuffer(byteBuffer);

                        // Runs model inference and gets result.
                        Model01Finetune.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                        float[] probabilities = outputFeature0.getFloatArray();
                        int maxProbabilityIndex = findMaxProbabilityIndex(probabilities);
                        float maxProbability = probabilities[maxProbabilityIndex];
                        float threshold = 0.5f; // batas ambang

                        // Get the class label with the highest probability
                        String topClass = categories[maxProbabilityIndex];


                        // Releases model resources if no longer used.
                        model.close();

                        // display the top class in the result
                        // compare maxProbability with the threshold to determine recognition
                        if (maxProbability >= threshold){
                            //Rempah dikenali, display the top class in the result
                            hasil_rempah_txt.setText("Rempah di atas kemungkinan\nadalah " + topClass);
                            // Call the method to fetch additional information from Firestore
                            fetchSpiceInformation(topClass);
                        } else {
                            // Bumbu tidak dikenali, tampilkan pesan tidak dikenali
                            hasil_rempah_txt.setText("Rempah tidak dikenali");
                        }
                    } catch (IOException e) {
                        // TODO Handle the exception
                    }

                }

            }
        });

        // Gallery Button 2
        galleryBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });
        // Camera Button 2 -> open the camera and user can capture
        snapBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });
    }

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
    // Method to find the index of the class with the highest probability
    private int findMaxProbabilityIndex(float[] probabilities) {
        int maxIndex = 0;
        float maxProbability = probabilities[0];
        for (int i = 1; i < probabilities.length; i++){
            if (probabilities[i] > maxProbability) {
                maxProbability = probabilities[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    // Method to fetch spice information from firestore
    private void fetchSpiceInformation(String topClass){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // collection name : spice_info
        CollectionReference spice_info_collection = db.collection("spice_info");
        Query query = spice_info_collection.whereEqualTo("nama", topClass);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                        String nama_latin = documentSnapshot.getString("nama_latin");
                        String warna = documentSnapshot.getString("warna");
                        String bau = documentSnapshot.getString("bau");
                        String bentuk = documentSnapshot.getString("bentuk");
                        String fakta = documentSnapshot.getString("fakta");

                        // Update TextViews
                        latin_rempah.setText("Nama Latin: " + nama_latin);
                        warna_rempah.setText("Warna : "+ warna );
                        bau_rempah.setText("Bau : "+ bau);
                        bentuk_rempah.setText("Bentuk :" + bentuk);
                        funfact.setText(fakta);
                    }
                }else{
                    Log.e("FetchInfo", "Error fetching documents: " + task.getException());
                    // An error occurred while fetching the documents
                    // Handle the error if necessary
                }

            }
        });
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