package nl.astraeus.forum.model;

import nl.astraeus.persistence.SimpleList;
import nl.astraeus.persistence.SimpleModel;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:07 PM
 */
public class Forum extends SimpleModel {
    public final static long serialVersionUID = 1L;

    private String name;
    private SimpleList<Topic> topic = new SimpleList<Topic>(Topic.class);
    private SimpleList<Member> member = new SimpleList<Member>(Member.class);
    
    public Forum() {}
    
    public Forum(String name) {
        this.name = name;
    }
}
