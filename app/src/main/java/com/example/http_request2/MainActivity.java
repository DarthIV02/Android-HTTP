package com.example.http_request2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // member variable for holding the ImageView
    // in which images will be loaded
    ImageView mDogImageView;
    Button nextDogButton;

    Button nextActivity;

    String dogImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the ImageView and the Button
        mDogImageView = findViewById(R.id.dogImageView);
        nextDogButton = findViewById(R.id.nextDogButton);
        nextActivity = findViewById(R.id.httpFunctionButton);

        OkHttpClient client = new OkHttpClient();

        // attaching on click listener to the button so that `loadDogImage()`
        // function is called everytime after clicking it.
        nextDogButton.setOnClickListener(View -> loadDogImage(client));

        // image of a dog will be loaded once at the start of the app
        loadDogImage(client);

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    // function for making a HTTP request using Volley and
    // inserting the image in the ImageView using Glide library
    private void loadDogImage(OkHttpClient client) {

        // url of the api through which we get random dog images
        String url = "https://dog.ceo/api/breeds/image/random";

        Request request = new Request.Builder()
                .url(url)
                .build();

        // since the response we get from the api is in JSON, we
        // need to use `JsonObjectRequest` for parsing the
        // request response
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    dogImageUrl = json.getString("message");
                    // load the image into the ImageView using Glide.
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Some error occurred! Cannot fetch dog image", Toast.LENGTH_LONG).show();
                    // log the error message in the error stream
                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call call, IOException e){
                Toast.makeText(MainActivity.this, "Some error occurred 2! Cannot fetch dog image", Toast.LENGTH_LONG).show();
                    // log the error message in the error stream
                Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Glide.with(MainActivity.this).load(dogImageUrl).into(mDogImageView);
    }
}
