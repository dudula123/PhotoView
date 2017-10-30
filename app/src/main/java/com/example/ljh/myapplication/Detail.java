package com.example.ljh.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    private ImageView image;
    private Button left;
    private Button back;
    private Button rigth;
    int position;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        image=(ImageView) findViewById(R.id.detail_image);

        final Bundle bundle = getIntent().getExtras();
        final String parentfile = bundle.getString("parentfile");
        final ArrayList arrayList=bundle.getStringArrayList("mpath");
        position=bundle.getInt("position");
        path=parentfile+"/"+ arrayList.get(position);

        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        left=(Button) findViewById(R.id.left);
        back=(Button) findViewById(R.id.back);
        rigth=(Button) findViewById(R.id.right);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position <= 0) {
                    Toast.makeText(Detail.this, "该图片已是第一张图", Toast.LENGTH_SHORT).show();
                } else {
                    position = position - 1;
                    path = parentfile + "/" + arrayList.get(position);
                    try {
                        FileInputStream fis = new FileInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        rigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num=arrayList.size()-1;
                if (position >= num) {
                    Toast.makeText(Detail.this, "该图片已是最后一张图", Toast.LENGTH_SHORT).show();
                } else {
                    position = position + 1;
                    path = parentfile + "/" + arrayList.get(position);
                    try {
                        FileInputStream fis = new FileInputStream(path);
                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
                        image.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        }

    }




