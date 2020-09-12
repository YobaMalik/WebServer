package com.example.demo.TensionCalcRD;

import com.example.demo.TensionCalcASME.IGradeMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GradeGradeMap implements IGradeMap {

    private final String dbPath;
    Map<String,String> gradeMap=new HashMap<>();

    public GradeGradeMap(@Value("${config.path}") String dbPath) throws IOException {
        this.dbPath=dbPath;
        this.getGradeMap();
    }
    @Override
    public void getGradeMap() throws IOException {

        try (Workbook wb = new XSSFWorkbook(new FileInputStream(new File(dbPath)));) {

            new SigmaRD().arrayST.keySet().forEach(key->{

                Sheet tSheet = wb.getSheet(key);
                for (int i = 1; i < tSheet.getLastRowNum(); i++) {
                    String desLife = tSheet.getRow(i).getCell(2).getStringCellValue();
                    String grade = tSheet.getRow(i).getCell(3).getStringCellValue();

                    if(!this.gradeMap.containsKey(desLife)){
                        this.gradeMap.put(grade,key);
                    }


                }
            });
        }
    }

    public Map<String,String> getGrMap(){
        return gradeMap;
    }

}
