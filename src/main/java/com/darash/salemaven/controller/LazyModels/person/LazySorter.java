/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.controller.LazyModels.person;

import com.darash.salemaven.entities.Person;
import org.primefaces.model.SortOrder;
import java.util.Comparator;
import org.primefaces.model.SortOrder;
 

public class LazySorter implements Comparator<Person> {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(Person person1, Person person2) {
        try {
            Object value1 = Person.class.getField(this.sortField).get(person1);
            Object value2 = Person.class.getField(this.sortField).get(person2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
