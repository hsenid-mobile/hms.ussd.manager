package hms.tap.ussd.manager.repo;

import hms.tap.ussd.manager.Session;

/**
 * Created by rajive on 4/21/15.
 */
public class GuavaCacheSessionRepoImpl implements SessionRepo {


    @Override
    public void create(Session newInstance) {
    }

    @Override
    public Session update(Session inTransientObject) {
        return null;
    }

    @Override
    public void delete(Session persistentObject) {
    }


    @Override
    public Session findBySessionId(String sessionId) {
        return GuavaSessionRepo.findBySessionId(sessionId);
    }

}
