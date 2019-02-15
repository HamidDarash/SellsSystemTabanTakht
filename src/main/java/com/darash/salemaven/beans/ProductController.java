package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Product;
import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.beans.util.JsfUtil.PersistAction;
import com.darash.salemaven.entities.Provider;
import com.darash.salemaven.services.ProductFacade;
import com.darash.salemaven.services.ProviderFacade;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("productController")
@SessionScoped
public class ProductController implements Serializable {

    @EJB
    private com.darash.salemaven.services.ProductFacade ejbFacade;
    private LazyDataModel<Product> items;
    private Product selected;
    @EJB
    private com.darash.salemaven.services.ProviderFacade providerFacade;

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Product>() {
            @Override
            public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Product> list = ejbFacade.filter(first, pageSize, filters);
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
            public Product getRowData(String key) {
                return ejbFacade.find(new Integer(key));
            }

        };
    }

    public ProviderFacade getProviderFacade() {
        return providerFacade;
    }

    public void setProviderFacade(ProviderFacade providerFacade) {
        this.providerFacade = providerFacade;
    }

    public ProductController() {
    }

    public List<Provider> findInAllProviderForAutoComplete(String search) {
        return providerFacade.findByNameOrCodeOrFullnameSearch(search);
    }

    public Product getSelected() {
        return selected;
    }

    public void setSelected(Product selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProductFacade getFacade() {
        return ejbFacade;
    }

    public Product prepareCreate() {
        selected = new Product();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ProductCreated"));
//        if (!JsfUtil.isValidationFailed()) {
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ProductUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ProductDeleted"));
//        if (!JsfUtil.isValidationFailed()) {
//            selected = null; // Remove selection
//            items = null;    // Invalidate list of items to trigger re-query.
//        }
    }

    public ProductFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ProductFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public LazyDataModel<Product> getItems() {
        return items;
    }

    public void setItems(LazyDataModel<Product> items) {
        this.items = items;
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

    public Product getProduct(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Product> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Product> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Product.class)
    public static class ProductControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductController controller = (ProductController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productController");
            return controller.getProduct(getKey(value));
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
            if (object instanceof Product) {
                Product o = (Product) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Product.class.getName()});
                return null;
            }
        }

    }

}
