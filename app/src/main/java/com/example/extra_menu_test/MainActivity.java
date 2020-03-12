package com.example.extra_menu_test;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.MediaStore;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    Uri imgUri;
    ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv = findViewById(R.id.imageView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    public void onGet(View v) {
//        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(it, 100);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        } else {
            savePhoto();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch(requestCode) {
                case 100:
                    Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imgUri);
//                    it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    sendBroadcast(it);

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
            Toast.makeText(this, requestCode == 100 ? "沒有拍到照片" : "沒有選取相片", Toast.LENGTH_LONG).show();
        }


    }

    public void showImg() {
        int iw, ih, vw, vh;
        boolean needRotate;

        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "讀取照片資訊時發生錯誤", Toast.LENGTH_LONG).show();
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
            bmp = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(imgUri), null, option);
        } catch (IOException e) {
            Toast.makeText(this, "無法取得照片", Toast.LENGTH_LONG).show();
        }
        imv.setImageBitmap(bmp);
    }

    public void onPick(View v) {
        Intent it = new Intent(Intent.ACTION_GET_CONTENT);
        it.setType("image/*");
        startActivityForResult(it, 101);
    }

    private void savePhoto() {
        imgUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new ContentValues()
        );
        Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
        it.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(it, 100);
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePhoto();
            } else {
                Toast.makeText(this, "程式需要寫入權限才能運作", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
