package nl.astraeus.forum.model;

import nl.astraeus.prevayler.*;

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
public class Topic extends PrevaylerModel {
    public final static long serialVersionUID = -9038882251579382910L;

    private long date = System.currentTimeMillis();
    private String title = "";
    private PrevaylerReference<Member> creator = new PrevaylerReference<Member>(Member.class);
    private PrevaylerList<Comment> comments;
    private int views;
    private Date lastPost;

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
    
    public String getDate() {
        DateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        
        return format.format(date);
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

    public PrevaylerList<Comment> getComments() {
        if (comments == null) {
            comments = new PrevaylerList<Comment>(Comment.class);
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
            DateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

            result = format.format(lastPost);
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
