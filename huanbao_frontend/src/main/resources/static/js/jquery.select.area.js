;(function($){
    var clsProvince = '.clsProvince';//省|市
    var clsCity = '.clsCity';//地州市
    var clsCounty = '.clsCounty';//区|县
    var clsTowns = '.clsTowns';//乡|镇|社区
    var clsVallage = '.clsVallage';//村|居委会
    var clsXxx = '.clsXxx';//组|街道
    window.selectArea = {
        renderSelect(data,containerDom,selectDom,labelText){
            var html = "<option value=''>"+labelText+"</option>";
            $.each(data,function(index,item){
                html += "<option value='"+item.id+"'>"+item.name+"</option>";
            });
            $(containerDom +' '+ selectDom).html(html);
            this.displayShow(containerDom,selectDom);
            this.displayHide(containerDom,selectDom);
            this.clearValue(containerDom,selectDom);
        },
        displayShow : function(containerDom,selectDom){
            $(containerDom +' '+ selectDom).css({"display":"inline"});
        },
        displayHide : function(containerDom,selectDom){
            if(selectDom == clsProvince){
                $(containerDom +' '+ clsCity).css({"display":"none"});
                $(containerDom +' '+ clsCounty).css({"display":"none"});
                $(containerDom +' '+ clsTowns).css({"display":"none"});
                $(containerDom +' '+ clsVallage).css({"display":"none"});
                $(containerDom +' '+ clsXxx).css({"display":"none"});
            }else if(selectDom == clsCity){
                $(containerDom +' '+ clsCounty).css({"display":"none"});
                $(containerDom +' '+ clsTowns).css({"display":"none"});
                $(containerDom +' '+ clsVallage).css({"display":"none"});
                $(containerDom +' '+ clsXxx).css({"display":"none"});
            }else if(selectDom == clsCounty){
                $(containerDom +' '+ clsTowns).css({"display":"none"});
                $(containerDom +' '+ clsVallage).css({"display":"none"});
                $(containerDom +' '+ clsXxx).css({"display":"none"});
            }else if(selectDom == clsTowns){
                $(containerDom +' '+ clsVallage).css({"display":"none"});
                $(containerDom +' '+ clsXxx).css({"display":"none"});
            }else if(selectDom == clsVallage || selectDom == clsXxx){
                $(containerDom +' '+ clsXxx).css({"display":"none"});
            }
        },
        displayEmpty : function(containerDom,selectDom){
            var html = "<option value=''>暂无选项</option>";
            if(selectDom == clsProvince){
                $(containerDom +' '+ clsProvince).html(html);
                $(containerDom +' '+ clsCity).html(html);
                $(containerDom +' '+ clsCounty).html(html);
                $(containerDom +' '+ clsTowns).html(html);
                $(containerDom +' '+ clsVallage).html(html);
                $(containerDom +' '+ clsXxx).html(html);
            }else if(selectDom == clsCity){
                $(containerDom +' '+ clsCity).html(html);
                $(containerDom +' '+ clsCounty).html(html);
                $(containerDom +' '+ clsTowns).html(html);
                $(containerDom +' '+ clsVallage).html(html);
                $(containerDom +' '+ clsXxx).html(html);
            }else if(selectDom == clsCounty){
                $(containerDom +' '+ clsCounty).html(html);
                $(containerDom +' '+ clsTowns).html(html);
                $(containerDom +' '+ clsVallage).html(html);
                $(containerDom +' '+ clsXxx).html(html);
            }else if(selectDom == clsTowns){
                $(containerDom +' '+ clsTowns).html(html);
                $(containerDom +' '+ clsVallage).html(html);
                $(containerDom +' '+ clsXxx).html(html);
            }else if(selectDom == clsVallage){
                $(containerDom +' '+ clsVallage).html(html);
                $(containerDom +' '+ clsXxx).html(html);
            }else if(selectDom == clsXxx){
                $(containerDom +' '+ clsXxx).html(html);
            }
        },
        clearValue : function(containerDom,selectDom){
            if(selectDom == clsProvince){
                $(containerDom +' '+ clsCity).val('');
                $(containerDom +' '+ clsCounty).val('');
                $(containerDom +' '+ clsTowns).val('');
                $(containerDom +' '+ clsVallage).val('');
                $(containerDom +' '+ clsXxx).val('');
            }else if(selectDom == clsCity){
                $(containerDom +' '+ clsCounty).val('');
                $(containerDom +' '+ clsTowns).val('');
                $(containerDom +' '+ clsVallage).val('');
                $(containerDom +' '+ clsXxx).val('');
            }else if(selectDom == clsCounty){
                $(containerDom +' '+ clsTowns).val('');
                $(containerDom +' '+ clsVallage).val('');
                $(containerDom +' '+ clsXxx).val('');
            }else if(selectDom == clsTowns){
                $(containerDom +' '+ clsVallage).val('');
                $(containerDom +' '+ clsXxx).val('');
            }else if(selectDom == clsVallage || selectDom == clsXxx){
                $(containerDom +' '+ clsXxx).val('');
            }
        },
        //仅显示‘选择省|市’,隐藏其所有子节点
        resetAreaData : function(containerDom){
            this.clearValue(containerDom,clsProvince);
            $(containerDom +' '+ clsProvince).val('');
            this.displayHide(containerDom,clsProvince);
            //$(containerDom +' '+ clsProvince).css({"display":"none"});弹出框时取消其注释在没有操作权限时会导致排版错乱
        },
        getData : function(url,pid,containerDom,selectDom,labelText){
            if(selectDom == clsProvince){
                this.resetAreaData(containerDom);
            }
            if(pid == null || pid == ''){
                this.displayHide(containerDom,selectDom);
                this.clearValue(containerDom,selectDom);
                $(containerDom +' '+ selectDom).val('');
                $(containerDom +' '+ selectDom).css({"display":"none"});
                return;
            }
            var _self = this;
            layerFn.queryGetHintResult(url,{pId : pid},function(data){
                if(data.code === AppKey.code.code200){
                    _self.renderSelect(data.data,containerDom,selectDom,labelText);
                }else if(data.code === AppKey.code.code201){
                    _self.displayHide(containerDom,selectDom);
                    _self.clearValue(containerDom,selectDom);
                    $(containerDom +' '+ selectDom).val('');
                    $(containerDom +' '+ selectDom).css({"display":"none"});
                }else{
                    _self.displayEmpty(containerDom,selectDom);
                    _self.displayHide(containerDom,selectDom);
                    $(containerDom +' '+ selectDom).find("option:selected").text(data.msg);
                }
            });
        }
	};
})(jQuery);