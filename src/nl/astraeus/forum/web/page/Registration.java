package nl.astraeus.forum.web.page;

import nl.astraeus.forum.model.Member;
import nl.astraeus.forum.model.MemberDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 8:13 PM
 */
public class Registration extends TemplatePage {

    private Page previous;
    private Page donePage;
    private Member member;
    private MemberDao dao = new MemberDao();
    private Collection<String> warnings = null;

    public Registration(Page previous, Page donePage) {
        this.previous = previous;
        this.donePage = donePage;
        this.member = new Member();
    }
    
    private void addWarning(String warn) {
        if (warnings == null) {
            warnings = new LinkedList<String>();
        }
        
        warnings.add(warn);
    }

    @Override
    public Page processRequest(HttpServletRequest request) {
        Page result = this;
        warnings = null;
        
        if ("register".equals(request.getParameter("action"))) {
            String nickName = request.getParameter("nickName");

            if (nickName == null || nickName.length() == 0) {
                addWarning("Please enter a valid nickname!");
            } else {
                if (dao.findByNickName(nickName) == null && !"anonymous".equals(nickName)) {
                    member.setNickName(nickName);
                    member.setEmail(request.getParameter("email"));

                    if (request.getParameter("password") != null &&
                            request.getParameter("password").length() > 0 &&
                            request.getParameter("password").equals(request.getParameter("password2"))) {
                        member.setPassword(request.getParameter("password"));

                        dao.store(member);
                        request.getSession().setAttribute("user", member);

                        result = donePage;
                    } else {
                        // warn password...
                        addWarning("Passwords don't match!");
                    }
                } else {
                    addWarning("Nickname already taken!");
                }
            }
        } else if ("cancel".equals(request.getParameter("action"))) {
            result = previous;
        }
        
        return result;
    }

    @Override
    public Map<String, Object> defineModel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        result.put("member", member);
        result.put("warnings", warnings);
       
        
        return result;
    }
}
