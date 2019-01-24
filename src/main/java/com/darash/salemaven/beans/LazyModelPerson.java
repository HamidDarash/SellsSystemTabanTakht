///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.darash.salemaven.beans;
//
//import com.darash.salemaven.entities.Person;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import org.primefaces.model.LazyDataModel;
//import org.primefaces.model.SortOrder;
//
///**
// *
// * @author Hamid
// */
//public class LazyModelPerson extends LazyDataModel<Person> {
//  
//    private List<Person> dataSource;
//
//    public LazyModelPerson(List<Person> dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public Person getRowData(String rowKey) {
//        for (Person person : dataSource) {
//            if (person.getId().equals(rowKey)) {
//                return person;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Object getRowKey(Person object) {
//        return object.getId();
//    }
//
//    @Override
//    public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//        List<Person> data = new ArrayList<>();
//        for (Person person : dataSource) {
//            boolean match = true;
//            if (filters != null) {
//                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
//                    try {
//                        String filterProperty = it.next();
//                        Object filterValue = filters.get(filterProperty);
//                        String fieldValue = String.valueOf(person.getClass().getField(filterProperty).get(person));
//
//                        if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
//                            match = true;
//                        } else {
//                            match = false;
//                            break;
//                        }
//                    } catch (Exception e) {
//                        match = false;
//                    }
//                }
//            }
//
//            if (match) {
//                data.add(person);
//            }
//        }
//    
//
//        //rowCount
//        int dataSize = data.size();
//        this.setRowCount(dataSize);
//
//        //paginate
//        if (dataSize > pageSize) {
//            try {
//                return data.subList(first, first + pageSize);
//            } catch (IndexOutOfBoundsException e) {
//                return data.subList(first, first + (dataSize % pageSize));
//            }
//        } else {
//            return data;
//        }
//    }
//}
