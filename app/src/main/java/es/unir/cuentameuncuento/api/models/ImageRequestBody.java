package es.unir.cuentameuncuento.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ImageRequestBody {
    @SerializedName("model")
    private String model;

    @SerializedName("prompt")
    private String prompt;

    @SerializedName("n")
    private int n;

    @SerializedName("size")
    private String size;

    @SerializedName("quality")
    private String quality;

    public ImageRequestBody(String model, String prompt, int n, String size, String quality) {
        this.model = model;
        this.prompt = prompt;
        this.n = n;
        this.size = size;
        this.quality = quality;
    }

    public ImageRequestBody() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImageRequestBody{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", n=" + n +
                ", size='" + size + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRequestBody that = (ImageRequestBody) o;
        return n == that.n && Objects.equals(model, that.model) && Objects.equals(prompt, that.prompt) && Objects.equals(size, that.size) && Objects.equals(quality, that.quality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, prompt, n, size, quality);
    }
}