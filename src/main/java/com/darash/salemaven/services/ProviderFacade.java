/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author daresh
 */
@Stateless
public class ProviderFacade extends AbstractFacade<Provider> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProviderFacade() {
        super(Provider.class);
    }
    
     public List<Provider> filter(int first, int pageSize, Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provider> criteriaQuery = cb.createQuery(Provider.class);
        Root<Provider> root = criteriaQuery.from(Provider.class);
        CriteriaQuery<Provider> select = criteriaQuery.select(root);

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
        TypedQuery<Provider> query = em.createQuery(select);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Provider> list = query.getResultList();
      
        return list;
    }

    public int getFilteredRowCount(Map<String, Object> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Provider> root = criteriaQuery.from(Provider.class);
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
