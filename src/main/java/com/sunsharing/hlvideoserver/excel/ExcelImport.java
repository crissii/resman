package com.sunsharing.hlvideoserver.excel;


import com.sunsharing.component.utils.base.DateUtils;
import com.sunsharing.eos.common.utils.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by criss on 15/7/7.
 */
public class ExcelImport {
    Logger logger = Logger.getLogger(ExcelImport.class);
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public List getSheetNums(File path)
    {
        List rst = new ArrayList();
        FileInputStream file = null;
        try
        {
            file  = new FileInputStream(path);
            Workbook wb = null;
            if(path.getName().endsWith(OFFICE_EXCEL_2003_POSTFIX))
            {
                wb = new HSSFWorkbook(file);
            }
            if(path.getName().endsWith(OFFICE_EXCEL_2010_POSTFIX))
            {
                wb = new XSSFWorkbook(file);
            }
            int sheets = wb.getNumberOfSheets();
            for(int i=0;i<sheets;i++)
            {
                String sheetName = wb.getSheetAt(i).getSheetName();
                rst.add(sheetName);
            }
            return rst;

        }catch (Exception e)
        {
            logger.error("失败",e);
            throw new RuntimeException("失败");
        }finally {
            if(file!=null)
            {
                try
                {
                    file.close();
                }catch (Exception e)
                {

                }
            }
        }
    }

    public List readExcel(File path,int sheetNum)
    {
        FileInputStream file = null;
        try
        {
            file  = new FileInputStream(path);
            Workbook wb = null;
            if(path.getName().endsWith(OFFICE_EXCEL_2003_POSTFIX))
            {
                wb = new HSSFWorkbook(file);
            }
            if(path.getName().endsWith(OFFICE_EXCEL_2010_POSTFIX))
            {
                wb = new XSSFWorkbook(file);
            }
            Sheet sh = wb.getSheetAt(sheetNum);
            // Read the Row
            int maxCellnum = 0;
            List rst = new ArrayList();
           for (int rowNum = 0; rowNum <= sh.getLastRowNum(); rowNum++) {
               Row row = sh.getRow(rowNum);
               short cellnum = row.getLastCellNum();
               if(cellnum>maxCellnum)
               {
                   maxCellnum = cellnum;
               }
               List data = new ArrayList();
               for(int c = 0;c<maxCellnum;c++)
               {
                   Cell cell = row.getCell(c);
                   String v = "";
                   if(cell==null){
                       //System.out.println("aaa");
                   }else
                   {
                       v = getValue(cell);
                   }
                   data.add(v);
               }
               rst.add(data);
           }
            return rst;

        }catch (Exception e)
        {
            logger.error("失败",e);
            throw new RuntimeException("失败");
        }finally {
            if(file!=null)
            {
                try
                {
                    file.close();
                }catch (Exception e)
                {

                }
            }
        }
    }

    private String getValue(Cell cell)
    {

        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_NUMERIC:
            {
                if(HSSFDateUtil.isCellDateFormatted(cell))
                {
                    Date d = cell.getDateCellValue();
                    if(d!=null)
                    {
                        return DateUtils.getDisplay(d).substring(0,10);
                    }else
                    {
                        return "";
                    }
                }else {
                    return new Double(cell.getNumericCellValue()).intValue()+"";
                }
            }
            case  Cell.CELL_TYPE_STRING:
            {
                String d = cell.getStringCellValue();
                if(StringUtils.isBlank(d))
                {
                    return "";
                }else
                {
                    return d;
                }
            }
            default:
                return "";

        }

    }

}
