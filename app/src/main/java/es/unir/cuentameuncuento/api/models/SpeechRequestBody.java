package es.unir.cuentameuncuento.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SpeechRequestBody {
    @SerializedName("model")
    private String model;

    @SerializedName("input")
    private String input;

    @SerializedName("voice")
    private String voice;

    public SpeechRequestBody(String model, String input, String voice) {
        this.model = model;
        this.input = input;
        this.voice = voice;
    }

    public SpeechRequestBody() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    @Override
    public String toString() {
        return "SpeechRequestBody{" +
                "model='" + model + '\'' +
                ", input='" + input + '\'' +
                ", voice='" + voice + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeechRequestBody that = (SpeechRequestBody) o;
        return Objects.equals(model, that.model) && Objects.equals(input, that.input) && Objects.equals(voice, that.voice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, input, voice);
    }
}