package com.example.zkf.ss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class PictureActivity extends Activity {
    public int[] imageId = new int[]{R.drawable.test1,R.drawable.test2,R.drawable.test3,R.drawable.test4,R.drawable.test5,R.drawable.test6
    };   // 定义并初始化保存图片id的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        GridView gridview = (GridView) findViewById(R.id.gridView); 			// 获取GridView组件
        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ImageView imageview;							//声明ImageView的对象
                if(convertView==null){
                    imageview=new ImageView(PictureActivity.this);		//实例化ImageView的对象
                    /*************设置图像的宽度和高度******************/
                    imageview.setAdjustViewBounds(true);
                    imageview.setMaxWidth(158);
                    imageview.setMaxHeight(150);
                    /**************************************************/
                    imageview.setPadding(5, 5, 5, 5);				//设置ImageView的内边距
                }else{
                    imageview=(ImageView)convertView;
                }
                imageview.setImageResource(imageId[position]);		//为ImageView设置要显示的图片
                return imageview;	//返回ImageView
            }
            /*
             * 功能：获得当前选项的ID
             */
            @Override
            public long getItemId(int position) {
                return position;
            }
            /*
             * 功能：获得当前选项
             */
            @Override
            public Object getItem(int position) {
                return position;
            }
            /*
             * 获得数量
             */
            @Override
            public int getCount() {
                return imageId.length;
            }
        };

        gridview.setAdapter(adapter); 									// 将适配器与GridView关联
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();    //获取Intent对象
                Bundle bundle = new Bundle();    //实例化要传递的数据包
                bundle.putInt("imageId", imageId[position]);// 显示选中的图片
                intent.putExtras(bundle);    //将数据包保存到intent中
                setResult(0x11, intent);    //设置返回的结果码，并返回调用该Activity的Activity
                finish();        //关闭当前Activity
            }
        });

    }
}
