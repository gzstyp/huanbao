;(function($){
    var clsProvince = '.clsProvince';//省|市
    var clsCity = '.clsCity';//地州市
    var clsCounty = '.clsCounty';//区|县
    var clsTowns = '.clsTowns';//乡|镇|社区
    var clsVallage = '.clsVallage';//村|居委会
    var clsXxx = '.clsXxx';//组|街道
    var areaLevel = sessionStorage.getItem("areaLevel");
    window.userArea = {
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
                this.level2Css(containerDom);
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
        level2Css : function(containerDom){
            $(containerDom +' '+ clsCounty).css({"display":"none"});
            $(containerDom +' '+ clsTowns).css({"display":"none"});
            $(containerDom +' '+ clsVallage).css({"display":"none"});
            $(containerDom +' '+ clsXxx).css({"display":"none"});
        },
        level2Value : function(containerDom){
            $(containerDom +' '+ clsCounty).val('');
            $(containerDom +' '+ clsTowns).val('');
            $(containerDom +' '+ clsVallage).val('');
            $(containerDom +' '+ clsXxx).val('');
        },
        clearValue : function(containerDom,selectDom){
            if(selectDom == clsProvince){
                $(containerDom +' '+ clsCity).val('');
                $(containerDom +' '+ clsCounty).val('');
                $(containerDom +' '+ clsTowns).val('');
                $(containerDom +' '+ clsVallage).val('');
                $(containerDom +' '+ clsXxx).val('');
            }else if(selectDom == clsCity){
                this.level2Value(containerDom);
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
        //仅显示‘选择省|市’
        resetAreaData : function(containerDom){
            this.clearValue(containerDom,clsProvince);
            $(containerDom +' '+ clsProvince).val('');
            this.displayHide(containerDom,clsProvince);
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
                if(data.code == AppKey.code.code200){
                    _self.renderSelect(data.data,containerDom,selectDom,labelText);
                }else if(data.code == AppKey.code.code201){
                    _self.displayHide(containerDom,selectDom);
                    _self.clearValue(containerDom,selectDom);
                    $(containerDom +' '+ selectDom).val('');
                    $(containerDom +' '+ selectDom).css({"display":"none"});
                }else{
                    _self.displayHide(containerDom,selectDom);
                    $(containerDom +' '+ selectDom).find("option:selected").text(data.msg);
                }
            });
        },
        renderUserArea : function(url,containerDom){
            var pid = '0';//这个不能写成 0 ,否则有问题
            var level = sessionStorage.getItem("areaLevel");
            var provinceId = sessionStorage.getItem("areaProvince");
            var cityId = sessionStorage.getItem("areaCity");
            var selectDom = '';
            var html = '';
            var labelText = '请选择省|市';
            if(level == 8){
                pid = '0';
                selectDom = '.clsProvince';
                labelText = '请选择省|市';
                html += '<select class="clsProvince" style="display:none;cursor:pointer;" onchange="userArea.selectChange(this.value,\''+url+'\',\''+containerDom+'\',\'.clsCity\',\'选择地州市\');"></select>';
                html += '<select class="clsCity" style="display:none;cursor:pointer;" onchange="userArea.selectChange(this.value,\''+url+'\',\''+containerDom+'\',\'.clsCounty\',\'请选择区|县\');"></select>';
                html += '<select class="clsCounty" style="display:none;cursor:pointer;"><option>选择区|县</option></select>';
            }
            if(level ==1){//显示2,3
                pid = provinceId;
                selectDom = '.clsCity';
                labelText = '请选择地州市';
                html += '<select class="clsCity" style="display:none;cursor:pointer;" onchange="userArea.selectChange(this.value,\''+url+'\',\''+containerDom+'\',\'.clsCounty\',\'请选择区|县\');"></select>';
                html += '<select class="clsCounty" style="display:none;cursor:pointer;"></select>';
            }else if(level ==2){//显示3
                pid = cityId;
                selectDom = '.clsCounty';
                labelText = '请选择区|县';
                html += '<select class="clsCounty" style="display:none;cursor:pointer;"></select>';
            }else if(level==3){//不显示
                $('.areaSelectText').remove();
            }
            $(containerDom).html(html);
            this.getData(url,pid,containerDom,selectDom,labelText);
        },
        selectChange : function(value,url,containerDom,subDom,labelText){
            this.getData(url,value,containerDom,subDom,labelText);
        }
	};
})(jQuery);