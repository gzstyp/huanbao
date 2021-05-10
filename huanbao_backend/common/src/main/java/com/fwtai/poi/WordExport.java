package com.fwtai.poi;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * word导出
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-01-06 18:19
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class WordExport{

    public static void exportWord(final String start,
                                  final String end,
                                  final String selectArea,
                                  final List<HashMap<String,Object>> listEmployee,
                                  final List<HashMap<String,Object>> listSiteType,
                                  final List<HashMap<String,Object>> listEnvironmentOuterPack,
                                  final List<HashMap<String,Object>> listEntranceRisk,
                                  final HashMap<String,Object> sampleTypeTotal,
                                  final List<HashMap<String,Object>> listEnvironmentEmployee,
                                  final String fileName,
                                  final HttpServletResponse response) throws Exception{
        final XWPFDocument doc = new XWPFDocument();//创建新文档
        final String title = "贵州省冷冻冷藏肉品新冠病毒监测结果报告";
        ToolWord.singleRow(doc,title,18,ParagraphAlignment.CENTER,true,true);

        final String describe = "为了贯彻落实国务院联防联控机制《关于开展冷冻冷藏肉品风险排查的通知》（联防联控机制综发〔2020〕210 号）文件的要求。为科学规范指导开展我省新冠病毒环境监测工作，深入开展病毒溯源和疫情防控，2020年8月10日贵州省卫生健康委员会联合多部委下发了《贵州省农贸（集贸）市场及冷冻冷藏产品新冠病毒环境和人员监测方案》。要求各市（州）及县（区），为加强对农贸（集贸）市场及大型食品冷库新冠病毒环境监测。现将我省此次新冠肺炎监测结果报告如下，采样时间为"+handleDate(start,end)+"。";
        ToolWord.paragraph(doc,describe,14,false,false);

        final String paragraph1 = "一、监测概况";
        ToolWord.paragraph(doc,paragraph1,14,true,true);
        if(listEmployee.size() > 0){
            final String paragraph2 = "本周"+listEmployee.size()+"市（州）全部完成了采样及检测任务。本次共采集相关样本xx份，经新冠核酸检测均为阴性";
            ToolWord.paragraph(doc,paragraph2,14,true,true);
        }

        int colsEmployee = 0;
        if(listEmployee.size() > 0){
            colsEmployee = ToolWord.getMax(listEmployee,"profession");
        }
        int colsSiteType = 0;
        if(listSiteType.size() > 0){
            colsSiteType = ToolWord.getMax(listSiteType,"site_type");
        }
        int colsEntranceRisk = 0;
        if(listEntranceRisk.size() > 0){
            colsEntranceRisk = ToolWord.getMax(listEntranceRisk,"total");
        }
        int colsEnvironmentOuterPack = 0;
        if(listEnvironmentOuterPack.size()>0){
            colsEnvironmentOuterPack = ToolWord.getMax(listEnvironmentOuterPack,"sample_total");
        }

        Integer itemTotal = 0;
        for(int i = 0; i < colsEmployee; i++){
            itemTotal += ToolWord.extractTotal(listEmployee,"profession_total",i);
        }

        Integer itemEnvironmentOuterPack = 0;
        for(int i = 0; i < colsEnvironmentOuterPack; i++){
            itemEnvironmentOuterPack += ToolWord.extractTotal(listEnvironmentOuterPack,"sample_total",i);
        }

        final String paragraph3 = "其中冷库食品xx份（其中冷库水产品xx份，其他冷冻肉类xx份），外环境样本xx份（其中产品外包装xx份），从业人员咽拭子"+itemTotal+"份，共计xx份。监测结果见表1。";
        ToolWord.paragraph(doc,paragraph3,14,true,false);

        ToolWord.singleRow(doc,"表1 全省食品、外环境（含包装）及相关从业人员监测情况",14,ParagraphAlignment.CENTER,true,false);

        //合并单元格
        if(listEnvironmentEmployee.size() > 0)
            mergeTableCell(doc,listEnvironmentEmployee);

        ToolWord.newLine(doc);//换行

        final String paragraph20 = "二、食品及外环境样本监测结果";
        ToolWord.paragraph(doc,paragraph20,14,true,true);
        String paragraph21 = "（一）不同类型场所监测结果   本次共监测"+colsSiteType+"种类型的销售场所，暂无数据。详见表2。";
        if(listSiteType.size() > 0){
            paragraph21 = "（一）不同类型场所监测结果   本次共监测"+colsSiteType+"种类型的销售场所，即"+ToolWord.getItems(listSiteType,"site_type",colsSiteType)+"。详见表2。";
        }

        ToolWord.paragraph(doc,paragraph21,14,true,false);

        ToolWord.singleRow(doc,"表2 全省不同类型场所监测情况",14,ParagraphAlignment.CENTER,true,false);

        if(listSiteType.size() > 0){
            ToolWord.createDocTable(doc,listSiteType,colsSiteType,"site_type","area","地区","type_total","合计");
        }

        ToolWord.newLine(doc);

        final String table3Info = "(二)不同来源食品监测情况   本次监测的进口或中高风险地区来源的海产品、肉类要求全部采集并检测，其余的搭配一定数量抽检。本次无中高风险地区来源的食品（本周中高风险地区为"+selectArea+"）。详见表3。";

        ToolWord.paragraph(doc,table3Info,14,false,false);

        ToolWord.singleRow(doc,"表3  不同来源食品监测情况",14,ParagraphAlignment.CENTER,true,false);

        //表3,不同来源食品监测情况
        if(listEntranceRisk.size() > 0){
            ToolWord.createDocTable(doc,listEntranceRisk,colsEntranceRisk,"entrance","name","地区","total","合计");
        }

        ToolWord.newLine(doc);

        final String table4Info = "（三）外环境不同样本类型监测情况   本次主要采集"+sampleTypeTotal.get("category")+"等"+sampleTypeTotal.get("total")+"类外环境样本类型。本次重点区分产品包装和其余外环境样本，共"+itemEnvironmentOuterPack+"份。详见表4。";

        ToolWord.paragraph(doc,table4Info,14,true,false);

        ToolWord.singleRow(doc,"表4  外环境样本监测情况",14,ParagraphAlignment.CENTER,true,false);

        //表4  外环境样本监测情况[产品外包装样本	其余外环境样本	合计]
        if(listEnvironmentOuterPack.size()>0){
            ToolWord.createDocTable(doc,listEnvironmentOuterPack,colsEnvironmentOuterPack,"sample_type","area_name","地区","sample_total","合计");
        }

        ToolWord.newLine(doc);

        final String title30 = "三、从业人员监测结果";
        ToolWord.paragraph(doc,title30,14,true,true);

        if(listEmployee.size() > 0){
            final String title31 = "本次监测主要采集销售、宰杀、加工、贮存、运输、管理员和其他7类从业人员的咽拭子样本。情况详见表5。";
            ToolWord.paragraph(doc,title31,14,true,false);
        }else{
            final String title31 = "从业人员监测结果暂无数据";
            ToolWord.paragraph(doc,title31,14,true,false);
        }

        if(listEmployee.size() > 0){
            ToolWord.singleRow(doc,"表5  不同从业人员监测情况",14,ParagraphAlignment.CENTER,true,false);
        }

        if(listEmployee.size() > 0){
            ToolWord.createDocTable(doc,listEmployee,colsEmployee,"profession","name","地区","profession_total","合计");
        }

        ToolWord.newLine(doc);

        final String title40 = "四、下一步防控建议";
        ToolWord.paragraph(doc,title40,14,true,true);

        final String title41 = "（一）切实做好外防输入工作。";
        ToolWord.paragraph(doc,title41,13,false,true);
        final String title42 = "虽然我省目前所采集的外环境及从业人员样本中新冠病毒核酸检测结果均为阴性，但境外新冠肺炎疫情依然严峻，境内多地亦有散发、乃至聚集性疫情。我省依旧面临新冠肺炎输入传播风险。";
        ToolWord.paragraph(doc,title42,14,true,false);

        final String title43 = "（二）摸清食品来源情况。";
        ToolWord.paragraph(doc,title43,13,false,true);
        final String title44 = "海关部门要加强进口食品检验检疫，商务、市场监管部门要摸清涉及场所以及分布底数，特别是要掌握进口、中高风险地区食品在我省流通环节情况。";
        ToolWord.paragraph(doc,title44,14,true,false);

        final String title45 = "（三）加强宣传，做好个人防护。";
        ToolWord.paragraph(doc,title45,13,false,true);
        final String title46 = "张贴和滚动播放宣传资料，工作人员佩戴口罩和加强手卫生，做到勤洗手、常通风，保持安全的社交距离。";
        ToolWord.paragraph(doc,title46,14,true,false);

        final String title47 = "（四）保持环境清洁，加强消毒措施。";
        ToolWord.paragraph(doc,title47,13,false,true);
        final String title48 = "做好源头管控，公共场所做好日常清洁和预防性消毒等应对措施。";
        ToolWord.paragraph(doc,title48,14,true,false);

        final String title49 = "（五）建立制度，加强监测。";
        ToolWord.paragraph(doc,title49,13,false,true);
        final String title50 = "建立员工健康监测制度，每日对员工健康状况进行登记；在市场入口外，增加体温测量设备，体温检测正常方可进入。";
        ToolWord.paragraph(doc,title50,14,true,false);

        ToolWord.singleRow(doc,new SimpleDateFormat("yyyy年MM月dd日").format(new Date()),13,ParagraphAlignment.RIGHT,true,false);

        ToolWord.downloadWord(doc,fileName,response);
    }

    private static String handleDate(final String start,final String end){
        String date = "";
        if(start != null && end != null){
            date = start +"至"+end;
        }
        if(start == null && end == null){
            date = "截至"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if(start == null && end != null){
            date = "截至"+end;
        }
        if(start != null && end == null){
            date = start+"至"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        return date;
    }

    //表1 全省食品、外环境（含包装）及相关从业人员监测情况
    private static void mergeTableCell(final XWPFDocument doc,final List<HashMap<String,Object>> list){
        final XWPFTable table = doc.createTable();
        final XWPFTableRow row0 = table.getRow(0);//创建表格的第1行,默认是创建1行1列的表格
        for(int i = 0; i < 7; i++){//共8个单元格,因为默认是已创建titleRow0.getCell(0);所以仅需添加7列
            row0.addNewTableCell();//在当前行继续创建新列
        }
        final XWPFTableRow row1 = table.createRow();//在表的行了的列数上创建新行且列数是和上1行的列数一样,此处是第2行

        final XWPFTableCell cell00 = row0.getCell(0);
        cell00.setText("地区");

        final XWPFTableCell cell01 = row0.getCell(1);
        cell01.setText("食品样本");
        final XWPFTableCell cell03 = row0.getCell(3);
        cell03.setText("外环境样子");
        final XWPFTableCell cell05 = row0.getCell(5);
        cell05.setText("从业人员咽拭子");

        final XWPFTableCell cell07 = row0.getCell(7);
        cell07.setText("合计");

        final XWPFTableCell cell11 = row1.getCell(1);
        cell11.setText("检测份数");
        final XWPFTableCell cell12 = row1.getCell(2);
        cell12.setText("阳性份数");

        final XWPFTableCell cell13 = row1.getCell(3);
        cell13.setText("检测份数");
        final XWPFTableCell cell14 = row1.getCell(4);
        cell14.setText("阳性份数");

        final XWPFTableCell cell15 = row1.getCell(5);
        cell15.setText("检测份数");
        final XWPFTableCell cell16 = row1.getCell(6);
        cell16.setText("阳性份数");

        ToolWord.cellsAlign(STVerticalJc.CENTER,STJc.CENTER,cell00,cell01,cell03,cell05,cell07,cell11,cell12,cell13,cell14,cell15,cell16);

        ToolWord.mergeCellsColumn(table,0,1,2);
        ToolWord.mergeCellsColumn(table,0,3,4);
        ToolWord.mergeCellsColumn(table,0,5,6);
        ToolWord.mergeCellsRow(table,0,0,1);
        ToolWord.mergeCellsRow(table,7,0,1);

        for(int i = 0; i < list.size(); i++){
            final HashMap<String,Object> map = list.get(i);
            final String areaName = (String)map.get("name");
            final String[] types = ((String)map.get("type")).split(",");
            final String[] positives = ((String)map.get("positive")).split(",");
            final String[] totals = ((String)map.get("total")).split(",");

            int totalRow = 0;
            for(int x = 0; x < totals.length; x++){
                totalRow += Integer.parseInt(totals[x]);
            }
            for(int y = 0; y < positives.length; y++){
                totalRow += Integer.parseInt(positives[y]);
            }
            final XWPFTableRow row = table.createRow();//在原有的表格上创建新行
            for(int y=0;y<8;y++){
                final XWPFTableCell cell = row.getCell(y);
                if(y==0){
                    ToolWord.cellCenter(cell,areaName,12);
                }else{
                    final int len = types.length;
                    if(len == 1){
                        final String type = types[0];
                        if(type.equals("2")){
                            if(y ==3){
                                ToolWord.cellCenter(cell,totals[0],12);
                            }else if(y ==4){
                                ToolWord.cellCenter(cell,positives[0],12);
                            }
                        }else if(type.equals("3")){
                            if(y ==5){
                                ToolWord.cellCenter(cell,totals[0],12);
                            }else if(y ==6){
                                ToolWord.cellCenter(cell,positives[0],12);
                            }
                        }
                    }else if(len == 2){
                        if(y ==3){
                            ToolWord.cellCenter(cell,totals[0],12);
                        }else if(y ==4){
                            ToolWord.cellCenter(cell,positives[0],12);
                        }else if(y ==5){
                            ToolWord.cellCenter(cell,totals[1],12);
                        }else if(y ==6){
                            ToolWord.cellCenter(cell,positives[1],12);
                        }
                    }
                    if(y == 7){
                        ToolWord.cellCenter(cell,String.valueOf(totalRow),12);
                    }
                }
            }
        }
        //处理最后1行的合计数据
        final ArrayList<HashMap<Integer,Integer>> listVales = ToolWord.extractColumnTotal(table,2,1);//因前两行是表头行
        final String[] values = ToolWord.extractEndTotal(listVales,7);
        final int valueLength = values.length;
        int totalAll = 0;
        for(int x = 0; x < valueLength; x++){
            totalAll += Integer.parseInt(values[x]);
        }
        final XWPFTableRow rowTotal = table.createRow();//在原来的表格上新增1行
        final XWPFTableCell cellTotal = rowTotal.getCell(0);
        ToolWord.cellCenter(cellTotal,"合计",12);
        for(int x = 0; x < valueLength; x++){
            final XWPFTableCell cell = rowTotal.getCell(x+1);// +1 是因为第1列是合计文本,即 rowTotal.getCell(0)
            ToolWord.cellCenter(cell,values[x],12);
        }
        final XWPFTableCell cellEnd7 = rowTotal.getCell(7);
        ToolWord.cellCenter(cellEnd7,String.valueOf(totalAll),12);
    }

    //动态创建单元格示例代码,若获取不到单元格的值请检查是否使用 paragraph.createRun().setText(content); 或换个调用顺序
    private static void mergeTableCellExample(final XWPFDocument doc,final List<HashMap<String,Object>> list){
        final XWPFTable table = doc.createTable();
        final XWPFTableRow titleRow0 = table.getRow(0);//todo 创建表格的第1行,默认是创建1行1列的表格
        final XWPFTableCell cell1 = titleRow0.getCell(0);
        cell1.setText("阳性份数");
        for(int i = 0; i < 7; i++){//todo 共8个单元格,因为默认是已创建titleRow0.getCell(0);所以仅需添加7列
            final XWPFTableCell cell = titleRow0.addNewTableCell();//todo 在当前行继续创建新列
            ToolWord.cellsAlign(STVerticalJc.CENTER,STJc.CENTER,cell);
            cell.setText("阳性份数"+i);
        }
        final XWPFTableRow titleRow1 = table.createRow();//todo 在表的行了的列数上创建新行且列数是和上1行的列数一样,此处是第2行

        /*//final XWPFTable table = doc.createTable(2,8);//创建一个2行8列的表格,勿删
        final XWPFTableCell cell00 = table.getRow(0).getCell(0);
        cell00.setText("地区");
        final XWPFTableCell cell01 = table.getRow(0).getCell(1);
        cell01.setText("食品样本");
        final XWPFTableCell cell03 = table.getRow(0).getCell(3);
        cell03.setText("外环境样子");
        final XWPFTableCell cell05 = table.getRow(0).getCell(5);
        cell05.setText("从业人员咽拭子");
        final XWPFTableCell cell07 = table.getRow(0).getCell(7);
        cell07.setText("合计");

        final XWPFTableCell cell11 = table.getRow(1).getCell(1);
        cell11.setText("检测份数");
        final XWPFTableCell cell12 = table.getRow(1).getCell(2);
        cell12.setText("阳性份数");

        final XWPFTableCell cell13 = table.getRow(1).getCell(3);
        cell13.setText("检测份数");
        final XWPFTableCell cell14 = table.getRow(1).getCell(4);
        cell14.setText("阳性份数");

        final XWPFTableCell cell15 = table.getRow(1).getCell(5);
        cell15.setText("检测份数");
        final XWPFTableCell cell16 = table.getRow(1).getCell(6);
        cell16.setText("阳性份数");

        ToolWord.cellsAlign(STVerticalJc.CENTER,STJc.CENTER,cell00,cell01,cell03,cell05,cell07,cell11,cell12,cell13,cell14,cell15,cell16);

        ToolWord.mergeCellsColumn(table,0,1,2);
        ToolWord.mergeCellsColumn(table,0,3,4);
        ToolWord.mergeCellsColumn(table,0,5,6);
        ToolWord.mergeCellsRow(table,0,0,1);
        ToolWord.mergeCellsRow(table,7,0,1);

        for(int i = 0; i < list.size(); i++){
            final HashMap<String,Object> map = list.get(i);
            final String areaName = (String)map.get("name");
            final String[] types = ((String)map.get("type")).split(",");
            final String[] positives = ((String)map.get("positive")).split(",");
            final String[] totals = ((String)map.get("total")).split(",");
            int totalRow = 0;
            for(int x = 0; x < totals.length; x++){
                totalRow += Integer.parseInt(totals[x]);
            }
            for(int y = 0; y < positives.length; y++){
                totalRow += Integer.parseInt(positives[y]);
            }
            final XWPFTableRow row = table.createRow();
            for(int y=0;y<8;y++){
                final XWPFTableCell cell = row.getCell(y);
                ToolWord.cellsAlign(STVerticalJc.CENTER,STJc.CENTER,cell);
                if(y==0){
                    cell.setText(areaName);
                }else{
                    final int len = types.length;
                    if(len == 1){
                        final String type = types[0];
                        if(type.equals("2")){
                            if(y ==3){
                                cell.setText(totals[0]);
                            }else if(y ==4){
                                cell.setText(positives[0]);
                            }
                        }else if(type.equals("3")){
                            if(y ==5){
                                cell.setText(totals[0]);
                            }else if(y ==6){
                                cell.setText(positives[0]);
                            }
                        }
                    }else if(len == 2){
                        if(y ==3){
                            cell.setText(totals[0]);
                        }else if(y ==4){
                            cell.setText(positives[0]);
                        }else if(y ==5){
                            cell.setText(totals[1]);
                        }else if(y ==6){
                            cell.setText(positives[1]);
                        }
                    }
                    if(y == 7){
                        cell.setText(String.valueOf(totalRow));
                    }
                }
            }
        }

        final List<XWPFTableRow> listRows = table.getRows();//获取行数
        final ArrayList<HashMap<Integer,Integer>> listVales = new ArrayList<HashMap<Integer,Integer>>();
        for(int x = 2; x < listRows.size(); x++){//因为...
            final XWPFTableRow tableRow = table.getRow(x);
            final List<XWPFTableCell> tableCells = tableRow.getTableCells();//获取每行的列数
            final HashMap<Integer,Integer> mapValues = new HashMap<Integer,Integer>();
            for(int y = 1; y < tableCells.size(); y++){//遍历每1行的每1列,因为第1列是区域地区,所以 y = 1
                final XWPFTableCell tableCell = tableCells.get(y);
                final List<XWPFParagraph> cellParagraphs = tableCell.getParagraphs();
                final XWPFParagraph paragraph = cellParagraphs.get(0);
                final String text = paragraph.getText();//可以试试 tableCells.get(y).getText();
                if(text.length() >0){
                    mapValues.put(y,Integer.parseInt(text));
                }
            }
            listVales.add(mapValues);
        }*/
    }
}