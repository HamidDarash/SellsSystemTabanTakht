package com.darash.salemaven.beans;

import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.entities.Credit;
import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.entities.FactorDetail;
import com.darash.salemaven.entities.Person;
import com.darash.salemaven.entities.Product;
import com.darash.salemaven.entities.Provider;
import com.darash.salemaven.services.FactorFacade;
import com.darash.salemaven.services.PersonFacade;
import com.darash.salemaven.services.ProductFacade;
import com.darash.salemaven.services.ProviderFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("factorController")
@ViewScoped
public class FactorController implements Serializable {

    @EJB
    private com.darash.salemaven.services.FactorFacade ejbFacade;
    @EJB
    private com.darash.salemaven.services.PersonFacade personFacade;
    @EJB
    private com.darash.salemaven.services.ProviderFacade providerFacade;
    @EJB
    private com.darash.salemaven.services.ProductFacade productFacade;

    private LazyDataModel<Factor> items;
    private Factor selected;
    private Person selectedPerson;
    private Provider selectedProvider;
    private List<FactorDetail> selectedFactorDetails = new ArrayList<>();
    private Product productSelectForInsert = null;
    private FactorDetail rowFactorDetail = null;
    private boolean stateForInsertDetail = false;

    public void onSelectSaveFactor() {
        if (selectedPerson != null && selectedProvider != null) {
            selected.setPerson(selectedPerson);
            selected.setProvider(selectedProvider);
            selected.setFinalRegistration(false);
            selected.setReturned(false);
            this.setStateForInsertDetail(true);
            selectedFactorDetails.add(new FactorDetail());
        }
    }

    public Factor prepareCreate() {
        selected = new Factor();
        initializeEmbeddableKey();
        return selected;
    }

    public FactorDetail getRowFactorDetail() {
        return rowFactorDetail;
    }

    public void setRowFactorDetail(FactorDetail rowFactorDetail) {
        this.rowFactorDetail = rowFactorDetail;
    }

    public boolean isStateForInsertDetail() {
        return stateForInsertDetail;
    }

    public void setStateForInsertDetail(boolean stateForInsertDetail) {
        this.stateForInsertDetail = stateForInsertDetail;
    }

    public Product getProductSelectForInsert() {
        return productSelectForInsert;
    }

    public void setProductSelectForInsert(Product productSelectForInsert) {
        this.productSelectForInsert = productSelectForInsert;
    }

    public ProductFacade getProductFacade() {
        return productFacade;
    }

    public void setProductFacade(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    public List<FactorDetail> getSelectedFactorDetails() {
        return selectedFactorDetails;
    }

    public void setSelectedFactorDetails(List<FactorDetail> selectedFactorDetails) {
        this.selectedFactorDetails = selectedFactorDetails;
    }

    public ProviderFacade getProviderFacade() {
        return providerFacade;
    }

    public void setProviderFacade(ProviderFacade providerFacade) {
        this.providerFacade = providerFacade;
    }

    public Provider getSelectedProvider() {
        return selectedProvider;
    }

    public void setSelectedProvider(Provider selectedProvider) {
        this.selectedProvider = selectedProvider;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }

    // find users autoComplete
    public List<Person> findInAllUsersForAutoComplete(String search) {
        return personFacade.findByInternationalOrNameOrCompanyCode(search);
    }

    // find provider autoComplete
    public List<Provider> findInAllProviderForAutoComplete(String search) {
        return providerFacade.findByNameOrCodeOrFullnameSearch(search);
    }

    // find product autoComplete
    public List<Product> findInAllProductForAutoComplete(String search) {
        return productFacade.findProductByIdOrModelOrProductName(search);
    }

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Factor>() {
            @Override
            public List<Factor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Factor> list = ejbFacade.filter(first, pageSize, filters);
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
            public Factor getRowData(String key) {
                return ejbFacade.find(new Integer(key));
            }

        };
    }

    public FactorController() {
    }

    public FactorFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(FactorFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public LazyDataModel<Factor> getItems() {
        return items;
    }

    public void setItems(LazyDataModel<Factor> items) {
        this.items = items;
    }

    public Factor getSelected() {
        return selected;
    }

    public void setSelected(Factor selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    //-------------------------------------------------------------------------------
    public void create() {
        persist(JsfUtil.PersistAction.CREATE, "فاکتور بدرستی ساخته شد");
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, "فاکتور بدرستی بروزرسانی شد");
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, "فاکتور بدرستی حذف شد");
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {

                    getEjbFacade().edit(selected);
                } else {
                    getEjbFacade().remove(selected);
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

    public Factor getFactor(java.lang.Long id) {
        return getEjbFacade().find(id);
    }

    public List<Factor> getItemsAvailableSelectMany() {
        return getEjbFacade().findAll();
    }

    public List<Factor> getItemsAvailableSelectOne() {
        return getEjbFacade().findAll();
    }

    @FacesConverter(forClass = Factor.class)
    public static class FactorControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FactorController controller = (FactorController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "FactorController");
            return controller.getFactor(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Factor) {
                Factor o = (Factor) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Factor.class.getName()});
                return null;
            }
        }

    }

    
//   public void onRowEdit(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Car Edited", ((Car) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
//     
//    public void onRowCancel(RowEditEvent event) {
//        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
   
}
