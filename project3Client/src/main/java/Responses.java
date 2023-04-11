import java.io.Serializable;
import java.util.ArrayList;

public class Responses implements Serializable {
	private static final long serialVersionUID = 1L;
    private int response;
    private String message;
    private Object data;
    ArrayList<Card> cards = new ArrayList<>();

    public Responses(int response) {
        this.response = response;
        message = "Dog Shit";
    }
    public Responses(int response, ArrayList<Card> cards) {
        this.response = response;
        this.cards = cards;
    }

    public int getResponse() {
        return response;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Responses{" +
                "status=" + response +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}