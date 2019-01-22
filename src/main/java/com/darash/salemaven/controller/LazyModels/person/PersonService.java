package com.darash.salemaven.controller.LazyModels.person;

import com.darash.salemaven.beans.PersonFacade;
import com.darash.salemaven.entities.Person;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "personService")
@ApplicationScoped
public class PersonService {

    @EJB
    private com.darash.salemaven.beans.PersonFacade ejbFacade;

    public PersonFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PersonFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public List<Person> getItems() {
        return getEjbFacade().findAll();
    }
    
    public int getCount(){
        return getEjbFacade().count();
    }
    
}
