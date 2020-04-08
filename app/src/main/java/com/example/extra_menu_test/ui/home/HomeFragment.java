package com.example.extra_menu_test.ui.home;

import android.Manifest;
import java.util.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.extra_menu_test.R;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ImageView imv;
    Uri imgUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getView().findViewById(R.id.imageView);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void showImg() {
        int iw, ih, vw, vh;
        boolean needRotate;

        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this.getActivity(), "讀取照片資訊時發生錯誤", Toast.LENGTH_LONG).show();
            return;
        }
        iw = option.outWidth;
        ih = option.outHeight;
        vw = imv.getWidth();
        vh = imv.getHeight();

        int scaleFactor = Math.min(iw/vw, ih/vh);

        option.inJustDecodeBounds = false;
        option.inSampleSize = scaleFactor;

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver()
                    .openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this.getActivity(), "無法取得照片", Toast.LENGTH_LONG).show();
        }
        imv.setImageBitmap(bmp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);
//                    it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    getActivity().sendBroadcast(it);

                    break;
                case 101:
                    imgUri = data.getData();
                    break;
            }
            showImg();
//                    Bitmap bmp = null;
//                    try {
//                        bmp = BitmapFactory.decodeStream(getContentResolver()
//                                .openInputStream(imgUri), null, null);
//                    } catch (IOException e) {
//                        Toast.makeText(this, "無法取得照片", Toast.LENGTH_LONG).show();
//                    }
//                    imv.setImageBitmap(bmp);


//                    break;

//
//                    break;
//            } else {
//            Toast.makeText(this, "沒有拍到照片", Toast.LENGTH_LONG).show();
//        }
//            showImg();

        } else {
            Toast.makeText(this.getActivity(), requestCode == 100 ? "沒有拍到照片" : "沒有選取相片", Toast.LENGTH_LONG).show();
        }


    }



    public void onPick(View v) {
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        startActivityForResult(it, 101);
    }

    public void onGet(View v) {
//        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(it, 100);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        } else {
            savePhoto();
        }

    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePhoto();
            } else {
                Toast.makeText(this.getActivity(), "程式需要寫入權限才能運作", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePhoto(){
        imgUri = getActivity().getApplicationContext().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new ContentValues()
        );
        Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
        it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(it, 100);
    }

}