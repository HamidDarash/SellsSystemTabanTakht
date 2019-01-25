package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Person;
import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.beans.util.JsfUtil.PersistAction;
import com.darash.salemaven.services.PersonFacade;
import com.darash.salemaven.entities.Credit;
import helper.DateConvertor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("personController")
@ViewScoped
public class PersonController implements Serializable {

    @Inject
    private PersonFacade ejbFacade;

    private LazyDataModel<Person> items;

    private Person selected;
    private Credit selectedCredit;

    public LazyDataModel<Person> getItems() {
        return items;
    }

    public void setItems(LazyDataModel<Person> items) {
        this.items = items;
    }

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Person>() {
            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Person> list = ejbFacade.filter(first, pageSize, filters);
                if (filters != null && filters.size() > 0) {
                    this.setRowCount(ejbFacade.getFilteredRowCount(filters));
                }
                return list;
            }

            @Override
            public int getRowCount() {
                return ejbFacade.count();
            }

            @Override
            public Person getRowData(String key) {
                return ejbFacade.find(new Integer(key));
            }

        };
    }

    public PersonFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PersonFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public String getShamsiDate(String date) {
        if (date.isEmpty()) {
            return "none";
        }
        String[] array = date.split("\\-", -1);
        int year = Integer.valueOf(array[0]);
        int month = Integer.valueOf(array[1]);
        int day = Integer.valueOf(array[2]);
        int[] tarikh_out = DateConvertor.gregorian_to_jalali(year, month, day);
        return String.valueOf(tarikh_out[0]) + "/" + String.valueOf(tarikh_out[1]) + "/" + String.valueOf(tarikh_out[2]);
    }

    public String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        return dateString;
    }

    public Date getLastCreditDate(Person p) {
        try {
            Date createdAt = p.getCredits().get(p.getCredits().size() - 1).getCreateAt();
            return createdAt;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new Date();
    }

    public Credit getSelectedCredit() {
        return selectedCredit;
    }

    public void setSelectedCredit(Credit selectedCredit) {
        this.selectedCredit = selectedCredit;
    }

    public Person getSelected() {

        return selected;
    }

    public void setSelected(Person selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PersonFacade getFacade() {
        return ejbFacade;
    }

    public Person prepareCreate() {
        selected = new Person();

        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PersonDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
        }
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

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Person getPerson(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Person> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Person> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Person.class)
    public static class PersonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonController controller = (PersonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personController");
            return controller.getPerson(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Person) {
                Person o = (Person) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Person.class.getName()});
                return null;
            }
        }

    }

    public void onRowEdit(RowEditEvent event) {

//        selected.getCredits().set(selected.getCredits().indexOf(event.getObject()),null );
        System.out.print(((Credit) event.getObject()).getId());
        FacesMessage msg = new FacesMessage("ویرایش شد", String.valueOf(((Credit) event.getObject()).getCredit()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("کنسل شد", String.valueOf(((Credit) event.getObject()).getCredit()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onNewAdd() {
        try {
            selected.getCredits().add(new Credit(0));
        } catch (Exception e) {

        }
        FacesMessage msg = new FacesMessage("اضافه کردن رکورد جدید");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowSelect(SelectEvent event) {
        System.out.println("Hello");
        FacesMessage msg = new FacesMessage("اعتبار فعلی : " + String.valueOf(this.getCreditSum(((Person) event.getObject()).getCredits())));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("کنسلی انتخاب", String.valueOf(((Credit) event.getObject()).getCredit()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowRemove(Credit c) {
        selected.getCredits().remove(c);
        FacesMessage msg = new FacesMessage("حذف آیتم مورد نظر");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
