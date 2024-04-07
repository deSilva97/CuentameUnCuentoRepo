package es.unir.cuentameuncuento.helpers;

import android.app.Activity;
import android.content.Intent;

public class ActivityHelper {

    public static void ChangeActivity(Activity from, Class<?> to, boolean returnable){
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
        if(returnable){
            from.finish();
        }
    }
}
