package com.fwtai.bean;

/**
 * 文件上传参数
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-07-06 9:02
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class UploadFile{

    /**原文件名*/
    private String originalName;
    /**文件名*/
    private String fileName;
    /**文件的完整路径|全路径*/
    private String fullPath;
    /**文件上传的跟目录*/
    private String basePath;
    /**url访问路径,不含前缀*/
    private String urlFile;
    /**含http全路径访问的url*/
    private String httpUri;
    /**文件域的表单字段名*/
    private String name;

    public String getOriginalName(){
        return originalName;
    }

    public void setOriginalName(final String originalName){
        this.originalName = originalName;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(final String fileName){
        this.fileName = fileName;
    }

    public String getFullPath(){
        return fullPath;
    }

    public void setFullPath(final String fullPath){
        this.fullPath = fullPath;
    }

    public String getBasePath(){
        return basePath;
    }

    public void setBasePath(final String basePath){
        this.basePath = basePath;
    }

    public String getUrlFile(){
        return urlFile;
    }

    public void setUrlFile(final String urlFile){
        this.urlFile = urlFile;
    }

    public String getHttpUri(){
        return httpUri;
    }

    public void setHttpUri(final String httpUri){
        this.httpUri = httpUri;
    }

    public String getName(){
        return name;
    }

    public void setName(final String name){
        this.name = name;
    }
}