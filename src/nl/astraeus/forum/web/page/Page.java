package nl.astraeus.forum.web.page;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:20 PM
 */
public abstract class Page {

    private String action = null;

    public boolean isAction(String action) {
        return action.equals(this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public abstract Page processRequest(HttpServletRequest request);
    public abstract Map<String, Object> defineModel(HttpServletRequest request);
    public abstract String render(HttpServletRequest request);

}
