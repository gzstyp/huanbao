package com.fwtai.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 日期分类查询统计-导出(贵阳市核酸检测累计表（7.1起报送）_按日期+分类统计)
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/2/23 22:20
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class CategoryGeneral{

    public static void exportExcel(final String label,final List<HashMap<String,Object>> data,final List<HashMap<String,Object>> listType,final String fileName,final HttpServletResponse response) throws Exception {
        ToolExcel.downloadExcel(reportExcel(label,data,listType),fileName,response);
    }

    /**
     * 单元格水平垂直居中
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/23 22:53
    */
    protected static XSSFCellStyle cellStyle(final XSSFWorkbook wb,final Cell cell){
        final XSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cell.setCellStyle(styleCenter);
        return styleCenter;
    }

    static XSSFWorkbook reportExcel(final String label,final List<HashMap<String,Object>> data,final List<HashMap<String,Object>> listType){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("日期分类统计");
        sheet.setColumnWidth(0,(int) (35.7 * 100));
        final Row labelRow0 = sheet.createRow(0);//第1行
        labelRow0.setHeightInPoints(30);
        final Cell row0cell0 = labelRow0.createCell(0);//创建第1行的第1个单元格
        cellStyle(wb,row0cell0);
        row0cell0.setCellValue("附件名称");
        int totalCell = 0;
        for(int i = 0; i < listType.size(); i++){
            totalCell += (Long) listType.get(i).get("crowdTotal");
        }
        totalCell = totalCell * 3 + (listType.size() * 3) + 3;//(listType.size() * 3)是人群分类;+3是总统计的各3项
        for (int j = 1; j <= totalCell; j++){
            labelRow0.createCell(j);
        }
        //合并单元格
        ToolExcel.cellRangeAddress(sheet,0,0,1,totalCell);
        final XSSFCellStyle styleCenter = wb.createCellStyle();
        final Font labelFont = wb.createFont();
        labelFont.setFontHeightInPoints((short)14);//设置字号
        labelFont.setColor(IndexedColors.BLACK.getIndex());//设置字体颜色
        labelFont.setBold(true);
        styleCenter.setFont(labelFont);
        styleCenter.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        styleCenter.setWrapText(true);//自动换行显示,即非一行显示!!!
        final Cell labelCell = labelRow0.getCell(1);
        labelCell.setCellStyle(styleCenter);
        labelCell.setCellValue(label);

        final Row row1 = sheet.createRow(1);//创建第2行
        row1.setHeightInPoints(22);//高度
        final Cell row1cell0 = row1.createCell(0);//创建第2行的第1个单元格
        cellStyle(wb,row1cell0);
        row1cell0.setCellValue("人群分类");

        //第2行,计算并创建总的单元格
        for (int j = 1; j <= totalCell;j++){
            row1.createCell(j);
        }
        //第2行合并单元格操作
        int intCrowdTypeTotal = 0;
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final String crowdName = (String) map.get("crowdName");
            final long crowdTotal = (Long) map.get("crowdTotal")*3;
            final int tabs = (int) crowdTotal + 3;
            if(i==0){
                ToolExcel.cellRangeAddress(sheet,1,1,1,tabs);//第1个分类
                final Cell cell = row1.getCell(1);
                cellStyle(wb,cell);
                cell.setCellValue(crowdName);
            }else {
                final Cell cell = row1.getCell(intCrowdTypeTotal+1);
                cellStyle(wb,cell);
                cell.setCellValue(crowdName);
                ToolExcel.cellRangeAddress(sheet,1,1,intCrowdTypeTotal+1,(intCrowdTypeTotal+tabs));//此处要-3;+1是因为要比上一次循环的位置上移1个单元格
            }
            intCrowdTypeTotal = intCrowdTypeTotal + tabs;//此处+3,每个分类的合计需要3个单元格
        }
        final Row row2 = sheet.createRow(2);//创建第3行
        row2.setHeightInPoints(50);//高度
        final Cell row2cell0 = row2.createCell(0);//创建第3行的第1个单元格
        cellStyle(wb,row2cell0);
        row2cell0.setCellValue("人群类型");

        //创建第3行
        for (int j = 1; j <= totalCell;j++){
            row2.createCell(j);
        }

        int intCrowdType = 0;
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final String crowdName = (String) map.get("crowdName");
            final String crowdType = (String) map.get("crowdType");
            final long crowdTotal = (Long) map.get("crowdTotal")*3;
            final int tabs = (int) crowdTotal + 3;
            final String[] arrays = crowdType.split(",");
            if(i==0){
                for(int x = 0; x < arrays.length; x++){
                    final String typeName = arrays[x];
                    final int tab = (x * 3) + 1;//1-3;4-6;7-9
                    final Cell cell = row2.getCell((x == 0) ? 1 : tab);//1、4、7、10
                    cellStyle(wb,cell);
                    cell.setCellValue(crowdName+typeName);//'人群(大)分类'+'人群类型'拼接组合的"人群类型",达到唯一标识,最后crowdTypeName();还原
                    final boolean bl = (x == 0);
                    ToolExcel.cellRangeAddress(sheet,2,2,(bl?1:tab),(bl?3:tab+2));
                    if(x == arrays.length-1){
                        final int tab3 = tab+3;
                        final Cell cellTotal = row2.getCell(tab3);
                        cellStyle(wb,cellTotal);
                        cellTotal.setCellValue(crowdName+"合计");
                        ToolExcel.cellRangeAddress(sheet,2,2,tab3,tab3+2);
                    }
                }
            }else{
                for(int x = 0; x < arrays.length; x++){
                    final String typeName = arrays[x];
                    final int tab = (x * 3) + 1;
                    final Cell cell = row2.getCell((x == 0) ? intCrowdType+1 : intCrowdType+tab);
                    cellStyle(wb,cell);
                    cell.setCellValue(crowdName+typeName);//拼接组合的"人群类型"
                    final boolean bl = (x == 0);
                    ToolExcel.cellRangeAddress(sheet,2,2,(bl?(intCrowdType+1):intCrowdType+tab),(bl?(intCrowdType+3):intCrowdType+tab+2));
                    if(x == arrays.length-1){
                        final int tab3 = intCrowdType+tab+3;
                        final Cell cellTotal = row2.getCell(tab3);
                        cellStyle(wb,cellTotal);
                        cellTotal.setCellValue(crowdName+"合计");
                        ToolExcel.cellRangeAddress(sheet,2,2,tab3,tab3+2);
                    }
                }
            }
            intCrowdType = intCrowdType + tabs;
        }
        //总计-合并单元格
        final int minus3Plus1 = totalCell - 3 + 1;
        ToolExcel.cellRangeAddress(sheet,1,2,minus3Plus1,totalCell);//核酸总计
        final Cell cellTotal = row1.getCell(minus3Plus1);
        cellStyle(wb,cellTotal);
        cellTotal.setCellValue("核酸总计");

        final Row row3 = sheet.createRow(3);//创建第4行
        row3.setHeightInPoints(36);
        final Cell row3cell0 = row3.createCell(0);
        cellStyle(wb,row3cell0);
        row3cell0.setCellValue("统计项目");

        //人群类型
        for (int j = 0; j < totalCell;j=j+3){
            for(int z = 0; z < 3; z++){
                final Cell cell = row3.createCell(j + z + 1);
                final XSSFCellStyle style = cellStyle(wb,cell);
                style.setWrapText(true);
                switch (z){
                    case 0:
                        cell.setCellValue("已采样人数");
                        break;
                    case 1:
                        cell.setCellValue("已检测人数");
                        break;
                    case 2:
                        cell.setCellValue("检测阳性人数");
                        break;
                    default:
                        break;
                }
            }
        }
        splitData(data,totalCell,wb,sheet);

        final Row totalRow = sheet.createRow(4+data.size());//累计数[4是0,1,2,3行已被创建]
        final Cell cellEnd = totalRow.createCell(0);
        cellStyle(wb,cellEnd);
        cellEnd.setCellValue("累计数");

        crowdTypeName(listType,row2);
        handleTotal(sheet,totalRow,totalCell,data.size());
        return wb;
    }

    /**
     * 计算最后行的累计统计
     * @param row 累计数
     * @param cells 总人群类型数量
     * @param listSize 数据的行数
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/24 10:32
    */
    private static void handleTotal(final XSSFSheet sheet,final Row row,final int cells,final int listSize){
        for(int i = 1; i <= cells; i++){
            final int value = handle2DValue(sheet,cells,listSize,i);
            final Cell cell = row.createCell(i);
            cellStyle(sheet.getWorkbook(),cell);
            cell.setCellValue(value);
        }
    }

    /**
     * 计算每一行的第几个单元格的合计|总计
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/24 19:31
    */
    private static int handle2DValue(final XSSFSheet sheet,final int cells,final int number,final int position){
        int result = 0;
        for(int i = 0; i < number; i++){
            final XSSFRow xssfRow = sheet.getRow(4+i);//[4是0,1,2,3行已被创建]
            for(int x = 1; x <= cells; x++){
                if(x == position){
                    final XSSFCell cell = xssfRow.getCell(x);
                    final double value = Double.parseDouble(String.valueOf(cell));
                    result += value;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 处理已生成'人群(大)分类'+'人群类型'拼接的"人群类型"的还原,即第3行的数据
    */
    static void crowdTypeName(final List<HashMap<String,Object>> listType,final Row row){
        int cutCrowdType = 0;
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final String crowdType = (String) map.get("crowdType");
            final long crowdTotal = (Long) map.get("crowdTotal")*3;
            final int tabs = (int) crowdTotal + 3;
            final String[] arrays = crowdType.split(",");
            if(i==0){
                for(int x = 0; x < arrays.length; x++){
                    final String array = arrays[x];
                    final int tab = (x * 3) + 1;
                    final Cell cell = row.getCell((x == 0) ? 1 : tab);
                    cell.setCellValue(array);
                }
            }else{
                for(int x = 0; x < arrays.length; x++){
                    final String array = arrays[x];
                    final int tab = (x * 3) + 1;
                    final Cell cell = row.getCell((x == 0) ? cutCrowdType+1 : cutCrowdType+tab);
                    cell.setCellValue(array);
                }
            }
            cutCrowdType = cutCrowdType + tabs;
        }
    }

    /**
     * 填充以日期的数据行
    */
    private static void splitData(final List<HashMap<String,Object>> list,final int cells,final XSSFWorkbook wb,final XSSFSheet sheet){
        for(int i = 0; i < list.size(); i++){
            final int rowIndex = 4+i;//4++数据行
            final Row row = sheet.createRow(rowIndex);
            row.setHeightInPoints(20);
            final HashMap<String,Object> map = list.get(i);
            final String crowd_date = String.valueOf(map.get("crowd_date"));
            final String[] crowdNames = ((String)map.get("crowdName")).split("\\|");
            final Cell cellDate = row.createCell(0);//创建数据行的第N行的第1个单元格且赋值
            cellStyle(wb,cellDate);
            cellDate.setCellValue(crowd_date);
            for (int j = 1; j <= cells;j++){
                final Cell cell = row.createCell(j);//创建空单元格
                cellStyle(wb,cell);
                cell.setCellValue(0);
            }
            int masculine = 0;
            int detection = 0;
            int sampling = 0;
            for(int x = 0; x < crowdNames.length; x++){
                final String crowdName = crowdNames[x];//愿检尽检人群|应检尽检人群
                renderTotalData(sheet,row,crowdName,cells,map,x);//每行的合计或总计
                renderDataRow(sheet,row,cells,map,x);//每行的数据填充
                masculine += Integer.parseInt(getIndexData(map,"totalMasculine",x));
                detection += Integer.parseInt(getIndexData(map,"totalDetection",x));
                sampling += Integer.parseInt(getIndexData(map,"totalSampling",x));
            }
            rowTotal(row,cells,sampling,detection,masculine);
        }
    }

    /**
     * 每行的总计显示
    */
    private static void rowTotal(final Row row,final int cells,final int sampling,final int detection,final int masculine){
        final Cell cellTotal2 = row.getCell(cells - 2);//倒数第3个
        final Cell cellTotal1 = row.getCell(cells - 1);//倒数第2个
        final Cell cellTotal0 = row.getCell(cells - 0);//倒数第1个
        cellTotal2.setCellValue(sampling);
        cellTotal1.setCellValue(detection);
        cellTotal0.setCellValue(masculine);
    }

    /**
     * 每行的数据填充
    */
    private static void renderDataRow(final XSSFSheet sheet,final Row row,final int cells,final HashMap<String,Object> map,final int tabIndex){
        final String crowdType = getIndexData(map,"crowdType",tabIndex);
        final String masculine = getIndexData(map,"masculine",tabIndex);
        final String detection = getIndexData(map,"detection",tabIndex);
        final String sampling = getIndexData(map,"sampling",tabIndex);
        final String crowd_name = getIndexData(map,"crowdName",tabIndex);
        final String[] samplings = sampling.split(",");//已采样
        final String[] detections = detection.split(",");//已检测
        final String[] masculines = masculine.split(",");//阳性人数
        final String[] values = crowdType.split(",");//应检尽检发热门诊就诊患者
        for(int x = 0; x < values.length; x++){
            final String v = values[x];
            final int position = dataGetPosition(sheet,cells,(crowd_name + v));
            if(position != -1){
                for(int z = 0; z < 3; z++){
                    final Cell rowCell = row.getCell(position + z);
                    switch (z){
                        case 0:
                            rowCell.setCellValue(samplings[x]);
                            break;
                        case 1:
                            rowCell.setCellValue(detections[x]);
                            break;
                        case 2:
                            rowCell.setCellValue(masculines[x]);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 计算每行的合计
    */
    private static void renderTotalData(final XSSFSheet sheet,final Row row,final String crowd_name,final int cells,final HashMap<String,Object> map,final int tabIndex){
        for (int j = 0; j < cells;j=j+3){
            final int index = j + 1;
            final XSSFRow xssfRow = sheet.getRow(2);
            final XSSFCell cell = xssfRow.getCell(index);
            final String value = cell.getStringCellValue();
            if(value.length() > 0){
                if((crowd_name+"合计").equals(value)){
                    final Cell cellTotal1 = row.getCell(j + 1);
                    final Cell cellTotal2 = row.getCell(j + 2);
                    final Cell cellTotal3 = row.getCell(j + 3);
                    final String totalMasculine = getIndexData(map,"totalMasculine",tabIndex);
                    final String totalDetection = getIndexData(map,"totalDetection",tabIndex);
                    final String totalSampling = getIndexData(map,"totalSampling",tabIndex);
                    cellTotal1.setCellValue(totalSampling);
                    cellTotal2.setCellValue(totalDetection);
                    cellTotal3.setCellValue(totalMasculine);
                }
            }
        }
    }

    /**
     * 获取人群类型的位置
    */
    private static int dataGetPosition(final XSSFSheet sheet,final int cells,final String value){
        for (int j = 1; j <= cells;j++){
            final XSSFRow xssfRow = sheet.getRow(2);
            final XSSFCell cell = xssfRow.getCell(j);
            final String v = cell.getStringCellValue();
            if(value.equals(v)){
                return j;
            }
        }
        return -1;
    }

    /**
     * 拆分获取指定的索引数据
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/23 23:19
    */
    private static String getIndexData(final HashMap<String,Object> map,final String key,final int tabIndex){
        final String[] values = ((String)map.get(key)).split("\\|");
        return values[tabIndex];
    }
}