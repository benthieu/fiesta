package fiesta.hevs.ch.backend;

import com.google.appengine.repackaged.com.google.api.client.util.DateTime;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Created by mathi on 14.07.2016.
 */
@Entity
public class Communication {
    @Id
    private Long id;
    @Index
    private Long festival_transport_id;
    private String name;
    private String message;
    @Index
    private String device_id_to;
    @Index
    private String device_id_from;
    private Date time_send;
    private boolean is_read;

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public Date getTime_send() {
        return time_send;
    }

    public void setTime_send(Date time_send) {
        this.time_send = time_send;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFestival_transport_id() {
        return festival_transport_id;
    }

    public void setFestival_transport_id(Long festival_transport_id) {
        this.festival_transport_id = festival_transport_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDevice_id_to() {
        return device_id_to;
    }

    public void setDevice_id_to(String device_id_to) {
        this.device_id_to = device_id_to;
    }

    public String getDevice_id_from() {
        return device_id_from;
    }

    public void setDevice_id_from(String device_id_from) {
        this.device_id_from = device_id_from;
    }
}
