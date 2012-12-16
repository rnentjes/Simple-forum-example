package nl.astraeus.forum.model;

import nl.astraeus.persistence.PersistentList;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:07 PM
 */
public class Forum extends ForumBaseModel {
    public final static long serialVersionUID = 1L;

    private String name;

    private PersistentList<Long, Topic> topic = new PersistentList<Long, Topic>(Topic.class);
    private PersistentList<Long, Member> member = new PersistentList<Long, Member>(Member.class);
    
    public Forum() {
        super(new ForumBaseDao());
    }
    
    public Forum(String name) {
        super(new ForumBaseDao());

        this.name = name;
    }
}
