package es.unir.cuentameuncuento.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.unir.cuentameuncuento.R;

public class ImageConverter {

    public interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
        void onError(String errorMessage);
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void convertUrlToBitmap(String imageUrl, BitmapCallback callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true);
                callback.onBitmapLoaded(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onError(String.valueOf(R.string.error_at_load_img_url));
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}