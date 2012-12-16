package nl.astraeus.forum.web;

import nl.astraeus.forum.model.Member;
import nl.astraeus.forum.model.MemberDao;
import nl.astraeus.forum.util.IOUtils;
import nl.astraeus.forum.web.page.*;
import nl.astraeus.http.HttpMethod;
import nl.astraeus.persistence.PersistentManager;
import nl.astraeus.persistence.Transaction;
import nl.astraeus.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:05 PM
 */
public class ForumServlet extends HttpServlet {

    private String basic;
    private String head;
    private String bottom;

    private Map<String, Class<? extends Page>> pageClassMapping = new HashMap<String, Class<? extends Page>>();

    @Override
    public void init() throws ServletException {
        super.init();

        pageClassMapping.put("", ForumOverview.class);
        pageClassMapping.put("overview", ForumOverview.class);
        pageClassMapping.put("diagnostics", Diagnostics.class);
        pageClassMapping.put("login", Login.class);
        pageClassMapping.put("registration", Registration.class);
        pageClassMapping.put("logout", ForumOverview.class);
        pageClassMapping.put("members", MemberOverview.class);
        pageClassMapping.put("topic", TopicOverview.class);
        pageClassMapping.put("comment", TopicOverview.class);


        new Transaction() {
            @Override
            public void execute() {
                MemberDao dao = new MemberDao();

                Member rien = dao.findByNickName("rien");

                if (rien == null) {
                    rien = new Member("rien", "rien", "rien@nentjes.com");
                }

                rien.setSuperuser(true);
                dao.store(rien);

                Member jeroen = dao.findByNickName("jeroen");

                if (jeroen == null) {
                    jeroen = new Member("jeroen", "jeroen", "jeroen@rakhorst.nl");
                }

                jeroen.setSuperuser(true);
                dao.store(jeroen);

                Member jan = dao.findByNickName("jan");

                if (jan == null) {
                    jan = new Member("jan", "jan", "jan@jan.nl");
                }

                dao.store(jan);
            }
        };

        try {
            basic = IOUtils.toString(getClass().getResourceAsStream("basic.html"));
            head = IOUtils.toString(getClass().getResourceAsStream("head.html"));
            bottom = IOUtils.toString(getClass().getResourceAsStream("bottom.html"));
        } catch (IOException e) {
            throw new ServletException(e);
        }

        System.setProperty(PersistentManager.AUTOCOMMIT, String.valueOf(false));
        System.setProperty(PersistentManager.SAFEMODE, String.valueOf(true));
    }

    protected void handle(final HttpServletRequest req, HttpServletResponse resp, final HttpMethod method) throws ServletException, IOException {
        long nano = System.nanoTime();

        HttpSession session =  req.getSession();
        boolean ajax = "true".equals(req.getParameter("ajax"));

        resp.setContentType("text/html");

        Page page = new ForumOverview();
        Page menu = new Menu();

        session.setMaxInactiveInterval(1800);

        String uri = req.getRequestURI();

        if (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        String href = uri; //req.getParameter("href");

        if (href == null) {
            href="";
        }

        String [] parts = href.split("/");

        if (parts.length == 0 || parts[0].length() == 0) {
            resp.getWriter().print(head);

            Page m = new Menu();
            Page p = new ForumOverview();

            resp.getWriter().print(menu.render(req));
            resp.getWriter().print(p.render(req));
            resp.getWriter().print(bottom);
        } else {
            String rest = parts[0];

            Class<? extends Page> pageClass = pageClassMapping.get(parts[0]);

            if (pageClass == null) {
                resp.setStatus(404);
                return;
            }

            Class [] parameterTypes = new Class[parts.length-1];
            for (int index = 0; index < parameterTypes.length; index++) {
                parameterTypes[index] = String.class;
            }

            try {
                Constructor<? extends Page> c = pageClass.getConstructor(parameterTypes);

                String [] constructorParameters = new String[parts.length-1];

                for (int index = 0; index < parameterTypes.length; index++) {
                    constructorParameters[index] = parts[index+1];
                }

                page = c.newInstance(constructorParameters);
            } catch (Exception e) {
                throw new ServletException(e);
            }

            final Page myPage = page;
            page.setAction(req.getParameter("action"));

            Transaction<Page> t = new Transaction<Page>() {
                @Override
                public void execute() {
                    if (method == HttpMethod.PUT) {
                        myPage.put(req);
                    } else if (method == HttpMethod.POST) {
                        String method = req.getParameter("method");

                        if ("put".equalsIgnoreCase(method)) {
                            myPage.put(req);
                        } else if ("delete".equalsIgnoreCase(method)) {
                            myPage.delete(req);
                        } else {
                            myPage.post(req);
                        }
                    } else if (method == HttpMethod.DELETE) {
                        myPage.delete(req);
                    } else {
                        setResult(myPage.processRequest(req));
                    }
                }
            };

            if (t.getResult() != null) {
                page = t.getResult();
            } else {
                page = myPage;
            }

            if (page.getRedirect() != null) {
                resp.sendRedirect(page.getRedirect());
            } else if (page.getResponseCode() != 200) {
                resp.sendError(page.getResponseCode());
            } else {
                resp.getWriter().print(head);
                resp.getWriter().print(menu.render(req));
                Warnings warnings = Warnings.getWarnings(req);
                if (warnings.hasWarnings()) {
                    resp.getWriter().print(warnings.render(req));
                }
                resp.getWriter().print(page.render(req));
                resp.getWriter().print(bottom);
            }
        }

        System.out.println("Request ends, time="+ Util.formatNano(System.nanoTime() - nano) +", page="+page.getClass().getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp, HttpMethod.GET);
    }

    @Override
    protected void doPost(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp, HttpMethod.POST);
    }

    @Override
    protected void doPut(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp, HttpMethod.PUT);
    }

    @Override
    protected void doDelete(final HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handle(req, resp, HttpMethod.DELETE);
    }

}
