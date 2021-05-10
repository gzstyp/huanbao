package com.fwtai.bean;

import java.util.ArrayList;

/**
 * 文件上传对象
 * @注意 错误信息，先判断是否有错误信息，再做业务处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-07-06 9:36
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class UploadObject{

    /*错误信息[先判断是否有错误信息，再做业务处理]*/
    private String errorMsg;
    /*除了文件之外的表单参数*/
    private PageFormData params;
    /*上传的文件列表*/
    private ArrayList<UploadFile> listFiles;

    public String getErrorMsg(){
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg){
        this.errorMsg = errorMsg;
    }

    public PageFormData getParams(){
        return params;
    }

    public void setParams(PageFormData params){
        this.params = params;
    }

    public ArrayList<UploadFile> getListFiles(){
        return listFiles;
    }

    public void setListFiles(ArrayList<UploadFile> listFiles){
        this.listFiles = listFiles;
    }
}