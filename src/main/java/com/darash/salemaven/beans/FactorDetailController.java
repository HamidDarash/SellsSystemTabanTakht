package com.darash.salemaven.beans;

import com.darash.salemaven.entities.FactorDetail;
import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.beans.util.JsfUtil.PersistAction;
import com.darash.salemaven.services.FactorDetailFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("factorDetailController")
@SessionScoped
public class FactorDetailController implements Serializable {

    @EJB
    private com.darash.salemaven.services.FactorDetailFacade ejbFacade;
    private List<FactorDetail> items = null;
    private FactorDetail selected;

    public FactorDetailController() {
    }

    public FactorDetail getSelected() {
        return selected;
    }

    public void setSelected(FactorDetail selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FactorDetailFacade getFacade() {
        return ejbFacade;
    }

    public FactorDetail prepareCreate() {
        selected = new FactorDetail();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_factor_detail").getString("FactorDetailCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_factor_detail").getString("FactorDetailUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_factor_detail").getString("FactorDetailDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<FactorDetail> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_factor_detail").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_factor_detail").getString("PersistenceErrorOccured"));
            }
        }
    }

    public FactorDetail getFactorDetail(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<FactorDetail> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<FactorDetail> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = FactorDetail.class)
    public static class FactorDetailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FactorDetailController controller = (FactorDetailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "factorDetailController");
            return controller.getFactorDetail(getKey(value));
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
            if (object instanceof FactorDetail) {
                FactorDetail o = (FactorDetail) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), FactorDetail.class.getName()});
                return null;
            }
        }

    }

}
