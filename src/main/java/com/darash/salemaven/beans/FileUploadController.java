/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import com.darash.salemaven.entities.Credit;
import com.darash.salemaven.entities.Person;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.faces.bean.SessionScoped;
import javax.persistence.PersistenceException;

@ManagedBean(name = "fileUploadController")
@SessionScoped
public class FileUploadController {

    @EJB
    private com.darash.salemaven.services.PersonFacade ejbFacade;

    private Integer progress;
    private boolean showProgressbar = false;

    private List<String> listErrorImport = new ArrayList<>();

    public List<String> getListErrorImport() {
        return listErrorImport;
    }

    public void setListErrorImport(List<String> listErrorImport) {
        this.listErrorImport = listErrorImport;
    }

    public boolean isShowProgressbar() {
        return showProgressbar;
    }

    public void setShowProgressbar(boolean showProgressbar) {
        this.showProgressbar = showProgressbar;
    }

    public Integer getProgress() {
        if (progress == null) {
            progress = 0;
        }
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    private final String destination = "c:\\tmp";

    public void upload(FileUploadEvent event) {

        // Do what you want with the file
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        FacesMessage msg = new FacesMessage("عملیات موفق ", event.getFile().getFileName() + " به درستی آپلود شد.");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void copyFile(String fileName, InputStream in) {
        try {
            String FolderPath = "c:\\tmp\\";
            OutputStream out;
            File theDir = new File(FolderPath + fileName);
            if (!theDir.exists()) {
                try {
                    theDir.mkdir();
                } catch (SecurityException se) {
                    System.err.println(se.getMessage());
                }
            }
            out = new FileOutputStream(new File(FolderPath + fileName));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            insertDataToDataBase(FolderPath + fileName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Asynchronous
//    @Transactional(dontRollbackOn = {MySQLIntegrityConstraintViolationException.class})
    private void rowInserter(Row row) {

        try {
            DataFormatter formatter = new DataFormatter();
            Cell column0 = row.getCell(0);
            Cell column1 = row.getCell(1);
            Cell column2 = row.getCell(2);
            Cell column3 = row.getCell(3);
            Cell column4 = row.getCell(4);
            Cell column5 = row.getCell(5);
            Cell column6 = row.getCell(6);
            Cell column7 = row.getCell(7);
            Cell column8 = row.getCell(8);
            Cell column9 = row.getCell(9);

            String id = formatter.formatCellValue(column0);
            String name = formatter.formatCellValue(column1);
            String email = formatter.formatCellValue(column2);
            String international = formatter.formatCellValue(column3);
            String mobile = formatter.formatCellValue(column4);
            String address = formatter.formatCellValue(column5);
            String company = formatter.formatCellValue(column6);
            String companyCode = formatter.formatCellValue(column7);
            String description = formatter.formatCellValue(column8);
            String credit = formatter.formatCellValue(column9);
            String errorField = "";
            if (name.isEmpty()) {
                errorField += " نام و نام خانوادگی ندارد";
            }
            if (international.isEmpty()) {
                errorField += " کد ملی ندارد";
            }
            if (mobile.isEmpty()) {
                errorField += " شماره موبایل ندارد";
            }
            if (company.isEmpty()) {
                errorField += " نام شرکت ندارد";
            }
            if (companyCode.isEmpty()) {
                errorField += " کد پرسنلی ندارد";
            }
            if (companyCode.isEmpty()) {
                errorField += " کد پرسنلی ندارد";
            }
            if (credit.isEmpty()) {
                errorField += " اعتبار نامشخص وارد شده است";
            }

            if (id.isEmpty()) { // باید این جهار فیلد باشد
                if (!international.isEmpty() && !mobile.isEmpty() && !companyCode.isEmpty() && !name.isEmpty()) { // اگر کد نداشت یعنی جدید ایجاد کن
                    if (international.length() > 10) {
                        international = "00" + international;
                    }
                    Person person = new Person(name, international, email, company, companyCode, address, mobile, description);
                    if (credit.isEmpty()) {
                        credit = "0";
                    }
                    Credit c = new Credit(Long.valueOf(credit));
                    person.getCredits().add(c);
                    try {
                        ejbFacade.edit(person);
                    } catch (Exception e) {
                        if (e.getCause().getCause() instanceof PersistenceException) {
                            listErrorImport.add("ردیف " + String.valueOf(row.getRowNum()) + String.valueOf("رکورد تکراری درج شد ") + " - "
                                    + "کد ملی : " + international);
                        }
                        System.err.println(e.getClass().getTypeName());
                    }

                } else {
                    listErrorImport.add("ردیف " + String.valueOf(row.getRowNum()) + "  ----  " + errorField);
                }
            } else { // edit person
                Credit c = new Credit(Long.valueOf(credit));
                Person person = ejbFacade.find(Integer.valueOf(id));
                if (person != null) {
                    person.getCredits().add(c);
                    ejbFacade.edit(person);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void rowCheckSheet(Sheet sheet) {
        int i = 0;
        int max = sheet.getPhysicalNumberOfRows() - 1;
        setProgress(0);
        showProgressbar = true;
        listErrorImport.clear();
        List<String> tempError = new ArrayList<>();
        for (Row row : sheet) {
            if (i > 0) {
                rowInserter(row);
            }
            setProgress((int) Math.round((i * 100) / max));
            i++;
        }
//        setProgress(100);
        showProgressbar = false;
    }

    private void insertDataToDataBase(String fileName) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(fileName));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            rowCheckSheet(datatypeSheet);

        } catch (FileNotFoundException e) {
            System.err.println("فایل مورد نظر یافت نشد");
        } catch (IOException e) {
            System.err.println("در خواندن و نوشتن فایل خطایی صورت پذیرفته است");
        }
    }

    public void findId() {
        List<Person> personInternationalCode = ejbFacade.findByInternationalCode("0082188815");
        if (personInternationalCode.isEmpty()) {
            System.out.println("no found item");
        }
        Iterator<Person> persons = personInternationalCode.iterator();
        while (persons.hasNext()) {
            Person p = persons.next();
            System.out.println(p.getEmail());
            System.out.println(p.getInternationalCode().length());
        }

    }

    public void onComplete() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(2000);
//        System.out.println("Sleep time in ms = " + (System.currentTimeMillis() - start));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ورود اطلاعات به درون دیتابیس با موفقیت انجام شد"));
        progress = 0;
    }

    public void proccessUI() throws InterruptedException {
        int i = 0;
        long start = System.currentTimeMillis();
        for (int j = 0; j < 10; j++) {
            Thread.sleep(500);
            listErrorImport.add(String.valueOf(i));
        }

    }
}
