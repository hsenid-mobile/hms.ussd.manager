package hms.tap.ussd.manager.repo;

import hms.tap.ussd.manager.Session;

public interface SessionRepo<E extends Session>  {

    /**
     *
     * <p>
     * Persists the newInstance entity into database.
     * </p>
     *
     * @param newInstance
     *            Entity to be saved
     *
     */
    public void create(E newInstance);

    /**
     *
     * <p>
     * Updates the given inTransientObject.
     * </p>
     *
     * @param inTransientObject
     *            Entity to be updated
     * @return the updated inTransientObject
     *
     */
    public E update(final E inTransientObject);

    /**
     * Removes an entity from persistent storage in the database.
     *
     * @param persistentObject
     *            the object to be deleted
     */
    public void delete(E persistentObject);


    /**
     * Find {@link com.ideamart.sample.ussd.lib.Session) by sessionId.
     * @param sessionId Session Id to find the session
     * @return {@link com.ideamart.sample.ussd.lib.Session}
     */
	Session findBySessionId(String sessionId);


}
