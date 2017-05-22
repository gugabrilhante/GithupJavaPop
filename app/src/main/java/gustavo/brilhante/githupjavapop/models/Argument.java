package gustavo.brilhante.githupjavapop.models;

/**
 * Created by Gustavo on 05/02/17.
 */

public class Argument {
    String key;
    String value;

    public Argument(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
