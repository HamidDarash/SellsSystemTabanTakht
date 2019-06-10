/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.LoggerFactory;

@Stateless
public class PersonFacade extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Person findByCompanyCode(String companyCode) {
        TypedQuery<Person> typedQuery = em.createNamedQuery("Person.findByCompanyCode", Person.class)
                .setParameter("companyCode", companyCode);
        return typedQuery.getSingleResult();
    }

    public List<Person> findAllByLimit(int first, int pageSize) {
        TypedQuery<Person> typedQuery = em.createNamedQuery("Person.findAll", Person.class)
                .setFirstResult(first)
                .setMaxResults(pageSize);
        return typedQuery.getResultList();
    }

    public List<Person> findByInternationalOrNameOrCompanyCode(String string) {
        TypedQuery<Person> typedQuery = em.createNamedQuery("Person.findByNameOrInternationalOrCompanyCode", Person.class)
                .setParameter("internationalCode", "%" + string + "%")
                .setParameter("name", "%" + string + "%")
                .setParameter("companyCode", "%" + string + "%");
        return typedQuery.getResultList();
    }

    public List<Person> findByInternationalCode(String Code) {
        TypedQuery<Person> typedQuery = em.createNamedQuery("Person.findByInternationalCode", Person.class)
                .setParameter("internationalCode", Code);
        return typedQuery.getResultList();
    }

    public PersonFacade() {
        super(Person.class);
    }

    public void ChangePersonInsertMode(boolean insertMode) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        // create update
        CriteriaUpdate<Person> update = cb.createCriteriaUpdate(Person.class);
        // set the root class
        Root e = update.from(Person.class);
        // set update and where clause
        update.set("insertMode", insertMode);
        // perform update
        this.em.createQuery(update).executeUpdate();
    }

    public List<Person> filter(int first, int pageSize, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = cb.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
//        criteriaQuery.orderBy(cb.desc(root.get(Person_.id)));
        CriteriaQuery<Person> select = criteriaQuery.select(root);

        if (filters != null && filters.size() > 0) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                Expression<String> expr = root.get(field).as(String.class);
                Predicate p = cb.like(cb.lower(expr),
                        "%" + value.toString().toLowerCase() + "%");
                predicates.add(p);
            }
            if (predicates.size() > 0) {
                criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
        TypedQuery<Person> query = em.createQuery(select);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Person> list = query.getResultList();

        return list;
    }

    public int getFilteredRowCount(Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        CriteriaQuery<Long> select = criteriaQuery.select(cb.count(root));

        if (filters != null && filters.size() > 0) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }

                Expression<String> expr = root.get(field).as(String.class);
                Predicate p = cb.like(cb.lower(expr),
                        "%" + value.toString().toLowerCase() + "%");
                predicates.add(p);
            }
            if (predicates.size() > 0) {
                criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
        Long count = em.createQuery(select).getSingleResult();
        return count.intValue();
    }
}
