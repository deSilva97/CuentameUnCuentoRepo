package es.unir.cuentameuncuento.impls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.utils.BitmapEncoder;

public class IconStorageDAOImpl {

    FirebaseStorage storage;

    String userID;

    Context context;

    public IconStorageDAOImpl(Context context) {
        storage = FirebaseStorage.getInstance();
        this.context = context;
        userID = UserDAOImpl.getIdUser();
    }

    private static String pathIcon(String endpoint){
        return UserDAOImpl.getIdUser() + "/" + endpoint;
    }

    public void create(Bitmap image, String imageID, BookDAOImpl.CompleteCallbackWithDescription callback){
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(pathIcon(imageID));

        byte[] data = BitmapEncoder.encodeBitmap(image);

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask
                .addOnFailureListener(exception -> callback.onComplete(false, context.getString(R.string.image_generate_fails)))
                .addOnSuccessListener(taskSnapshot -> callback.onComplete(true, context.getString(R.string.image_generate_success)));
    }

    public static void read(BookAdapterElement element, String imageID,  CompleteCallbackWithBookElement callback){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child(pathIcon(imageID));

        final long ONE_MEGABYTE = 1024  * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            callback.onIconFounded(element, bitmap);
        }).addOnFailureListener(exception -> {
            callback.onIconFounded(element, null);
            if(exception instanceof StorageException){
                StorageException storageException = (StorageException) exception;
            }
        });
    }

    public void delete(String id){

        StorageReference storageRef = storage.getReference();

        StorageReference desertRef = storageRef.child(pathIcon(id));

        desertRef.delete();
    }

    public void deleteAll(){
        StorageReference storageRef = storage.getReference();
        StorageReference userFolderRef = storageRef.child(userID);

        userFolderRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        item.delete();
                    }
                });
    }

    private void findAllInPrefix(StorageReference prefix, List<Bitmap> iconList) {
        prefix.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference subPrefix : listResult.getPrefixes()) {
                findAllInPrefix(subPrefix, iconList);
            }
            for (StorageReference item : listResult.getItems()) {
                item.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    iconList.add(icon);
                });
            }
        });
    }

    public interface CompleteCallbackWithBookElement{
        void onIconFounded(BookAdapterElement element, Bitmap bitmap);
    }

}
