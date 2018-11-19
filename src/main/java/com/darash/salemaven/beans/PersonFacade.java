/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Person;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author daresh
 */
@Stateless
public class PersonFacade extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
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

}
