package com.example.aislechallenge.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aislechallenge.R;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberFragment extends Fragment {

    EditText ccEditText, phoneEditText;
    MaterialButton button;
    ProgressBar phoneProgressBar;

    public PhoneNumberFragment() {
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
        return inflater.inflate(R.layout.fragment_phone_number, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ccEditText = view.findViewById(R.id.et_country_code);
        phoneEditText = view.findViewById(R.id.et_mobile);
        button = view.findViewById(R.id.btn_continue_mobile_screen);
        phoneProgressBar = view.findViewById(R.id.phone_fragment_progress);

        ccEditText.setText(R.string.country_code);
        phoneEditText.setText(R.string.mobile_num);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigation.findNavController(view);
                button.setEnabled(false);
                phoneProgressBar.setVisibility(View.VISIBLE);
                makeRequest();
            }
        });
    }

    void makeRequest() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://testa2.aisle.co/V1/users/phone_number_login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavHostFragment.findNavController(PhoneNumberFragment.this).navigate(R.id.action_phoneNumberFragment_to_otpFragment);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setEnabled(true);
                phoneProgressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "An error occurred, please retry.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("number", "+919876543212");
                return map;
            }
        };
        queue.add(stringRequest);
    }
}