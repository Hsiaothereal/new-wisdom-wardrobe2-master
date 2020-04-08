package com.example.extra_menu_test.ui.gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.extra_menu_test.R;

import java.util.ArrayList;
import java.util.Arrays;

public class GalleryFragment extends Fragment {

    ArrayList<Integer> mImageIds = new ArrayList<>(Arrays.asList(
            R.mipmap.a05, R.mipmap.a06, R.mipmap.a07, R.mipmap.a08, R.mipmap.a09, R.mipmap.a10, R.mipmap.a11, R.mipmap.a12, R.mipmap.a13
    ));

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

//        GridView gridView = findViewById(R.id.myGrid);
//        gridView.setAdapter(new ImageAdaptor(mImageIds, this));
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int item_pos = mImageIds.get(i);
//
//                ShowDialogBox(item_pos);
//            }
//        });
        return root;
    }

//    public void ShowDialogBox(final int item_pos) {
//        final Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.custom_diaog);
//
//        ImageView image = dialog.findViewById(R.id.img);
//
//        String title = getResources().getResourceName(item_pos);
//
//        int index = title.indexOf("/");
//        String name = title.substring(index+1, title.length());
//
//        image.setImageResource(item_pos);
//
//        dialog.show();
    }
