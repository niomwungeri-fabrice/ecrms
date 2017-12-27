package rw.mentors.ecrms.genericDao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;

import rw.mentors.ecrms.exception.DataManipulationException;
import rw.mentors.ecrms.util.QueryParameters;

public abstract class GenericDaoImpl<T extends Serializable> implements GenericDao<T> {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session sessionfactory() {
		return sessionFactory.getCurrentSession();
	}

	private Class<T> type;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericDaoImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class) pt.getActualTypeArguments()[0];
	}

	@Override
	public T save(T t) {
		sessionfactory().save(t);
		sessionfactory().flush();
		return t;
	}

	@Override
	public T saveOrUpdate(T t) {
		sessionfactory().saveOrUpdate(t);
		sessionfactory().flush();
		return t;
	}

	@Cascade(CascadeType.DELETE)
	@Override
	public T delete(T t) {
		sessionfactory().delete(t);
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return sessionfactory().createCriteria(this.type).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findOne(String id) {
		return (T) sessionfactory().get(this.type, id);
	}

	@Override
	public T update(T t) {
		sessionfactory().update(t);
		sessionfactory().flush();
		return t;
	}

	@SuppressWarnings("unchecked")
	public final T executeNamedQuerySingle(final String name, final QueryParameters props) {
		T result = (T) getMappedQueryWithArgs(name, props).uniqueResult();

		if (result == null) {
			throw new DataManipulationException("Object not found");
		}
		return result;
	}

	private Query getMappedQueryWithArgs(final String name, final QueryParameters props) {
		// Assign all the parameters
		Query query = sessionfactory().getNamedQuery(name);
		Map<String, Object> args = props.getArgs();
		for (String key : args.keySet()) {
			query.setParameter(key, args.get(key));
		}
		// Now assign the lists if there are any
		Map<String, Collection<?>> lists = props.getLists();
		for (String key : lists.keySet()) {
			query.setParameterList(key, (Collection<?>) lists.get(key));
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	public List<T> executeNamedQueryMultiple(final String name, final QueryParameters props) {
		return getMappedQueryWithArgs(name, props).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> executeNamedQueryMultiple(final String name) {
		return sessionfactory().getNamedQuery(name).list();
	}

}
