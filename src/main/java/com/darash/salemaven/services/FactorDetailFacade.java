/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.FactorDetail;
import com.darash.salemaven.entities.Product;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

/**
 *
 * @author daresh
 */
@Stateless
public class FactorDetailFacade extends AbstractFacade<FactorDetail> {

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void ChangeProductInfo(boolean warranty,
            boolean insurance, String model,
            String unit, int product_id) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        // create update
        CriteriaUpdate<FactorDetail> update = cb.createCriteriaUpdate(FactorDetail.class);
        // set the root class
        Root e = update.from(FactorDetail.class);
        // set update and where clause
        update.set("warranty", warranty);
        update.set("insurance", insurance);
        update.set("unit", unit);
        update.set("model", model);
        update.where(cb.equal(e.get("productId"), product_id));
        // perform update
        this.em.createQuery(update).executeUpdate();
    }

    public FactorDetailFacade() {
        super(FactorDetail.class);
    }

}
