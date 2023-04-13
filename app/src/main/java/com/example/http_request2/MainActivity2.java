package com.example.http_request2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient(); //creating and managing HTTP connections

    TextView IdTextView;
    TextView FirstNameTextView;
    TextView LastNameTextView;
    TextView EmailTextView;
    TextView CreatedTextView;
    TextView UpdatedTextView;

    HashMap<String, String> response_get;

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON); // class in the OkHttp library that
        // represents the content of a request body. It provides an interface for creating different
        // types of request bodies, such as text, JSON, binary data, and multipart data.
        Request request = new Request.Builder() // method in OkHttp that creates a builder for
                // building an HTTP request. It is used to create and configure a Request object
                // that represents an HTTP request.
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute(); // When a request is made using OkHttp,
        // the Response class is used to represent the response returned by the server.
        // The Response class provides methods for accessing the different parts of the response,
        // such as code() to get the HTTP status code, header() to get a specific header value,
        // and body() to get the response body.
        return response.body().string();
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String put(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON); // new
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button PutButton = findViewById(R.id.buttonPut);
        Button PostButton = findViewById(R.id.buttonPost);
        Button GetButton = findViewById(R.id.buttonGet);
        Button DeleteButton = findViewById(R.id.buttonDelete);
        Button BackButton = findViewById(R.id.buttonBack);

        IdTextView = findViewById(R.id.TextViewId);
        FirstNameTextView = findViewById(R.id.TextViewFirstName);
        LastNameTextView = findViewById(R.id.TextViewLastName);
        EmailTextView = findViewById(R.id.TextViewEmail);
        CreatedTextView = findViewById(R.id.TextViewCreated);
        UpdatedTextView = findViewById(R.id.TextViewUpdated);

        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t1 = new Thread(postThread);
                t1.start();
            }
        });

        PutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t1 = new Thread(putThread);
                t1.start();
            }
        });

        GetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t1 = new Thread(getThread);

                t1.start();

                try {
                    t1.join();
                } catch (InterruptedException e) {
                    // Handling the exception
                    System.out.println("Interrupted Exception");
                }

                //System.out.println(response_get.get("\"id\""));
                FirstNameTextView.setText(response_get.get("\"firstName\"").substring(1, response_get.get("\"firstName\"").length()-1));
                LastNameTextView.setText(response_get.get("\"lastName\"").substring(1, response_get.get("\"lastName\"").length()-1));
                EmailTextView.setText(response_get.get("\"emailId\"").substring(1, response_get.get("\"emailId\"").length()-1));
                CreatedTextView.setText(response_get.get("\"createdBy\"").substring(1, response_get.get("\"createdBy\"").length()-1));
                UpdatedTextView.setText(response_get.get("\"updatedby\"").substring(1, response_get.get("\"updatedby\"").length()-1));
            }
        });

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t1 = new Thread(deleteThread);
                t1.start();
            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    Runnable postThread = new Runnable() {

        @Override
        public void run() {
            try  {
                String json = "{\r\n" +
                        "\"firstName\" : \"" + FirstNameTextView.getText().toString() + "\",\r\n" +
                        "\"lastName\" : \""+ LastNameTextView.getText().toString() +"\",\r\n" +
                        "\"emailId\" : \""+ EmailTextView.getText().toString() +"\",\r\n" +
                        "\"createdBy\" : \""+ CreatedTextView.getText().toString() +"\",\r\n" +
                        "\"updatedby\" : \""+ UpdatedTextView.getText().toString() +"\"\r\n" +
                        "}";

                String response = null;
                try {
                    response = post("http://10.0.2.2:8080/api/v1/users", json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable putThread = new Runnable() {

        @Override
        public void run() {
            try  {
                String json = "{\r\n" +
                        "\"firstName\" : \"" + FirstNameTextView.getText().toString() + "\",\r\n" +
                        "\"lastName\" : \""+ LastNameTextView.getText().toString() +"\",\r\n" +
                        "\"emailId\" : \""+ EmailTextView.getText().toString() +"\",\r\n" +
                        "\"createdBy\" : \""+ CreatedTextView.getText().toString() +"\",\r\n" +
                        "\"updatedby\" : \""+ UpdatedTextView.getText().toString() +"\"\r\n" +
                        "}";

                String response = null;
                try {
                    response = put("http://10.0.2.2:8080/api/v1/users/" + IdTextView.getText().toString(), json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable getThread = new Runnable() {

        @Override
        public void run() {
            try  {

                String response = null;
                try {
                    response = get("http://10.0.2.2:8080/api/v1/users/" + IdTextView.getText().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(response);
                HashMap<String, String> myMap = new HashMap<String, String>();
                response = response.substring(1, String.valueOf(response).length()-1);
                String[] pairs = response.split(",");
                for (int i=0;i<pairs.length;i++) {
                    String pair = pairs[i];
                    String[] keyValue = pair.split(":");
                    myMap.put(keyValue[0], keyValue[1]);
                }

                response_get = myMap;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Runnable deleteThread = new Runnable() {

        @Override
        public void run() {
            try  {

                String response = null;
                try {
                    response = delete("http://10.0.2.2:8080/api/v1/users/" + IdTextView.getText().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}