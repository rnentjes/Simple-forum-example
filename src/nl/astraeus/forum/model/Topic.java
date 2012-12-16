package nl.astraeus.forum.model;

import nl.astraeus.persistence.SimplePersistent;
import nl.astraeus.persistence.SimplePersistentList;
import nl.astraeus.persistence.SimplePersistentReference;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:14 PM
 */
public class Topic extends SimplePersistent {
    public final static long serialVersionUID = -9038882251579382910L;

    private long date = System.currentTimeMillis();
    private String title = "";
    private SimplePersistentReference<Member> creator = new SimplePersistentReference<Member>(Member.class);
    private SimplePersistentList<Comment> comments;
    private int views;
    private Date lastPost;
    private DateFormat format = null;

    public Topic() {}
    
    public Topic(Member creator) {
        this.creator.set(creator);
    }
    
    public String getTitle() {
        return this.title;
    }

    public String getShortTitle() {
        String title = this.title;

        if (title.length() > 50) {
            title = title.substring(0, 47)+"...";
        }

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private DateFormat getFormat() {
        if (format == null) {
            format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        }

        return format;
    }
    
    public String getDate() {
        return getFormat().format(new Date(date));
    }
    
    public void addComment(Comment comment) {
        getComments().add(comment);

        lastPost = new Date();
    }

    public Member getCreator() {
        return creator.get();
    }

    private static class CommentComparator implements Comparator<Comment>, Serializable {
        public int compare(Comment o1, Comment o2) {
            if (o1 == null || o1.getDate() == null) {
                return 1;
            } else if (o2 == null || o2.getDate() == null) {
                return -1;
            } else {
                return o1.getDate().compareTo(o2.getDate());
            }
        }
    }

    public SimplePersistentList<Comment> getComments() {
        if (comments == null) {
            comments = new SimplePersistentList<Comment>(Comment.class);
        }

        return comments;
    }

    public void addView() {
        views++;
    }
    
    public int getNumberOfReplies() {
        return (getComments().size()-1);
    }
    
    public int getNumberOfViews() {
        return views;
    }
    
    public String getLastPost() {
        String result = "never";

        if (lastPost != null && lastPost.getTime() != 0) {
            result = getFormat().format(lastPost);
        }

        return result;
    }

    public Long getLastPostMilli() {
        long result = 0;

        if (lastPost != null) {
            result = lastPost.getTime();
        }

        return result;
    }
}
