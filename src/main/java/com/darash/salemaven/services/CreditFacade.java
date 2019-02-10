/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.Credit;
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
public class CreditFacade extends AbstractFacade<Credit> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CreditFacade() {
        super(Credit.class);
    }

    public List<Credit> findByCredit(long credit) {
        TypedQuery<Credit> typedQuery = em.createNamedQuery("Credit.findByCredit", Credit.class)
                .setParameter("credit", credit);
        return typedQuery.getResultList();
    }

}
