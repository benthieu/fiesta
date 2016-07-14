package fiesta.hevs.ch.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
/**
 * Created by mathi on 14.07.2016.
 */
@Entity
public class Communication {
    @Id
    private Long id;
    private Long festival_transport_id;
    private String name;
    private String message;
    private String device_id_to;
    private String device_id_from;

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
