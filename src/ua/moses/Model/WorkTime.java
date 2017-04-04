package ua.moses.Model;

/**
 * Created by Admin on 03.04.2017.
 */
public class WorkTime {
    private Worker worker;
    private float workingHours;

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public float getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(float workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public String toString(){
        return worker + ". Отработано часов: " + workingHours;
    }
}
