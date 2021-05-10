package com.fwtai.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 日期区域查询统计-导出(中高风险地区入黔返黔人员核酸检测统计汇总表_按日期+区域统计)
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/2/23 22:20
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class DateArea{

    public static void exportExcel(final String label,final List<HashMap<String,Object>> data,final List<String> listType,final String fileName,final HttpServletResponse response) throws Exception {
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

    static XSSFWorkbook reportExcel(final String label,final List<HashMap<String,Object>> data,final List<String> listType){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("中高风险地区");
        sheet.setColumnWidth(0,(int) (35.7 * 100));
        final Row labelRow = sheet.createRow(0);//第1行
        labelRow.setHeightInPoints(48);
        final Cell row0cell0 = labelRow.createCell(0);//创建第1行的第1个单元格
        cellStyle(wb,row0cell0);
        row0cell0.setCellValue(label);
        int totalCell = listType.size();
        totalCell = totalCell * 4 + 1 + 4 + 1;//第1个+1是A单元格;+4是合计的4项;第2个+1是备注
        for (int j = 0; j < totalCell; j++){
            if(j != 0){
                final Cell cell = labelRow.createCell(j);
                cellStyle(wb,cell);
            }
        }
        sheet.setColumnWidth(totalCell-1,(int)(35.7 * 200));//单元格的宽度
        //合并单元格
        ToolExcel.cellRangeAddress(sheet,0,0,0,totalCell-2);

        final Row rowArea = sheet.createRow(1);//第2行,地区
        rowArea.setHeightInPoints(24);
        for (int j = 0; j < totalCell; j++){
            final Cell cell = rowArea.createCell(j);
            cellStyle(wb,cell);
            if(j == 0){
                cell.setCellValue("地区");
            }else if(j == totalCell - 1){
                final XSSFCellStyle styleCenter = cellStyle(wb,cell);
                styleCenter.setAlignment(HorizontalAlignment.LEFT);//居左
                cell.setCellStyle(styleCenter);
                cell.setCellValue("备注");
            }
        }
        //填充地区
        for(int i = 0; i < listType.size();i++){
            if(i == 0){
                createTabArea(sheet,rowArea,1,4,listType.get(i));//处理行的第1个
                if(i == listType.size()-1){//处理最后的合计
                    createTabArea(sheet,rowArea,(0 + 1 + 4),(0 + 4 + 4),"合计");//当且仅当只有一个时的处理最后一个的合计
                }
            }else{
                final int tab = i * 4;
                createTabArea(sheet,rowArea,(tab + 1),(tab + 4),listType.get(i));
                if(i == listType.size()-1){//处理最后的合计
                    createTabArea(sheet,rowArea,(tab + 1 + 4),(tab + 4 + 4),"合计");//处理最后一个的合计
                }
            }
        }

        final Row rowDate = sheet.createRow(2);//第3行,时间
        rowDate.setHeightInPoints(40);
        final Cell row2cell0 = rowDate.createCell(0);//创建第3行的第1个单元格
        cellStyle(wb,row2cell0);
        row2cell0.setCellValue("时间");

        for(int i = 0; i < listType.size();i++){
            if(i == 0){//处理行的第1个
                for(int x = 1; x < 5; x++){
                    itemArea(wb,x,rowDate.createCell(i + x));
                }
                if(i == listType.size()-1){//当且仅当只有一个时的处理最后一个的合计
                    for(int x = 1; x < 5; x++){
                        itemArea(wb,x,rowDate.createCell(0 + x + 4));
                    }
                    //处理最后一个单元格
                    final Cell cell = rowDate.createCell(totalCell-1);//-1是因为第一个单元格已被创建
                    final XSSFCellStyle styleCenter = cellStyle(wb,cell);
                    styleCenter.setAlignment(HorizontalAlignment.LEFT);//居左
                    cell.setCellStyle(styleCenter);
                    cell.setCellValue("风险区域");
                }
            }else{
                final int tab = i * 4;
                for(int x = 1; x < 5; x++){
                    itemArea(wb,x,rowDate.createCell(tab + x));
                }
                if(i == listType.size()-1){//处理最后一个的合计
                    for(int x = 1; x < 5; x++){
                        itemArea(wb,x,rowDate.createCell(tab + x + 4));
                    }
                    //处理最后一个单元格
                    final Cell cell = rowDate.createCell(totalCell-1);//-1是因为第一个单元格已被创建
                    final XSSFCellStyle styleCenter = cellStyle(wb,cell);
                    styleCenter.setAlignment(HorizontalAlignment.LEFT);//居左
                    cell.setCellStyle(styleCenter);
                    cell.setCellValue("风险区域");
                }
            }
        }
        //数据行
        splitData(data,totalCell,wb,sheet);
        final Row totalRow = sheet.createRow(3+data.size());//累计数[3是0,1,2行已被创建]
        final Cell cellEnd = totalRow.createCell(0);
        cellStyle(wb,cellEnd);
        cellEnd.setCellValue("合计");
        handleTotal(sheet,totalRow,(totalCell - 2),data.size());// totalCell - 2 是因为第1个单元格是日期+是最后一个单元格是备注
        return wb;
    }

    /**
     * 填充以日期的数据行
     */
    private static void splitData(final List<HashMap<String,Object>> list,final int cells,final XSSFWorkbook wb,final XSSFSheet sheet){
        for(int i = 0; i < list.size(); i++){
            final int rowIndex = 3+i;//3++都是数据行
            final Row row = sheet.createRow(rowIndex);
            row.setHeightInPoints(20);
            final HashMap<String,Object> map = list.get(i);
            final String crowd_date = String.valueOf(map.get("sampling_date"));
            final Cell cellDate = row.createCell(0);//创建数据行的第N行的第1个单元格且赋值
            cellStyle(wb,cellDate);
            cellDate.setCellValue(crowd_date);
            final BigDecimal totalWeijiance = (BigDecimal) map.get("totalWeijiance");
            final BigDecimal totalYinxing = (BigDecimal) map.get("totalYinxing");
            final BigDecimal totalYangxing = (BigDecimal) map.get("totalYangxing");
            final BigDecimal sampling = totalWeijiance.add(totalYinxing).add(totalYangxing);
            for (int j = 1; j < cells-1;j++){// j = 1是因为第1个单元格是日期; cells-1 是最后一个单元格是备注
                final Cell cell = row.createCell(j);//创建空单元格
                cellStyle(wb,cell);
                switch (cells - j){
                    case 2://倒数第2个单元格
                        cell.setCellValue(totalWeijiance.intValue());
                        break;
                    case 3://倒数第3个单元格
                        cell.setCellValue(totalYangxing.intValue());
                        break;
                    case 4:
                        cell.setCellValue(totalYinxing.intValue());
                        break;
                    case 5://倒数第5个单元格
                        cell.setCellValue(sampling.intValue());
                        break;
                    default:
                        cell.setCellValue(0);
                        break;
                }
            }
            renderDataRow(sheet,row,cells,map);//每行的数据填充,每行循环一次
        }
    }
    
    /**
     * 地区或合计
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/25 23:16
    */
    private static void createTabArea(final XSSFSheet sheet,final Row row,final int startCol,final int endCol,final String area){
        final Cell cell = row.getCell(startCol);
        cell.setCellValue(area);
        ToolExcel.cellRangeAddress(sheet,1,1,startCol,endCol);
    }

    /**
     * 处理每个地区的合计及检测结果
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/25 23:13
    */
    private static void itemArea(final XSSFWorkbook wb,final int tabIndex,final Cell cell){
        final XSSFCellStyle style = cellStyle(wb,cell);
        style.setWrapText(true);
        switch (tabIndex){
            case 1:
                cell.setCellValue("采样数");
                break;
            case 2:
                cell.setCellValue("阴性");
                break;
            case 3:
                cell.setCellValue("阳性");
                break;
            case 4:
                cell.setCellValue("待出");
                break;
            default:
                break;
        }
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
            final XSSFRow xssfRow = sheet.getRow(3+i);//[3是0,1,2行已被创建]
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

    protected static String[] splitArray(final HashMap<String,Object> map,final String key){
        return ((String) map.get(key)).split(",");
    }

    /**
     * 每行的数据填充,每行循环一次
    */
    private static void renderDataRow(final XSSFSheet sheet,final Row row,final int cells,final HashMap<String,Object> map){
        final String[] names = splitArray(map,"name");//云岩区,乌当区,凯里市,开阳县,修文县,花溪区
        final String[] yinxings = splitArray(map,"yinxing");//阴性
        final String[] yangxings = splitArray(map,"yangxing");//阳性
        final String[] weijiances = splitArray(map,"weijiance");//待出
        final String[] totals = splitArray(map,"total");//采样数
        for(int x = 0; x < names.length; x++){
            final String name = names[x];
            final int position = dataGetPosition(sheet,cells,(name));
            if(position != -1){
                for(int z = 0; z < 4; z++){
                    final Cell rowCell = row.getCell(position + z);
                    switch (z){
                        case 0:
                            rowCell.setCellValue(totals[x]);
                            break;
                        case 1:
                            rowCell.setCellValue(yinxings[x]);
                            break;
                        case 2:
                            rowCell.setCellValue(yangxings[x]);
                            break;
                        case 3:
                            rowCell.setCellValue(weijiances[x]);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 获取人群类型的位置
    */
    private static int dataGetPosition(final XSSFSheet sheet,final int cells,final String value){
        for (int j = 1; j <= cells;j++){
            final XSSFRow xssfRow = sheet.getRow(1);
            final XSSFCell cell = xssfRow.getCell(j);
            final String v = cell.getStringCellValue();
            if(value.equals(v)){
                return j;
            }
        }
        return -1;
    }
}