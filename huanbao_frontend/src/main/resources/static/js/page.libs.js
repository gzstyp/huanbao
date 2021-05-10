;(function(){
    //不带请求动画少后面两个参数,仅走select查询方法
    function getQuery(url,params,success){
        ajaxRequestGet(url,params,null,function(data){
            success(data);
        });
    }
    //含带请求动画,方法beforeSend不为空,仅走select查询方法
    function getQueryHint(url,params,success,msg){
        ajaxRequestGet(url,params,function(request){
            msg = (msg == null || msg == '') ? "正在处理……" :msg;
            self.layerIndex = layerFn.loading(msg);
        },function(data){
            layerFn.closeIndex(self.layerIndex);
            success(data);
        },function(response,err){
            layerFn.closeIndex(self.layerIndex);
        },function(){
            layerFn.closeIndex(self.layerIndex);
        });
    }
    //不带请求动画少后面两个参数,仅走insert,update,delete方法
    function postRow(url,params,success){
        ajaxRequestPost(url,params,null,function(data){
            success(data);
        });
    }
    //有带请求动画,方法beforeSend不为空,仅走insert,update,delete方法
    function postRowHint(url,params,success,msg){
        ajaxRequestPost(url,params,function(request){
            msg = (msg == null || msg == '') ? "正在处理……" :msg;
            self.layerIndex = layerFn.loading(msg);
        },function(data){
            layerFn.closeIndex(self.layerIndex);
            success(data);
        },function(response,err){
            layerFn.closeIndex(self.layerIndex);
        },function(){
            layerFn.closeIndex(self.layerIndex);
        });
    }
    //后面的两个参数是给带请求动画用的
    function ajaxRequestGet(url,params,beforeSend,success,error,complete){
        ajaxGet(url,params,function(request){
            if(beforeSend){
                beforeSend(request);
            }
        },function(data){
            success(data);
        },function(response,err){
            //处理各种错误
        },function(response,status){
            if(complete){
                complete(response,status);
            }
            //这里处理是否需要更新token
        });
    }
    //后面的两个参数是给带请求动画用的
    function ajaxRequestPost(url,params,beforeSend,success,error,complete){
        ajaxPost(url,params,function(request){
            if(beforeSend){
                beforeSend(request);
            }
        },function(data){
            success(data);
        },function(response,err){
            //处理各种错误
        },function(response,status){
            if(complete){
                complete(response,status);
            }
            //这里处理是否需要更新token
        });
    }
    //ajax的GET请求
    function ajaxGet(url,params,beforeSend,success,error,complete){
        ajaxOpt({
            url : url,
            params : params,
            beforeSend : function(request){
                if(beforeSend !=null && beforeSend != undefined && beforeSend != ''){
                    beforeSend(request);
                }
            },
            success : function(data){
                success(data);
            },
            error : function(response,err){
                if(error !=null && error != undefined && error != ''){
                    error(response,err);
                }
            },
            complete : function(response,status){
                if(complete !=null && complete != undefined && complete != ''){
                    complete(response,status);
                }
            }
        });
    }
    //ajax的POST请求
    function ajaxPost(url,params,beforeSend,success,error,complete){
        ajaxOpt({
            type : 'POST',
            url : url,
            params : params,
            beforeSend : function(request){
                if(beforeSend !=null && beforeSend != undefined && beforeSend != ''){
                    beforeSend(request);
                }
            },
            success : function(data){
                success(data);
            },
            error : function(response,err){
                if(error !=null && error != undefined && error != ''){
                    error(response,err);
                }
            },
            complete : function(response,status){
                if(complete !=null && complete != undefined && complete != ''){
                    complete(response,status);
                }
            }
        });
    }
    //若 type为空则是GET请求,用法:ajaxOpt({type:'POST'});
    function ajaxOpt(options){
        var type = options.type || 'GET';
        var url = options.url;
        var params = options.params || {};
        var beforeSend = options.beforeSend || null;
        var success = options.success;
        var error = options.error || null;
        var complete = options.complete || null;
        $.ajax({
            type : type,
            url : urlPrefix + url,
            //xhrFields : {withCredentials: true},
            //crossDomain : true,
            headers : {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem("refreshToken") || ''},
            dataType : "json",
            data : params,
            beforeSend : function(request){
                if(beforeSend !=null && beforeSend != undefined && beforeSend != ''){
                    beforeSend(request);
                }
            },
            success : function(data){
                if(success !=null && success != undefined && success != ''){
                    success(data);
                }
            },
            error : function(response,err){
                if(error !=null && error != undefined && error != ''){
                    error(response,err);
                }
            },
            complete : function(response,status){
                if(complete !=null && complete != undefined && complete != ''){
                    complete(response,status);
                }
            }
        });
    }
})(jQuery);