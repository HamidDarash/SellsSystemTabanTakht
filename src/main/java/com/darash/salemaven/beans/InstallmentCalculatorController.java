/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Person;
import com.darash.salemaven.services.CreditFacade;
import com.darash.salemaven.services.PersonFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("installmentCalculatorController")
@ViewScoped
public class InstallmentCalculatorController implements Serializable {

    @EJB
    private PersonFacade personFacade;

    @EJB
    private CreditFacade creditFacade;

    private long moneyFull;

    private int installmentCount = 10;

    private Person selectedPerson = null;

    private long prePayment = 0;

    private double generalProfit;
    private long purePrice;
    private double installmentValueComputing;
    private double sumInstallmentAndPaymanet;
    private long sumPurgeAndProfit;

    private String exportHtml = "<div>محاسباتی صورت نگرفته است</div>";

    public String getExportHtml() {
        return exportHtml;
    }

    public void setExportHtml(String exportHtml) {
        this.exportHtml = exportHtml;
    }

    public double getGeneralProfit() {
        return generalProfit;
    }

    public void setGeneralProfit(double generalProfit) {
        this.generalProfit = generalProfit;
    }

    public long getPurePrice() {
        return purePrice;
    }

    public void setPurePrice(long purePrice) {
        this.purePrice = purePrice;
    }

    public double getInstallmentValueComputing() {
        return installmentValueComputing;
    }

    public void setInstallmentValueComputing(double installmentValueComputing) {
        this.installmentValueComputing = installmentValueComputing;
    }

    public double getSumInstallmentAndPaymanet() {
        return sumInstallmentAndPaymanet;
    }

    public void setSumInstallmentAndPaymanet(double sumInstallmentAndPaymanet) {
        this.sumInstallmentAndPaymanet = sumInstallmentAndPaymanet;
    }

    public long getSumPurgeAndProfit() {
        return sumPurgeAndProfit;
    }

    public void setSumPurgeAndProfit(long sumPurgeAndProfit) {
        this.sumPurgeAndProfit = sumPurgeAndProfit;
    }

    public long getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(long prePayment) {
        this.prePayment = prePayment;
    }

    public int getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(int installmentCount) {
        this.installmentCount = installmentCount;
    }

    public long getMoneyFull() {
        return moneyFull;
    }

    public void setMoneyFull(long moneyFull) {
        this.moneyFull = moneyFull;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> findInAllUsersForAutoComplete(String search) {
        try {
            return personFacade.findByInternationalOrNameOrCompanyCode(search);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public long getCreditSum(Person per) {
        if (per != null) {
            return creditFacade.getCreditUser(per);
        }
        return 0;
    }

    public void resetform() {
        this.selectedPerson = null;
        this.exportHtml = "<div>محاسباتی صورت نگرفته است</div>";
        this.generalProfit = 0.0;
        this.installmentCount = -1;
        this.installmentValueComputing = 0.0;
        this.moneyFull = 0;
        this.prePayment = 0;
        this.purePrice = 0;
        this.sumPurgeAndProfit = 0;
        this.sumInstallmentAndPaymanet = 0;

    }

    public void printHtmlReport() {
        String html = "<div>";
        html += "<p>";
        html += "<span>پرسنل محترم</span>" + " " + "<span style='color:red;'>"
                + selectedPerson.getName() + "</span>" + " <span> از شرکت"
                + "</span> " + "<span style='color:red;'>" + selectedPerson.getCompany() + " </span>";
        html += " <span>کالای مورد نظر با قیمت </span>" + "<span style='color:red;'>" + String.valueOf(this.moneyFull) + " ريال</span>";
        html += " <span>با اقساط </span>" + "<span style='color:red;'>" + String.valueOf(this.installmentCount) + " ماهه</span>  می باشد";
        html += this.prePayment > 0 ? "<span>که شما مبلغ </span>" + "<span style='color:red;'>"
                + String.valueOf(this.prePayment) + " ريال</span>" + "<span>را به صورت پیش پرداخت می خواهید پرداخت کنید</span>" : "";
        html += "<span>مبلغ هر قسط می شود</span>" + "<span style='color:red;'>" + String.valueOf((int) this.sumInstallmentAndPaymanet) + " ريال</span> ";
        html += "<span> کل سود محاسبه شده بروی این کالا</span>" + " <span style='color:red;'>" + 
                String.valueOf((int) this.installmentValueComputing) + " ريال </span> میباشد";
        html += "</p>";
        html += "</div>";
        this.setExportHtml(html);
    }

    public void computeInstallment() {
        try {
            if (selectedPerson != null) {
                if (this.moneyFull <= 0) {
                    this.exportHtml = "<div>هیچ قیمتی وارد نکرده اید</div>";
                } else {
                    long credit = this.getCreditSum(selectedPerson);
                    double profit = 0.0;//درصد قسط
                    if (this.installmentCount > 6) {
                        profit = 1.8;
                    } else {
                        profit = 1.5;
                    }
                    //محاسبه هر قسط
                    this.generalProfit = (this.installmentCount * profit) / 100;
                    //خالص مانده
                    this.purePrice = this.moneyFull - this.prePayment;
                    // کل سود روی خالص  مانده
                    this.installmentValueComputing = purePrice * generalProfit;
                    // مبلغ هر قسط ماهیانه
                    this.sumInstallmentAndPaymanet
                            = (installmentValueComputing + purePrice) / this.installmentCount;
                    //جمع سود و خالص پرداختی
                    this.sumPurgeAndProfit = purePrice + ((int) installmentValueComputing);
                    if (credit > this.sumPurgeAndProfit) {
                        this.printHtmlReport();
                        System.out.println("generalProfit: " + this.generalProfit);
                        System.out.println("purePrice: " + this.purePrice);
                        System.out.println("installmentValueComputing: " + this.installmentValueComputing);
                        System.out.println("sumInstallmentAndPaymanet: " + this.sumInstallmentAndPaymanet);
                        System.out.println("sumPurgeAndProfit: " + this.sumPurgeAndProfit);
                    } else {
                        this.exportHtml = "<div>" + "<span>پرسنل محترم " + "</span>"
                                + "<span>" + (selectedPerson.getName().length() > 0 ? selectedPerson.getName() : selectedPerson.getInternationalCode()) + "</span> "
                                + "<span>شما اعتبار کافی برای خرید کالا را ندارید</span>"
                                + "</div>";
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("خطا در محاسبات رخ داده");
            this.exportHtml = "<div>" + "<span>خطا در محاسبات رخ داده است</span>" + "</div>";
        }

    }

}
