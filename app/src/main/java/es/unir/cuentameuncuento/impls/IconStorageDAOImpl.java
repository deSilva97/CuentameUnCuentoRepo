package es.unir.cuentameuncuento.impls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import es.unir.cuentameuncuento.utils.BitmapEncoder;

public class IconStorageDAOImpl {

    FirebaseStorage storage;

    String userID;

    public IconStorageDAOImpl() {
        storage = FirebaseStorage.getInstance();
        userID = UserDAOImpl.getIdUser();
    }


    private static String pathIcon(String endpoint){
        return UserDAOImpl.getIdUser() + "/" + endpoint;
    }

    public void create(Bitmap image, String imageID){
        StorageReference storageRef = storage.getReference();
        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(pathIcon(imageID));

        Log.d("StoryIcon", "image uploaded=" + imageID);

        // Get the data from an ImageView as bytes
        byte[] data = BitmapEncoder.encodeBitmap(image);

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("BookDAOImpl", "Picture can not uploaded");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("BookDAOImpl", "Picture  uploaded");
            }
        });
    }

    public static void read(String userID, String imageID, BookDAOImpl.CompleteCallbackWithBitmap callback){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child(pathIcon(imageID));
        Log.d("StoryIcon", "image downloaded=" + imageID);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);;
                callback.onComplete(bitmap);
                Log.d("StoryIcon", "download success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                callback.onComplete(null);
                Log.e("StoryIcon", "download failure " + exception);

                if(exception instanceof StorageException){
                    StorageException storageException = (StorageException) exception;
                    Log.e("StoryIcon", "HTTP Result Code=" + storageException.getHttpResultCode());
                }


            }
        });
    }

    public void delete(String id){

        StorageReference storageRef = storage.getReference();

        StorageReference desertRef = storageRef.child(pathIcon(id));

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d("BookDAOImpl", "Icono borrado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.e("BookDAOImpl", "No se ha podido borrar el icono");
            }
        });
    }

}
