package com.serionz.alcproject.emojify;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.serionz.alcproject.R;

import java.io.File;
import java.io.IOException;

public class EmojifyMainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    private static final String FILE_PROVIDER_AUTHORITY = "com.serionz.alcproject.fileprovider";

    private ImageView mImageView;

    private Button mEmojifyButton;
    private FloatingActionButton mSaveFab;
    private FloatingActionButton mShareFab;
    private FloatingActionButton mClearFab;

    private TextView mTitleTextView;

    private String mTempPhotoPath;
    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emojify_main);

        // Bind the views
        mImageView = (ImageView) findViewById(R.id.image_view);
        mEmojifyButton = (Button) findViewById(R.id.emojify_button);
        mSaveFab = (FloatingActionButton) findViewById(R.id.save_button);
        mShareFab = (FloatingActionButton) findViewById(R.id.share_button);
        mClearFab = (FloatingActionButton) findViewById(R.id.clear_button);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);

    }

    public void emojifyMe(View view) {
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private void launchCamera() {
        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Temporary file where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else {
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    private void processAndSetImage() {
        // Toggle Visibility of Views
        mEmojifyButton.setVisibility(View.GONE);
        mTitleTextView.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.VISIBLE);
        mClearFab.setVisibility(View.VISIBLE);

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);

        // Detect Faces
        Emojifier.detectFaces(this, mResultsBitmap);

        mImageView.setImageBitmap(mResultsBitmap);
    }

    public void saveMe(View view) {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
    }

    public void shareMe(View view) {
        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        BitmapUtils.saveImage(this, mResultsBitmap);
        BitmapUtils.shareImage(this, mTempPhotoPath);
    }

    public void clearImage(View view) {
        mImageView.setImageResource(0);
        mEmojifyButton.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mShareFab.setVisibility(View.GONE);
        mSaveFab.setVisibility(View.GONE);
        mClearFab.setVisibility(View.GONE);

        BitmapUtils.deleteImageFile(this, mTempPhotoPath);
    }
}
