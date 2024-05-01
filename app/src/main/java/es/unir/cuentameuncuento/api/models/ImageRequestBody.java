package es.unir.cuentameuncuento.api.models;

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

    public ImageRequestBody(String model, String prompt, int n, String size) {
        this.model = model;
        this.prompt = prompt;
        this.n = n;
        this.size = size;
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


    @Override
    public String toString() {
        return "ImageRequestBody{" +
                "model='" + model + '\'' +
                ", prompt='" + prompt + '\'' +
                ", n=" + n +
                ", size='" + size + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRequestBody that = (ImageRequestBody) o;
        return n == that.n && Objects.equals(model, that.model) && Objects.equals(prompt, that.prompt) && Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, prompt, n, size);
    }
}