package es.unir.cuentameuncuento.api.models;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("index")
    private int index;

    @SerializedName("message")
    private Message message;

    @SerializedName("logprobs")
    private Object logprobs; // Puedes reemplazar Object con el tipo apropiado si es necesario

    @SerializedName("finish_reason")
    private String finishReason;

    public Choice(int index, Message message, Object logprobs, String finishReason) {
        this.index = index;
        this.message = message;
        this.logprobs = logprobs;
        this.finishReason = finishReason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Object getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(Object logprobs) {
        this.logprobs = logprobs;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}