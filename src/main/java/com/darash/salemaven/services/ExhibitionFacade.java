/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.Exhibition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Hamid
 */
@Stateless
public class ExhibitionFacade extends AbstractFacade<Exhibition> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExhibitionFacade() {
        super(Exhibition.class);
    }
    
}
