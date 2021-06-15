package com.example.aislechallenge.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.aislechallenge.R;
import com.example.aislechallenge.models.Person;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HomeActivity extends AppCompatActivity {

    NestedScrollView homeLayout;
    ProgressBar homeProgress;
    ImageView person1, person2, person3;
    TextView p1Name, p1Details, p2Name, p3Name;
    String token = "";
    BottomNavigationView navView;
    Person p1, p2, p3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navView = findViewById(R.id.bottom_nav);

        token = getIntent().getStringExtra("token");

        homeProgress = findViewById(R.id.progress_bar_home);
        homeProgress.setVisibility(View.VISIBLE);

        homeLayout = findViewById(R.id.home_ll);
        homeLayout.setVisibility(View.GONE);

        person1 = findViewById(R.id.image1);
        person2 = findViewById(R.id.image2);
        person3 = findViewById(R.id.image3);

        p1Name = findViewById(R.id.person1);
        p1Details = findViewById(R.id.person1_detail);
        p2Name = findViewById(R.id.person2);
        p3Name = findViewById(R.id.person3);

        getData();
    }

    void getData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://testa2.aisle.co/V1/users/test_profile_list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject person = jsonObject.getJSONObject("invites").
                    getJSONArray("profiles").getJSONObject(0).getJSONObject("general_information");


            int age = 2021 - Integer.parseInt(person.getString("date_of_birth").substring(0, 4));
            String name = person.getString("first_name") + ", " + age;
            String description = person.getString("sun_sign") + ", " + person.getString("marital_status");
            String imageUrl = jsonObject.getJSONObject("invites").getJSONArray("profiles").getJSONObject(0)
                    .getJSONArray("photos").getJSONObject(0).getString("photo");
            p1 = new Person(name, imageUrl, description);

            JSONArray likedProfiles = jsonObject.getJSONObject("likes").getJSONArray("profiles");
            p2 = new Person(likedProfiles.getJSONObject(0).getString("first_name"),
                    likedProfiles.getJSONObject(0).getString("avatar"),
                    "");
            p3 = new Person(likedProfiles.getJSONObject(1).getString("first_name"),
                    likedProfiles.getJSONObject(1).getString("avatar"),
                    "");

            showData();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while getting data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showData() {
        Glide.with(this).load(p1.imageUrl).into(person1);
        p1Name.setText(p1.name);
        p1Details.setText(p1.description);


        Glide.with(this)
                .load(p2.imageUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(person2);
        p2Name.setText(p2.name);


        Glide.with(this)
                .load(p3.imageUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(person3);
        p3Name.setText(p3.name);

        BadgeDrawable notes = navView.getOrCreateBadge(R.id.item_notes);
        notes.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPurple));
        notes.setNumber(9);
        notes.setBadgeTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        notes.setVisible(true);

        BadgeDrawable matches = navView.getOrCreateBadge(R.id.item_matches);
        matches.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPurple));
        matches.setNumber(100);
        matches.setMaxCharacterCount(3);
        matches.setBadgeTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        matches.setVisible(true);

        homeProgress.setVisibility(View.GONE);
        homeLayout.setVisibility(View.VISIBLE);
    }

}