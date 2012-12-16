package nl.astraeus.forum.model;

import nl.astraeus.persistence.PersistentDao;

/**
 * User: rnentjes
 * Date: 11/19/12
 * Time: 10:21 PM
 */
public class ForumBaseDao<M extends ForumBaseModel> extends PersistentDao<Long, M> {

    /*
    protected Class<M> getModelClass() {
        Class<M> result = null;

        ParameterizedType pt = null;

        pt = (ParameterizedType) getClass().getGenericSuperclass();

        Type type = pt.getActualTypeArguments()[0];
        result = (Class<M>) type;

        return result;
    }*/
}
