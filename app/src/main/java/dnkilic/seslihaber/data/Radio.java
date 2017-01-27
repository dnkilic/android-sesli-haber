package dnkilic.seslihaber.data;

/**
 * Created by Speedy on 18.1.2017.
 */

public class Radio {

    private String canal_name;
    private String stream;

    public Radio(String canal_name, String stream) {
        this.canal_name = canal_name;
        this.stream = stream;
    }

    public String getCanal_name() {
        return canal_name;
    }

    public void setCanal_name(String canal_name) {
        this.canal_name = canal_name;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
