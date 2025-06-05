package com.example.remember;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    static final int PICK_IMAGE_REQUEST = 1;
    Button loginb, signupb, uploadphotob, changenameb;
    ImageView avatar;
    TextView nameinput;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        loginb = view.findViewById(R.id.login);
        signupb = view.findViewById(R.id.signup);
        uploadphotob = view.findViewById(R.id.uploadphoto);
        changenameb = view.findViewById(R.id.changename);
        avatar = view.findViewById(R.id.imageView);
        nameinput = view.findViewById(R.id.name);


        uploadphotob.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        });
        changenameb.setOnClickListener(v -> {
            SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
            String name = nameinput.getText().toString().trim();
            if (name.isEmpty() || name.length() > 10){
                Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            } else preferences.edit().putString("name",name).apply();

        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri sourceUri = data.getData();

            // Destination file URI
            Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped.jpg"));

            // Start uCrop activity
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(1, 1) // Square crop
                    .withMaxResultSize(512, 512)
                    .start(requireContext(), this);
        }

        // uCrop result
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                avatar.setImageURI(resultUri);

                // Save cropped image to internal storage if needed
                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(resultUri);
                    File avatarFile = new File(requireContext().getFilesDir(), "avatar.jpg");
                    OutputStream outputStream = new FileOutputStream(avatarFile);

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    inputStream.close();
                    outputStream.close();

                    SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
                    preferences.edit().putString("imagePath", avatarFile.getAbsolutePath()).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            cropError.printStackTrace();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        String path = preferences.getString("imagePath", null);
        if (path != null) {
            File avatarFile = new File(path);
            if (avatarFile.exists()) {
                avatar.setImageURI(Uri.fromFile(avatarFile));
            }
        }
        String name = preferences.getString("name", null);
        if (name != null){
            nameinput.setText(name);
        }
    }
}