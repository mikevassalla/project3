import java.io.Serializable;
import java.util.ArrayList;

public class Responses implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer player;
    private int response;
    private String message;
    private Object data;
    private int ante;
    private int pair;
    private ArrayList<Card> cards = new ArrayList<>();
    ArrayList<String> cs = new ArrayList<>();

    public Responses(int response) {
        this.response = response;
    }
    public Responses(int response, int player) {
        this.response = response;
        this.player = player;
    }
    public Responses(int response, String message) {
        this.response = response;
        this.message = message;
    }
    public Responses(int response, ArrayList<Card> cards) {
        this.response = response;
        this.cards = cards;
    }
    public Responses(int response, ArrayList<String> cs, String n) {
        this.response = response;
        this.cs = cs;
    }
    public Responses(int response, int ante, int pair, Integer player) {
    	this.response = response;
    	this.ante = ante;
    	this.pair = pair;
    	this.player = player;
    }
    public Integer getPlayer() {
    	return player;
    }
    public int getResponse() {
        return response;
    }
    public String getMessage() {
        return message;
    }
    public int getAnte() {
    	return ante;
    }
    public int getPair() {
    	return pair;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ArrayList<Card> getCards() {
    	return cards;
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