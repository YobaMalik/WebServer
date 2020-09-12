package com.example.demo.TensionCalcASME;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SpecGradeMap implements IGradeMap {
    Map<String,HashMap<String,String>> test=new LinkedHashMap<>();
    Map<String,String> gradeMap=new HashMap<>();


    private String dbPath;

    public SpecGradeMap(@Value("${config.path}") String dbPath )throws IOException {
        this.dbPath = dbPath;

        this.getGradeMap();
    }

    @Override
    public void getGradeMap() throws IOException {
        try (Workbook wb = new XSSFWorkbook(new FileInputStream(new File(dbPath)));) {
            Map<String,HashMap<String,String>> test1=new HashMap<>();
            Sheet tSheet = wb.getSheet("SigmaT");
            for (int i = 1; i < tSheet.getLastRowNum()+1; i++) {
                String specT = tSheet.getRow(i).getCell(2).getStringCellValue();
                String gradeT = tSheet.getRow(i).getCell(3).toString().replaceAll(".0","");
                if(test1.containsKey(specT)){
                    test1.get(specT).put(gradeT,gradeT);
                } else{
                    HashMap<String,String> lul=new HashMap<>();
                    lul.put(gradeT,gradeT);
                    test1.put(specT,lul);
                }
                gradeMap.putIfAbsent(gradeT,gradeT);
            }
            test1.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(e->test.put(e.getKey(),e.getValue()));
        }
    }

    public Map<String,HashMap<String,String>> getVal() throws IOException {

        return this.test;
    }
    public Map<String,String> getGradeVal() throws IOException {

        return this.gradeMap;
    }


}