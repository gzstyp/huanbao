package com.fwtai.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例代码
 * https://elim.iteye.com/blog/2049110
 * https://www.cnblogs.com/unruly/archive/2017/09/06/7483858.html
 * https://www.cnblogs.com/zyc-blogs/p/10558859.html
 * https://blog.csdn.net/ztt_1119/article/details/69390807
 * http://poi.apache.org/apidocs/dev/org/apache/poi/xwpf/usermodel/XWPFTable.html
 * https://blog.csdn.net/ztt_1119/article/details/69390807
 * https://www.cnblogs.com/sun-flower1314/p/10128796.html
 * https://wenku.baidu.com/view/2507cb89905f804d2b160b4e767f5acfa0c78336.html
 * https://53873039oycg.iteye.com/blog/2157923
 * https://www.cnblogs.com/dayuruozhi/p/6490868.html
 * https://www.cnblogs.com/AardWolf/p/11268068.html
 * https://blog.csdn.net/owen_william/article/details/81290024
 *
 * https://blog.csdn.net/owen_william/article/details/81290024
 * http://deepoove.com/poi-tl/apache-poi-guide.html ok
 */
public class PoiWord extends XWPFDocument{

    //初始化XWPFDocument
    public static XWPFDocument initDoc(List<Map<String,Object>> list) throws Exception{

        final XWPFDocument document = new XWPFDocument();
        //设置边距
        final CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        final CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(720L));
        pageMar.setTop(BigInteger.valueOf(1440L));
        pageMar.setRight(BigInteger.valueOf(720L));
        pageMar.setBottom(BigInteger.valueOf(1440L));


        //================插入图片======================
        XWPFParagraph pictures = document.createParagraph();
        pictures.setIndentationLeft(300);//前面缩进300
        XWPFRun insertNewRun = pictures.createRun();
        int format =0;
        String imgFile ="E:\\Images\\Images\\win7.jpg";

