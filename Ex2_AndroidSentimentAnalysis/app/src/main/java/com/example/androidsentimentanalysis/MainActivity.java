//package com.example.androidsentimentanalysis;
//
//import android.annotation.SuppressLint;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//
//    private EditText edtInput;
//    private Button btnSubmit;
//    private ImageView imgSentiment;
//    private RelativeLayout layout;
//
//    // API Key trực tiếp
////    private static final String API_KEY = "AIzaSyAARiR_O17Fsvbckhu_XYEOQ0zDFIUe_zg";
////    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
////Flask API
//    private static final String API_URL = "http://10.0.2.2:5000/";
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Ánh xạ giao diện
//        edtInput = findViewById(R.id.edtInput);
//        btnSubmit = findViewById(R.id.btnSubmit);
//        imgSentiment = findViewById(R.id.imgSentiment);
//        layout = findViewById(R.id.layout);
//
//        // Xử lý sự kiện khi bấm nút Submit
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userInput = edtInput.getText().toString().trim();
//                if (!userInput.isEmpty()) {
//                    analyzeSentiment(userInput);
//                } else {
//                    Toast.makeText(MainActivity.this, "Please enter a text!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    // Gửi request đến API để phân tích cảm xúc
//    private void analyzeSentiment(String text) {
////        String url = API_URL + "?key=" + API_KEY;
//        String url = API_URL;
//
//        JSONObject requestBody = new JSONObject();
//        try {
//            JSONArray contentsArray = new JSONArray();
//            JSONObject contentObject = new JSONObject();
//            JSONArray partsArray = new JSONArray();
//            JSONObject textObject = new JSONObject();
//
//            // Đúng cấu trúc JSON API yêu cầu
//            textObject.put("text", text);
//            partsArray.put(textObject);
//            contentObject.put("parts", partsArray);
//            contentsArray.put(contentObject);
//            requestBody.put("contents", contentsArray);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                url,
//                requestBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("API_RESPONSE", "Raw Response: " + response.toString());
//
//                            // Trích xuất nội dung từ API response
//                            String sentiment = response.getJSONArray("candidates")
//                                    .getJSONObject(0)
//                                    .getJSONObject("content")
//                                    .getJSONArray("parts")
//                                    .getJSONObject(0)
//                                    .getString("text");
//
//                            Log.d("API_RESPONSE", "Extracted Sentiment: " + sentiment);
//                            updateUI(sentiment);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(MainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "API Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                        Log.e("API_ERROR", error.toString());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
//        // Gửi request
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jsonObjectRequest);
//    }
//
//
//    // Cập nhật giao diện dựa trên kết quả cảm xúc
//    private void updateUI(String sentiment) {
//        sentiment = sentiment.toLowerCase();
//
//        if (sentiment.contains("positive") || sentiment.contains("happy") || sentiment.contains("good")) {
//            layout.setBackgroundColor(Color.GREEN);
//            imgSentiment.setImageResource(R.drawable.smile);
//        } else if (sentiment.contains("negative") || sentiment.contains("sad") || sentiment.contains("bad")) {
//            layout.setBackgroundColor(Color.RED);
//            imgSentiment.setImageResource(R.drawable.sad);
//        }
//    }
//
//}


package com.example.androidsentimentanalysis;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;
    private Button btnSubmit;
    private ImageView imgSentiment;
    private RelativeLayout layout;

    // URL của API Flask
    private static final String API_URL = "http://10.0.2.2:5000/predict";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ giao diện
        edtInput = findViewById(R.id.edtInput);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgSentiment = findViewById(R.id.imgSentiment);
        layout = findViewById(R.id.layout);

        // Xử lý sự kiện khi bấm nút Submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = edtInput.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    analyzeSentiment(userInput);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a text!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Gửi request đến API Flask
    private void analyzeSentiment(String text) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API_URL,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("API_RESPONSE", "Raw Response: " + response.toString());
                            int prediction = response.getInt("prediction");
                            updateUI(prediction);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "API Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("API_ERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Gửi request
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    // Cập nhật giao diện dựa trên kết quả cảm xúc
    private void updateUI(int sentiment) {
        if (sentiment == 1) { // Positive
            layout.setBackgroundColor(Color.GREEN);
            imgSentiment.setImageResource(R.drawable.smile);
        } else { // Negative
            layout.setBackgroundColor(Color.RED);
            imgSentiment.setImageResource(R.drawable.sad);
        }
    }
}
