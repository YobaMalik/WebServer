package com.example.demo.MyController;

import com.example.demo.Certification.TPTCCategory;
import com.example.demo.Form.Form;
import com.example.demo.Form.TPTCForm;
import com.example.demo.OBRE.CreateOBRE;
import com.example.demo.Part45.GetInfoPart45;
import com.example.demo.PaspAddDoc.GetInfoFSVOM;
import com.example.demo.Pasport.GetInfoPasp;
import com.example.demo.ServerFiles;
import com.example.demo.WordReplacePackage.CreateDocsFromTemplateI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Scope("singleton")
public class AdminController {
    ExecutorService newTask = Executors.newFixedThreadPool(8);
    private Map<String,ByteArrayOutputStream> fileArray=new HashMap<>();
    @Autowired    private HttpSession httpSession;

    @Inject ServerFiles files;
    @Inject CreateOBRE OBRE;
    @Inject CreateDocsFromTemplateI wordFiles;
    @Inject GetInfoPasp newTable;
    @Inject GetInfoPart45 newInfoWb;
    @Inject GetInfoFSVOM newZipArchive;
    @RequestMapping(value = "/res", method = RequestMethod.GET)

    @Secured("ROLE_ADMIN")
    public String getResTHML(HttpServletRequest request) {
        return "downloadAllResult";
    }

    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String uploadMultiFileHandler(Model model) throws Exception {
        Form form = new Form();
        model.addAttribute("form", form);
        return "uploadMultiFile";
    }

    @RequestMapping(value = "/OBRE", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String oBRECreate(HttpServletRequest request) throws IOException {
        Map<String, ByteArrayOutputStream> resultList=new ConcurrentHashMap<>();
        this.OBRE.createDocs(this.fileArray,resultList,this.newTask);
        this.files.uploadResultFile(resultList,"Template.docx");
        this.fileArray.clear();
        return "calcTime";
    }

    @RequestMapping(value = "/createSOM", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String createSOM(HttpServletRequest request) throws IOException{
        Map<String,ByteArrayOutputStream> resMap=new ConcurrentHashMap<>();
        this.newZipArchive.createDocs(this.fileArray,resMap,this.newTask);
        this.files.writeToServer(resMap,"zipSomReposrt.zip");
        this.fileArray.clear();
        return "calcTime";
    }

    @RequestMapping(value = "/StrengthTest", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String createStrength(HttpServletRequest request) throws IOException{
        Map<String,ByteArrayOutputStream> fileStengthCalc=new ConcurrentHashMap<>();
        this.newInfoWb.getInfo(this.fileArray,fileStengthCalc);
        files.writeToServer(fileStengthCalc,"MaterialInfo.zip");
        this.fileArray.clear();
        return "calcTime";
    }


    @RequestMapping(value = "/wtf", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String uploadZu(HttpServletResponse request, Principal principal) throws IOException {
        Map<String,ByteArrayOutputStream> resultDocs=new ConcurrentHashMap<>();
        this.wordFiles.createDocs(this.fileArray,resultDocs,this.newTask);
        this.files.writeToServer(resultDocs,"WordTemplate.zip");
        this.fileArray.clear();

        return "calcTime";
    }


    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String calculate(HttpServletRequest request, HttpSession ses) throws IOException{
        if (this.httpSession.getId().equals(ses.getId())) {
            Map<String,ByteArrayOutputStream> resultList=new ConcurrentHashMap<>();
            this.newTable.createDocs(this.fileArray,resultList,this.newTask);
            this.files.uploadResultFile(resultList,"ResultTable.xlsx");
            this.fileArray.clear();
        }
        return "calcTime";
    }

    @RequestMapping(value = "/uploadFilesBystream", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public String getFilesByStream(HttpServletRequest request) throws IOException{
        files.getFilesArray(request,this.fileArray);
        return "calcTime";
    }
    @RequestMapping(value = "/clearArray", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void deleteUploadFile(HttpServletRequest request) {
        this.fileArray.clear();
    }

    @RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
    @Secured("ROLE_ADMIN")
    public String uploadMultiFileHandlerPOST(HttpServletRequest request, Model model,@ModelAttribute("form") Form form) {
        return files.doUpload( model, form,this.fileArray);
    }


}
