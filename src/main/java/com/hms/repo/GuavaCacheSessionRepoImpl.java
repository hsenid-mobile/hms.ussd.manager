package com.hms.repo;

import com.hms.Session;
import com.hms.repo.GuavaSessionRepo;
import com.hms.repo.SessionRepo;

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
