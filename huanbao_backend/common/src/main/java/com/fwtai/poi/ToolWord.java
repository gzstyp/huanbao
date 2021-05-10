package com.fwtai.poi;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;

/**
 * poi操作word
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-01-05 10:09
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class ToolWord{

    /**
     生成表头,格式如下
       ************************************
       *  地区  * 运输 * 加工 * 管理员 * 合计 *
       ************************************
       * 贵阳市 * 98  * 25   *　17　  * 140 *
       ************************************
       * 安顺市 * 102  * 61  *　  　  * 163 *
       ************************************
       * 合计  * 200  * 86  *　  17　 * 303 *
       ************************************
     * @param startColumnText 指定第1行的第1列的文字内容,用于不规则的2D表格
     * @param endColumnText 指定第1行的最后1列的文字内容
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 13:19
    */
    protected static void initTableTitle(final XWPFTableRow row,final String[] values,final String startColumnText,final String endColumnText){
        cellCenter(row.getCell(0),startColumnText,12);
        for(final String value : values){
            final XWPFTableCell cell = row.addNewTableCell();//在当前行继续创建新列
            cellCenter(cell,value,12);
        }
        cellCenter(row.addNewTableCell(),endColumnText,12);
    }

    /**
     * 填充数据,先确定表头后,再执行填充数据行,即先执行initTableTitle()生成表头
     * @param startColumnText 第N行的第1列的文字内容
     * @param endColumnPosition 第N行的最后1列的位置索引,它和后一个参数对应
     * @param endColumnText 第N行的最后1列的文字内容,它和前一个参数对应
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 13:23
    */
    protected static void fillRowData(final XWPFTableRow row,final String[] values,final String startColumnText,final int endColumnPosition,final String endColumnText){
        cellCenter(row.getCell(0),startColumnText,12);
        for(int x = 0; x < values.length; x++){
            final XWPFTableCell cell = row.getCell(x+1);//+1是因为第1列是地区区域
            cellCenter(cell,values[x],12);
        }
        cellCenter(row.getCell(endColumnPosition),endColumnText,12);
    }

    /**
     * 指定单元格赋值文本内容及字体大小,默认的垂直居中和水平居中
     * @注意事项 单元格若不经过 XWPFRun.setText(content);文本内容的会取不到值!
     * @param cell 单元格
     * @param content 文本内容
     * @param fontSize 字体大小
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021年1月5日 12:22:21
    */
    protected static void cellCenter(final XWPFTableCell cell,final String content,final int fontSize){
        //给当前列中添加段落，就是给列添加内容
        final XWPFParagraph paragraph = cell.getParagraphs().get(0);
        final XWPFRun run = paragraph.createRun();
        run.setText(content);//设置内容
        run.setFontSize(fontSize);//设置大小
        //给列中的内容设置样式
        rowCellAlign(cell,STVerticalJc.CENTER,STJc.CENTER);//上下居中,左右居中
    }

    /**
     * 设置单元格的对齐方式
     * @param cell 单元格
     * @param vertical 垂直对齐方式
     * @param horizontal 水平对齐方式
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 12:18
    */
    protected static void rowCellAlign(final XWPFTableCell cell,final STVerticalJc.Enum vertical,final STJc.Enum horizontal){
        final CTTc cttc = cell.getCTTc();
        final CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(vertical);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(horizontal);
        final CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setType(STTblWidth.DXA);
        tblWidth.setW(BigInteger.valueOf(360*8));//宽度
    }

    /**
     * 批量设置单元格的样式
     * @param cells 仅能放在最后一个参数的位置
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/13 16:31
    */
    protected static void cellsAlign(final STVerticalJc.Enum vertical,final STJc.Enum horizontal,final XWPFTableCell... cells){
        for(int x = 0; x < cells.length; x++){
            rowCellAlign(cells[x],vertical,horizontal);
        }
    }

    /**设置单元格内容及样式,若不使用 paragraph.createRun().setText(content); 获取不到单元格的文本内容*/
    protected static void rowCellAlign(final XWPFTableCell cell,final STVerticalJc.Enum vertical,final STJc.Enum horizontal,final int fontSize,final String content){
        //给当前列中添加段落，就是给列添加内容
        final XWPFParagraph paragraph = cell.getParagraphs().get(0);
        final XWPFRun run = paragraph.createRun();
        run.setText(content);//设置内容
        run.setFontSize(fontSize);//设置大小
        //给列中的内容设置样式
        rowCellAlign(cell,vertical,horizontal);
    }

    /**
     * 创建表格并填充数据,2D二维表格,计算最后一行的计算
     * @param doc word文档对象
     * @param listData 数据
     * @param cols 最大个数作为表头的列个数
     * @param horizontalKey 从 listData 里分组[水平横向方向的字段]的key,一般指的是类型或类别的count(xxx)字段,如工种或场所类型
     * @param startVerticalKey 从 listData 里分组[垂直竖向方向的字段]获取作为数据行的第1行的第1列的key,一般是最外层的 group by xxx字段
     * @param startColumnText 表头的第1个单元格的名称文本内容,如:地区
     * @param totalKey 一般是指count(xxx)的别名,如 count(xxx) as xxx_total,即填入 xxx_total 该值即可
     * @param endColumnText 表头的第最后1个单元格的名称文本内容,如:合计
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 19:18
    */
    protected static void createDocTable(final XWPFDocument doc,final List<HashMap<String,Object>> listData,final int cols,final String horizontalKey,final String startVerticalKey,final String startColumnText,final String totalKey,final String endColumnText){
        final XWPFTable table = doc.createTable();//默认是创建1行1列的表格
        final HashMap<String,Object> map = listGetHashMap(listData,cols,horizontalKey);
        //初始化表头行
        final XWPFTableRow titleRow = table.getRow(0);//创建的的一行一列的表格，获取第一行
        //填充表头行各单元格
        initTableTitle(titleRow,((String)map.get(horizontalKey)).split(","),startColumnText,endColumnText);
        //填充数据行
        for(int i = 0; i < listData.size(); i++){
            final HashMap<String,Object> mapRow = listData.get(i);
            final XWPFTableRow row = table.createRow();//在原来的表格上新增1行,列数和上行一样
            final String[] vulues = ((String) mapRow.get(totalKey)).split(",");
            long itemTotal = 0;
            for(int x = 0; x < vulues.length; x++){
                itemTotal += Long.parseLong(vulues[x]);
            }
            final String item = (String)mapRow.get(startVerticalKey);
            fillRowData(row,vulues,item,cols+1,String.valueOf(itemTotal));//cols+1,因为第1列是地区区域
            if(i == listData.size() - 1){
                final ArrayList<HashMap<Integer,Integer>> listVales = extractColumnTotal(table,1,1);//因为第1行是表头字段,所以 int x = 1;
                final String[] values = extractEndTotal(listVales,cols+2);//注意为什么要 cols+2 !!!,因为从 int i = 1,而不是从 int i = 0开始,再加上最后一列的合计
                fillRowData(table.createRow(),values,"合计",cols+1,null);//最后一个参数为 null 无需传,因为是填充数据行,该方法本身已给单元格赋值,无需指定最后一列的文本内容,否则会累加
            }
        }
    }

    /**
     * 从list元素中获取指定的元素
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/14 17:38
    */
    private static HashMap<String,Object> listGetHashMap(final List<HashMap<String,Object>> listData,final int maxColumn,final String horizontalKey){
        HashMap<String,Object> map = new HashMap<String,Object>(maxColumn);
        for(int i = 0; i < listData.size(); i++){
            final String[] values = ((String) listData.get(i).get(horizontalKey)).split(",");
            if(maxColumn == values.length){
                map = listData.get(i);
                break;
            }
        }
        return map;
    }

    /**
     * 除去表头行之外计算每一行的每一列合计
     * @param startRow 开始行,即除表头行之外,数据行开始
     * @param startColumn 开始列,一般第0列是地区或区域,所以从1开始
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/14 17:37
    */
    protected static ArrayList<HashMap<Integer,Integer>> extractColumnTotal(final XWPFTable table,final int startRow,final int startColumn){
        final List<XWPFTableRow> listRows = table.getRows();//获取行数
        final ArrayList<HashMap<Integer,Integer>> listVales = new ArrayList<HashMap<Integer,Integer>>();
        for(int x = startRow; x < listRows.size(); x++){
            final XWPFTableRow tableRow = table.getRow(x);
            final List<XWPFTableCell> tableCells = tableRow.getTableCells();//获取每行的列数
            final HashMap<Integer,Integer> mapValues = new HashMap<Integer,Integer>();
            for(int y = startColumn; y < tableCells.size(); y++){//遍历每1行的每1列,因为第1列是区域地区,所以 y = 1
                final String text = tableCells.get(y).getText();//若数据有误,可以试试 xwpfTableCell.getParagraphs().get(0).getText();
                if(text.length() >0){
                    mapValues.put(y,Integer.parseInt(text));
                }
            }
            listVales.add(mapValues);
        }
        return listVales;
    }

    /**
     * 最后1行为合计的最后1列的值的计算
     * @param column 列总数,注意本项目里表格第1列是地区区域文本及最后1列是合计
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/14 17:35
    */
    protected static String[] extractEndTotal(final ArrayList<HashMap<Integer,Integer>> list,final int column){
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < column; i++){
            final Integer total = calculateTotal(list,i);
            if(sb.length() > 0){
                sb.append(",").append(total);
            }else{
                sb = new StringBuilder(String.valueOf(total));//注意这个必须为 String 类型,否则得到的是空字符串""
            }
        }
        return sb.toString().split(",");
    }

    /**
     * 最后一行的每一列计算合计,适用于表头没有合并单元格的表格,可以参考页面js!!!
     * @param totalKey 一般是指count(xxx)的别名,如 count(xxx) as xxx_total,即填入 xxx_total 该值即可
     * @param indexColumn 是列数的索引,通过 for(int i = 0; i < cols; i++){ cols是最大的行数,循环传入,值为:1,2,3……
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/7 19:44
    */
    protected static Integer extractTotal(final List<HashMap<String,Object>> listData,final String totalKey,final int indexColumn){
        int columnTotal = 0;
        for(int i = 0; i < listData.size(); i++){
            final HashMap<String,Object> row = listData.get(i);
            final String[] values = ((String) row.get(totalKey)).split(",");
            try {
                final String value = values[indexColumn];
                columnTotal += Integer.parseInt(value);
            } catch (final Exception e){}
        }
        return columnTotal;
    }

    /**
     * 获取最大值
     * @param totalKey 一般指的是类型或类别的count(xxx)字段,如工种或场所类型
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/7 20:03
    */
    protected static int getMax(final List<HashMap<String,Object>> listData,final String totalKey){
        //final OptionalInt optMax = data.stream().mapToInt(HashMap::size).max();//简化代码
        final OptionalInt optional = listData.stream().mapToInt(value -> {
            final String arrs = (String) value.get(totalKey);
            final String[] split = arrs.split(",");
            return split.length;
        }).max();
        return optional.getAsInt();
    }

    /**计算每列的总和*/
    protected static Integer calculateTotal(final ArrayList<HashMap<Integer,Integer>> listVales,final int indexColumn){
        Integer columnTotal = 0;
        for(int i = 0; i < listVales.size(); i++){
            final HashMap<Integer,Integer> map = listVales.get(i);
            try {
                columnTotal += map.get(indexColumn);
            } catch (final Exception e){}
        }
        return columnTotal;
    }

    /**
     * 单行文本内容出处理
     * @param content 内容
     * @param fontSize 字体大小
     * @param align 文本对齐方式
     * @param newline 末尾是否换行
     * @param bold 文本是否加粗
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 19:17
    */
    protected static void singleRow(final XWPFDocument doc,final String content,final int fontSize,final ParagraphAlignment align,final boolean newline,final boolean bold){
        final XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(align);//对齐方式
        //final XWPFRun title = paragraph.insertNewRun(0);
        final XWPFRun title = paragraph.createRun();//有问题试试上一行的方法
        title.setText(content);
        title.setFontSize(fontSize);
        if(bold)
            title.setBold(true);
        if(newline)
            title.addCarriageReturn();
    }

    /**
     * 单行文本内容出处理
     * @param content 内容
     * @param fontSize 字体大小
     * @param newline 末尾是否换行
     * @param bold 文本是否加粗
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 19:17
    */
    protected static void paragraph(final XWPFDocument doc,final String content,final int fontSize,final boolean newline,final boolean bold){
        final XWPFParagraph paragraph = doc.createParagraph();
        final XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontSize(fontSize);
        if(newline)
            run.addCarriageReturn();//换行,新的内容不会跟这末尾
        if(bold)
            run.setBold(true);
    }

    /**换行,新的文本内容会连在末尾后面 */
    protected static void newLine(final XWPFDocument doc){
        final XWPFParagraph paragraph = doc.createParagraph();
        final XWPFRun run = paragraph.createRun();
        run.addBreak();//换行,新的文本内容会连在末尾后面
    }

    /**换了新行,新的内容不会连在末尾后面*/
    protected static void newLineReturn(final XWPFDocument doc){
        final XWPFParagraph paragraph = doc.createParagraph();
        final XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();//换了新行,新的内容不会连在末尾后面
    }

    /**
     * 获取分类或类型,一般用于生成表头行的列
     * @param horizontalKey 分类或类型的字段,分组[水平横向方向的字段]的key,一般指的是类型或类别的count(xxx)字段
     * @param maxColumn 上一次操作获取的最大值的列数
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/7 16:30
    */
    protected static String getItems(final List<HashMap<String,Object>> listData,final String horizontalKey,final int maxColumn){
        final HashMap<String,Object> result = listGetHashMap(listData,maxColumn,horizontalKey);
        final String[] values = ((String)result.get(horizontalKey)).split(",");
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < values.length; x++){
            final String value = values[x];
            if(sb.length() > 0){
                sb.append("、").append(value);
            }else{
                sb = new StringBuilder(value);//无需处理最后一个字符
            }
        }
        return sb.toString();
    }

    /**
     * word跨列合并单元格
     * @param row 指定行,行是从0开始的
     * @param start 指定开始列,是从0开始
     * @param end 指定结束列,是从0开始
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/13 15:40
    */
    protected static void mergeCellsColumn(final XWPFTable table,final int row,final int start,final int end){
        for (int cellIndex = start; cellIndex <= end; cellIndex++) {
            final XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
            if (cellIndex == start){
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            }else{
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * word跨行并单元格
     * @param col 指定列,行是从0开始
     * @param start 开始行,从0开始
     * @param end 结束行,从0开始
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/13 15:42
    */
    protected static void mergeCellsRow(final XWPFTable table,final int col,final int start,final int end){
        for (int rowIndex = start; rowIndex <= end; rowIndex++) {
            final XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            if(rowIndex == start){
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            }else{
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    /**
     * 获取单元格
     * @param positionRow 行的位置,从0开始
     * @param positionCell 列的位置,从0开始
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/13 16:24
    */
    protected static XWPFTableCell getRowCells(final XWPFTable table,final int positionRow,final int positionCell){
        return table.getRow(positionRow).getCell(positionCell);
    }

    /**
     * word导出下载
     * @param fileName 含后缀名
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 15:52
    */
    protected static void downloadWord(final XWPFDocument doc,final String fileName,final HttpServletResponse response) throws IOException{
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.write(os);
        response.reset();
        response.setContentType("application/vnd.ms-word;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=" + new String((fileName).getBytes(), "iso-8859-1"));
        final ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new ByteArrayInputStream(os.toByteArray()));
            bos = new BufferedOutputStream(out);
            final byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))){
                bos.write(buff, 0, bytesRead);
            }
        }catch (final IOException e) {
            throw e;
        }finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}