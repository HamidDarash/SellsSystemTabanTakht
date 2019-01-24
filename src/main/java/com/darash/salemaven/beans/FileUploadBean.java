/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darash.salemaven.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class FileUploadBean implements Serializable {

    private String fileContent;
    private String fileName;

    public void handleUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
  
        String FolderPath = "/resources/upload";
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        FolderPath = externalContext.getRealPath(FolderPath);
        
        try (InputStream input = file.getInputstream()) {
            Files.copy(input, new File(FolderPath, fileName).toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileContent = file.getContentType();
        fileName = file.getFileName();
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getFileName() {
        return fileName;
    }
}
