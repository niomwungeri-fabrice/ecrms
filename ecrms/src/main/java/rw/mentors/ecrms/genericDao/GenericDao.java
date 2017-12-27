package rw.mentors.ecrms.genericDao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Serializable> {

	T save(final T t);

	T saveOrUpdate(final T t);

	T update(final T t);

	T delete(final T t);

	List<T> findAll();

	T findOne(final String id);
}
