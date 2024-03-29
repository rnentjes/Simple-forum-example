package nl.astraeus.forum.model;

import nl.astraeus.persistence.SimplePersistent;
import nl.astraeus.persistence.SimplePersistentList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: rnentjes
 * Date: 3/28/12
 * Time: 3:16 PM
 */
public class Member extends SimplePersistent {
    public final static long serialVersionUID = 1L;

    private String nickName;
    private String email;
    private String password;
    private boolean superuser;
    private Date created;
    private Date lastPost;
    private SimplePersistentList<Comment> comments;
    private SimplePersistentList<Topic> topics;

    public Member() {
        this("","","");
    }

    public Member(String nickName, String password, String email) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.created = new Date();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public boolean isSuperuser() {
        return superuser;
    }

    public boolean getSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public String toString() {
        return getNickName();
    }

    public SimplePersistentList<Comment> getComments() {
        if (comments == null) {
            comments = new SimplePersistentList<Comment>(Comment.class);
        }
        
        return comments;
    }

    public SimplePersistentList<Topic> getTopics() {
        if (topics == null) {
            topics = new SimplePersistentList<Topic>(Topic.class);
        }

        return topics;
    }

    public void addTopic(Topic topic) {
        getTopics().add(topic);
        
        lastPost = new Date();
    }

    public void addComment(Comment comment) {
        getComments().add(comment);
        
        lastPost = new Date();
    }

    public String getLastPost() {
        String result = "never";

        if (lastPost != null && lastPost.getTime() > 0) {
            DateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

            result = format.format(lastPost);
        }

        return result;
    }

    public String getMemberSince() {
        String result = "unknown";

        if (created != null && created.getTime() > 0) {
            DateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");

            result = format.format(created);
        }

        return result;
    }

    public int getNumberOfTopics() {
        return getTopics().size();
    }
    
    public int getNumberOfComments() {
        return getComments().size();
    }
}
