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
    private String redirect = null;
    private int response = 200;

    public String getRedirect() {
        return redirect;
    }

    public int getResponseCode() {
        return response;
    }

    public boolean isAction(String action) {
        return action.equals(this.action);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void post(HttpServletRequest request) {
        sendResponse(501);
    }
    public void put(HttpServletRequest request) {
        sendResponse(501);
    }
    public void delete(HttpServletRequest request) {
        sendResponse(501);
    }

    protected void authorize(boolean check) {
        if (!check) {
            sendResponse(401);
        }
    }

    protected void sendResponse(int nr) {
        response = nr;
    }

    protected void redirect(String uri) {
        redirect = "http://localhost:8080"+uri;
    }

    public abstract Page processRequest(HttpServletRequest request);
    public abstract Map<String, Object> defineModel(HttpServletRequest request);
    public abstract String render(HttpServletRequest request);

}
