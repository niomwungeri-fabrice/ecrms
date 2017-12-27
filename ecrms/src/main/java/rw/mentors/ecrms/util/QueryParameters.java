package rw.mentors.ecrms.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NIYOMWUNGERI
 * @Date Jun 8, 2017
 */
public class QueryParameters {
    private Map<String, Object> args = new HashMap<String, Object>();

    private Map<String, Collection<?>> lists = new HashMap<String, Collection<?>>();

    public QueryParameters add(final String key, final Object arg) {
        args.put(key, arg);

        return this;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public QueryParameters addList(final String key, final Collection<?> objs) {
        lists.put(key, objs);

        return this;
    }

    public Map<String, Collection<?>> getLists() {
        return lists;
    }
}
