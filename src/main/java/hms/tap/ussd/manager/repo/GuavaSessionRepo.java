package hms.tap.ussd.manager.repo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import hms.tap.ussd.manager.Session;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by rajive on 4/21/15.
 */
public class GuavaSessionRepo {

    // creating a cashe loader to keep menustate referance according to the session id
    private static CacheLoader<String, Session> loader = new CacheLoader<String, Session>() {
        @Override
        public Session load(final String key) throws Exception {
            return createExpensiveGraph(key);
        }

    };

    private static Session createExpensiveGraph(final String key) {
        Session session = new Session();
        session.setSessionId(key);
        return session;
    }

    // cashe build the cashloader this will remove the element after 2minutes from the last access.
    //this use to keep menustate according to the session id upto some period, after that it will remove the menustate.
    private static LoadingCache<String, Session> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES)
            .build(loader);

    public static Session findBySessionId(String sessionId) {
        try {
           return cache.get(sessionId);
        } catch (ExecutionException e) {


        }
        return null;
    }
}
