package fiesta.hevs.ch.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by mathi on 14.07.2016.
 */
@Entity
public class Transport {
    @Id
    private Long id;
    @Index
    private Long festival_id;
    private String driver;
    private String destination;
    private int num_free_space;
    private int hour_start;
    private int minute_start;
    private String driver_email;
    private String device_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFestival_id() {
        return festival_id;
    }

    public void setFestival_id(Long festival_id) {
        this.festival_id = festival_id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getNum_free_space() {
        return num_free_space;
    }

    public void setNum_free_space(int num_free_space) {
        this.num_free_space = num_free_space;
    }

    public int getHour_start() {
        return hour_start;
    }

    public void setHour_start(int hour_start) {
        this.hour_start = hour_start;
    }

    public int getMinute_start() {
        return minute_start;
    }

    public void setMinute_start(int minute_start) {
        this.minute_start = minute_start;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
