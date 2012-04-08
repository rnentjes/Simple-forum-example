package nl.astraeus.forum;

import nl.astraeus.forum.model.CommentDao;
import nl.astraeus.forum.model.MemberDao;
import nl.astraeus.forum.model.TopicDao;
import org.junit.Test;

/**
 * User: rnentjes
 * Date: 4/8/12
 * Time: 10:57 PM
 */
public class TestFindAll {

    @Test
    public void testFindAllTopic() {
        TopicDao dao = new TopicDao();

        dao.findAll();

    }

    @Test
    public void testFindAllComments() {
        CommentDao dao = new CommentDao();

        dao.findAll();

    }

    @Test
    public void testFindAllMembers() {
        MemberDao dao = new MemberDao();

        dao.findAll();

    }

}
