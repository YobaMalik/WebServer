package com.example.demo.OBRE;

import com.example.demo.Pasport.ExtractPasportData;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;

public class PaspInfo extends ExtractPasportData {
    public PaspInfo(InputStream excPasp) throws IOException {
        super(excPasp);
    }
}
