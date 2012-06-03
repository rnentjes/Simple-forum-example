package nl.astraeus.forum.web.page;

import nl.astraeus.forum.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:23 PM
 */
public class ForumOverview extends TemplatePage {

    private TopicDao dao = new TopicDao();

    private int page =1;
    private int resultsPerPage = 15;
    private Boolean hasNext = false;

    public ForumOverview() {

    }

    public ForumOverview(String page) {
        this.page = Integer.parseInt(page);
    }

    @Override
    public Page processRequest(HttpServletRequest request) {
        Page result = this;

        if ("new".equals(request.getParameter("action"))) {
            Member member = (Member)request.getSession().getAttribute("user");

            result = new TopicEdit(this, new Topic(member));
        } else if ("previous".equals(request.getParameter("action"))) {
            if (page > 1) {
                page--;
            }
        } else if ("next".equals(request.getParameter("action"))) {
            if (page < getNumberOfPages()) {
                page++;
            }
        } else if (isAction("edit")) {
            long id = Long.parseLong(request.getParameter("actionValue"));

            result = new TopicEdit(this, dao.find(id));
        } else if (isAction("remove")) {
            long topicId = Long.parseLong(request.getParameter("actionValue"));

            Topic topic = dao.find(topicId);

            CommentDao commentDao = new CommentDao();

            for (Comment comment: topic.getComments()) {
                commentDao.remove(comment);
            }

            dao.remove(topicId);
        }

        return result;
    }

    @Override
    public Map<String, Object> defineModel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        int start = (page-1)*resultsPerPage;
        int end = start + resultsPerPage;

        Collection<Topic> topics = dao.find(new Comparator<Topic>() {
            public int compare(Topic o1, Topic o2) {
                if (o2.getLastPostMilli().equals(o1.getLastPostMilli())) {
                    return 0;
                } else if (o2.getLastPostMilli() > o1.getLastPostMilli()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }, start, end);

        result.put("topics", topics);

        hasNext = (topics.size() == resultsPerPage);

        result.put("member", request.getSession().getAttribute("user"));
        result.put("controller", this);

        return result;
    }

    public Integer getPreviousPageNumber() {
        Integer result = null;

        if (getHasPrevious()) {
            result = page - 1;
        }

        return result;
    }

    public Integer getNextPageNumber() {
        Integer result = null;

        if (getHasNext()) {
            result = page + 1;
        }

        return result;
    }

    public int getPageNumber() {
        return page;
    }

    public int getNumberOfPages() {
        return ((dao.size()-1) / resultsPerPage) + 1;
    }

    public int getNumberOfResults() {
        return dao.size();
    }

    public Boolean getHasPrevious() {
        return (page > 1);
    }

    public Boolean getHasNext() {
        return hasNext;
    }
}
