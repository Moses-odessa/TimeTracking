package ua.moses.Model;

/**
 * Created by Admin on 24.03.2017.
 */
public class Worker {
    private int id;
    private String fullName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString(){
        return id + ":" + fullName;
    }
}
