package com.example.campus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

public class CreateAnnouncementActivity extends AppCompatActivity {
    public static final String TAG = "CreateAnnouncementsActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivAnnouncementImage;
    private Button btnSubmit;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    Club club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        club = (Club) Parcels.unwrap(getIntent().getParcelableExtra(Club.class.getSimpleName()));



        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivAnnouncementImage = findViewById(R.id.ivAnnouncementImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
                Intent i = new Intent(CreateAnnouncementActivity.this, AnnouncementsActivity.class);
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
        Uri fileProvider = FileProvider.getUriForFile(CreateAnnouncementActivity.this, "campus.com.codepath.fileprovider", photoFile);
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
                ivAnnouncementImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        if(!description.isEmpty()){
            post.setDescription(description);
        }

        if(photoFile !=null){
            post.setImage(new ParseFile(photoFile));
        }

        post.setPoster(currentUser);
        post.setClub(club);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!= null){
                    Toast.makeText(CreateAnnouncementActivity.this, "Error while Saving!", Toast.LENGTH_SHORT).show();
                }
                etDescription.setText(" ");
                ivAnnouncementImage.setImageResource(0);
            }
        });
    }


}