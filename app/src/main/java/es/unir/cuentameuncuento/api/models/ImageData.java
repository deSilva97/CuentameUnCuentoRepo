package es.unir.cuentameuncuento.api.models;

import com.google.gson.annotations.SerializedName;

public class ImageData {

    @SerializedName("revised_prompt")
    private String revised_prompt;
    @SerializedName("url")
    private String url;

    public String getRevised_prompt() {
        return revised_prompt;
    }

    public String getUrl() {
        return url;
    }
}

