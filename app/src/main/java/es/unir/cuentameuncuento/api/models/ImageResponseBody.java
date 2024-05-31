package es.unir.cuentameuncuento.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ImageResponseBody {
    @SerializedName("created")
    private long created;

    @SerializedName("data")
    private List<ImageData> data;


    public long getCreated() {
        return created;
    }
    public List<ImageData> getData() {
        return data;
    }
}
