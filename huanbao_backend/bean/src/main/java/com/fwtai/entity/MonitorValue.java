package com.fwtai.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public final class MonitorValue implements Serializable{

    @ApiModelProperty(notes = "监测设备标识",required = true,value = "设备标识(MN设备唯一编码)")
    private String deviceFlag;

    @ApiModelProperty(notes = "数据时间",required = true,value = "数据时间精确到秒,格式:2021-5-12 13:14:16")
    private String dataTime;

    @ApiModelProperty(notes = "温度",required = true,value = "温度,格式:16.52")
    private String temperature;

    @ApiModelProperty(notes = "湿度",required = true,value = "湿度,格式:51.20")
    private String humidity;

    @ApiModelProperty(notes = "空气质量规格pm2.5",required = true,value = "空气质量规格pm2.5,格式:98.50")
    private String quality25;

    @ApiModelProperty(notes = "空气质量规格pm10",required = true,value = "空气质量规格pm10,格式:126.90")
    private String quality10;

    @ApiModelProperty(notes = "噪声值",required = true,value = "噪声值,格式:58.90")
    private String volume;

    public String getDeviceFlag(){
        return deviceFlag;
    }

    public void setDeviceFlag(String deviceFlag){
        this.deviceFlag = deviceFlag;
    }

    public String getDataTime(){
        return dataTime;
    }

    public void setDataTime(String dataTime){
        this.dataTime = dataTime;
    }

    public String getTemperature(){
        return temperature;
    }

    public void setTemperature(String temperature){
        this.temperature = temperature;
    }

    public String getHumidity(){
        return humidity;
    }

    public void setHumidity(String humidity){
        this.humidity = humidity;
    }

    public String getQuality25(){
        return quality25;
    }

    public void setQuality25(String quality25){
        this.quality25 = quality25;
    }

    public String getQuality10(){
        return quality10;
    }

    public void setQuality10(String quality10){
        this.quality10 = quality10;
    }

    public String getVolume(){
        return volume;
    }

    public void setVolume(String volume){
        this.volume = volume;
    }
}