package nl.astraeus.forum.web.page;

import nl.astraeus.forum.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:23 PM
 */
public class TopicOverview extends TemplatePage {

    private long topicId;
    private Boolean editing = null;
    private String description = null;
    private boolean first = true;

    private TopicDao dao = new TopicDao();

    public TopicOverview(String topicId) {
        if ("new".equals(topicId)) {
            this.topicId = 0L;
            editing = true;
            description = "";
        } else {
            this.topicId = Long.parseLong(topicId);
        }
    }

    protected TopicOverview() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void put(HttpServletRequest request) {
        String description = request.getParameter("description");

        if (description != null && description.length() != 0) {
            Topic topic = null;

            if (topicId == 0L ) {
                topic = new Topic();

            } else {
                topic = dao.find(topicId);
            }

            Member member = (Member)request.getSession().getAttribute("user");
            Comment comment = new Comment(member, request.getParameter("description"));

            CommentDao dao = new CommentDao();

            dao.store(comment);

            topic.addComment(comment);

            if (member != null) {
                MemberDao memberDao = new MemberDao();

                member.addComment(comment);

                memberDao.store(member);
            }

            dao.store(topic);

            redirect("/topic/"+topic.getId());
        }
    }

    @Override
    public Page processRequest(HttpServletRequest request) {
        Page result = this;

        if ("new".equals(request.getParameter("action"))) {
            result = new TopicEdit(this, new Topic());
        } else if ("comment".equals(request.getParameter("action"))) {
            long id = Long.parseLong(request.getParameter("actionValue"));
            
            editing = true;
            description = "";
        } else if ("save".equals(request.getParameter("action"))) {
            editing = null;
            String description = request.getParameter("description");
            
            if (description != null && description.length() != 0) {
                Topic topic = dao.find(topicId);

                Member member = (Member)request.getSession().getAttribute("user");
                Comment comment = new Comment(member, request.getParameter("description"));

                CommentDao dao = new CommentDao();

                dao.store(comment);

                topic.addComment(comment);

                if (member != null) {
                    MemberDao memberDao = new MemberDao();

                    member.addComment(comment);

                    memberDao.store(member);
                }

                dao.store(topic);

                //moveToPage("/topic/"+topic.getId());
            }
        } else if ("cancel".equals(request.getParameter("action"))) {
            editing = null;
        } else if ("edit".equals(request.getParameter("action"))) {
            result = new TopicEdit(this, dao.find(topicId));
        } else if ("remove".equals(request.getParameter("action"))) {
            dao.remove(topicId);
        //} else if ("back".equals(request.getParameter("action"))) {
        //    result = previous;
        }
        
        first = true;

        return result;
    }

    @Override
    public Map<String, Object> defineModel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("topic", dao.find(topicId));
        result.put("editing", editing);
        result.put("description", description);
        result.put("controller", this);
        result.put("member", request.getSession().getAttribute("user"));

        return result;
    }
    
    public String getBackground() {
        String result = "#edfffc";

        if (!first) {
            result = "#e9fdf7";
        }
        
        first = !first;

        return result;
    }

}
