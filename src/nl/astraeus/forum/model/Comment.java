package nl.astraeus.forum.model;

import nl.astraeus.persistence.SimpleModel;
import nl.astraeus.persistence.SimpleReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:14 PM
 */
public class Comment extends SimpleModel {
    public final static long serialVersionUID = -9038882251579382910L;

    private long date = System.currentTimeMillis();
    private String description = "";
    private SimpleReference<Member> creator = new SimpleReference<Member>(Member.class);

    public Comment() {}

    public Comment(Member creator) {
        this(creator, "");
    }

    public Comment(String description) {
        this(null, description);
    }

    public Comment(Member creator, String description) {
        this.creator.set(creator);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        String description = this.description;

        if (description.length() > 50) {
            description = description.substring(0, 47)+"...";
        }

        return description;
    }
    
    public String getDate() {
        String result = "never";

        if (date > 0) {
            DateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

            result = format.format(new Date(date));
        }

        return result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getCreator() {
        return creator.get();
    }
    
}
