/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.convertor;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Davood
 */
@FacesConverter("persian_date")
public class PersianDateConvertor implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            ULocale locale = new ULocale("fa_IR@calendar=persian");
            SimpleDateFormat df;
            if (component.getAttributes().get("pattern") != null) {
                df = new SimpleDateFormat(component.getAttributes().get("pattern").toString(), locale);
            } else {
                df = new SimpleDateFormat("yyyy/MM/dd", locale);
            }
            Date d = df.parse(value);
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd", ULocale.ENGLISH);
            return df2.parse(df2.format(d));
        } catch (ParseException ex) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "فرمت تاریخ وارد شده درست نیست", ""));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        ULocale ul = new ULocale("fa_IR@calender=persian");
        SimpleDateFormat df;
        if (component.getAttributes().get("pattern") != null) {
            df = new SimpleDateFormat(component.getAttributes().get("pattern").toString(), ul);
        } else {
            df = new SimpleDateFormat("yyyy/MM/dd", ul);
        }
        return df.format(value);
    }
}
