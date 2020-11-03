package com.example.demo.MyController;
import com.example.demo.Certification.TPTCCategory;
import com.example.demo.Form.Form;
import com.example.demo.Form.TPTCForm;
import com.example.demo.OBRE.CreateOBRE;
import com.example.demo.Part45.GetInfoPart45;
import com.example.demo.PaspAddDoc.GetInfoFSVOM;
import com.example.demo.Pasport.GetInfoPasp;
import com.example.demo.ServerFiles;
import com.example.demo.TestPressure.Abstract.ModelTestPressure;
import com.example.demo.WordReplacePackage.CreateDocsFromTemplateI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Scope("session")
public class MyController {



    @Inject ServerFiles files;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String homePage(HttpSession session, Principal principal,Model model) throws IOException {
        model.addAttribute("PressureInfoTest",new ModelTestPressure());
        model.addAttribute("TPTC",new TPTCForm());
        System.out.println(principal);
        return "index";
    }

    @PostMapping(value="/TPTC032")
    @ResponseBody
    public TPTCCategory testTPTC( @RequestBody TPTCForm form){
        return new TPTCCategory(form);
    }

    @RequestMapping(value = "/wtfGet", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void wtf(HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + "WordTemplate.zip");
        this.files.uploadToClient(response.getOutputStream(),"WordTemplate.zip","application/zip");
    }

    @RequestMapping(value = "/GetSom", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void getSom (HttpServletResponse response) throws IOException{
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + "zipSomReposrt.zip");
        files.uploadToClient(response.getOutputStream(),"zipSomReposrt.zip","application/zip");
    }

    @RequestMapping(value = "/getStr", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void getStr (HttpServletResponse response) throws IOException{
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + "MaterialInfo.zip");
        files.uploadToClient(response.getOutputStream(),"MaterialInfo.zip","application/zip");
    }

    @RequestMapping(value = "/turnoffthemachine", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void offserver(HttpServletResponse response) throws IOException {
        Process prc=Runtime.getRuntime().exec(new String[]{"shutdown","now"});
        System.exit(0);
    }



    @RequestMapping (value="/downloadresult",method=RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void getFile( HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + "ResultTable.xlsx");
        files.uploadToClient(response.getOutputStream(),"ResultTable.xlsx","application/vnd.ms-excel");
      //  files.upploadResultPaspInfo(response);
    }

    @RequestMapping (value="/downloadOBRE",method=RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public void oBRE( HttpServletResponse response) throws IOException {
//        files.uploadOBREresult(response);
        response.setContentType("application/msword");
        response.setHeader("Content-disposition", "attachment; filename=" + "Template.docx");
        files.uploadToClient(response.getOutputStream(), "Template.docx","application/msword");
    }



}

