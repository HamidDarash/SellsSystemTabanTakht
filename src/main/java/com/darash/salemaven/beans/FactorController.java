/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.services.FactorFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("factorController")
@ViewScoped
public class FactorController implements Serializable{
    @Inject
    private com.darash.salemaven.services.FactorFacade ejbFacade;
    private LazyDataModel<Factor> items;
    private Factor selected;
    
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
    
     public Factor prepareCreate() {
        selected = new Factor();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FactorCreated"));
//        if (!JsfUtil.isValidationFailed()) {
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, "فاکتور بدرستی بروزرسانی شد");
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, "فاکتور بدرستی حذف شد");
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
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
}
 
 