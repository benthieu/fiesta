package fiesta.hevs.ch.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by mathi on 14.07.2016.
 */
@Entity
public class TransportAlt {
    @Id
    private Long id;
    @Index
    private Long festival_id;
    private String name;
    private String description;
    private String link;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
