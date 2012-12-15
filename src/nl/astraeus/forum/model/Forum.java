package nl.astraeus.forum.model;

import nl.astraeus.persistence.SimplePersistent;
import nl.astraeus.persistence.SimplePersistentList;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:07 PM
 */
public class Forum extends SimplePersistent {
    public final static long serialVersionUID = 1L;

    private String name;
    private SimplePersistentList<Topic> topic = new SimplePersistentList<Topic>(Topic.class);
    private SimplePersistentList<Member> member = new SimplePersistentList<Member>(Member.class);
    
    public Forum() {}
    
    public Forum(String name) {
        this.name = name;
    }
}
