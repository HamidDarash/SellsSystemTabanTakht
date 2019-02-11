package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Provider;
import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.beans.util.JsfUtil.PersistAction;
import com.darash.salemaven.services.ProviderFacade;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("providerController")
@ViewScoped
public class ProviderController implements Serializable {

    @Inject
    private com.darash.salemaven.services.ProviderFacade ejbFacade;
    private LazyDataModel<Provider> items;
    private Provider selected;

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Provider>() {
            @Override
            public List<Provider> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Provider> list = ejbFacade.filter(first, pageSize, filters);
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
            public Provider getRowData(String key) {
                return ejbFacade.find(new Integer(key));
            }

        };
    }

    public ProviderController() {
    }

    public Provider getSelected() {
        return selected;
    }

    public void setSelected(Provider selected) {
        this.selected = selected;
    }

    public ProviderFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ProviderFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public LazyDataModel<Provider> getItems() {
        return items;
    }

    public void setItems(LazyDataModel<Provider> items) {
        this.items = items;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProviderFacade getFacade() {
        return ejbFacade;
    }

    public Provider prepareCreate() {
        selected = new Provider();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, "تامین کننده بدرستی ایجاد شد");
//        if (!JsfUtil.isValidationFailed()) {
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
    }

    public void update() {
        persist(PersistAction.UPDATE, "تامین کننده مورد نظر بدرستی ویرایش شد");
    }

    public void destroy() {
        persist(PersistAction.DELETE, "تامین کننده مورد نظر حذف شد");
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
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

    public Provider getProvider(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Provider> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Provider> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Provider.class)
    public static class ProviderControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProviderController controller = (ProviderController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "providerController");
            return controller.getProvider(getKey(value));
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
            if (object instanceof Provider) {
                Provider o = (Provider) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Provider.class.getName()});
                return null;
            }
        }

    }

}
