package com.example.hai.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends Activity {
    Button button1;
    Button button2;
    ImageView imageView;
    TextView textView;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        button1= (Button) findViewById(R.id.btn1);//获取选择图片按钮
        button1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //创建intent对象
                Intent intent = new Intent(MainActivity.this,PictureActivity.class);
                startActivityForResult(intent, 0x11);//启动intent对应的Activity
            }
        });
        button2 = (Button) findViewById(R.id.btn2);//获取开始识别按钮
        button2.setOnClickListener(new View.OnClickListener(){



            @Override
            public void onClick(View v) {
               // BufferedImage image=null;
                //切割
               Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
               // String str = String.valueOf(image.getPixel(10,8));
              // Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                //image.getPixel(0,0);
                ArrayList<Integer> leftList=new ArrayList<Integer>();
                ArrayList<Integer> rightList=new ArrayList<Integer>();
                ArrayList<Integer> upList=new ArrayList<Integer>();
                ArrayList<Integer> downList=new ArrayList<Integer>();
                int flag1=0;//0为无黑1有
                int flag2=0;//当前列
                for(int j=0;j<image.getWidth();j++) {
                    flag2=0;
                    for (int i=0;i<image.getHeight();i++) {
                        if(image.getPixel(j,i) !=-1) {flag2=1;break;}
                    }
                    if(flag1==0 && flag2==1) {
                        leftList.add(j-1);flag1=1;
                    }
                    if(flag1==1 && flag2==0) {
                        rightList.add(j-1);flag1=0;
                    }
                }

                for(int p=0;p<leftList.size();p++) {
                    flag1=0;
                    for(int i=0;i<image.getHeight();i++) {
                        flag2=0;
                        for(int j=leftList.get(p);j<=rightList.get(p);j++) {
                            if(image.getPixel(j, i)!=-1) {flag2=1;break;}
                        }
                        if(flag1==0 && flag2==1) {
                            upList.add(i);flag1=1;
                        }
                        if(flag1==1 && flag2==0) {
                            downList.add(i-1);flag1=0;
                        }
                    }
                }
                ArrayList<Bitmap> imageList=new ArrayList<Bitmap>();
                for(int i=0;i<upList.size();i++) {
                    imageList.add(Bitmap.createBitmap(rightList.get(i)-leftList.get(i), downList.get(i)-upList.get(i), Bitmap.Config.RGB_565));
                }
                int n=0;
                for(Bitmap img:imageList) {
                    for(int i=0;i<img.getWidth();i++) {
                        for(int j=0;j<img.getHeight();j++) {
                            img.setPixel(i, j, image.getPixel(i+leftList.get(n), j+upList.get(n)));
                        }
                    }
                    n++;
                }
                //判断
              ArrayList<Integer> list=new ArrayList<Integer>();
                for(Bitmap img:imageList) {
                    int max=-1;
                    int num = -1;
                    for(int ind=0;ind<10;ind++) {
                        int sum=0;

                        Field field = null;
                        try {
                            field =R.drawable.class.getField("im"+ind);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        int DrawableId = 0;
                        try {
                            DrawableId = field.getInt(new R.drawable());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        InputStream is = getResources().openRawResource(DrawableId);
                        Bitmap images = BitmapFactory.decodeStream(is);;
                        for(int i=0;i<img.getWidth();i++) {
                            for(int j=0;j<img.getHeight();j++) {
                                if(img.getPixel(i,j)==images.getPixel(i, j)) {
                                    sum+=1;
                                }
                            }
                        }
                        if(sum>max) {
                            max=sum;
                            num=ind;
                        }

                    }
                    list.add(num);
                }


                str = "Result: "+String.valueOf(list);
                //Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
                textView = (TextView) findViewById(R.id.textview);
                textView.setText(str);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11 && resultCode==0x11){	//判断是否为待处理的结果
            Bundle bundle=data.getExtras();		//获取传递的数据包
            int imageId=bundle.getInt("imageId");	//获取选择的图片ID
            ImageView iv=(ImageView)findViewById(R.id.imageView);	//获取布局文件中添加的ImageView组件
            iv.setImageResource(imageId);	//显示选择的图片
        }
    }
}
