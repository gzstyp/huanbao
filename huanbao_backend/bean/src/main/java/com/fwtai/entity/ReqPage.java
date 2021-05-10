package com.fwtai.entity;

import com.fwtai.config.ConfigFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 带搜索功能且支持排序的数据获取
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-07-10 10:53
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@ApiModel("列表数据搜索功能的分页参数且支持单字段排序")
public final class ReqPage{

    @ApiModelProperty(notes = "请求的第几页的数据,当前页",required = true,value = "当前页,默认为1")
    private Integer current = 1;

    @ApiModelProperty(notes = "每页大小,每页显示的数据量",required = true,value = "每页大小,默认为10,每页显示的数据量,最大不能超过50")
    private Integer pageSize = 10;

    @ApiModelProperty(notes = "排序关键字,升序ASC或降序DESC",required = false,value = "排序关键字,升序ASC或降序DESC,没有默认值,不填时不排序")
    private String order;

    @ApiModelProperty(notes = "排序字段|排序的列",required = false,value = "排序字段|排序的列,不填时不排序")
    private String column;

    public Integer getCurrent(){
        if(current <= 0) return 1;
        return current;
    }

    public void setCurrent(final Integer current){
        if(current < 1){
            this.current = 1;
        }else{
            this.current = current;
        }
    }

    public Integer getPageSize(){
        return pageSize;
    }

    public void setPageSize(final Integer pageSize){
        if(pageSize > ConfigFile.size_api_default){
            this.pageSize = 10;
        }else{
            this.pageSize = pageSize;
        }
    }

    public String getOrder(){
        return order;
    }

    public void setOrder(String order){
        this.order = order;
    }

    public String getColumn(){
        return column;
    }

    public void setColumn(String column){
        this.column = column;
    }
}