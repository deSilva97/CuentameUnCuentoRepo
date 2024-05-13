package es.unir.cuentameuncuento.impls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.adapters.BookAdapterElement;
import es.unir.cuentameuncuento.models.Book;
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

    public void create(Bitmap image, String imageID, BookDAOImpl.CompleteCallbackWithDescription callback){
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
                callback.onComplete(false, "failure on image generate");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("BookDAOImpl", "Picture  uploaded");
                callback.onComplete(true, "success on image generate");
            }
        });
    }

    public static void read(BookAdapterElement element, String imageID,  CompleteCallbackWithBookElement callback){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child(pathIcon(imageID));
        Log.d("StoryIcon", "image downloaded=" + imageID);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);;
                callback.onIconFounded(element, bitmap);
                Log.d("StoryIcon", "download success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                callback.onIconFounded(element, null);
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

    public void deleteAll(){
        StorageReference storageRef = storage.getReference();
        StorageReference userFolderRef = storageRef.child(userID);

        userFolderRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // Eliminar todos los archivos listados
                    for (StorageReference item : listResult.getItems()) {
                        item.delete().addOnSuccessListener(aVoid -> {
                            Log.d("BookDAOImpl", "Archivo borrado: " + item.getName());
                        }).addOnFailureListener(exception -> {
                            Log.e("BookDAOImpl", "No se ha podido borrar el archivo: " + item.getName(), exception);
                        });
                    }

//                    // Si necesitas manejar subdirectorios, deberías llamar recursivamente a deleteAll() aquí
//                    for (StorageReference prefix : listResult.getPrefixes()) {
//                        deleteAll(prefix.getPath());  // Debes asegurarte de que este método pueda manejar paths completos o ajustar la lógica
//                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e("BookDAOImpl", "No se pudieron listar los archivos para borrar", exception);
                });
    }

//    public void findAll(CompleteCallback callback){
//        StorageReference listRef = storage.getReference().child(userID);
//
//        listRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        List<Bitmap> iconList = new ArrayList<>();
//
//                        // Recursivamente listar directorios, si necesitas explorar directorios anidados
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            findAllInPrefix(prefix, iconList);  // Este método debería ser similar a findAll, pero operando sobre el prefijo
//                        }
//
//                        // Iterar sobre cada ítem directo bajo el directorio
//                        for (StorageReference item : listResult.getItems()) {
//                            item.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
//                                // Convertir bytes a Bitmap
//                                Bitmap icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                iconList.add(icon);
//                                Log.d("IconStorageDAOImpl", "Icono agregado a la lista: " + item.getName());
//                            }).addOnFailureListener(e -> {
//                                Log.e("IconStorageDAOImpl", "Error al descargar el icono: " + item.getName(), e);
//                            });
//                        }
//
//                        callback.onComplete(iconList);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("IconStorageDAOImpl", "Error al listar ítems", e);
//                    }
//                });
//    }

    private void findAllInPrefix(StorageReference prefix, List<Bitmap> iconList) {
        prefix.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference subPrefix : listResult.getPrefixes()) {
                findAllInPrefix(subPrefix, iconList); // Llamada recursiva para subdirectorios
            }
            for (StorageReference item : listResult.getItems()) {
                item.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap icon = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    iconList.add(icon);
                    Log.d("IconStorageDAOImpl", "Icono agregado a la lista desde subdirectorio: " + item.getName());
                }).addOnFailureListener(e -> {
                    Log.e("IconStorageDAOImpl", "Error al descargar el icono de subdirectorio: " + item.getName(), e);
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("IconStorageDAOImpl", "Error al listar ítems en subdirectorio: " + prefix.getPath(), e);
        });
    }

    public interface CompleteCallbackWithBookElement{
        void onIconFounded(BookAdapterElement element, Bitmap bitmap);
    }

}
