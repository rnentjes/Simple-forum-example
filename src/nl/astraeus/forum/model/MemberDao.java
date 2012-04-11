package nl.astraeus.forum.model;

import nl.astraeus.persistence.Filter;
import nl.astraeus.persistence.SimpleDao;

import java.util.Collection;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 8:42 PM
 */
public class MemberDao extends SimpleDao<Member> {
    
    private static class NickNameFilter extends Filter<Member> {
        private String nickName;

        private NickNameFilter(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public boolean include(Member model) {
            return nickName.equals(model.getNickName());
        }
    }
    
    public Member login(final String nickName, String password) {
        Member result = findByNickName(nickName);

        if (result != null && !result.checkPassword(password)) {
            result = null;
        }

        return result;
    }

    public Member findByNickName(String nickName) {
        Member result = null;

        Collection<Member> members = filter(new NickNameFilter(nickName));

        if (members.size() == 1) {
            result = members.iterator().next();
        }

        return result;
    }

    @Override
    public void store(Member model) {
        System.out.println("Storing member ("+model+") "+model.getNickName()+", topics: "+model.getTopics().size()+", comments: "+model.getComments().size());

        super.store(model);
    }
}
