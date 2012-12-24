package models;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.github.jmkgreen.morphia.utils.IndexDirection;
import org.bson.types.ObjectId;
import play.data.validation.Constraints;

import java.util.Date;

/**
 * Note entity description
 */
@Entity("notes")
public class Note {
    @Id
    private ObjectId id;

    /**
     * Note text
     */
    @Constraints.Required
    private String text;

    /**
     * Note date
     */
    @Indexed(IndexDirection.DESC)
    private Date date = new Date();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
