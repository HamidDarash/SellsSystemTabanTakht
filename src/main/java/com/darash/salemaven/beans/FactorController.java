package com.darash.salemaven.beans;

import com.darash.salemaven.beans.util.JsfUtil;
import com.darash.salemaven.entities.Credit;
import com.darash.salemaven.entities.Exhibition;
import com.darash.salemaven.entities.Factor;
import com.darash.salemaven.entities.FactorDetail;
import com.darash.salemaven.entities.Person;
import com.darash.salemaven.entities.Product;
import com.darash.salemaven.entities.Provider;
import com.darash.salemaven.services.CreditFacade;
import com.darash.salemaven.services.ExhibitionFacade;
import com.darash.salemaven.services.FactorFacade;
import com.darash.salemaven.services.PersonFacade;
import com.darash.salemaven.services.ProductFacade;
import com.darash.salemaven.services.ProviderFacade;
import java.io.Serializable;
import java.text.DecimalFormat;
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
import javax.faces.view.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import helper.QrCode;
import java.text.ParseException;
import java.util.UUID;
import javax.servlet.ServletContext;

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
    @EJB
    private com.darash.salemaven.services.ExhibitionFacade exhibitionFacade;
    @EJB
    private com.darash.salemaven.services.CreditFacade creditFacade;

    private LazyDataModel<Factor> items;
    private Factor selected;
    private Person selectedPerson = null;
    private Person lastSelectedPerson = null;
    private QrCode qrCode = new QrCode();

    public Person getLastSelectedPerson() {
        return lastSelectedPerson;
    }

    public void setLastSelectedPerson(Person lastSelectedPerson) {
        this.lastSelectedPerson = lastSelectedPerson;
    }
    private Provider selectedProvider = null;
    private List<FactorDetail> selectedFactorDetails;
    private Product productSelectForInsert = null;
    private Exhibition selectedExhibitionProvider;
    private FactorDetail rowFactorDetail;
    private FactorDetail rowFactorDetailSelect;
    private boolean stateForInsertDetail = false;
    private String selectedFactorPurgeAndProfitGeneral = null;
    private String selectedLastCondinationTypeFactor = "";

    //-------------------------------------------
    //                 fillable
    //    
    //-------------------------------------------
    private int countProduct = 0;
    private String discountProduct = "0";

    public String formatMony(Long x) {
        try {
            return new DecimalFormat("###,###,###").format(x);
        } catch (Exception e) {
            return "";
        }
    }

    public String formatMonyString(String x) {
        try {
            long c = Long.valueOf(x);
            return new DecimalFormat("###,###,###").format(c);
        } catch (Exception e) {
            return "";
        }
    }

    public long getCreditSelectedUser() {
        if (selectedPerson != null) {
            return creditFacade.getCreditUser(selectedPerson);
        }
        return 0;
    }

    public long getCreditSum(Person per) {
        if (per != null) {
            return creditFacade.getCreditUser(per);
        }
        return 0;
    }

    public void generateQrCode(String path, String code) {
        qrCode.generate(path, code);
    }

    public String getSelectedLastCondinationTypeFactor() {
        return selectedLastCondinationTypeFactor;
    }

    public void setSelectedLastCondinationTypeFactor(String selectedLastCondinationTypeFactor) {
        this.selectedLastCondinationTypeFactor = selectedLastCondinationTypeFactor;
    }

    public String getSelectedFactorPurgeAndProfitGeneral() {
        return selectedFactorPurgeAndProfitGeneral;
    }

    public void setSelectedFactorPurgeAndProfitGeneral(String selectedFactorPurgeAndProfitGeneral) {
        this.selectedFactorPurgeAndProfitGeneral = selectedFactorPurgeAndProfitGeneral;
    }

    public FactorDetail getRowFactorDetailSelect() {
        return rowFactorDetailSelect;
    }

    public void setRowFactorDetailSelect(FactorDetail rowFactorDetailSelect) {
        this.rowFactorDetailSelect = rowFactorDetailSelect;
    }

    public void deleteFactorDetailListEditForm(FactorDetail item) {
        try {
            this.selected.getFactorDetails().remove(item);
            this.rowFactorDetailSelect = null;
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "آیتم " + item.getProductName() + " " + "حذف شد",
                    "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
        }

    }

    public void deleteFactorDetailList(FactorDetail item) {
        try {
            this.selectedFactorDetails.remove(item);
            this.rowFactorDetailSelect = null;
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "آیتم " + item.getProductName() + " " + "حذف شد",
                    "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
        }

    }

    public void processing() {
        synchronized (this) {
            try {
                wait(1000);
            } catch (InterruptedException e1) {
                // waiting interrupted
            }
        }

    }

    public CreditFacade getCreditFacade() {
        return creditFacade;
    }

    public void setCreditFacade(CreditFacade creditFacade) {
        this.creditFacade = creditFacade;
    }

    public ExhibitionFacade getExhibitionFacade() {
        return exhibitionFacade;
    }

    public void setExhibitionFacade(ExhibitionFacade exhibitionFacade) {
        this.exhibitionFacade = exhibitionFacade;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }

    public String getDiscountProduct() {
        return discountProduct;
    }

    public void setDiscountProduct(String discountProduct) {
        this.discountProduct = discountProduct;
    }

    public List<Exhibition> filterOnlyActive(boolean bool) {
        try {
            return exhibitionFacade.getActivateExhibition(bool);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public Factor prepareUpdate() {
        this.productSelectForInsert = new Product();
        discountProduct = "0";
        countProduct = 0;
        return selected;
    }

    public void messageSelectDate() {
        FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO, this.selected.getDateFactor().toString(),
                "Cell Changed");
        FacesContext.getCurrentInstance().addMessage(null, msg2);
    }

    public Factor prepareCreate() {
        selected = new Factor();
        selectedFactorDetails = new ArrayList<>();
        rowFactorDetailSelect = new FactorDetail();
        setDiscountProduct("0");
        setCountProduct(0);
        initializeEmbeddableKey();
        return selected;
    }

    public Exhibition getSelectedExhibitionProvider() {
        return selectedExhibitionProvider;
    }

    public void setSelectedExhibitionProvider(Exhibition selectedExhibitionProvider) {
        this.selectedExhibitionProvider = selectedExhibitionProvider;
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
        try {
            return personFacade.findByInternationalOrNameOrCompanyCode(search);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    // find provider autoComplete
    public List<Provider> findInAllProviderForAutoComplete(String search) {
        try {
            return providerFacade.findByNameOrCodeOrFullnameSearchAndExhibition(search, selectedExhibitionProvider);
        } catch (Exception e) {
        }
        return new ArrayList<>();

    }

    // find product autoComplete
    public List<Product> findInAllProductForAutoCompleteByProviderCreateForm(String search) {
        try {
            return productFacade.findProductByIdOrModelOrProductNameByProvider(search, selectedProvider);
        } catch (Exception e) {

        }
        return new ArrayList<>();
    }

    // find product autoComplete
    public List<Product> findInAllProductForAutoCompleteByProviderEditForm(String search) {

        return productFacade.findProductByIdOrModelOrProductNameByProvider(search, selected.getProvider());

    }

    public void resetSelectProduct() {
        selectedFactorDetails.clear();
        productSelectForInsert = null;
        JsfUtil.addSuccessMessage("تامین کننده تغییر کرد");
    }

    @PostConstruct
    public void initDataModel() {
        items = new LazyDataModel<Factor>() {
            @Override
            public List<Factor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<Factor> list = ejbFacade.filter(first, pageSize, filters);
                if (filters != null && filters.size() > 0) {
                    try {
                        this.setRowCount(ejbFacade.getFilteredRowCount(filters));
                    } catch (ParseException ex) {
                        Logger.getLogger(FactorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public void toggleRegisterFactor() {
        if (selected != null) {
            selected.setFinalRegistration(!selected.isFinalRegistration());
            ejbFacade.edit(selected);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "تغییر وضعیت فاکتور",
                    "اطلاعات برنامه");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void toggleReturnedFactor() {
        if (selected != null) {
            selected.setReturned(!selected.getReturned());
            ejbFacade.edit(selected);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "تغییر مرجوعی فاکتور",
                    "اطلاعات برنامه");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void onAddToFactorDetailEdit() {
        if (countProduct <= 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "تعداد محصول نباید صفر وارد شود",
                    "خطا رخ داده");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            rowFactorDetail = new FactorDetail();
            rowFactorDetail.setUnit(productSelectForInsert.getUnit());
            rowFactorDetail.setPrice(productSelectForInsert.getPrice());
            rowFactorDetail.setProductName(productSelectForInsert.getProductName());
            rowFactorDetail.setProductId(productSelectForInsert.getId());
            rowFactorDetail.setWarranty(productSelectForInsert.isWarranty());
            rowFactorDetail.setInsurance(productSelectForInsert.isInsurance());
            this.rowFactorDetail.setDiscount(discountProduct);
            this.rowFactorDetail.setCountProduct(countProduct);
            this.rowFactorDetail.setPriceAfterDiscount(String.valueOf(this.getCountProduct() * Long.valueOf(this.productSelectForInsert.getPrice()) - Long.valueOf(this.getDiscountProduct())));
            this.selected.getFactorDetails().add(this.rowFactorDetail);
            this.setDiscountProduct("0");
            this.setCountProduct(0);
            this.productSelectForInsert = null;
        }
    }

    public void onAddToFactorDetail() {

        if (countProduct <= 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "تعداد محصول نباید صفر وارد شود",
                    "خطا رخ داده");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            rowFactorDetail = new FactorDetail();
            JsfUtil.addSuccessMessage("اضافه شد به لیست");
            rowFactorDetail.setUnit(productSelectForInsert.getUnit());
            rowFactorDetail.setPrice(productSelectForInsert.getPrice());
            rowFactorDetail.setProductName(productSelectForInsert.getProductName());
            rowFactorDetail.setProductId(productSelectForInsert.getId());
            rowFactorDetail.setModel(productSelectForInsert.getModel());
            rowFactorDetail.setWarranty(productSelectForInsert.isWarranty());
            rowFactorDetail.setInsurance(productSelectForInsert.isInsurance());
            this.rowFactorDetail.setDiscount(discountProduct);
            this.rowFactorDetail.setCountProduct(countProduct);
            this.rowFactorDetail.setPriceAfterDiscount(String.valueOf(this.getCountProduct() * Long.valueOf(this.productSelectForInsert.getPrice()) - Long.valueOf(this.getDiscountProduct())));
            this.selectedFactorDetails.add(this.rowFactorDetail);
            this.setDiscountProduct("0");
            this.setCountProduct(0);
            this.productSelectForInsert = null;
        }

    }

    public void checkInsertModeUserSelected() {
        if (lastSelectedPerson != null) {
            lastSelectedPerson.setInsertMode(false);
            personFacade.edit(lastSelectedPerson);
        }
        if (selectedPerson != null) {
            selectedPerson.setInsertMode(false);
            personFacade.edit(selectedPerson);
        }
        selected = null;
        lastSelectedPerson = null;
        selectedPerson = null;
        selectedProvider = null;
    }

    public void checkInsertModeUserSelectedEdit() {
        if (lastSelectedPerson != null) {
            lastSelectedPerson.setInsertMode(false);
            personFacade.edit(lastSelectedPerson);
        }
        if (selected.getPerson() != null) {
            selected.getPerson().setInsertMode(false);
            personFacade.edit(selected.getPerson());
        }
    }

    public void checkCreditPersonEdit() {
        if (selected.getPerson() != null && selected.getPerson().isInsertMode()) {
            showMessageInsertModeLock();
        } else if ((getCreditSum(selected.getPerson()) <= 0) && selected.getCondinationFactor().equals("اقساط")) {
            showMessage();
        } else {
            if (lastSelectedPerson != null) {
                lastSelectedPerson.setInsertMode(false);
                personFacade.edit(lastSelectedPerson);
            }
            if (selected.getPerson() != null) {
                selected.getPerson().setInsertMode(true);
                personFacade.edit(selected.getPerson());
                lastSelectedPerson = selected.getPerson();
                JsfUtil.addSuccessMessage("پرسنل در حالت ایجاد فاکتور قرار گرفت");
            }
        }
    }

    public void checkCreditPerson() {
        if (selectedPerson != null && selectedPerson.isInsertMode()) {
            showMessageInsertModeLock();
            selectedPerson = null;
        } else if ((getCreditSum(selectedPerson) <= 0) && selected.getCondinationFactor().equals("اقساط")) {
            showMessage();
            selectedPerson = null;
        } else {
            if (lastSelectedPerson != null) {
                lastSelectedPerson.setInsertMode(false);
                personFacade.edit(lastSelectedPerson);
            }
            if (selectedPerson != null) {
                selectedPerson.setInsertMode(true);
                personFacade.edit(selectedPerson);
                lastSelectedPerson = selectedPerson;
                JsfUtil.addSuccessMessage("پرسنل در حالت ایجاد فاکتور قرار گرفت");
            }
        }
    }

    public void showMessage() {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا", "اعتبار پرسنل کافی نیست"));
    }

    public void showMessageInsertModeLock() {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا", "این کاربر در حالت ثبت فاکتور می باشد لطفا بعدا ثبت فاکتور کنید"));
    }

    public void showMessageInsertModeLockOrCredit() {
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_ERROR, "خطا", "این کاربر در حالت ثبت فاکتور می باشد یا اعتبار کافی ندارد لطفا بعدا ثبت فاکتور کنید"));
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {

                    if (persistAction == JsfUtil.PersistAction.CREATE) {

                        int sumPrice = 0;//جمع کل
                        int sumDiscount = 0;//جمع کل تخفیف

                        for (FactorDetail fd : selectedFactorDetails) {
                            fd.setFactor(selected);
                            sumPrice += Integer.valueOf(fd.getPriceAfterDiscount());
                            sumDiscount += Integer.valueOf(fd.getDiscount().isEmpty() ? "0" : fd.getDiscount());
                        }

                        double profit = 0.0;//درصد قسط
                        if (Integer.valueOf(selected.getInstallmentCount()) > 6) {
                            profit = 1.8;
                        } else {
                            profit = 1.5;
                        }

                        System.out.println("پیش پرداخت " + selected.getPrepayable());
                        //محاسبه هر قسط
                        double generalProfit = (Integer.valueOf(selected.getInstallmentCount()) * profit) / 100;
                        //خالص مانده
                        int purePrice = sumPrice - Integer.valueOf(selected.getPrepayable());
                        System.out.println("Khales Mandeh: " + purePrice);

                        // کل سود روی خالص  مانده
                        double installmentValueComputing = purePrice * generalProfit;
                        // مبلغ هر قسط ماهیانه
                        double sumInstallmentAndPaymanet
                                = (installmentValueComputing + purePrice) / Integer.valueOf(selected.getInstallmentCount());
                        //جمع سود و خالص پرداختی
                        long sumPurgeAndProfit = purePrice + ((int) installmentValueComputing);

                        selected.setFactorDetails(selectedFactorDetails);
                        selected.setSumFactor(String.valueOf(sumPrice));//جمع کل کالاها
                        selected.setPayable(String.valueOf(purePrice));//جمع کل کالاها کم میشود از جمع تخفیف
                        selected.setSumDiscount(String.valueOf(sumDiscount));//جمع کا تخفیفات
                        selected.setSumWage((int) (selectedProvider.getWage() * purePrice));//جمع کل کارمزد
                        selected.setFinalRegistration(true);//ثبت نهایی شود

                        selected.setInstallmentCount(selected.getCondinationFactor().equals("اقساط") ? selected.getInstallmentCount() : "0");

                        selected.setInstallmentValue(selected.getCondinationFactor().equals("اقساط") ? String.valueOf((int) sumInstallmentAndPaymanet) : "0");
                        selected.setPercentage(selected.getCondinationFactor().equals("اقساط") ? profit : 0);
                        selected.setSumInstallmentValue(selected.getCondinationFactor().equals("اقساط") ? String.valueOf((int) sumInstallmentAndPaymanet) : "0");
                        selected.setSumPurgeAndProfitGeneral(selected.getCondinationFactor().equals("اقساط") ? String.valueOf(sumPurgeAndProfit) : String.valueOf(sumPrice - sumDiscount));

                        if ((getCreditSum(selectedPerson) > sumPurgeAndProfit)
                                && selected.getCondinationFactor().equals("اقساط")) {

                            selected.setPerson(selectedPerson);
                            selected.setProvider(selectedProvider);
                            selected.setExhibition(selectedExhibitionProvider);
                            selectedPerson.getFactors().add(selected);
                            selectedProvider.getFactors().add(selected);
                            selectedExhibitionProvider.getFactors().add(selected);
                            ejbFacade.create(selected);

                            selectedPerson.setInsertMode(false);
                            personFacade.edit(selectedPerson);

                            //جمع خلص مانده + سود کل مانده
                            // از اعتبار پرسنل کسر میشود
                            System.out.println("Person : " + selected.getPerson());
                            Credit credit = new Credit(-(purePrice + ((int) installmentValueComputing)), selectedPerson);
                            creditFacade.create(credit);

                        } else if ((getCreditSum(selectedPerson) < sumPurgeAndProfit)
                                && selected.getCondinationFactor().equals("اقساط")
                                && selectedPerson.isInsertMode()) {
                            showMessage();
                        }

                        if (selected.getCondinationFactor().equals("نقدی")) {
                            selected.setPerson(selectedPerson);
                            selected.setProvider(selectedProvider);
                            selected.setExhibition(selectedExhibitionProvider);
                            selectedPerson.getFactors().add(selected);
                            selectedProvider.getFactors().add(selected);
                            selectedExhibitionProvider.getFactors().add(selected);

                            getEjbFacade().create(selected);
                        }

                        selected = null;
                        productSelectForInsert = null;
                        selectedPerson = null;
                        lastSelectedPerson = null;
                        selectedProvider = null;
                        rowFactorDetailSelect = null;
                        rowFactorDetail = null;
                        discountProduct = "0";

                    } else {

                        int sumPrice = 0;//جمع کل
                        int sumDiscount = 0;//جمع کل تخفیف
                        long lastFactorCreditValue = Long.valueOf(selected.getSumPurgeAndProfitGeneral());
                        System.out.println("last credit in factor :" + lastFactorCreditValue);

                        for (FactorDetail fd : selected.getFactorDetails()) {
                            fd.setFactor(selected);
                            sumPrice += Integer.valueOf(fd.getPriceAfterDiscount());
                            sumDiscount += Integer.valueOf(fd.getDiscount().isEmpty() ? "0" : fd.getDiscount());

                        }

                        System.out.println("sumPrice factor: " + sumPrice);
                        System.out.println("sumDiscount factor: " + sumDiscount);

                        double profit = 0.0;//درصد قسط
                        if (Integer.valueOf(selected.getInstallmentCount()) > 6) {
                            profit = 1.8;
                        } else {
                            profit = 1.5;
                        }

                        System.out.println("پیش پرداخت " + selected.getPrepayable());
                        //محاسبه هر قسط
                        double generalProfit = (Integer.valueOf(selected.getInstallmentCount()) * profit) / 100;
                        //خالص مانده
                        int purePrice = sumPrice - Integer.valueOf(selected.getPrepayable());
                        System.out.println("Khales Mandeh: " + purePrice);

                        // کل سود روی خالص  مانده
                        double installmentValueComputing = purePrice * generalProfit;
                        // مبلغ هر قسط ماهیانه
                        double sumInstallmentAndPaymanet = (installmentValueComputing + purePrice) / Integer.valueOf(selected.getInstallmentCount());
                        //جمع سود و خالص پرداختی
                        long sumPurgeAndProfit = purePrice + ((int) installmentValueComputing);

                        selected.setSumFactor(String.valueOf(sumPrice));//جمع کل کالاها
                        selected.setPayable(String.valueOf(purePrice));//جمع کل کالاها کم میشود از جمع تخفیف
                        selected.setSumDiscount(String.valueOf(sumDiscount));//جمع کا تخفیفات
                        selected.setSumWage((int) (selected.getProvider().getWage() * purePrice));//جمع کل کارمزد
                        selected.setFinalRegistration(true);//ثبت نهایی شود

                        selected.setInstallmentCount(selected.getCondinationFactor().equals("اقساط") ? selected.getInstallmentCount() : "0");

                        selected.setInstallmentValue(selected.getCondinationFactor().equals("اقساط") ? String.valueOf((int) sumInstallmentAndPaymanet) : "0");
                        selected.setPercentage(selected.getCondinationFactor().equals("اقساط") ? profit : 0);
                        selected.setSumInstallmentValue(selected.getCondinationFactor().equals("اقساط") ? String.valueOf((int) sumInstallmentAndPaymanet) : "0");
                        selected.setSumPurgeAndProfitGeneral(selected.getCondinationFactor().equals("اقساط") ? String.valueOf(sumPurgeAndProfit) : String.valueOf(sumPrice - sumDiscount));

                        if (this.getSelectedLastCondinationTypeFactor().equals("اقساط")
                                && selected.getCondinationFactor().equals("نقدی")
                                && !selected.getPerson().isInsertMode()) {
                            getEjbFacade().edit(selected);
                            long valueCredit = Long.valueOf(this.getSelectedFactorPurgeAndProfitGeneral());
                            List<Credit> credits = creditFacade.findByCredit(-valueCredit);
                            if (credits != null && !credits.isEmpty()) {
                                creditFacade.remove(credits.get(0));
                            }
                        }

                        if (((selected.getCondinationFactor().equals("اقساط")
                                && this.getSelectedLastCondinationTypeFactor().equals("نقدی"))
                                || this.getSelectedLastCondinationTypeFactor().equals("اقساط")
                                && selected.getCondinationFactor().equals("اقساط"))
                                && !selected.getPerson().isInsertMode()) {

                            if (((getCreditSum(selected.getPerson())
                                    + lastFactorCreditValue) > sumPurgeAndProfit)) {
                                getEjbFacade().edit(selected);

                                selected.getPerson().setInsertMode(false);
                                personFacade.edit(selected.getPerson());

                                long valueCredit = Long.valueOf(this.getSelectedFactorPurgeAndProfitGeneral());
                                List<Credit> credits = creditFacade.findByCredit(-valueCredit);

                                if (credits != null && !credits.isEmpty()) {
                                    creditFacade.remove(credits.get(0));
                                }
                                Credit credit = new Credit(-(purePrice
                                        + ((int) installmentValueComputing)), selected.getPerson());

                                creditFacade.create(credit);
                            } else {
                                showMessageInsertModeLockOrCredit();
                            }
                        }

                        if (selected.getCondinationFactor().equals("نقدی")
                                && this.getSelectedLastCondinationTypeFactor().equals("نقدی")
                                && !selected.getPerson().isInsertMode()) {
                            getEjbFacade().edit(selected);
                        }

                        selected = null;
                        productSelectForInsert = null;
                        rowFactorDetail = null;
                        discountProduct = "0";

                    }

                } else {
                    if (selected.getCondinationFactor().equals("اقساط")) {
                        long valueCredit = Long.valueOf(this.getSelectedFactorPurgeAndProfitGeneral());
                        List<Credit> credits = creditFacade.findByCredit(-valueCredit);
                        if (credits != null && !credits.isEmpty()) {
                            creditFacade.remove(credits.get(0));
                        }
                    }
                    selected.setFactorDetails(null);
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
            if (object instanceof Factor) {
                Factor o = (Factor) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Factor.class.getName()});
                return null;
            }
        }
    }

    public void onSelectTypeFactor() {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, this.selected.getCondinationFactor(), "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowSelect(SelectEvent event) {
        this.setSelectedFactorPurgeAndProfitGeneral(((Factor) event.getObject()).getSumPurgeAndProfitGeneral());
        this.setSelectedLastCondinationTypeFactor(((Factor) event.getObject()).getCondinationFactor());
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String resHomeImgPath = servletContext.getRealPath("resources/img");
        UUID uuid = UUID.randomUUID();
        String infoPerson = ((Factor) event.getObject()).getPerson().getInternationalCode() + "  "
                + ((Factor) event.getObject()).getPerson().getCompanyCode() + "  " + uuid.toString();
        this.generateQrCode(resHomeImgPath, infoPerson);
//        JsfUtil.addSuccessMessage(resHomeImgPath);
    }

    public void removeFactor() {
        persist(JsfUtil.PersistAction.DELETE, "فاکتور مورد نظر حذف گردید");
    }

    public String getRandomAddressFile() {
        return "/resources/img/qrcodeFactor.png?dummy=" + UUID.randomUUID().toString();
    }

}
