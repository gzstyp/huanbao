;(function($){
    //仅作备份参考,使用本js需要改写权限业务
    var clsProvince = '.clsProvince';//省|市
    var clsCity = '.clsCity';//地州市
    var clsCounty = '.clsCounty';//区|县
    var clsTowns = '.clsTowns';//乡|镇|社区
    var clsVallage = '.clsVallage';//村|居委会
    var clsXxx = '.clsXxx';//组|街道
    window.jQSelect = {
        renderSelect(data,containerDom,selectDom,labelText){
            var html = "<option value=''>"+labelText+"</option>";
            $.each(data,function(index,item){
                html += "<option value='"+item.id+"'>"+item.name+"</option>";
            });
            $(containerDom +' '+ selectDom).html(html);
            this.displayShow(containerDom,selectDom);
            this.displayHide(containerDom,selectDom);
            this.setValue(containerDom,selectDom);
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
        setValue : function(containerDom,selectDom){
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
            this.setValue(containerDom,clsProvince);
            $(containerDom +' '+ clsProvince).val('');
            this.displayHide(containerDom,clsProvince);
            $(containerDom +' '+ clsProvince).css({"display":"none"});
        },
        getAreaData : function(pid,containerDom,selectDom,labelText){
            if(selectDom == clsProvince){
                this.resetAreaData(containerDom);
            }
            if(pid == null || pid == ''){
                this.displayHide(containerDom,selectDom);
                this.setValue(containerDom,selectDom);
                $(containerDom +' '+ selectDom).val('');
                $(containerDom +' '+ selectDom).css({"display":"none"});
                return;
            }
            var _self = this;
            self.layerIndex = layerFn.loading('正在加载……');
            $.ajax({
                type : "GET",
                url : urlPrefix + '/user/queryAreaSelect',
                dataType : "json",
                data : {pId : pid},
                headers : {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem('refreshToken') || ''},//好使
                crossDomain: true == !(document.all),
                success : function(data){
                    if(data.code == AppKey.code.code200){
                        _self.renderSelect(data.data,containerDom,selectDom,labelText);
                    }else if(data.code == AppKey.code.code205){
                        layerFn.tokenLogin();
                    }
                },
                error : function(response,status){},
                statusCode : {
                    401 : function(response){
                        layerFn.handleClose("没有操作权限");
                    },
                    404 : function(response){
                        var json = eval('('+ response.responseText +')');
                        layerFn.handleClose("请求("+json.path+")路径不存在");
                    },
                    500 : function(response){
                        layerFn.handleClose("系统出现错误,稍候重试");
                    },
                    502 : function(response){
                        layerFn.handleClose("网关代理出错,联系管理员");
                    }
                },
                complete : function(response,status){
                    layerFn.closeIndex(self.layerIndex);
                }
            });
        }
	};
})(jQuery);