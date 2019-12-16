package com.example.moviefiendver2.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.moviefiendver2.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

//이미지 다운로드 하기 위한 클래스
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String urlStr;
    private ImageView imageView;

    private static HashMap<String, Bitmap> bitmapHash = new HashMap<>();

    public ImageLoadTask(String url, ImageView imageView){
        this.urlStr = url;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try {
            //전에 비트맵 파일이 이미 받아온 것이라면 삭제해버이고 새로 받는다. 메모리 효율을 위해(out of memory 예방)
            if(bitmapHash.containsKey(urlStr)){
                Bitmap oldBitmap = bitmapHash.remove(urlStr);
                if(oldBitmap != null && !oldBitmap.isRecycled()){   //가끔씩 런타임 에러가 발생한다. 어떻게 해결해야 하나;
                    oldBitmap.recycle();
                    oldBitmap = null;
                }
            }
            URL url = new URL(urlStr);

            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream()); //이미지 다운로드
            bitmapHash.put(urlStr, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);   //doInBackground 에서 반환한 bitmap을 여기서 받아서 imageView에 설정해준다.
        imageView.invalidate();
    }
}