        if(imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
        else if(imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
        else if(imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
        else if(imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
        else if(imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
        else if(imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
        else if(imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
        else if(imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
        else if(imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
        else if(imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
        else if(imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
        else {
            System.err.println("Unsupported picture: " + imgFile +
                ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");

        }
        insertNewRun.addPicture(new FileInputStream(imgFile), format, imgFile, Units.toEMU(350), Units.toEMU(150)); //高150，宽350
        // insertNewRun.addBreak(BreakType.PAGE);//图片独占一页
        insertNewRun.addBreak();//添加一个回车空行

        //====================设置段落======================
        XWPFParagraph firstParagraph = document.createParagraph();
        firstParagraph.setAlignment(ParagraphAlignment.RIGHT);//设置段落内容靠右
        firstParagraph.setIndentationRight(500);//末尾缩进300

        XWPFRun run = firstParagraph.createRun();
        run.setText("测试段落   样式设置");
        run.setBold(true); //加粗
        run.setFontSize(12);//字体大小
        run.setFontFamily("华文中宋");

        run.addBreak();//添加一个回车空行


        //============添加标题====================
        XWPFParagraph titleParagraph = document.createParagraph();
        //设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleParagraphRun = titleParagraph.createRun();
        titleParagraphRun.setText("测试使用poi导出word");
        titleParagraphRun.setBold(true);//加粗
        titleParagraphRun.setFontSize(22);//字体大小
        titleParagraphRun.setFontFamily("华文中宋");


        titleParagraphRun.addBreak();
        //==============添加表格数据===========
        //数据表格
        XWPFTable ComTable = document.createTable();//默认创建一个一行一列的表格

        //===========表格表头行===============
        XWPFTableRow titleRow = ComTable.getRow(0);//创建的的一行一列的表格，获取第一行
        titleRow.setHeight(500);//设置当前行行高
        titleStyle(titleRow);//设置表头行样式和内容

        //将数据封装到表格中
        for(Map<String,Object>  m:list){
            //=============合并行----合并单位名称======================
            XWPFTableRow DWMCRow = ComTable.createRow();//创建表头行下新的一行，列数是按照表头行的列数创建的
            DWMCRow.setHeight(600);//设置行高
            //获取当前行的列数，原理第一行有几列，下面创建的新行就有几列，如果创建表格时指定了，就按指定的数创建多少列
            List<XWPFTableCell> DWMCCellList = DWMCRow.getTableCells();
            for (int i = 0; i < DWMCCellList.size(); i++) {//经过这个循环后就将这一行的所有列合并成一列了
                //对单元格进行合并的时候,要标志单元格是否为起点,或者是否为继续合并
                if (i == 0)
                    DWMCCellList.get(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);//这是起点
                else
                    DWMCCellList.get(i).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);//继续合并
            }
            XWPFTableCell DWMCCell = DWMCCellList.get(0);//当前行合并成一列了，获取这一列
            TitleCelldata1(DWMCCell,m.get("dwmc")==null?"无":m.get("dwmc").toString());//给列添加样式和数据


            //=================================遍历具体的数据===============================
            List<Map<String,Object>>  list1 = (List<Map<String,Object>>)m.get("data");
            int i=0;
            for(Map<String,Object>  dm:list1){
                XWPFTableRow dataRow = ComTable.createRow();//创建一个新的行
                dataRow.setHeight(500);//设置行高
                List<XWPFTableCell> DataCellList = dataRow.getTableCells();//获取创建行的所有的列
                XWPFTableCell tc0 = DataCellList.get(0);//获取创建行的第一列
                CelldataCss(tc0,"800");//给第一列序号添加样式
                tc0.setText(++i+"");//给第一列添加值
                for(int ci=1;ci<DataCellList.size();ci++){//循环给剩下的列赋值
                    XWPFTableCell tc = DataCellList.get(ci);
                    //CelldataCss(tc,"1300");//给列添加样式
                    CellData(dm, tc, ci);//填充数据
                }
            }
        }
        return document;
    }

    //设置表头行样式和内容
    public static void titleStyle(final XWPFTableRow titleRow){
        XWPFTableCell xhCell = titleRow.getCell(0);//创建的表格没有指定几行几列，就默认是一行一列，这里获取创建的第一列
        xhCell.setText("");
        CelldataCss(xhCell,"800");
        XWPFTableCell bmCell =titleRow.addNewTableCell();//在当前行继续创建新列

        TitleCelldata(bmCell,"部01门");

        XWPFTableCell xmCell =titleRow.addNewTableCell();
        TitleCelldata(xmCell,"姓名");

        XWPFTableCell zwCell =titleRow.addNewTableCell();
        TitleCelldata(zwCell,"职务");

        XWPFTableCell bgdhCell =titleRow.addNewTableCell();
        TitleCelldata(bgdhCell,"办公电话");

        XWPFTableCell sjhmCell =titleRow.addNewTableCell();
        TitleCelldata(sjhmCell,"手机号码");

        XWPFTableCell yxCell =titleRow.addNewTableCell();
        TitleCelldata(yxCell,"邮箱");
    }

    //设置数据列样式
    public static void CelldataCss(final XWPFTableCell cell,final String width){
        /** 设置水平居中 */
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setW(new BigInteger(width));//设置列宽度
        tblWidth.setType(STTblWidth.DXA);

    }
    //设置内容信息样式
    public static void dataCelldata(XWPFTableCell cell,String txt,String tname){
        //给当前列中添加段落，就是给列添加内容
        XWPFParagraph p = cell.getParagraphs().get(0);
        XWPFRun headRun0 = p.createRun();
        headRun0.setText(txt);//设置内容
        headRun0.setFontSize(11);//设置大小
        // headRun0.setBold(true);//是否粗体
        headRun0.setFontFamily("仿宋_GB2312");
        //给列中的内容设置样式
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        if("zw".equals(tname)){
            tblWidth.setW(new BigInteger("2050"));//设置列宽度
        }else{
            tblWidth.setW(new BigInteger("1600"));//设置列宽度
        }
        tblWidth.setType(STTblWidth.DXA);
    }

    //设置表头和单位信息样式
    public static void TitleCelldata(XWPFTableCell cell,String content){
        //给当前列中添加段落，就是给列添加内容
        XWPFParagraph p = cell.getParagraphs().get(0);
        XWPFRun headRun0 = p.createRun();
        headRun0.setText(content);//设置内容
        headRun0.setFontSize(12);//设置大小
        headRun0.setBold(true);//是否粗体
        headRun0.setFontFamily("华文中宋");
        //给列中的内容设置样式
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        if("职务".equals(content)){
            tblWidth.setW(new BigInteger("2050"));//设置列宽度
        }else{
            tblWidth.setW(new BigInteger("1600"));//设置列宽度
        }
        tblWidth.setType(STTblWidth.DXA);
    }
    //设置表头和单位信息样式
    public static void TitleCelldata1(XWPFTableCell cell,String content){
        //给当前列中添加段落，就是给列添加内容
        XWPFParagraph p = cell.getParagraphs().get(0);
        XWPFRun headRun0 = p.createRun();
        headRun0.setText(content);//设置内容
        headRun0.setFontSize(12);//设置大小
        headRun0.setBold(true);//是否粗体
        headRun0.setFontFamily("华文中宋");
        //给列中的内容设置样式
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setW(new BigInteger(1600*6+800+450+""));//设置列宽度
        tblWidth.setType(STTblWidth.DXA);
        cell.setColor("DCDCDC");
    }

    //将数据赋值到每一列上
    public static void CellData(Map<String,Object> m,XWPFTableCell cell,Integer ic){
        switch(ic){
            case 1:
                dataCelldata(cell, m.get("bm")==null?"无":m.get("bm").toString(),"bm");
                break;
            case 2:
                dataCelldata(cell, m.get("mc")==null?"无":m.get("mc").toString(),"mc");
                break;
            case 3:
                dataCelldata(cell, m.get("zw")==null?"无":m.get("zw").toString(),"zw");
                break;
            case 4:
                dataCelldata(cell, m.get("bgdh")==null?"无":m.get("bgdh").toString(),"bgdh");
                break;
            case 5:
                dataCelldata(cell, m.get("sjhm")==null?"无":m.get("sjhm").toString(),"sjhm");
                break;
            case 6:
                dataCelldata(cell, m.get("yx")==null?"无":m.get("yx").toString(),"yx");
                break;
        }
    }

    //手动构造数据
    public static List<Map<String,Object>> data(){
        List<Map<String,Object>>  list = new ArrayList<Map<String,Object>>();

        Map<String,Object> m1 = new HashMap<String,Object>();
        m1.put("dwmc", "测试部门1");
        List<Map<String,Object>>  list1 = new ArrayList<Map<String,Object>>();
        for(int i=0;i<5;i++){
            Map<String,Object> d1 = new HashMap<String,Object>();
            d1.put("bm", "部门"+i);//部门
            d1.put("mc", "名称"+i);//名称
            d1.put("zw", "职务"+i);//职务
            d1.put("bgdh", "办公电话"+i);//办公电话
            d1.put("sjhm", "手机号码"+i);//手机号码
            d1.put("yx", "邮箱"+i);//邮箱
            d1.put("xh", i+1);//序号
            list1.add(d1);
        }
        m1.put("data", list1);

        Map<String,Object> m2 = new HashMap<String,Object>();
        m2.put("dwmc", "测试部门2");
        List<Map<String,Object>>  list2 = new ArrayList<Map<String,Object>>();
        for(int i=0;i<3;i++){
            Map<String,Object> d1 = new HashMap<String,Object>();
            d1.put("bm", "部门"+i);//部门
            d1.put("mc", "名称"+i);//名称
            d1.put("zw", "职务"+i);//职务
            d1.put("bgdh", "办反反复复烦烦烦烦烦烦烦烦烦烦烦烦烦烦烦吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼吼公电话"+i);//办公电话
            d1.put("sjhm", "手机号码"+i);//手机号码
            d1.put("yx", "邮箱"+i);//邮箱
            d1.put("xh", i+1);//序号
            list2.add(d1);
        }
        m2.put("data", list2);

        Map<String,Object> m3 = new HashMap<String,Object>();
        m3.put("dwmc", "测试部门3");
        List<Map<String,Object>>  list3 = new ArrayList<Map<String,Object>>();
        for(int i=0;i<2;i++){
            Map<String,Object> d1 = new HashMap<String,Object>();
            d1.put("bm", "部门"+i);//部门
            d1.put("mc", "名称"+i);//名称
            d1.put("zw", "职务"+i);//职务
            d1.put("bgdh", "办公电话"+i);//办公电话
            d1.put("sjhm", "手机号码"+i);//手机号码
            d1.put("yx", "邮箱"+i);//邮箱
            d1.put("xh", i+1);//序号
            list3.add(d1);
        }
        m3.put("data", list3);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        return list;
    }

    /**
     * insert Picture
     * @param document
     * @param filePath
     * @param inline
     * @param width
     * @param height
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     */
    private static void insertPicture(XWPFDocument document, String filePath,CTInline inline, int width, int height) throws InvalidFormatException,
        FileNotFoundException {
        document.addPictureData(new FileInputStream(filePath),XWPFDocument.PICTURE_TYPE_PNG);
        int id = document.getAllPictures().size() - 1;
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        // String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
        // blipId =document.addPictureData(fastDFSClient.download(src),Document.PICTURE_TYPE_PNG);

        //String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
        String blipId = document.getAllPackagePictures().get(id).getRelationId(document.getAllPackagePictures().get(id).getParent());
        String picXml = getPicXml(blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);
        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("IMG_" + id);
        docPr.setDescr("IMG_" + id);
    }

    /**
     * get the xml of the picture
     * @param blipId
     * @param width
     * @param height
     * @return
     */
    private static String getPicXml(String blipId, int width, int height) {
        String picXml =
            "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">" +
                "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">" +
                "         <pic:nvPicPr>" + "            <pic:cNvPr id=\"" + 0 +
                "\" name=\"Generated\"/>" + "            <pic:cNvPicPr/>" +
                "         </pic:nvPicPr>" + "         <pic:blipFill>" +
                "            <a:blip r:embed=\"" + blipId +
                "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>" +
                "            <a:stretch>" + "               <a:fillRect/>" +
                "            </a:stretch>" + "         </pic:blipFill>" +
                "         <pic:spPr>" + "            <a:xfrm>" +
                "               <a:off x=\"0\" y=\"0\"/>" +
                "               <a:ext cx=\"" + width + "\" cy=\"" + height +
                "\"/>" + "            </a:xfrm>" +
                "            <a:prstGeom prst=\"rect\">" +
                "               <a:avLst/>" + "            </a:prstGeom>" +
                "         </pic:spPr>" + "      </pic:pic>" +
                "   </a:graphicData>" + "</a:graphic>";
        return picXml;
    }

    public static void main(String[] args) throws Exception {
        final XWPFDocument document= initDoc(data());
        final FileOutputStream out = new FileOutputStream("C:\\部门通讯录.docx");
        document.write(out);
        out.close();
        System.out.println("导出成功!!!!!!!!");
    }

    private static void writeWord(){
        final XWPFDocument doc = new XWPFDocument();//创建新文档
        try {
            final FileOutputStream out = new FileOutputStream("C:\\simple.docx");//生成文档
            final XWPFParagraph paragraph = doc.createParagraph();//创建新段落
            //XWPFRun是段落的基本组成单元，它可以是一个文本，也可以是一张图片。

            // 段落起始插入XWPFRun
            final XWPFRun insertNewRun = paragraph.insertNewRun(0);
            insertNewRun.setText("在段落起始位置插入这段文本");

            // 段落末尾创建XWPFRun
            final XWPFRun run = paragraph.createRun();
            run.setText("为这个段落追加文本");

            // 颜色
            run.setColor("00ff00");
            // 粗体
            run.setBold(true);

            //文本换行
            run.addCarriageReturn();

            //修改XWPFRun文本
            final List<XWPFRun> runs = paragraph.getRuns();
            // setText默认为追加文本，参数0表示设置第0个位置的文本，覆盖上一次设置
            runs.get(0).setText("追加文本", 0);
            runs.get(0).setText("修改文本", 0);

            final InputStream stream = new FileInputStream("E:\\Images\\Images\\win7.jpg");
            final XWPFRun runImage = paragraph.createRun();
            runImage.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(256), Units.toEMU(256));

            //创建新表格,创建一个三行三列的表格：--------------------------------------------------------

            final XWPFTable table = doc.createTable(3,3);
            //设置单元格文本,表格是由表格行XWPFRow构成，每行是由单元格XWPFCell构成，每个单元格内部又是由许多XWPFParagraph段落构成。

            table.getRow(1).getCell(1).setText("我是内容啊");

            //上面这一段代码和下面这一段代码是等价的：

            final XWPFParagraph p1 = table.getRow(0).getCell(0).addParagraph();
            final XWPFRun r1 = p1.createRun();
            r1.setText("我是内容");


            doc.write(out);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static void word(final String fileName,final HttpServletResponse response) throws Exception{
        final XWPFDocument doc = new XWPFDocument();//创建新文档
        final XWPFParagraph paragraph = doc.createParagraph();//创建新段落
        //XWPFRun是段落的基本组成单元，它可以是一个文本，也可以是一张图片。

        // 段落起始插入XWPFRun
        final XWPFRun insertNewRun = paragraph.insertNewRun(0);
        //final XWPFRun runDescribe = paragraph.insertNewRun(0);//保留
        insertNewRun.setText("在段落起始位置插入这段文本");

        // 段落末尾创建XWPFRun
        final XWPFRun run = paragraph.createRun();
        run.setText("为这个段落追加文本");

        // 颜色
        run.setColor("00ff00");
        // 粗体
        run.setBold(true);

        //文本换行
        run.addCarriageReturn();

        //修改XWPFRun文本
        final List<XWPFRun> runs = paragraph.getRuns();
        // setText默认为追加文本，参数0表示设置第0个位置的文本，覆盖上一次设置
        runs.get(0).setText("追加文本", 0);
        runs.get(0).setText("修改文本", 0);

        final InputStream stream = new FileInputStream("E:\\Images\\Images\\win7.jpg");
        final XWPFRun runImage = paragraph.createRun();
        runImage.addPicture(stream, XWPFDocument.PICTURE_TYPE_PNG, "Generated", Units.toEMU(256), Units.toEMU(256));

        //创建新表格,创建一个三行三列的表格：--------------------------------------------------------

        final XWPFTable table = doc.createTable(3,3);
        //设置单元格文本,表格是由表格行XWPFRow构成，每行是由单元格XWPFCell构成，每个单元格内部又是由许多XWPFParagraph段落构成。

        table.getRow(1).getCell(1).setText("EXAMPLE OF TABLE");

        //上面这一段代码和下面这一段代码是等价的：

        final XWPFParagraph p1 = table.getRow(0).getCell(0).addParagraph();
        final XWPFRun r1 = p1.createRun();
        r1.setText("文档下载");
        //downloadWord(doc,fileName,response);
    }


}