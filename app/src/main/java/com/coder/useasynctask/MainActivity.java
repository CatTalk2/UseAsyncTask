package com.coder.useasynctask;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    private ImageView imageView;
    //ͼƬ��ַ
    private final String url="http://www.jycoder.com/json/movies/2.jpg";
    //��ʾ����
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //������ʾ����
        pDialog=new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Loading...");
        pDialog.setMax(100);

        imageView= (ImageView) findViewById(R.id.movie_image);

        //ִ��Task
        new ImageDownloadTask().execute(url);

    }

    public class ImageDownloadTask extends AsyncTask<String,Integer,Bitmap>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {

            HttpURLConnection connection = null;

            Bitmap bitmap=null;
            try {
                URL url=new URL(urls[0]);
                connection= (HttpURLConnection) url.openConnection();

                //��ȡ�ļ�����С
                int length=connection.getContentLength();
                int len=0,total_length=0,value=0;
                byte[] data=new byte[1024];

                InputStream in= new BufferedInputStream(connection.getInputStream());

                bitmap= BitmapFactory.decodeStream(in);


                while((len = in.read(data)) != -1){
                    total_length += len;
                    value = (int)((total_length/(float)length)*100);
                    //����update���������½���
                    publishProgress(value);
                }



                } catch (IOException e) {
                e.printStackTrace();
            }finally {
                    if (connection!=null)
                    connection.disconnect();
            }
            return bitmap;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if(pDialog!=null)
                pDialog.dismiss();
            pDialog = null;
            //��Bitmap����Imageview
            imageView.setImageBitmap(bitmap);

        }


    }



}
