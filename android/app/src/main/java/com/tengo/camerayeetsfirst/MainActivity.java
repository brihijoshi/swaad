package com.tengo.camerayeetsfirst;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST = 2;

    private ImageView mCameraButton;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraButton = findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptCamera();
            }
        });
        Log.e("starting", "started app");
    }

    protected void attemptCamera() {
        Log.e("attempt camera", "attempting a camera open");
        if (lacksPermissions()) {
            String[] permissions = new String[3];
            permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            permissions[2] = Manifest.permission.CAMERA;
            requestPermissions(permissions, PERMISSIONS_REQUEST);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Log.e("state", "Opening camera");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            Uri photoURI = FileProvider.getUriForFile(this, "com.tengo.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e("Storage", storageDir.getAbsolutePath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean lacksPermissions() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 3
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!

                    openCamera();
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("activity result", "response with code " + requestCode);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri actualUri = data.getData();
                Log.e("camera data", "actual path is " + actualUri.toString());
            } else {
                Log.e("camera data", "camera null");
            }
            setPic();

            // TODO send to server
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("url")
                    .build();
            Requests.GitHubService service = retrofit.create(Requests.GitHubService.class);
            Call<List<String>> repos = service.listRepos("octocat");

//            repos.execute();

        }
        if (resultCode == RESULT_CANCELED) {
            Log.e("activity result", "activity cancelled");
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mCameraButton.getWidth();
        int targetH = mCameraButton.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        mCameraButton.setImageBitmap(bitmap);

        try {
            InputStream in = new FileInputStream(new File(currentPhotoPath));
            byte[] buf = new byte[in.available()];
            while (in.read(buf) != -1);
            Log.e("bytes", Arrays.toString(buf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
