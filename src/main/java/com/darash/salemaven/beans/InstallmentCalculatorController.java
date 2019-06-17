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

    public void computeInstallment() {
        if (selectedPerson != null) {
            long credit = this.getCreditSum(selectedPerson);
            if (credit > (this.moneyFull - this.prePayment)) {

            }
        }
    }

}
