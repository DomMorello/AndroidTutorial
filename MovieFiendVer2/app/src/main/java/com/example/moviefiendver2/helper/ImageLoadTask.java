//package com.example.moviefiendver2.helper;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.widget.ImageView;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.HashMap;
//

/* 이 클래스는 매우 느리고 recycle관련 에러가 계속 발생해 Glide 라이브러리로 모두 바꿈. */


////이미지 다운로드 하기 위한 클래스
//public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
//
//    private String urlStr;
//    private ImageView imageView;
//
//    private static HashMap<String, Bitmap> bitmapHash = new HashMap<>();
//
//    public ImageLoadTask(String url, ImageView imageView) {
//        this.urlStr = url;
//        this.imageView = imageView;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
//
//    /* Canvas: trying to use a recycled bitmap android.graphics.Bitmap@36a2934
//      앱을 실행 중에 가끔씩 이런 오류가 뜨면서 앱이 비정상 종료되는데 이 클래스에 대한 정확한 작동원리 등을
//      잘 몰라서 고치는 것에 실패함. recycle된 것을 또 사용하려고 한다는데 그게 어느 부분인지도 잘 모르겠음. */
//
//    @Override
//    protected Bitmap doInBackground(Void... voids) {
//        Bitmap bitmap = null;
//        try {
//            //전에 비트맵 파일이 이미 받아온 것이라면 삭제해버리고 새로 받는다. 메모리 효율을 위해(out of memory 예방)
//            if (bitmapHash.containsKey(urlStr)) {
//                Bitmap oldBitmap = bitmapHash.remove(urlStr);
//                if (oldBitmap != null || !oldBitmap.isRecycled()) {
//                    oldBitmap.recycle();
//                    oldBitmap = null;
//                }
//            }
//            URL url = new URL(urlStr);
//
//            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream()); //이미지 다운로드
//            bitmapHash.put(urlStr, bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//
//        imageView.setImageBitmap(bitmap);   //doInBackground 에서 반환한 bitmap을 여기서 받아서 imageView에 설정해준다.
//        imageView.invalidate();
//    }
//}
