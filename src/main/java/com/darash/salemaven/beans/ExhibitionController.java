package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Exhibition;
import com.darash.salemaven.services.util.JsfUtil;
import com.darash.salemaven.services.util.JsfUtil.PersistAction;
import com.darash.salemaven.services.ExhibitionFacade;

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

@Named("exhibitionController")
@SessionScoped
public class ExhibitionController implements Serializable {

    @EJB
    private com.darash.salemaven.services.ExhibitionFacade ejbFacade;
    private List<Exhibition> items = null;
    private Exhibition selected;

    public ExhibitionController() {
    }

    public Exhibition getSelected() {
        return selected;
    }

    public void setSelected(Exhibition selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ExhibitionFacade getFacade() {
        return ejbFacade;
    }

    public Exhibition prepareCreate() {
        selected = new Exhibition();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/BundleExibition").getString("ExhibitionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/BundleExibition").getString("ExhibitionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/BundleExibition").getString("ExhibitionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Exhibition> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleExibition").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleExibition").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Exhibition getExhibition(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Exhibition> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Exhibition> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Exhibition.class)
    public static class ExhibitionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ExhibitionController controller = (ExhibitionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "exhibitionController");
            return controller.getExhibition(getKey(value));
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
            if (object instanceof Exhibition) {
                Exhibition o = (Exhibition) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Exhibition.class.getName()});
                return null;
            }
        }

    }
    
    public void convertDateStringToDate(){
        
    }

}
