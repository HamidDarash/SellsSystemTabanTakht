/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.controller.LazyModels.person;

import com.darash.salemaven.beans.PersonFacade;
import com.darash.salemaven.controller.PersonController;
import com.darash.salemaven.entities.Credit;
import com.darash.salemaven.entities.Person;
import helper.DateConvertor;
import helper.JalaliCalendar;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

@ManagedBean(name = "lzViewPerson")
@ViewScoped
public class LazyViewPerson implements Serializable {

    private LazyDataModel<Person> lazyDataPersonModel;
    private Person selected;

    public LazyDataModel<Person> getLazyDataPersonModel() {
        return lazyDataPersonModel;
    }

    public void setLazyDataPersonModel(LazyDataModel<Person> lazyDataPersonModel) {
        this.lazyDataPersonModel = lazyDataPersonModel;
    }

    public Person getSelected() {
        return selected;
    }

    public void setSelected(Person selected) {
        this.selected = selected;
    }

    @ManagedProperty("#{personService}")
    private PersonService personService;

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public String getLastCreditDate(Person p) {
        try {
            Date createdAt = p.getCredits().get(p.getCredits().size() - 1).getCreateAt();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(createdAt);
            return getShamsiDate(dateString);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String getShamsiDate(String date) {
        if(date.isEmpty()) return "none";
        String[] array = date.split("\\-", -1);
//        for (int i = 0; i < array.length; i++) {
//            System.out.println(array[i]);  
//            
//        }
//         System.out.println("-------------------"); 
        int year = Integer.valueOf(array[0]);
        int month = Integer.valueOf(array[1]);
        int day = Integer.valueOf(array[2]);
        int[] tarikh_out = DateConvertor.gregorian_to_jalali(year, month, day);
        return String.valueOf(tarikh_out[0]) + "/" + String.valueOf(tarikh_out[1]) + "/" + String.valueOf(tarikh_out[2]);
    }

    public long getCreditSum(List<Credit> credits) {
        Iterator it = credits.iterator();
        long sum = 0;
        while (it.hasNext()) {
            Credit obj = (Credit) it.next();
            sum += obj.getCredit();
        }
        return sum;
    }

    @PostConstruct
    private void init() {
        lazyDataPersonModel = new LazyModelPerson(personService.getItems());
    }

}
