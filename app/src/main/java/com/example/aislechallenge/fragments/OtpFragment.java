package com.example.aislechallenge.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aislechallenge.R;
import com.example.aislechallenge.activities.HomeActivity;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OtpFragment extends Fragment {

    TextView phoneTV, timerTextView;
    EditText otpEditText;
    MaterialButton button;
    ProgressBar progressBar;
    int time = 60;

    public OtpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        phoneTV = view.findViewById(R.id.text_mobile_num);
        timerTextView = view.findViewById(R.id.timer_text_view);
        otpEditText = view.findViewById(R.id.et_otp);
        button = view.findViewById(R.id.btn_continue_otp_screen);
        progressBar = view.findViewById(R.id.otp_fragment_progress);
        phoneTV.setText(R.string.spaced_mobile_num);
        otpEditText.setText(R.string.otp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                makeOTPRequest();
            }
        });

        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("0:" + checkDigit(time));
                time--;
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
            }
        }.start();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    void makeOTPRequest() {

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://testa2.aisle.co/V1/users/verify_otp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Intent intent = new Intent(getContext(), HomeActivity.class);
                    intent.putExtra("token", jsonObject.getString("token"));
                    startActivity(intent);
                    if (getActivity() != null)
                        getActivity().finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "An error occurred, please retry.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setEnabled(true);
                Toast.makeText(requireContext(), "An error occurred, please retry.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("number", "+919876543212");
                map.put("otp", "1234");
                return map;
            }
        };
        queue.add(stringRequest);
    }
}