package com.example.ljh.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljh.myapplication.Bean.FolderBean;
import com.example.ljh.myapplication.Util.ImageAdapter;
import com.example.ljh.myapplication.Util.PopWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private GridView mGridLayoutl;
    private List<String> mImgs;
    private ImageAdapter mImageAdapter;
    private RelativeLayout bottomLayout;
    private TextView mDirName;
    private File mCurrentFile;
    private List<FolderBean> mFolderBeanList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private PopWindow mPopuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
        initEvents();
    }

    //初始化主界面
    private void initView() {
        mGridLayoutl = (GridView) findViewById(R.id.id_gridview);
        mGridLayoutl.setBackgroundDrawable(getResources().getDrawable(R.mipmap.backpicture));

        bottomLayout = (RelativeLayout) findViewById(R.id.bootom_layout);
        mDirName = (TextView) findViewById(R.id.bottom_dir);
        bottomLayout.getBackground().setAlpha(150);

        //初始化popwindow
        mPopuWindow = new PopWindow(this,mFolderBeanList);

        mPopuWindow.setOnSelectedListener(new PopWindow.OnSelectedListener() {
            @Override
            public void onSelected(FolderBean bean) {
                Log.i("aaca","popupWindow selected");
                mCurrentFile = new File(bean.getDir());
                mImgs = Arrays.asList(mCurrentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".png")||name.endsWith(".jpg")){
                            return  true;
                        }
                        return false;
                    }
                }));
                mImageAdapter = new ImageAdapter(MainActivity.this
                        ,mImgs
                        ,mCurrentFile.getAbsolutePath());
                mGridLayoutl.setAdapter(mImageAdapter);

                mGridLayoutl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        List<String> mpaths=  mImageAdapter.mImagePaths;
                        ArrayList arrayList = new ArrayList();
                        for(int i=0,j=mpaths.size();i<j;i++){
                            arrayList.add(i,mpaths.get(i));
                        }
                        Intent intent=new Intent(MainActivity.this,Detail.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("parentfile",mCurrentFile.getAbsolutePath());
                        mBundle.putStringArrayList("mpath",arrayList);
                        mBundle.putInt("position",position);
                        intent.putExtras(mBundle);
                        startActivity(intent);

                    }
                });
                mDirName.setText(mCurrentFile.getName());
                mPopuWindow.dismiss();
            }
        });
    }

    private Handler mHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
            bindDatasToGridView();
        }
    };

    private void bindDatasToGridView() {
        if (mCurrentFile==null) {
            Toast.makeText(this, "没有图片", Toast.LENGTH_LONG);
            return;
        }
        mImgs = Arrays.asList(mCurrentFile.list());

        mImageAdapter=new ImageAdapter(this,mImgs,mCurrentFile.getAbsolutePath());
        mGridLayoutl.setAdapter(mImageAdapter);
        mDirName.setText(mCurrentFile.getName());
    }

    //利用contentprovider扫描所有图片
    private Set<String> mDir = new HashSet<>();
    private void initDatas() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_LONG);
            return;
        }
        progressDialog = ProgressDialog.show(this, null, "loading...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = MainActivity.this.getContentResolver();

                Cursor cursor = cr.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);
                while (cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile==null){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if (mDir.contains(dirPath)){
                        continue;
                    }else {
                        mDir.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgDir(path);
                    }
                    if (parentFile.list()==null){
                        continue;
                    }
                    mFolderBeanList.add(folderBean);
                }
                cursor.close();
                mDir=null;
                //图片查询完成
                mHandle.sendEmptyMessage(2);
            }
        }).start();

    }
    //初始化底部PopuWindow事件
    private void initEvents() {
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopuWindow.showAsDropDown(bottomLayout,0,0);
                mGridLayoutl.setBackgroundColor(Color.parseColor("#E8E8E7"));

            }
        });
    }
}
