package nl.astraeus.forum.web.page;

import nl.astraeus.forum.model.Member;
import nl.astraeus.forum.model.MemberDao;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 8:13 PM
 */
public class Login extends TemplatePage {

    private MemberDao dao = new MemberDao();
    private String action = null;

    public Login() {}

    public Login(String action) {
        this.action = action;
    }

    @Override
    public Page processRequest(HttpServletRequest request) {
        Page result = this;

        if (isAction("login")) {
            String nickName = request.getParameter("nickName");
            String password = request.getParameter("password");

            if (nickName != null && nickName.length() > 0) {
                Member member = dao.login(nickName, password);

                if (member != null) {
                    request.getSession().setAttribute("user", member);
                    result = new ForumOverview();
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, Object> defineModel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        result.put("member", null);
        
        return result;
    }
}
