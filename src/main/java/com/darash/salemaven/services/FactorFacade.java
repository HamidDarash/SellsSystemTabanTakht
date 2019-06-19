/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.services;

import com.darash.salemaven.entities.Exhibition;
import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.entities.Factor_;
import com.darash.salemaven.entities.Person;
import com.darash.salemaven.entities.Provider;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.ULocale;
import helper.CalendarUtil;
import helper.DateConvertor;
import helper.JalaliCalendar;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.convert.ConverterException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 * @author daresh
 */
@Stateless
public class FactorFacade extends AbstractFacade<Factor> {

    private static Logger logger = Logger.getLogger(FactorFacade.class);

    @EJB
    PersonFacade personFacade;

    @PersistenceContext(unitName = "com.darash_salemaven_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FactorFacade() {
        super(Factor.class);
    }

    //SELECT factor.date_factor,factor.id,factor.payable,factor.exhibition_id,exhibition.name_exhibition,exhibition.date_start,provider.fullname FROM factor
    //INNER JOIN provider
    //WHERE factor.provider_id = provider.id and factor.date_factor='2019-06-10' and provider.activate=true
//    public List<Factor> reportFactorsByProviderBetweenDate() {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Factor> searchQuery = cb.createQuery(Factor.class);
//        Root<Factor> aRoot = searchQuery.from(Factor.class);
//        Join<Factor, Provider> bJoin = aRoot.join("mappedB", JoinType.INNER);
//        bJoin.on(cb.equal(bJoin.get("idLanguage"), 22));
//    }

