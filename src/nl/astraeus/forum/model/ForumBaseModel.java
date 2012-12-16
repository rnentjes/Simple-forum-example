package nl.astraeus.forum.model;

import nl.astraeus.persistence.Persistent;

/**
 * User: rnentjes
 * Date: 11/19/12
 * Time: 10:16 PM
 */
public abstract class ForumBaseModel implements Persistent<Long> {

    private static long maxId = 0;
    private long id;

    public <D extends ForumBaseDao<? extends ForumBaseModel>> ForumBaseModel(D dao) {
        synchronized (dao.getClass()) {
            if (maxId == 0) {
                for (ForumBaseModel m : dao.findAll()) {
                    maxId = Math.max(maxId, m.getId());
                }
            }

            id = ++maxId;
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public ForumBaseModel clone() throws CloneNotSupportedException {
        return (ForumBaseModel)super.clone();
    }

    public int compareTo(Object o) {
        ForumBaseModel other = (ForumBaseModel)o;

        return Long.valueOf(getId()).compareTo(other.getId());
    }
}
