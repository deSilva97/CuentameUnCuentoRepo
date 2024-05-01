package es.unir.cuentameuncuento.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapHelper {

    /**
     * Convierte un Bitmap a un array de bytes.
     * @param bitmap El Bitmap que se va a convertir.
     * @return Un array de bytes representando el Bitmap en formato JPEG.
     */
    public static byte [] ConvertBitmap(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        return data;
    }

    /**
     * Convierte un Bitmap a un array de bytes.
     * @param data El Bitmap que se va a convertir.
     * @return Un array de bytes representando el Bitmap en formato JPEG.
     */
    public static Bitmap VectorizeBitmap(byte[] data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }
}