    public List<Factor> findByProviderInExhibition(Exhibition exhibition, Provider provider) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factor> criteriaQuery = cb.createQuery(Factor.class);
        Root<Factor> root = criteriaQuery.from(Factor.class);
        criteriaQuery.orderBy(cb.desc(root.get(Factor_.id)));
        CriteriaQuery<Factor> select = criteriaQuery.select(root);
        Predicate p;
        List<Predicate> predicates = new ArrayList<>();
        Expression<Exhibition> exprExhibition = root.get("exhibition").as(Exhibition.class);
        p = cb.equal(exprExhibition, exhibition);
        predicates.add(p);
        Expression<Provider> exprProvider = root.get("provider").as(Provider.class);
        p = cb.equal(exprProvider, provider);
        predicates.add(p);
        if (predicates.size() > 0) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        TypedQuery<Factor> query = em.createQuery(select);
        List<Factor> list = query.getResultList();
        return list;
    }

    public List<Factor> filter(int first, int pageSize, Map<String, Object> filters) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Factor> criteriaQuery = cb.createQuery(Factor.class);
        Root<Factor> root = criteriaQuery.from(Factor.class);
        criteriaQuery.orderBy(cb.desc(root.get(Factor_.id)));
        CriteriaQuery<Factor> select = criteriaQuery.select(root);

        if (filters != null && filters.size() > 0) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                System.out.println(entry.getKey());
                String field = entry.getKey();
                Object value = entry.getValue();
                Predicate p;
                if (value == null) {
                    continue;
                }

                if (field.equals("person.companyCode") || field.equals("provider.shopName") || field.equals("exhibition.nameExhibition")) {

                    if (field.equals("exhibition.nameExhibition")) {
                        try {
                            CriteriaQuery<Exhibition> criteriaQueryExhibition = cb.createQuery(Exhibition.class);
                            Root<Exhibition> rootExhibition = criteriaQueryExhibition.from(Exhibition.class);
                            CriteriaQuery<Exhibition> selectExhibition = criteriaQueryExhibition.select(rootExhibition);
                            selectExhibition.where(cb.equal(rootExhibition.get("nameExhibition"), value));
                            Expression<String> exprExhibition = rootExhibition.get("nameExhibition").as(String.class);
                            selectExhibition.where(cb.like(exprExhibition, "%" + value.toString() + "%"));
                            Exhibition exhibition = em.createQuery(selectExhibition).getSingleResult();
                            Expression<Exhibition> expr2 = root.get("exhibition").as(Exhibition.class);
                            p = cb.equal(expr2, exhibition);
                            predicates.add(p);

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                    if (field.equals("provider.shopName")) {
                        try {
                            CriteriaQuery<Provider> criteriaQueryProvider = cb.createQuery(Provider.class);
                            Root<Provider> rootProvider = criteriaQueryProvider.from(Provider.class);
                            CriteriaQuery<Provider> selectProvider = criteriaQueryProvider.select(rootProvider);
                            selectProvider.where(cb.equal(rootProvider.get("shopName"), value));
                            Expression<String> exprProviderName = rootProvider.get("shopName").as(String.class);
                            selectProvider.where(cb.like(exprProviderName, "%" + value.toString() + "%"));
                            List<Provider> providers = em.createQuery(selectProvider).getResultList();
                            Expression<Provider> expr2 = root.get("provider").as(Provider.class);
                            for (Provider pr : providers) {
                                p = cb.equal(expr2, pr);
                                predicates.add(p);
                            }

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                    if (field.equals("person.companyCode")) {
                        try {
                            CriteriaQuery<Person> criteriaQueryPerson = cb.createQuery(Person.class);
                            Root<Person> rootPerson = criteriaQueryPerson.from(Person.class);
                            CriteriaQuery<Person> selectPerson = criteriaQueryPerson.select(rootPerson);
                            selectPerson.where(cb.equal(rootPerson.get("companyCode"), value));
                            Person person = em.createQuery(selectPerson).getSingleResult();

                            Expression<Person> expr2 = root.get("person").as(Person.class);
                            p = cb.equal(expr2, person);
                            predicates.add(p);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                } else if (field.equals("finalRegistration") || field.equals("returned")) {
                    if (field.equals("finalRegistration")) {
                        Expression<Boolean> exprfinalRegistration = root.get("finalRegistration").as(Boolean.class);
                        p = cb.equal(exprfinalRegistration, value);
                        predicates.add(p);
                    }
                    if (field.equals("returned")) {
                        Expression<Boolean> exprfinalRegistration = root.get("returned").as(Boolean.class);
                        p = cb.equal(exprfinalRegistration, value);
                        predicates.add(p);
                    }
                } else {
                    Expression<String> expr = root.get(field).as(String.class);
                    String valueField = value.toString().toLowerCase();

                    if (field.equals("dateFactor")) {
                        try {
                            valueField = ConvertStrToDate(valueField);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    }
                    p = cb.like(cb.lower(expr),
                            "%" + valueField + "%");
                    predicates.add(p);
                }
            }
            if (predicates.size() > 0) {
                criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
        TypedQuery<Factor> query = em.createQuery(select);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<Factor> list = query.getResultList();

        return list;
    }

    public int getFilteredRowCount(Map<String, Object> filters) throws ParseException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Factor> root = criteriaQuery.from(Factor.class);
        CriteriaQuery<Long> select = criteriaQuery.select(cb.count(root));

        if (filters != null && filters.size() > 0) {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();
                Predicate p;

                if (value == null) {
                    continue;
                }

                if (field.equals("person.companyCode") || field.equals("provider.shopName") || field.equals("exhibition.nameExhibition")) {

                    if (field.equals("exhibition.nameExhibition")) {
                        try {
                            CriteriaQuery<Exhibition> criteriaQueryExhibition = cb.createQuery(Exhibition.class);
                            Root<Exhibition> rootExhibition = criteriaQueryExhibition.from(Exhibition.class);
                            CriteriaQuery<Exhibition> selectExhibition = criteriaQueryExhibition.select(rootExhibition);
                            selectExhibition.where(cb.equal(rootExhibition.get("nameExhibition"), value));
                            Expression<String> exprExhibition = rootExhibition.get("nameExhibition").as(String.class);
                            selectExhibition.where(cb.like(exprExhibition, "%" + value.toString() + "%"));
                            Exhibition exhibition = em.createQuery(selectExhibition).getSingleResult();
                            Expression<Exhibition> expr2 = root.get("exhibition").as(Exhibition.class);
                            p = cb.equal(expr2, exhibition);
                            predicates.add(p);

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                    if (field.equals("provider.shopName")) {
                        try {
                            CriteriaQuery<Provider> criteriaQueryProvider = cb.createQuery(Provider.class);
                            Root<Provider> rootProvider = criteriaQueryProvider.from(Provider.class);
                            CriteriaQuery<Provider> selectProvider = criteriaQueryProvider.select(rootProvider);
                            selectProvider.where(cb.equal(rootProvider.get("shopName"), value));
                            Expression<String> exprProviderName = rootProvider.get("shopName").as(String.class);
                            selectProvider.where(cb.like(exprProviderName, "%" + value.toString() + "%"));
                            List<Provider> providers = em.createQuery(selectProvider).getResultList();
                            Expression<Provider> expr2 = root.get("provider").as(Provider.class);
                            for (Provider pr : providers) {
                                p = cb.equal(expr2, pr);
                                predicates.add(p);
                            }

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                    if (field.equals("person.companyCode")) {
                        try {
                            CriteriaQuery<Person> criteriaQueryPerson = cb.createQuery(Person.class);
                            Root<Person> rootPerson = criteriaQueryPerson.from(Person.class);
                            CriteriaQuery<Person> selectPerson = criteriaQueryPerson.select(rootPerson);
                            selectPerson.where(cb.equal(rootPerson.get("companyCode"), value));
                            Person person = em.createQuery(selectPerson).getSingleResult();

                            Expression<Person> expr2 = root.get("person").as(Person.class);
                            p = cb.equal(expr2, person);
                            predicates.add(p);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }

                } else if (field.equals("finalRegistration") || field.equals("returned")) {
                    if (field.equals("finalRegistration")) {
                        Expression<Boolean> exprfinalRegistration = root.get("finalRegistration").as(Boolean.class);
                        p = cb.equal(exprfinalRegistration, value);
                        predicates.add(p);
                    }
                    if (field.equals("returned")) {
                        Expression<Boolean> exprfinalRegistration = root.get("returned").as(Boolean.class);
                        p = cb.equal(exprfinalRegistration, value);
                        predicates.add(p);
                    }
                } else {
                    Expression<String> expr = root.get(field).as(String.class);
                    String valueField = value.toString().toLowerCase();
                    if (field.equals("dateFactor")) {
                        try {
                            valueField = ConvertStrToDate(valueField);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    }
                    p = cb.like(cb.lower(expr),
                            "%" + valueField + "%");
                    predicates.add(p);
                }
            }
            if (predicates.size() > 0) {
                criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
            }
        }
        Long count = em.createQuery(select).getSingleResult();
        return count.intValue();
    }

    private String ConvertStrToDate(String s) {
        String[] tempArray;
        String delimiter = "/";
        tempArray = s.split(delimiter);
        int[] temp = {};

        temp = DateConvertor.jalali_to_gregorian(Integer.valueOf(tempArray[0]), Integer.valueOf(tempArray[1]),
                Integer.valueOf(tempArray[2]));

        if (temp.length == 3) {
            for (int i = 0; i < temp.length; i++) {
                if (temp[i] < 10) {
                    tempArray[i] = "0" + String.valueOf(temp[i]);
                } else {
                    tempArray[i] = String.valueOf(temp[i]);
                }
            }
        }
        if (tempArray.length == 3) {
            return tempArray[0] + "-" + tempArray[1] + "-" + tempArray[2];
        }
        return "No Detect ....";
    }
}
