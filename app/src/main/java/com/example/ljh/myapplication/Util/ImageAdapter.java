package com.example.ljh.myapplication.Util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ljh.myapplication.R;

import java.util.List;

//适用于GridView的Adapter
public class ImageAdapter extends BaseAdapter {

    public List<String> mImagePaths;
    private String path;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<String>mDatas, String path) {
        this.mImagePaths=mDatas;
        this.path=path;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView==null){
            convertView=inflater.inflate(R.layout.item,parent,false);
            imageView=(ImageView)convertView.findViewById(R.id.id_item_image);
            convertView.setTag(imageView);
        }else {

            imageView= (ImageView) convertView.getTag();
        }
        imageView.setImageResource(R.mipmap.pictures_no);
        imageView.setColorFilter(null);

        ImageLoader.getInstance(3)
                .loadImage(path+"/"+mImagePaths.get(position),imageView);
        return convertView;

    }

}
