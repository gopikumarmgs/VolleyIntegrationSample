package com.volley.gs.volleyintegrationsample;


import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String IMAGE_REQUEST_URL = "https://cnet4.cbsistatic.com/hub/i/2011/10/27/a66dfbb7-fdc7-11e2-8c7c-d4ae52e62bcc/android-wallpaper5_2560x1600_1.jpg";
    private static final String STRING_REQUEST_URL = "https://api.ipify.org/";
    private static final String JSON_OBJECT_REQUEST_URL = "https://api.ipify.org?format=json";

    ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    private Button btn_string;
    private Button btn_json;
    private Button btn_image;
    private View showDialogView;
    private TextView outputTextView;
    private ImageView outputImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        btn_string = (Button)findViewById(R.id.button_get_string);
        btn_json = (Button)findViewById(R.id.button_get_json_object);
        btn_image = (Button)findViewById(R.id.button_get_image);

        btn_string.setOnClickListener(this);
        btn_image.setOnClickListener(this);
        btn_json.setOnClickListener(this);
    }

    public void getStringResponse(String url) {

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AlertDialog.Builder builder = getDialog();
                outputTextView.setText(response.toString());
                builder.show();
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        // Adding String request to request queue
        VolleyApplication.getInstance().getRequestQueue().add(strReq);
    }

    public void getJson(String url){

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AlertDialog.Builder builder = getDialog();
                        outputTextView.setText(response.toString());
                        builder.show();
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });

        // Adding JsonObject request to request queue
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjectReq);
    }

    public void getImage(String url){
        ImageLoader imageLoader = VolleyApplication.getInstance().getmImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    AlertDialog.Builder builder = getDialog();
                    outputImageView.setImageBitmap(response.getBitmap());
                    builder.show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_get_image:
                getImage(IMAGE_REQUEST_URL);
                break;
            case R.id.button_get_json_object :
                getJson(JSON_OBJECT_REQUEST_URL);
                break;
            case R.id.button_get_string :
                getStringResponse(STRING_REQUEST_URL);
                break;
        }
    }

    private AlertDialog.Builder getDialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        showDialogView = li.inflate(R.layout.dialog, null);
        outputImageView = (ImageView)showDialogView.findViewById(R.id.image_view_dialog);
        outputTextView = (TextView)showDialogView.findViewById(R.id.text_view_dialog);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(showDialogView);
        alertDialogBuilder
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setCancelable(false)
                .create();
        return alertDialogBuilder;
    }
}


