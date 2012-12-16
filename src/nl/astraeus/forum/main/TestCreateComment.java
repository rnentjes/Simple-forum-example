package nl.astraeus.forum.main;

import nl.astraeus.forum.model.Comment;
import nl.astraeus.forum.model.CommentDao;
import nl.astraeus.persistence.Persistent;
import nl.astraeus.persistence.PersistentManager;

import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/31/12
 * Time: 8:38 PM
 */
public class TestCreateComment {

    public static void main(String [] args) {
        System.setProperty(PersistentManager.AUTOCOMMIT, String.valueOf(true));

        Comment comment = new Comment("Some comment on something");

        CommentDao dao = new CommentDao();

        dao.store(comment);

        for (Map.Entry<Class<? extends Persistent>, Integer> entry: PersistentManager.get().getObjectTypeMap().entrySet()) {
            System.out.println("Class: "+entry.getKey()+" -> "+entry.getValue());
        }
    }

}
