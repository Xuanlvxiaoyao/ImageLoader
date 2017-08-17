package com.example.imageloader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends FragmentActivity {
    private Button save,show;
    private OkHttpClient okHttpClient=new OkHttpClient();
    private String url="http://p5.so.qhimgs1.com/t01edf13c37f278a70b.jpg";
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
    }



    private void initview() {
      show= (Button) findViewById(R.id.show_btn);
        save= (Button) findViewById(R.id.save_btn);
        img= (ImageView) findViewById(R.id.iv);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder().url(url)
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] bytes = response.body().bytes();
                        ImageLoaderUtils.memory_saveImg(url,bytes);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "保存图片成功", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              img.setImageBitmap(ImageLoaderUtils.memory_getImg(url));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoaderUtils.memory_removeImg(url);
    }
}
