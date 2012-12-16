package nl.astraeus.forum.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 8:42 PM
 */
public class MemberDao extends ForumBaseDao<Member> {

    private static Random random = new Random(System.nanoTime());

    public Member login(final String nickName, String password) {
        Member result = findByNickName(nickName);

        if (result != null && !result.checkPassword(password)) {
            result = null;
        }

        return result;
    }

    public Member findByNickName(String nickName) {
        return createQuery().equals("nickName", nickName).getSingleResult();
    }

    @Override
    public void store(Member model) {
        System.out.println("Storing member ("+model+") "+model.getNickName()+", topics: "+model.getTopics().size()+", comments: "+model.getComments().size());

        super.store(model);
    }

    public Member findRandom() {
        List<Member> list = new LinkedList<Member>(findAll());

        return list.get(random.nextInt(list.size()));
    }
}
