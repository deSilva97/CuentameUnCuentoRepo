package es.unir.cuentameuncuento.api.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class StoryRequestBody {
    @SerializedName("model")
    private String model;
    @SerializedName("messages")
    private List<Message> messages;


    public StoryRequestBody(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public StoryRequestBody(){

    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
