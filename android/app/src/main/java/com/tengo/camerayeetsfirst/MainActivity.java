package com.tengo.camerayeetsfirst;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST = 2;
    private static final String URI_PREFIX = "http://";
    private static final String HOST = "169.228.190.224";
    private static final String PORT = "80";
    private static final String BASE_URL = URI_PREFIX + HOST + ":" + PORT;

    private ImageView mImageView;
    private Button mCameraButton;
    private Button mRecipeButton;
    private ProgressBar mLoadingSpinner;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton = findViewById(R.id.camera_button);
        mRecipeButton = findViewById(R.id.recipe_button);
        mImageView = findViewById(R.id.icon);
        mLoadingSpinner = findViewById(R.id.loading_spinner);

        makeHandshake();
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCamera();
            }
        });
        mRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRecipePostRequest();
            }
        });
        Log.e("starting", "started app");
    }

    protected void attemptCamera() {
        Log.e("attempt camera", "attempting a camera open");
        if (lacksPermissions()) {
            final String[] permissions = new String[4];
            permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            permissions[2] = Manifest.permission.CAMERA;
            permissions[3] = Manifest.permission.INTERNET;
            requestPermissions(permissions, PERMISSIONS_REQUEST);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Log.e("state", "Opening camera");
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            final Uri photoURI = FileProvider.getUriForFile(this, "com.tengo.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
        }
    }

    private boolean lacksPermissions() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length == 4
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("activity result", "response with code " + requestCode);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            makeImagePostRequest();
        }
    }

    private void makeHandshake() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("clientId", "15")
                .build();

        final String postUrl = BASE_URL + "/handshake";
        AnimationUtils.fadeInToVisible(mLoadingSpinner);
        Networking.postRequest(postUrl, requestBody, new Networking.NetworkDelegate() {
            @Override
            public void onSuccess(final Response response) {
                networkUIHandler("Handshake completed");
            }
            @Override
            public void onFailure() {
                networkUIHandler("Network failure occurred!");
            }
        });
    }

    private void makeImagePostRequest() {
        byte[] bytes = ImageUtils.getImageBytes(mImagePath);
        if (bytes == null)
            throw new RuntimeException("Cannot get bytes for image");

        RequestBody postBodyImage = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "food.jpg", RequestBody.create(MediaType.parse("image/*jpg"), bytes))
                .build();

        final String postUrl = BASE_URL + "/ingredient";
        AnimationUtils.fadeInToVisible(mLoadingSpinner);
        Networking.postRequest(postUrl, postBodyImage, new Networking.NetworkDelegate() {
            @Override
            public void onSuccess(final Response response) {
                try {
                    String addedItem = response.body().string();
                    String outputString = addedItem.isEmpty() ? "Could not recognize any items" : "Added " + addedItem;
                    networkUIHandler(outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                    networkUIHandler(null);
                }
            }

            @Override
            public void onFailure() {
                networkUIHandler("Network failure occurred!");
            }
        });
    }

    private void makeRecipePostRequest() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("maxResults", "8")
                .build();

        final String postUrl = BASE_URL + "/recipe";
        AnimationUtils.fadeInToVisible(mLoadingSpinner);
        Networking.postRequest(postUrl, requestBody, new Networking.NetworkDelegate() {
            @Override
            public void onSuccess(final Response response) {
                JSONObject jsonObject;
                Recipe[] recipes;
                try {
                    String jsonResponse = response.body().string();
                    networkUIHandler(jsonResponse.isEmpty() ? "No relevant recipes" : null);
                    jsonObject = new JSONObject(jsonResponse);
                    String temp = jsonObject.getJSONArray("results").toString();
                    Log.e("response value", temp);
                    recipes = new Gson().fromJson(temp, Recipe[].class);
                } catch (Exception e) {
                    e.printStackTrace();
                    networkUIHandler(null);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("recipes", recipes);
                Intent intent = new Intent(getApplicationContext(), RecipeResults.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                networkUIHandler("Network failure occurred!");
            }
        });
    }

    private void networkUIHandler(@Nullable final String toastText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AnimationUtils.fadeOutToGone(mLoadingSpinner);
                if (toastText != null)
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private @NonNull
    File createImageFile() throws IOException {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        final File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e("Storage", storageDir.getAbsolutePath());
        final File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mImagePath = image.getAbsolutePath();
        return image;
    }

}
