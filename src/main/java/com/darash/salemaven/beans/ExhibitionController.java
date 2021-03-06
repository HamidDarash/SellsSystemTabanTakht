package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Exhibition;
import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.entities.Provider;
import com.darash.salemaven.services.util.JsfUtil;
import com.darash.salemaven.services.util.JsfUtil.PersistAction;
import com.darash.salemaven.services.ExhibitionFacade;
import com.darash.salemaven.services.FactorFacade;
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
import javax.inject.Named;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named("exhibitionController")
@ViewScoped
public class ExhibitionController implements Serializable {

    @EJB
    private com.darash.salemaven.services.ExhibitionFacade ejbFacade;
    @EJB
    private com.darash.salemaven.services.ProviderFacade providerFacade;
    @EJB
    private com.darash.salemaven.services.FactorFacade factorFacade;

    private LazyDataModel<Exhibition> items;
    private Exhibition selected;
    private Provider selectedProvider;

    public List<Factor> factorsOfProviderInExhibition() {
        try {
            return factorFacade.findByProviderInExhibition(selected, selectedProvider);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public FactorFacade getFactorFacade() {
        return factorFacade;
    }

    public void setFactorFacade(FactorFacade factorFacade) {
        this.factorFacade = factorFacade;
    }

    public ProviderFacade getProviderFacade() {
        return providerFacade;
    }

    public void setProviderFacade(ProviderFacade providerFacade) {
        this.providerFacade = providerFacade;
    }

    public ExhibitionFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ExhibitionFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public LazyDataModel<Exhibition> getItems() {
        return items;
    }

    public void setItems(LazyDataModel<Exhibition> items) {
        this.items = items;
    }

    public List<Provider> findInAllProviderForAutoComplete(String search) {
        return providerFacade.findByNameOrCodeOrFullnameSearch(search);
    }

    public void addToListProviders() {
        if (selectedProvider != null) {
            if (!selected.getProviders().contains(selectedProvider)) {
                selectedProvider.getExhibitions().add(selected);
                selected.getProviders().add(selectedProvider);
                selectedProvider = null;
                JsfUtil.addSuccessMessage("با موفقیت اضافه شد");
            } else {
                JsfUtil.addSuccessMessage("تامبن کننده در لیست موجود است");
                selectedProvider = null;
            }
        }
    }

    public int convertSumWageInteger(double sumWage) {
        return (int) sumWage;
    }

    public void deleteProviderOfList(Provider p) {
        p.getExhibitions().remove(selected);
        selected.getProviders().remove(p);
        JsfUtil.addSuccessMessage("بدرستی از لیست حذف شد");
    }

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Exhibition>() {
            @Override
            public List<Exhibition> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Exhibition> list = ejbFacade.filter(first, pageSize, filters);
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
            public Exhibition getRowData(String key) {
                return ejbFacade.find(new Integer(key));
            }
        };
    }

    public ExhibitionController() {

    }

    public Provider getSelectedProvider() {
        return selectedProvider;
    }

    public void setSelectedProvider(Provider selectedProvider) {
        this.selectedProvider = selectedProvider;
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
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/BundleExibition").getString("ExhibitionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/BundleExibition").getString("ExhibitionDeleted"));
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                    selected = null;
                    selectedProvider = null;
                } else {
                    selected.setProviders(null);
                    getFacade().remove(selected);
                    selected = null;
                    selectedProvider = null;
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

    public String headerProviderFactors() {
        if (this.selectedProvider != null) {
            return "فاکتورهای " + this.selectedProvider.getShopName() + "  |  " + this.selected.getNameExhibition();
        }
        return "فاکتورهای فروشگاه";
    }

    public void rowSelectedDataTableProvider(SelectEvent event) {

    }
}
