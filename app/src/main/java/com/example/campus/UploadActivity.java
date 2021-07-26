package com.example.campus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.campus.ui.home.HomeFragment;
import com.example.campus.ui.profile.ProfileFragment;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private ImageButton homeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        homeButton = findViewById(R.id.homeButton);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photoFile==null|| ivPostImage.getDrawable()== null){
                    Toast.makeText(UploadActivity.this,"There is no image !" , Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePic(currentUser, photoFile);

                FragmentManager fm = getSupportFragmentManager();
                ProfileFragment fragment = new ProfileFragment();
                fm.beginTransaction().add(R.id.navigation_dashboard,fragment).commit();

            }

        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to go to the FeedActivity
                Intent i = new Intent(UploadActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName){
        // Get Safe directory for photos
        // Use "getExternalFileDir" on context to access package specific directories
        // This way we don't need to request read/write runtime permissions
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist yet
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");

        }
        // Return the file Target for the photo based on filename
        File file = new File(mediaStorageDir.getPath()+ File.separator+ fileName);
        return file;
    }

    private void launchCamera() {
        // create intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap file into a content provider

        // required for API >= 24
        Uri fileProvider = FileProvider.getUriForFile(UploadActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // if you call startActivityResult() using an intent that no app can handle, your app will crash
        //So long as the result is not null, it's safe to use the intent
        if(intent.resolveActivity(getPackageManager())!= null){
            // Start the image capture intent to take a photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePic(ParseUser currentUser, File photoFile) {
        currentUser.put("Profile_pic", new ParseFile(photoFile));
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!= null){
                    Toast.makeText(UploadActivity.this, "Error while Saving!", Toast.LENGTH_SHORT).show();
                }
                ivPostImage.setImageResource(0);
            }
        });
    }



}