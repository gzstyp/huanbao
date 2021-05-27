/**
 * 自定义公共js文件,含有通用的winFn;含有依赖jQuery验证的verifyFn;依赖layer的layerFn;
 * @作者 田应平
 * @QQ号 444141300
 * @原理 模块化,键值对,不管是方法还是属性都是键值对=或:,=是赋值;:是键值对
 * @Web  www.fwtai.com
 * @创建时间 2016年12月27日 00:54:53
*/
var refreshFlag = true;
;(function(){
    var pathName = window.document.location.pathname;
    var urlPath = window.document.location.href;
    var pos = urlPath.indexOf(pathName);
    var domain = urlPath.substring(0,pos);
    urlPrefix = 'http://192.168.3.108:901';//获取页面是不需要添加的,只是在ajax请求接口时才需要添加,注意个地方IE8++存在跨域问题
    var tokenUrl = 'accessToken='+(sessionStorage.getItem('accessToken') || '') + '&refreshToken=' + (sessionStorage.getItem('refreshToken') || '');
    var tokenStart = '?'+tokenUrl;
    var tokenParams = '&'+tokenUrl;
    window.AppKey = {
        baseUri : window.location.origin,/**AppKey.baseUri*/
        iconDir : 'js/layer/theme/default/',/**AppKey.iconDir*/
        confirm : '确定',/**AppKey.confirm*/
        close : '关闭',/**AppKey.close*/
        submit : '提交',/**AppKey.submit*/
        cancel : '取消',/**AppKey.cancel*/
        title : "系统提示",
        msg_error : "哦!网络故障请<a href='javascript:;' onclick='if(self==top){window.location.reload();}else{top.location.reload();}' style='text-decoration:none;color:#3b8cff;outline:none;cursor:pointer;'>刷新</a>或稍后重试",/**连接服务器失败*/
        msg_not_login : "未登录或登录超时,请<a href='javascript:;' onclick='if(self==top){window.location.reload();}else{top.location.reload();}' style='text-decoration:none;color:#3b8cff;outline:none;cursor:pointer;'>刷新</a>重试",/**稍后重试*/
        token_not_login : "未登录或登录超时,请<a href='/index.html' onclick='if(self==top){window.location.reload();}else{top.location.reload();}' style='text-decoration:none;color:#3b8cff;outline:none;cursor:pointer;'>登录</a>再重试",/**稍后重试*/
        notLogin : '未登录或登录超时',
        loginUrl : '/index.html',/**跳转到登录界面;用法:AppKey.loginUrl*/
        control : '/main.html',/**跳转到后台中心;用法:AppKey.control*/
        sysError : '系统出现错误',
        layerArea : '270px',/**AppKey.layerArea*/
        loadMsg : function(){
            var num = Math.random() * 10 + 1;/**随机生成1到10之间的数,包含1不包含10,即<=1 && =>9*/
            num = parseInt(num,10);
            if(num == 1){
                return '加载是件闹心事……';
            }else if(num == 2){
                return '感谢你的等待,请稍等……';
            }else if(num == 3){
                return '网络差,请稍等……';
            }else if(num == 4){
                return '正在载入数据……';
            }else if(num == 5){
                return '网络阻塞,努力加载中……';
            }else if(num == 6){
                return '服务器压力大,努力处理中……';
            }else if(num == 7){
                return '处理中,请稍等……';
            }else if(num == 8){
                return '数据正在赶来,请稍等……';
            }else{
                return '网络不给力,努力加载中……';
            }
        },
        code : {
            /**自定义code及msg*/
            code198 : 198,
            /**请求失败|操作失败*/
            code199 : 199,
            /**请求成功|操作成功*/
            code200 : 200,
            /**返回空值|暂无数据*/
            code201 : 201,
            /**请求协议参数不完整*/
            code202 : 202,
            /**密钥验证失败*/
            code203 : 203,
            /**系统出现错误*/
            code204 : 204,
            /**未登录或登录超时或token已失效*/
            code205 : 205,
            /**账号或密码不正确*/
            code206 : 206,
            /**非法操作!或你的账号已被删除,一般用于被迫强制退出登录*/
            code207 : 207,
            /**连接服务器失败*/
            code208 : 208,
            /**无权限操作*/
            code401 : 401
        },
        msg : {
            /**操作失败*/
            msg199 : '操作失败',
            /**操作成功*/
            msg200 : '操作成功',
            /**暂无数据*/
            msg201 : '暂无数据',
            /**参数不完整*/
            msg202 : '参数不完整',
            /**系统出现错误*/
            msg204 : '系统出现错误',
            /**未登录或登录超时*/
            msg205 : '未登录或登录超时',
            /**无权限操作*/
            msg401 : '无权限操作'
        }
    },
    /**通用的方法,依赖jQuery*/
    window.winFn = {
        /**用JS获取地址栏参数且已编码的方法[采用正则表达式获取地址栏参数],用法:winFn.getParamURI('id');推荐*/
        getParamURI : function(key){
            var reg = new RegExp("(^|&)"+ key +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);//search,查询?后面的参数,并匹配正则
            if(r!=null)return decodeURI(r[2]);return "";
        },
        /**用JS获取地址栏参数且已编码的方法[采用正则表达式获取地址栏参数],用法:winFn.getParamURL('id');*/
        getParamURL : function(key){
            var url = document.location.toString();
            var arrObj = url.split("?");
            if (arrObj.length > 1) {
                var arrPara = arrObj[1].split("&");
                var arr;
                for (var i = 0; i < arrPara.length; i++){
                    arr = arrPara[i].split("=");
                    if (arr != null && arr[0] == key){
                        return decodeURI(arr[1]);
                    }
                }
                return "";
            }else{
                return "";
            }
        },
        /**赋值:winFn.setDomValue(dom,value);*/
        setDomValue : function(dom,value){
            try{
                if (value == null || value == '' || value == undefined || value == 'undefined'){
                    $(dom).val('');
                    $(dom).textbox('setValue','');
                } else {
                    $(dom).val(value);
                    $(dom).textbox('setValue',value);
                }
            }catch(e){}
        },
        /**获取值:winFn.getDomValue(dom);if(value == null){}*/
        getDomValue : function(dom){
            var v = $(dom).val();
            if(v == null || v.length <= 0)return null;
            if(v == '_') return null;
            return $.trim(v);
        },
        /**强制获取焦点:winFn.focus('#xx');*/
        focus : function(dom){
            $(dom).focus();
        },
        /**无需判断是否有值,即不需要 if(data == 200);winFn.setDataSelect(defaultText,emptyText,'#id或.class',data,value,showText,select);一般第3个参数都是id，即可'#Xxx'*/
        setDataSelect:function(defaultText,emptyText,selectDom,data,value,showText,select){
            var htm = "<option value=''>"+defaultText+"</option>";
            if (data == null || data == '' || data == undefined || data.length <= 0){
                htm = "<option value=''>"+emptyText+"</option>";
                $(selectDom).attr('disabled','disabled');
                $(selectDom).attr('title',(emptyText == null || emptyText == '' || emptyText == undefined)?'暂无选项':emptyText);
            } else {
                $(selectDom).attr('title',(defaultText == null || defaultText == '' || defaultText == undefined)?'选择选项':defaultText);
                for(var i=0;i<data.length;i++){
                    htm+="<option value="+data[i][value]+">"+data[i][showText]+"</option>";
                }
                if($(selectDom).prop("disabled")){
                    $(selectDom).prop("disabled",false);
                }
            }
            $(selectDom).html(htm);
            if(!verifyFn.isEmpty(select) && data.length > 0){
                for(var i=0;i<data.length;i++){
                    if(data[i][value]==select){
                        $(selectDom).val(select);
                        break;
                    }
                }
            }
        },
        /**恢复下拉选项:winFn.setSelectReset(selectDom,defaultText);*/
        setSelectReset : function(selectDom,defaultText){
            var htm = "<option value=''>"+defaultText+"</option>";
            if($(selectDom).prop("disabled")){
                $(selectDom).prop("disabled",false);
            }
            $(selectDom).html(htm);
            $(selectDom).attr('title',defaultText);
        },
        /**只适用于最顶级的级联的更改事件:winFn.onchange(dom,onchangeCall);可以在成功加载list数据之后再调用本方法;*/
        onchange : function(dom,onchangeCall){
            $(dom).on('change',function(){
                onchangeCall($(dom).val());
            });
        },
        /**禁用滚动条:winFn.overflowHidden();*/
        overflowHidden : function(){
            $("body").eq(0).css("overflow","hidden");
        },
        /**启用滚动条:winFn.overflowAuto();*/
        overflowAuto : function(){
            $("body").eq(0).css("overflow","auto");
        },
        /**自定义正在加载效果,没遮罩层:winFn.ajaxLoading(dom,msg);*/
        ajaxLoading : function(dom,msg){
            if(msg == null || msg == '' || msg == undefined){
                $(dom).hide();
                $(dom+" span").text('');
            }else {
                $(dom).show();
                $(dom+" span").text(msg);
            }
        },
        /**两个时间比较:winFn.dateCompare(domStartTime,domEndTime);*/
        dateCompare : function(domStartTime,domEndTime){
            var start = DateTime.parse($("#"+domStartTime).val());
            var end = DateTime.parse($("#"+domEndTime).val());
            return start > end;
        },
        /**刷新验证码:winFn.refreshCode(domImage,"imgCode");其中imgCode是获取验证码的url,即标签为<img src="imgCode">*/
        refreshCode : function(domImage,url){
            $(domImage).attr("src",urlPrefix + url+"?date="+new Date().getTime());
        },
        /**显示分页条上的正在加载效果:winFn.pageBarShow();*/
        pageBarShow : function(){
            $(AppKey.LoadingImg).css({"display":"block"});
        },
        /**隐藏关闭分页条上的正在加载效果:winFn.pageBarHide();*/
        pageBarHide : function(){
            $(AppKey.LoadingImg).css({"display":"none"});
        },
        /**单个元素或节点赋值,不含html:winFn.setText(dom,value);*/
        setText : function(dom,value){
            $(dom).text(value);
        },
        /**用于批量操作选择;checkboxName是name的标识:winFn.checkboxBatch(checkboxName,flag);*/
        checkboxBatch : function(checkboxName,flag){
            var checked = document.getElementsByName(checkboxName);
            var ids = "";
            if(flag == null || flag == '' || flag == undefined){flag = ",";}
            for ( var i = 0; i < checked.length; i++){
                if (checked[i].checked){
                    if ($.trim(ids).length > 0)ids += flag;
                    ids += checked[i].value;
                }
            }
            var arr = new Array();
            arr = ids.split(flag);//转换成数组,然后获取数组的长度即可
            if(arr.length <= 0 || ids == ""){
                return null;
            }else {
                return ids;
            }
        },
        /**获取数组的长度:winFn.arrLength(arr,flag);winFn.arrLength(arr);*/
        arrLength : function(arr,flag){
            if(flag == null || flag == '' || flag == undefined)flag = ",";
            if(arr == null || arr == '')return 0;
            return arr.split(flag).length;
        },
        /**判断是否已选择复选框,用法:if(!winFn.checkboxSelect('#menu_is_select')){};*/
        checkboxSelect : function(selectDom){
            return $(selectDom).prop("checked");
        },
        /**以复选框的选择而全选否则以取消而全部取消选择;局部areaDom块,为空时是整个页面;winFn.checkDom(checkboxObj,checkboxName,areaDom);*/
        checkDom : function(checkboxObj,checkboxName,areaDom){
            $(checkboxObj).on("change", function(){
                var _self = this;
                if(areaDom != null && areaDom != ''){
                    $(areaDom + " input[name='"+checkboxName+"']").each(function(){
                        this.checked = _self.checked;
                    });
                }else{
                    $("input[name='"+checkboxName+"']").each(function(){
                        this.checked = _self.checked;
                    });
                }
            });
        },
        /**以复选框的选中或取消对应的操作:winFn.checkFn(checkboxObj,on,off);*/
        checkFn : function(checkboxObj,on,off){
            $(checkboxObj).on("change", function(){
                var _self = this;
                if (_self.checked){
                    on();
                } else {
                    off();
                }
            });
        },
        /**以复选框的选中显示input或取消隐藏input的操作,原生态的input输入框默认是隐藏:winFn.checkOnOff(checkboxObj,inputDom,on,off);*/
        checkOnOff : function(checkboxObj,inputDom,on,off){
            $(checkboxObj).on("change",function(){
                var _self = this;
                if (_self.checked){
                    $(inputDom).css({"visibility":"visible"});
                    $(inputDom).show();
                    on();
                } else {
                    $(inputDom).css({"visibility":"hidden"});
                    $(inputDom).hide();
                    off();
                }
            });
        },
        /**以复选框的选择或取消对应的起始时间到结束时间的开关:winFn.checkBetween(this,start,end);obj一般填写this; onchange="winFn.checkBetween(this,'#start','#end');"*/
        checkBetween : function(obj,start,end){
            $(start).textbox({
                disabled: !obj.checked
            });
            $(end).textbox({
                disabled: !obj.checked
            });
        },
        /**全部选择或全部取消操作:局部areaDom块,为空时是整个页面;checkboxName是name的标识;flag如果为true时全部选择,为false或其他则全部取消选择;用法:winFn.checkboxAll(checkboxName,flag,areaDom);*/
        checkboxAll : function(checkboxName,flag,areaDom){
            if(flag == null || flag == '' || flag == undefined){
                flag = false;
            }
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input[name='"+checkboxName+"']").each(function(){
                    $(this).prop("checked",flag);
                });
            }else{
                $(areaDom + " input[name='"+checkboxName+"']").each(function(){
                    $(this).prop("checked",flag);
                });
            }
        },
        /**反选复选框:局部areaDom块或整个页面的复选框的反选复选框;checkboxName是name的标识;用法:winFn.checkboxInverse(checkboxName,areaDom);*/
        checkboxInverse : function(checkboxName,areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input[name='"+checkboxName+"']").each(function(){
                    var b = $(this).prop("checked");
                    $(this).prop("checked",!b);
                });
            }else{
                $(areaDom + "input[name='"+checkboxName+"']").each(function(){
                    var b = $(this).prop("checked");
                    $(this).prop("checked",!b);
                });
            }
        },
        /**选择奇数的复选框;局部areaDom块,为空时是整个页面:winFn.checkboxOdd(areaDom);*/
        checkboxOdd : function(areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                $("input:checkbox").each(function (index){
                    if( index%2 == 0){
                        $("input:checkbox").eq(index).prop("checked",true);
                    }
                });
            }else{
                $(areaDom + "input:checkbox").each(function (index){
                    if( index%2 == 0){
                        $("input:checkbox").eq(index).prop("checked",true);
                    }
                });
            }
        },
        /**选中所有[even偶数]|[odd奇数](index从0开始);局部areaDom块,为空时是整个页面:winFn.checkboxFlag(flag,areaDom);*/
        checkboxFlag : function(flag,areaDom){
            if(areaDom == null || areaDom == '' || areaDom == undefined){
                if(flag != 'even' || flag != 'odd')return;
                if (flag == 'even'){
                    $("[type='checkbox']:even").prop("checked",'true');
                } else {
                    $("[type='checkbox']:odd").prop("checked",'true');
                }
            }else{
                if(flag != 'even' || flag != 'odd')return;
                if (flag == 'even'){
                    $(areaDom + "[type='checkbox']:even").prop("checked",'true');
                } else {
                    $(areaDom + "[type='checkbox']:odd").prop("checked",'true');
                }
            }
        },
        /**清空表单-用法:winFn.clearFormData('#sys_menu_type');*/
        clearFormData : function(formId){
            $(':input',formId).not(':button, :submit, :reset').val('').removeAttr('selected');
            var $_obj = $(formId).find("select");
            var count = $_obj.length;
            if (count > 0){
                $($_obj).each(function(i){
                    $(this).val('');
                });
            }
        },
        /**Js随机产生4个数字或字母:winFn.randomWord();*/
        randomWord : function(){
            var chars = ['1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','P','Q','R','S','T','U','V','W','X','Y','Z'];
            var res = "";
            for(var i = 0; i < 4 ; i ++){
                var id = Math.ceil(Math.random() * 31);//乘以长度 - 1 ;
                res += chars[id];
            }
            return res;
        },
        /**限制输入长度:winFn.limitLength(dom,count);*/
        limitLength : function(dom,count){
            var value = $(dom).val();
            $(dom).on('keyup',function(e){
                if(count < value.length){
                    e.preventDefault();
                    $(dom).val(value.substr(0,count));
                }
            });
        },
        /**获取下拉列表的所选值或文本*/
        select : {
            /**调用方法winFn.select.getText('#gender');*/
            getText:function(selectDom){
                if($(selectDom).val() == ''){
                    return '';
                }else{
                    return $(selectDom).find("option:selected").text();
                }
            },
            getValue:function(selectDom){
                if($(selectDom).val() == ''){
                    return '';
                }else{
                    return $(selectDom).val();
                }
            }
        },
        /**radio根据Value值设置Radio为选中状态;jquery给单选框的赋值;winFn.radioSetValue(radioName,value);winFn.radioSetValue('STATE',"${pd.STATE}");*/
        radioSetValue : function(radioName,value){
            $("input[name='"+radioName+"'][value="+value+"]").attr("checked",true);
        },
        /**radio取消选中状态;winFn.radioCancel(radioName);*/
        radioCancel : function(radioName){
            $("input:radio[name='"+radioName+"']").attr("checked",false);
        },
        /**获取单选框的值;winFn.getRadioValue(radioName);*/
        getRadioValue : function(radioName){
            var chkRadio = document.getElementsByName(radioName);
            for(var i = 0; i < chkRadio.length; i++){
                if(chkRadio[i].checked)
                    return chkRadio[i].value;
            }
        },
        /**为指定name的value的单选框的赋值,适用于iCheck组件;winFn.setRadioValue(radioName,value);*/
        setRadioValue : function(radioName,value){
            var chkRadio = document.getElementsByName(radioName);
            for(var i = 0; i < chkRadio.length; i++){
                if(chkRadio[i].value == value){
                    $(chkRadio[i]).iCheck('check');
                    break;
                }
            }
        },
        input : {
            /**启用编辑|打开[含下拉菜单或input]:winFn.input.on(inputDom);*/
            on : function(inputDom){
                $(inputDom).removeAttr("disabled");
                $(inputDom).removeAttr("style");
            },
            /**禁用|关闭|设置不可编辑或输入[含下拉菜单或input]*/
            off : function(inputDom){
                $(inputDom).attr("disabled","disabled");
                $(inputDom).css("background-color","#e2e2e2");
            }
        },
        /**初始化表格的行的鼠标滑动样式效果;调用:winFn.tableMouse('cls');*/
        tableMouse : function(cls){
            $("."+ cls + " tbody tr").mouseenter(function(){
                $(this).css({background : "#e6e6e6"});
                $(this).children('td').each(function(index, ele){
                    $(ele).css({color: "#000"});
                });
            }).mouseleave(function(){
                $(this).css({background : "#fff"});
                $(this).children('td').each(function(index, ele){
                    $(ele).css({color: "#383838"});
                });
            });
        },
        /**获取浏览器可视区域宽度,兼容主流浏览器:winFn.fnGetWidth();*/
        fnGetWidth : function(){
            var viewportwidth = 0;
            if (typeof window.innerWidth != 'undefined'){
                viewportwidth = window.innerWidth;
            }else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0){
                viewportwidth = document.documentElement.clientWidth;
            }else {
                viewportwidth = document.getElementsByTagName('body')[0].clientWidth;
            }
            return viewportwidth;
        },
        /**获取浏览器可视区域高度,兼容主流浏览器:winFn.fnGetHeight();*/
        fnGetHeight : function(){
            var viewportheight = 0;
            if (typeof window.innerWidth != 'undefined'){
                viewportheight = window.innerHeight;
            }else if (typeof document.documentElement != 'undefined' && typeof document.documentElement.clientWidth != 'undefined' && document.documentElement.clientWidth != 0){
                viewportheight = document.documentElement.clientHeight;
            }else {
                viewportheight = document.getElementsByTagName('body')[0].clientHeight;
            }
            return viewportheight;
        },
        /**上传按钮,dom为按钮或a标签;fileField为文件域的dom元素:winFn.domUpload(dom,fileField,callback);*/
        domUpload : function(dom,fileField,callback){
            $(fileField).on("change", function(){
                $(dom).text("上传");
            });
            $(dom).on("click",function(){
                var self = this;
                if (self.uploading === true){//如果正在上传
                    return false;
                }
                var files = $(fileField).get(0).files; // 判断有没有文件
                if (files.length == 0){
                    return false;
                }
                self.uploading = true; // 标记正在上传
                self.originText = self.innerText;
                self.i = 1;
                self.interval = setInterval(function(){
                    self.symbol = ".";
                    if (self.i == 2) self.symbol = " ..";
                    if (self.i == 3) self.symbol = " ...";
                    self.innerText = "上传中" + self.symbol;
                    self.i++;
                    if (self.i > 3){
                        self.i = 1;
                    }
                },1000);
                function successCallback(self){
                    clearInterval(self.interval);
                    self.uploading = false;
                    self.innerText = "上传成功";
                }
                function errorCallback(self){
                    clearInterval(self.interval);
                    self.uploading = false;
                    self.innerText = "上传失败";
                    setTimeout(function(){
                        self.innerText = self.originText;
                    },5000);
                }
                /*callback(data);
                if (data.result == 1){
                    successCallback(self);
                } else {
                    errorCallback(self);
                }*/
            });
        },
        /**限制输入长度用法:maxlength="64" onkeyup="return winFn.inputLimit(this,'xxx');"*/
        inputLimit : function(o,inputDom,showDom){
            function control(inputDom,showDom,max){
                var s = document.getElementById(inputDom).value.length;
                if(s>maxl){
                    document.getElementById(inputDom).value = document.getElementById(inputDom).value.substr(0,maxl);
                }else{
                    document.getElementById(showDom).innerHTML = s;
                }
            }
            var LimitLen = o.getAttribute ? parseInt(o.value.length):"";
            control(inputDom,showDom,nMaxLen);
            if(o.getAttribute && o.value.length>LimitLen){
                document.getElementById(showDom).innerText = 8;
                o.value = o.value.substring(0,LimitLen);
            }
        },
        /**为datagrid过滤数据:winFn.dataFilter(data);*/
        dataFilter : function(data){
            if(data.code == AppKey.code.code207){
                layerFn.pageLogin(data.msg,data.code);
                return '';
            }else if(data.code == AppKey.code.code204){
                layerFn.alert(data.msg,data.code);
                return '';
            }else{
                return data;
            }
        },
        /**指定文件类型,返回false时说明不是指定的文件类型;用法:winFn.checkExt(originalFile,'png,gif,jpg');*/
        checkExt:function(originalFile,arrayExt){
            if($.trim(arrayExt) == '' || arrayExt == null || arrayExt == undefined){
                return false;
            }
            var ext = originalFile.substr(originalFile.lastIndexOf(".")+1,originalFile.length);
            arrayExt = arrayExt.split(',');
            var b = false;
            for(var i=0; i<arrayExt.length;i++){
                if(arrayExt[i].toLowerCase() == ext.toLowerCase()){
                    b = true;
                    break;
                }
            }
            return b;
        },
        /**验证字符串是否是json对象:winFn.jsonVerify(data);是返回true,否则false;*/
        jsonVerify : function(data){
            return typeof(data) == "object" && Object.prototype.toString.call(data).toLowerCase() == "[object object]" && !data.length;
        },
        /**获取form表单参数,用于参数前必须带:winFn.formParams(form);返回的是带&的字符串;推荐使用!*/
        formParams : function(form){
            return $(form).serialize();
        },
        /** 将form表单转为json对象:winFn.formField(form);返回的是{}格式,强烈推荐使用!
         * 用法 var params = winFn.formField('#formRegister');
         * layerFn.ajaxPostHint(url,'操作中,请稍候……',params,function(data){});
        */
        formField : function(formId){
            var kv = $(formId).serializeArray();
            var json = {};
            $.each(kv, function (i, v) {
                json[v.name] = v.value;
            });
            return json;
        },
        /**以form的name获取表单参数:winFn.formAjaxParams(formDom);返回的是Object,key-value*/
        formAjaxParams : function(formDom){
            var params = {};
            var field = $(formDom).serializeArray();
            $.each(field,function(){
                if(params[this.name] != undefined){
                    if (!params[this.name].push){
                        params[this.name] = [params[this.name]];
                    }
                    params[this.name].push(this.value || '');
                }else{
                    params[this.name] = this.value || '';
                }
            });
            return params;
        },
        /**判断表单是否有是否至少已填一个数据,已填有数据返回true;否则返回false;用法:winFn.checkParams(form);*/
        checkParams : function(form){
            var arrs = $(form).serialize().split("&");
            var b = false;
            for(var i = 0; arrs[i]; i++) {
                var tmp = arrs[i].split("=");
                if (tmp[1] != null && tmp[1] != ""){
                    b = true;
                    break;
                }
            }
            return b;
        },
        /**打开新页面,如有参数拼装在url里:winFn.winOpen(url);*/
        winOpen : function(url){
            if(self==top){
                window.location.href = url;
            }else{
                top.location.href = url;
            }
        },
        /**下载文件,含文件名的参数拼装在url里:用法:winFn.downloadFile(url);layerFn.alert('已进入后台处理,请耐心等待,处理完成将会自动下载!',AppKey.code.code200);*/
        downloadFile : function(url){
            $("<form method='post' action='" + urlPrefix + url + "'></form>").appendTo("body").submit().remove();
        },
        /**把字符串解析为json格式:winFn.parseJson(str);*/
        parseJson : function(str){
            return $.parseJSON(str);
        },
        /**获取当前YYYY-MM-DD日期:winFn.getCurrentDate();*/
        getCurrentDate : function(){
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var day = now.getDate();
            var clock = year + "-";
            if(month < 10) clock += "0";
            clock += month + "-";
            if(day < 10) clock += "0";
            clock += day;
            return clock;
        },
        /**获取当前YYYY年MM月DD日格式:winFn.getTodayDate();*/
        getTodayDate : function(){
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var day = now.getDate();
            var clock = year + "年";
            if(month < 10) clock += "0";
            clock += month + "月";
            if(day < 10) clock += "0";
            clock += day+"日";
            return clock;
        },
        /**获取当前YYYY-MM-DD HH:MM时间;winFn.getCurrentTime();*/
        getCurrentTime : function(){
            var now = new Date();
            var year = now.getFullYear();
            var month = now.getMonth() + 1;
            var day = now.getDate();
            var clock = year + "-";
            if(month < 10) clock += "0";
            clock += month + "-";
            if(day < 10) clock += "0";
            clock += day;
            var minute = now.getMinutes();
            var hour = now.getHours();
            if(minute < 10){
                minute = ":0"+minute;
            }else{
                minute = ":"+minute;
            }
            if(hour < 10){
                hour = " 0"+hour;
            }else{
                hour = " "+hour;
            }
            clock += hour;
            clock += minute;
            return clock;
        },
        /**单文件上传:winFn.uploadSingle(url,'#form',callBack,error);*/
        uploadSingle : function(url,formDom,callBack,error){
            $(formDom).ajaxSubmit({
                type : 'POST',
                url : urlPrefix + url,
                dataType: "json",
                success : function(data){
                    callBack(data);
                },
                error : function(response,textStatus,errorThrown){
                    error(response);
                }
            });
        },
        /**多文件上传:winFn.uploadMulti(url,callBack,error);*/
        uploadMulti : function(url,callBack,error){
        },
        /**按Enter回车键事件:winFn.Enter(fn);*/
        Enter : function(fn){
            opts.event({
                code : 13,
                fn : function(){
                    if (fn != null && fn !=''){
                        fn();
                    }
                }
            });
        },
        /**IE低版本支持默认现实提示信息:winFn.iePlaceholder();*/
        iePlaceholder : function(){
            $('input,textarea').placeholder({customClass:'ie_placeholder'});
        },
        /**元素标签是否启用,默认为启用:tagEnabled(domFlag,true);*/
        tagEnabled : function (domFlag,whether){
            if(whether == null || whether == ''){
                whether = false;
            }
            if (whether){
                $(domFlag).attr("disabled",true);
            } else {
                $(domFlag).attr("disabled",false);
                $(domFlag).removeAttr("disabled");
            }
        },
        /**删除数组little在数组large中的元素;winFn.removeLargeExistLittle(plsition_vals,hole_plsition_vals);*/
        removeLargeExistLittle : function(large,little){
            var result = [];
            for(var i = 0; i < large.length;i++){
                var k=0;
                for(var j=0;j<little.length;j++){
                    if(large[i]!=little[j]){
                        k++;
                        if(k==little.length){
                            result.push(large[i]);
                        }
                    }
                }
            }
            return result;
        },
        /**判断一个元素是否存在于这个数组中,存在true,否则false:winFn.checkinArray(val,arrs);*/
        checkinArray : function(val,arrs){
            return $.inArray(val,arrs) >= 0 ? true :false;
        },
        /**垂直上下分为两部分:winFn.initVertical2part(domTop,domBottom,size);*/
        initVertical2part : function(domTop,domBottom,size){
            if(size == null || size =='') size = 0;
            var height = $(window).height()-size;
            $(domTop).css("height",(height/2)+'px');
            $(domBottom).css("height",(height/2)+'px');
        },
        /**水平左右分为两部分:winFn.initHorizontal2part(domLeft,domRight,size);*/
        initHorizontal2part : function(domLeft,domRight,size){
            if(size == null || size =='') size = 0;
            var width = $(window).width()-size;
            $(domLeft).css({"width":(width/2)+'px',"display":"inline-block"});
            $(domRight).css({"width":(width/2)+'px',"display":"inline-block"});
        },
        /**计算比例值和总值的百分比:winFn.formatPercent(value,total);*/
        formatPercent : function(value,total){
            if(value == total){
                return '100.00';
            }else if(value == 0){
                return '0.00';
            }else{
                var pt = value / total;//获取百分比
                var str_p = pt.toString();
                var str_v = str_p.substring(0,6).substring(2);//截取
                str_v = str_v.slice(0,2) +'.'+ str_v.slice(2);//插入新值
                var zero = str_v.substr(0,1);
                if(zero == '0'){
                    str_v = str_v.substr(1);
                }
                return str_v;
            }
        },
        /**随机生成4个字符串:winFn.rdmCode()*/
        rdmCode : function(){
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        },
        /**通过生成随机数生成uuid唯一值:winFn.getUuid();*/
        getUuid : function(){
            return (winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode()+winFn.rdmCode());
        },
        /**数字转换为农历中文名称*/
        numberTransform : function(value){
            var result = '初';
            if(value <= 10){
                if(value == 1){
                    return result + '一';
                } else if(value == 2){
                    return result + '二';
                } else if(value == 3){
                    return result + '三';
                } else if(value == 4){
                    return result + '四';
                } else if(value == 5){
                    return result + '五';
                } else if(value == 6){
                    return result + '六';
                } else if(value == 7){
                    return result + '七';
                } else if(value == 8){
                    return result + '八';
                } else if(value == 9){
                    return result + '九';
                } else if(value == 10){
                    return result + '十';
                }
            }else if(value > 10 && value <=20){
                result = '十';
                if(value == 11){
                    return result + '一';
                } else if(value == 12){
                    return result + '二';
                } else if(value == 13){
                    return result + '三';
                } else if(value == 14){
                    return result + '四';
                } else if(value == 15){
                    return result + '五';
                } else if(value == 16){
                    return result + '六';
                } else if(value == 17){
                    return result + '七';
                } else if(value == 18){
                    return result + '八';
                } else if(value == 19){
                    return result + '九';
                } else if(value == 20){
                    return '二十';
                }
            }else{
                result = '廿';
                if(value == 21){
                    return result + '一';
                } else if(value == 22){
                    return result + '二';
                } else if(value == 23){
                    return result + '三';
                } else if(value == 24){
                    return result + '四';
                } else if(value == 25){
                    return result + '五';
                } else if(value == 26){
                    return result + '六';
                } else if(value == 27){
                    return result + '七';
                } else if(value == 28){
                    return result + '八';
                } else if(value == 29){
                    return result + '九';
                } else if(value == 30){
                    return '三十';
                }else if(value == 31){
                    return '三十一';
                }
            }
        },
        /**
         * 动态加载js
         * @param {} src js路径
         * @param {} callback 回调函数
         * @param {} targetEl 目标el
        */
        loadJs: function(src){
            var head = document.getElementsByTagName("head");
            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = src;
            head[0].appendChild(script);
        },
        /**
         * 动态加载css文件
         * @param {} href css路径
         * @param {} targetEl 目标el
        */
        loadCss: function(href){
            var head = document.getElementsByTagName("head");
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.type = "text/css";
            link.href = href;
            head[0].appendChild(link);
        },
        /*winFn.getPageHashToken('/info.html?id=1024');
        layerFn.handleTop('请耐心等待,处理完成将会自动下载!',AppKey.code.code200);
        winFn.getPageHashToken(url);
        */
        getPageHashToken : function(url){
            url = urlPrefix + url + tokenParams;
            window.open(url,'_blank').location;/*打开新页面*/
        },
        /*用于页面宽度很宽只能使用新页面替代,带token*/
        openPageHashToken : function(url){
            url = url + tokenParams;
            window.open(url,'_blank').location;/*打开新页面*/
        },
        getPageNormal : function(url){
            window.location.href = url;
        },
        /*用法:winFn.dataTableData;*/
        dataTableData : function(url,params,callback,oSettings){//oSettings是各个参数
            oSettings.jqXHR = $.ajax({
                dataType : 'json',
                type : "GET",
                url : urlPrefix + url,
                data : params,
                headers : {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem('refreshToken') || ''},//好使
                beforeSend : function(request){
                    self.layerIndex = layerFn.loading('正在处理……');
                },
                success : function(url,params,response,oSettings){
                    layerFn.closeIndex(self.layerIndex);
                    var data = response.responseJSON;
                    var permissions = data.permissions;
                    if(permissions != null && permissions.length > 0){
                        $("div").data("permissions",permissions);
                    }else{
                        $("div").removeData("permissions");
                    }
                    if(data.code === AppKey.code.code401){
                        var result = "{'msg':'没有操作权限','recordsFiltered':0,'code':401,'data':[],'recordsTotal':0}";
                        callback(eval('(' + result + ')'));
                    }else if(data.code === AppKey.code.code205){
                        layerFn.tokenLogin();return;
                    }else{
                        callback(data);
                    }
                },
                /*success : callback,//好使*/
                statusCode : {
                    404 : function(response){
                        status404(response);
                    },
                    500 : function(){
                        status500();
                    },
                    502 : function(response){
                        layerFn.alert("网关代理出错,联系管理员");
                    }
                },
                error : function(XMLHttpRequest,textStatus,errorThrown){
                    if(textStatus == 'parsererror'){
                        layerFn.noticeLogin();
                    }else if(textStatus == 'error'){
                        layerFn.connectFailure();
                        return;
                    }else{
                        layerFn.alert('未知错误,稍后重试');
                    }
                },
                complete : function(response,status){
                    layerFn.closeIndex(self.layerIndex);
                    try{
                        var json = oSettings.jqXHR.responseJSON;
                        if(json.code != AppKey.code.code200){
                            $('.dataTables_empty').text(json.msg);
                        }
                        if(json.code == AppKey.code.code200){
                            var renewal = json.renewal;
                            if(renewal){
                                if(refreshFlag){
                                    refreshFlag = false;
                                    renewalToken();
                                }
                            }
                        }
                    }catch(e){}
                }
            });
        },
        /*前两个参数是input隐藏域的domId的属性对应的值是value,后两个参数是下拉列表的属性id为labelDom对应的text值是text;inputDom是隐藏域的值的dom,value的是隐藏域的值,labelDom是下拉显示文字的dom,text需要显示的文字:winFn.dropdown(inputDom,'',labelDom,text)*/
        dropdown : function(inputDom,value,labelDom,text){
            $(inputDom).val(value);
            $(labelDom).text(text);
        },
        /*下拉列表且数据从后端获取,最终是调用方法 winFn.dropdown()来实现;dropdownUlOptions为ul的下的a元素的id,inputId为隐藏域的id,dropdownMenuId是显示选择后的文字id(即选项选择类别的dom),showText是默认显示的文字,用法:winFn.initDropdown(data.data,'#dropdownMenuOptions','#CATEGORY_ID','#dropdownCATEGORY_ID','选择分类');*/
        initDropdown(data,dropdownUlOptions,inputId,dropdownMenuId,showText){
            var html = '<li><a href="javascript:winFn.dropdown(\''+inputId+'\',\'\',\''+dropdownMenuId+'\',\''+showText+'\');">'+showText+'</a></li>';
            $.each(data,function(index,item){
                html += '<li><a href="javascript:winFn.dropdown(\''+inputId+'\',\''+item.VALUE+'\',\''+dropdownMenuId+'\',\''+item.LABEL+'\');" id="'+item.VALUE+'">'+item.LABEL+'</a></li>';
            });
            $(dropdownUlOptions).html(html);
        },
        /* 新增恢复文本显示时用,新增时直接是文字字符串: winFn.addDropdown('#dropdowncrowd_id','选择分类'); */
        addDropdown(dropdownMenuId,showText){
            $(dropdownMenuId).html(showText);
        },
        /* 仅作显示文字,第一个参数是指定下拉列表的dom的id,第二个参数是指定ul下的li的a标签的id从而获取该id的dom的的文本并显示在下拉文本,若是v是页面的固定值则自行自定义;用法:
        if(k == 'result'){
            winFn.editDropdown('#dropdownLabelresult','xxx'+v);//可以是自定义,也可以是字段,一般推荐是字段,空时用xxx_,其余xxx0;xxx1;v是从后端返回的值,如是对应的1;否对应的0
        }
        */
        // 第二个参数是id,是li下的a标签的id且无需添加#1前缀
        // 注意若是页面固定值则是 winFn.editDropdown('#dropdownLabelxxx字段','xxx字段'+v);如是动态从后台获取的话,则是winFn.editDropdown('#dropdownLabelxxx字段',v);
        editDropdown(dropdownMenuId,domId){
            try{
                winFn.addDropdown(dropdownMenuId,$('#'+domId).html());
            }catch(e){}
        },
        /*根据不同的值显示对应的文字,其中:textDom是显示文字的dom,text需要显示的文字,winFn.writeBack('#xx','');*/
        writeBack: function(textDom,text){
            $(textDom).text(text);
        },
        /*基于ace框架选择文件,后面的方法可有可无;winFn.initFileShoose('#file_import',['xls','xlsx'],()=>{});*/
        initFileShoose : function(doms,allowExt,change){
            $(doms).ace_file_input({
                no_file:'未选择任何文件',
                btn_choose:'选择文件',
                btn_change:'重新选择',
                droppable:false,
                onchange:null,
                thumbnail:false,
                allowExt: allowExt || ["jpeg","jpg","png","gif","bmp"]
            }).on('change',function(data){
                if(!data.result){
                    if(change){
                        change();
                    }
                    layerFn.alert('请选择正确的文件格式!',AppKey.code.code199);
                }
            });
        },
        /**移除基于ace框架文件选择 winFn.removeInputFile('');*/
        removeInputFile : function(parentDom){
            $(parentDom+' .ace-file-input a.remove').click();
        },
        /*默认初始化日期格式,属于bootstrap-datepicker.js;用法:winFn.initDate();或winFn.initDate('#id或.class');*/
        initDate : function(dom){
            if(dom == null || dom ==''){
                $('.date-picker').datepicker({
                    autoclose: true,
                    todayHighlight: true
                });
            }else{
                $(dom).datepicker({
                    autoclose: true,
                    todayHighlight: true
                });
            }
        },
        /*默认初始化日期格式,属于bootstrap-datetimepicker.js;用法:winFn.initDateTime();或winFn.initDateTime('#id或.class');*/
        initDateTime : function(dom){
            if(dom == null || dom ==''){
                $('.date-picker').datetimepicker({
                    locale: 'ZH-CN'
                });
            }else{
                $(dom).datetimepicker({
                    locale: 'ZH-CN'
                });
            }
        },
        /**页面统计,第一步:通过排序js统计列中计算最大的列作为表头列数 别名的key字段*/
        getMaxValue(listData,key){
            var list = new Array();
            for(var index in listData){
                var obj = listData[index];
                for(k in obj){
                    if(k == key){
                        var arrs = obj[key].split(',');
                        list.push(arrs.length);
                    }
                }
            }
            list.sort(function(value1,value2){
                return value1-value2;
            })
            return eval(list[list.length-1]);
        },
        /**页面统计,第二步:获取表头行的文本,key一般是类型或类别的字段;len长度;*/
        getItems : function(listData,key,column){
            var items = '';
            for(var x = 0; x < listData.length; x++){
                var obj = listData[x];
                var arrs = obj[key].split(',');
                if(column === arrs.length){
                    items = obj[key];
                    break;
                }
            }
            return items;
        },
        //页面统计,第三步:填充行[含表头及数据行(通过node来区分,node为thead是表头;node为tbody是数据行;)]
        fillRowData : function(node,startColumnText,columns,endColumnText,max){
            var html = '';
            html += '<'+node+'>';
            html += '<tr>';
            html += '<td><div align="center">'+((startColumnText == null || startColumnText == '') ? '' : startColumnText)+'</div></td>';
            for(var i=0;i<max;i++){
                if(columns != null){
                    var text = columns[i];
                    html += '<td><div align="center">'+((text == null || text == '') ? '' : text)+'</div></td>';
                }else{
                    html += '<td></td>';
                }
            }
            html += '<td><div align="center">'+((endColumnText == null || endColumnText == '') ? '' : endColumnText)+'</div></td>';
            html += '</tr>';
            html += '</'+node+'>';
            return html;
        },
        //页面统计,第四步:用于填充最后1行的合计行;totalKey 一般是指count(xxx)的别名,如 count(xxx) as xxx_total,即填入 xxx_total 该值即可;columnIndex是每一列数的索引,通过 for(int i = 0; i < cols; i++){ cols是最大的行数,循环传入,值为:1,2,3……
        calculateTotal : function(listData,totalKey,columnIndex){
            var column = 0;
            for(var i=0;i<listData.length;i++){
                var rows = listData[i];
                var values = rows[totalKey].split(',');
                var value = values[columnIndex];
                if(value != null && value!=''){//todo 两个都不能缺!!!
                    column += Number.parseInt(value);
                }
            }
            return column;
        },
        /**
         * 初始化规则表格并填充数据,不涉及合并单元格
         * @param domTable 表格dom元素
         * @param data list数据
         * @param countAsKey 内层的 count(xxx) as xxx_total,填写该值即可,如 profession_total 或 type_total
         * @param groupByType 一般是指类型或类别的字段,如工种或场所类型,如工种|场所类型(profession|site_type)
         * @param startColumnText 指定第1行的第1列的文本内容,如地区|区域
         * @param startColumnKey 指定遍历每行的每N列的字段,group by xxx 如 name 或 area
         * @param endColumnText 指定第1行的最后1列的文本内容,如合计
         * @param endRowStartColumnText 指定最后1行的最后1列的文本内容,如合计|总计
        */
        initFillTable : function(domTable,data,countAsKey,groupByType,startColumnText,startColumnKey,endColumnText,endRowStartColumnText){
            var max = winFn.getMaxValue(data,countAsKey);
            var items = winFn.getItems(data,groupByType,max);
            var htmlHeader = winFn.fillRowData('thead',startColumnText,items.split(','),endColumnText,max);//表头
            $(domTable).html(htmlHeader);
            var htmlIndex = '';
            var totalAll = 0;//最后1行的最后1列的值的计算
            for(var i=0;i<data.length;i++){
                var rows = data[i];
                var values = rows[countAsKey].split(',');//等价 rows.xxx.split(',');
                var total = 0;
                for(var j = 0; j < values.length; j++){
                    total += Number.parseInt(values[j]);
                }
                htmlIndex += winFn.fillRowData('tbody',rows[startColumnKey],values,total,max);//数据行
                totalAll += total;
            }
            var list = new Array();
            for(var k = 0; k < max; k++){
                var v = winFn.calculateTotal(data,countAsKey,k);
                list.push(v);
            }
            htmlIndex += winFn.fillRowData('tbody',endRowStartColumnText,list,totalAll,max);//最后一行合计
            $(domTable).append(htmlIndex);
        }
    },
    /**easyui专属方法,依赖easyui*/
    window.euiFn = {
        /**格式化日期YYYY-MM-DD;euiFn.dateFormatter(value);*/
        dateFormatter : function(value){
            var date = new Date(value);
            var year = date.getFullYear().toString();
            if(isNaN(year))return;
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
            if(month < 10)month = "0" + month;
            if(day < 10)day = "0" + day;
            return year + "-" + month + "-" + day;
        },
        /**格式化日期YYYY-MM-DD HH:MM:SS;euiFn.dateFormatter(value);*/
        dateTimeFormatter : function(value){
            var date = new Date(value);
            var year = date.getFullYear().toString();
            if(isNaN(year))return;
            var month = (date.getMonth() + 1);
            var day = date.getDate().toString();
            var h = date.getHours().toString();
            var m = date.getMinutes().toString();
            var s = date.getSeconds().toString();
            if(month < 10)month = "0" + month;
            if(day < 10)day = "0" + day;
            if(h < 10)h = "0" + h;
            if(m < 10)m = "0" + m;
            if(s < 10)s = "0" + s;
            return year + "-" + month + "-" + day + ' '+ h + ':' + m + ':'+ s;
        },
        /**把js对象的datagrid数据行的某行row转为json对象:euiFn.paramsJson(row);*/
        paramsJson : function(row){
            if(row == null || row == '' || row.length <= 0)return null;
            var jsonParams = '{';
            for(var key in row){
                jsonParams = jsonParams + '"'+key+'"' +':"'+row[key]+'",';
            }
            return jsonParams.substring(0,(jsonParams.length-1)) + '}';
        },
        /**把js数组datagrid数据行转为json数组:euiFn.paramArray(datagridId,fields);fields为指定字段,字段间用,分开*/
        paramArray : function(datagridId,fields){
            if(datagridId == null || datagridId.length <= 0 || datagridId == undefined)return null;
            if(fields != null && fields != '' && fields != undefined){
                var rows = $(datagridId).datagrid('getChecked');
                var array = '[';
                $.each(rows,function(index,data){
                    var strs = fields.split(",");
                    var json = '';
                    for (i=0; i<strs.length; i++){
                        var k = strs[i];
                        var v = data[k];
                        if(v != null && v != '' && v != undefined){
                            if(json.length > 0){
                                json += '"'+k+'":"'+v+'",';
                            }else{
                                json += '{"'+k+'":"'+v+'",';
                            }
                        }
                    }
                    json = json.substring(0,(json.length - 1)) + '}';
                    array += json+',';
                });
                array = array.substring(0,(array.length-1)) +']'
                return array;
            }
        },
        /**把js数组datagrid数据行转为json数组:euiFn.paramsRows(rows,fields);fields为指定字段,字段间用,分开,其中 var rows = euiFn.getRowsData(thisPage.datagrid)*/
        paramsRows : function(rows,fields){
            if(rows == null || rows.length <= 0 || rows == undefined)return null;
            if(fields == null || fields == '' || fields == undefined)return null;
            var array = '[';
            $.each(rows,function(index,data){
                var strs = fields.split(",");
                var json = '';
                for (i=0; i<strs.length; i++){
                    var k = strs[i];
                    var v = data[k];
                    if(v != null && v != '' && v != undefined){
                        if(json.length > 0){
                            json += '"'+k+'":"'+v+'",';
                        }else{
                            json += '{"'+k+'":"'+v+'",';
                        }
                    }
                }
                json = json.substring(0,(json.length - 1)) + '}';
                array += json+',';
            });
            array = array.substring(0,(array.length-1)) +']'
            return array;
        },
        /**从row对象JSON.stringify(row)转为json对象后提取为请求参数:euiFn.rowParams(jsonObj,fields);其中的jsonObj是JSON.stringify(row)的数据,fields指定字段,以,隔开,如果为空则是全部字段*/
        rowParams : function(jsonObj,fields){
            if(jsonObj == null || jsonObj.length <= 0 || jsonObj == undefined)return null;
            var params = '';
            if(fields != null && fields != "" && fields != undefined && fields.length > 0){
                for(var k in jsonObj){
                    var arrs = fields.split(",");
                    for (i=0; i < arrs.length; i++){
                        if(arrs[i] == k){
                            var value = jsonObj[k];
                            params += k +'='+value+'&';
                            break;
                        }
                    }
                }
            }else{
                for(var k in jsonObj){
                    var value = jsonObj[k];
                    params += k +'='+value+'&';
                }
            }
            return params.substring(0,(params.length-1));
        },
        /**把js对象的datagrid数据行的某行row转为请求参数:euiFn.paramsObject(object)*/
        paramsObject : function(object){
            if(object == null || object == '' || object.length <= 0)return null;
            var params = '';
            for(key in object){
                params = params+key+'='+object[key]+'&';
            }
            return params.substring(0,(params.length-1));
        }
    },
    /**依赖jQuery*/
    window.verifyFn = {
        /**验证是否指定的文件格式:verifyFn.checkFileType(this,'#excel','请选择Excel格式文件','xls,xlsx');*/
        checkFileType : function(obj,domId,msg,arr){
            var fileType = obj.value.substr(obj.value.lastIndexOf(".")).toLowerCase();//获得文件后缀名
            arr = arr.split(",");
            var count = 0 ;
            var length = arr.length ;
            for (var i = 0; i < length; i++){
                if(fileType != '.'+arr[i]){
                    count ++;
                }
            }
            if(count === length){
                msg = (verifyFn.isEmpty(msg))?'请选择正确的文件格式':msg;
                $(domId).val('');
                layerFn.alert(msg);
            }
        },
        /**验证是否是正整数,是返回true,否则返回false:verifyFn.checkIsNum(value);*/
        checkIsNum : function(value){
            var reg = /^[0-9]*[1-9][0-9]*$/;
            return reg.test(value);
        },
        /**验证输入框是否已输入数据,未输入返回true,否则返回false:verifyFn.inputRequired(dom);*/
        inputRequired : function(dom){
            var value = winFn.getDomValue(dom);
            if (verifyFn.isEmpty(value)){
                return true;
            } else {
                return false;
            }
        },
        /**表单验证 if(verifyFn.inputParam('#dom','请输入内容'))return;*/
        inputCheck : function(dom,msg){
            var value = winFn.getDomValue(dom);
            if(value == null || value.length === 0 || (typeof(value) == 'string' && value.replace(/^\s+|\s+$/gm,'').length <= 0)){
                layerFn.alert(msg,AppKey.code.code199);
                return true;
            }
            return false;
        },
        /**验证是否为空:verifyFn.isEmpty(value);*/
        isEmpty : function(object){
            if(typeof object == 'function'){
                if(object == null || object == '' || object == undefined || object == 'undefined')return true;
            }
            if (object != null && object != ''){
                if(object == 'undefined' || object == 'UNDEFINED')return true;
            }
            if(object == null || object == undefined || object == '')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'null')return true;
            if($.trim(object).length > 0 && $.trim(object) == 'NULL')return true;
            if(object.length == 0)return true;
            if($.trim(object).length <= 0)return true;
            if($.trim(object) == '')return true;
            return false;
        },
        /**验证是否为email格式:verifyFn.isEmail(value);*/
        isEmail : function(value){
            if (/^([\w\.\-])+\@\w+(\.[a-zA-Z]+)+$/.test(value))return true;
            return false;
        },
        /**验证手机号是否填写正确:verifyFn.isPhone(value);*/
        isPhone : function(value){
            return (/^(\+\d{2}-)?(\d{2,3}-)?([1][3,4,5,6,7,8][0-9]\d{8})$/.test(value));
        },
        /**判断这两个数据是否不相等，区分大小写,不相等返回true:verifyFn.checkEqual(value1,value2);*/
        checkEqual : function(value1,value2){
            if(value1 != value2){
                return true;
            }else {
                return false;
            }
        },
        /**检查是否支持对应的浏览器:verifyFn.checkBrowser(hint,target);*/
        checkBrowser : function(hint,target){
            var ua = navigator.userAgent.toLowerCase(), s, app = {}, url, host = window.location.host;
            (s = ua.match(/msie ([\d.]+)/)) ? app.ie = s[1] : (s = ua.match(/firefox\/([\d.]+)/)) ? app.firefox = s[1] : (s = ua.match(/chrome\/([\d.]+)/)) ? app.chrome = s[1] : (s = ua.match(/opera.([\d.]+)/)) ? app.opera = s[1] : (s = ua.match(/version\/([\d.]+).*safari/)) ? app.safari = s[1] : 0;
            var bro = false;
            if (app.ie){
                bro = true;// 如果浏览器为IE则跳转到;
            } else if (app.chrome){
                bro = false;// 如果浏览器为Chrome则跳转到相对链接html/
            } else if (app.opera){
                bro = false;// 如果浏览器为Opera则则跳转到相对链接/html/
            } else if (app.firefox){
                bro = false;// 如果浏览器为firefox则跳转到上级目录/
            } else if (app.safari){
                bro = true;// 如果浏览器为Safari则跳转到当前域名
            } else {
                bro = true;// 其他浏览器打开跳转到空白页面;
            }
            if (bro){
                document.location.href = hint;
            }else {
                document.location.href = target;
            }
        },
        /**验证手机号或电话号码是否正确:verifyFn.checkTelephone(value);*/
        checkTelephone : function(tel){
            var mobile = /^1\d{10}$/,phone = /^0\d{2,3}-?\d{7,8}$/;
            return mobile.test(tel) || phone.test(tel);
        },
        /** 将表单序列化为JSON对象:verifyFn.initExtend();*/
        initExtend : function(){
            $.fn.serializeObject = function(){
                var o = {};
                var a = this.serializeArray();
                $.each(a, function() {
                    if (o[this.name]) {
                        if (!o[this.name].push) {
                            o[this.name] = [ o[this.name] ];
                        }
                        o[this.name].push(this.value || '');
                    } else {
                        o[this.name] = this.value || '';
                    }
                });
                return o;
            };
            /**自定义下拉列表,名字必须以$.fn.bindXxx的开头,但是在编辑时赋值有问题;用法:
             * dom <select id="educationId" class="form-control"></select>
             js $("#educationId").bindInitSelect({url:'',params:{pid:10},value:"kid",text:"text",selectText:'选择学历',change:function(object){}});
            */
            $.fn.bindInitSelect = function (options){
                var $element = $(this);
                layerFn.queryGetHandleHint(options.url,options.params,function(data){
                    if(data.code == AppKey.code.code200){
                        data = data.data;
                        $element.empty().append('<option value="">'+options.selectText+'</option>');
                        $.each(data,function (i){
                            $element.append($("<option></option>").val(data[i][options.value]).html(data[i][options.text]));
                        });
                        $element.on("change",function(e){
                            if (options.change != null){
                                var obj = $("#"+$element.attr('id')+" option:selected");
                                options.change({value:obj.val(),text:obj.text()});
                            }
                        });
                    }else{
                        $element.empty().append('<option value="">'+data.msg+'</option>');
                    }
                });
            };
        }
    },
    /**layer专属方法:layerFn.queryXxx;layerFn.submit();layerFn.ajaxXxx,其他的不推荐使用*/
    window.layerFn = {
        /**确认操作,且取消按钮和右上角具有同一事件:layerFn.confirm(msg,function(){},function(){});如果需要调用到第2个方法则第1个方法是不能少的,写function(){}即可*/
        confirm : function (msg,fnVerify,fnCancel){
            msg = verifyFn.isEmpty(msg)?"确定要操作吗?":msg;
            var exitIndex = top.layer.confirm('<div style="color:red;"><img src="'+AppKey.iconDir+'help_icon.png" style="vertical-align:middle;margin-right:3px;"/>'+msg+'</div>',{
                title : AppKey.title,
                area : AppKey.layerArea,
                btn : [AppKey.confirm,AppKey.cancel],
                cancel : fnCancel,
            },function(index){
                layerFn.closeIndex(index);
                if(fnVerify){fnVerify();}
            },function(index){
                layerFn.closeIndex(index);
                if(fnCancel){fnCancel();}
            });
            layerFn.EscLayer(exitIndex);
        },
        /**不带正在加载提示信息POST请求远程数据,layerFn.ajaxPost(url,params,succeed);*/
        ajaxPost : function(url,params,succeed){
            postRow(url,params,function(data){
                if(data.code == AppKey.code.code200){
                    succeed(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**不带正在加载提示信息GET请求远程数据,layerFn.ajaxGet(url,params,succeed);*/
        ajaxGet : function(url,params,succeed){
            getQuery(url,params,function(data){
                if(data.code == AppKey.code.code200){
                    succeed(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**不推荐使用,不带正在加载提示信息GET请求远程数据,未处理code=401的情况需要自行处理,但已处理code为205业务,当然在调用时也可以再处理,如：text(data.msg);layerFn.ajaxGetResult(url,params,succeed);*/
        ajaxGetResult : function(url,params,succeed){
            getQuery(url,params,function(data){
                succeed(data);
            });
        },
        /**带动画的ajax的POST提交,仅适用于增、删、改操作且已做请求失败的处理;layerFn.submit(url,params,fnSuccess);成功时才关闭对话框,所以不需要处理失败的回调及code为200以外的数据处理*/
        submit : function(url,params,fnSuccess){
            postRowHint(url,params,function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**带动画的ajax的GET请求,仅适用于查询操作且已做请求失败的处理;layerFn.queryGetHint(url,params,fnSuccess);成功时才关闭对话框,所以不需要处理失败的回调及code为200以外的数据处理*/
        queryGetHint : function(url,params,fnSuccess){
            getQueryHint(url,params,function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**带动画的ajax的GET请求,仅适用于查询操作且未做code!=200的情况;layerFn.queryGetHandleHint(url,params,fnSuccess);需要处理失败的回调及code为200以外的数据处理*/
        queryGetHandleHint : function(url,params,fnSuccess){
            getQueryHint(url,params,function(data){
                fnSuccess(data);
            });
        },
        /**不推荐使用,带动画的ajax的GET请求,未处理code=401的情况需要自行处理,但已处理code为205业务,当然在调用时也可以再处理,如：text(data.msg);layerFn.queryGetHintResult(url,params,fnSuccess);*/
        queryGetHintResult : function(url,params,fnSuccess){
            getQueryHint(url,params,function(data){
                fnSuccess(data);
            });
        },
        /**以id查询单条数据的全部字段信息,适用于存在就查看或编辑否则就添加,带正在加载的提示信息,无需处理失败的回调及code为200及201以外的数据处理,用法:layerFn.queryByIdHint(url,id,function(data){编辑},function(data){添加});*/
        queryByIdHint : function(url,id,fnSuccess,fnEmpty){
            getQueryHint(url,{id:id},function(data){
                if(data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else if(data.code == AppKey.code.code201){
                    if(fnEmpty != null && fnEmpty != ''){
                        fnEmpty(data);
                    }else{
                        layerFn.alert(data.msg,data.code);
                    }
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**根据id删除数据;用法:layerFn.delByIdHint(url,id,fnSuccess);无需处理失败的回调及code为200以外的数据处理*/
        delByIdHint : function(url,id,fnSuccess){
            postRowHint(url,{id:id},function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /**删除[含批量]数据;用法:layerFn.delBatchHint(url,ids,fnSuccess);无需处理失败的回调及code为200以外的数据处理*/
        delBatchHint : function(url,ids,fnSuccess){
            postRowHint(url,{ids:ids},function(data){
                if (data.code == AppKey.code.code200){
                    fnSuccess(data);
                }else{
                    layerFn.alert(data.msg,data.code);
                }
            });
        },
        /*用法:self.layerIndex = layerFn.loading('正在处理……');可选值:('操作中,请稍候……','196px');|('正在处理……','179px');|*/
        loading : function(msg,width){
            msg = (msg == null || msg == '' || msg == undefined)?'操作中,请稍候……':msg;
            width = (width == null || width == '') ? '196px' : width;
            return top.layer.msg(msg,{icon:16,time:-1,shade:[0.3,'#000'],area:width});
        },
        closeIndex : function(index){
            top.layer.close(index);
        },
        closedAll : function(){
            top.layer.closeAll();
        },
        /*用法:layerFn.connectFailure(msg);有可能是url的前面添加的前缀导致的*/
        connectFailure : function(msg){
            msg = (msg == null || msg == '' || msg == undefined)?'连接服务器失败,请稍后重试':msg;
            this.alert(msg);
        },
        handleResult : function(msg,code,time){
            msg = (msg == null || msg == '' || msg == undefined)?'操作错误':msg;
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            var img_style = '"vertical-align:middle;margin-right:3px;"';
            var content = '<div>'+msg+'</div>';
            switch(code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code204:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                default:
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
            }
            time = (time == null || time == '') ? 2500 : time;
            var exitIndex = top.layer.alert(content,{title:AppKey.title,closeBtn:1,shade:0,time:time,anim:2,offset:'rb',btn:null,});
            layerFn.EscLayer(exitIndex);
        },
        handleTop : function(msg,code){
            msg = (msg == null || msg == '' || msg == undefined)?'警告':msg;
            if(!isNaN(code)){
                var imagerUrl = AppKey.iconDir+'warn.png';
                var img_style = '"vertical-align:middle;margin-right:3px;"';
                msg = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
            }
            top.layer.alert(msg,{title:false,closeBtn:0,shade: 0,btn:false,time:1500,offset: 't'});
        },
        handleClose : function(msg,code){
            msg = (msg == null || msg == '' || msg == undefined)?'警告':msg;
            if(!isNaN(code)){
                var imagerUrl = AppKey.iconDir+'warn.png';
                var img_style = '"vertical-align:middle;margin-right:3px;"';
                msg = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
            }
            top.layer.alert(msg,{title:'系统提示',move:false,btn:false,area : ['250px','auto']});
        },
        /**按Esc键关闭layer对话框或仅用于查看的layer层:layerFn.EscLayer(index);*/
        EscLayer : function(index){
            opts.event({
                code : 27,
                fn : function(){
                    layerFn.closeIndex(index);
                    index = null;
                }
            });
        },
        /**
         * 判断是否已登录,未登录直接跳转到jsp登录页面,否则返回当前传递的对象:layerFn.checkLogin(result);true 表示为真;
            var bl = layerFn.checkLogin(result);
            if(bl)return;
            //业务处理
         */
        checkLogin : function(data){
            if(data.toString().indexOf("key_page_login") != -1){
                layerFn.noticeLogin();
                return true;
            }else{
                return false;
            }
        },
        /*一般的请求提示重新登录,用法:layerFn.noticeLogin();return;*/
        noticeLogin : function(){
            layerFn.closeEvent(AppKey.msg_not_login,AppKey.code.code204,function(){
                if(self==top){
                    window.location.href = AppKey.loginUrl;
                }else{
                    top.location.href = AppKey.loginUrl;
                }
            });
        },
        /*用于token过期时提示登录,用法:layerFn.tokenLogin();return;*/
        tokenLogin : function(){
            layerFn.closeEvent(AppKey.token_not_login,AppKey.code.code204,function(){
                if(self==top){
                    window.location.href = AppKey.loginUrl;
                }else{
                    top.location.href = AppKey.loginUrl;
                }
            });
        },
        /**用于编辑或添加,调用方法:layerFn.addOrEdit(title,domDivId,[width,height],function(layerIndex,layero){},btn3,btn3Call);第5个参数是第3个按钮名称[支持html],第6个参数是5个按钮参数是事件;不含右上角关闭的事件*/
        addOrEdit : function(title,domDivId,area,closedCall,btn3,btn3Call){
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                }
            });
        },
        /**含2个按钮2个回调,一个右上角的关闭回调,用于编辑或添加,调用方法:layerFn.addOrEditEvent(title,domDivId,['width_px','height_px'],function(layerIndex,layero){},function(layerIndex,layero){},function(){});第5个参数是第2个按钮的事件;cancelCall是右上角关闭的事件*/
        addOrEditEvent : function(title,domDivId,area,closedCall,fn2Call,cancelCall){
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                resize : false,
                btn : [AppKey.submit,AppKey.cancel],
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn2: function(index,layero){
                    if (fn2Call != null && fn2Call != ''){
                        fn2Call(index,layero);
                    }
                },
                cancel: function(){
                    if (cancelCall != null && cancelCall != ''){
                        cancelCall();
                    }
                }
            });
        },
        /**含3个按钮3个回调,一个右上角的关闭回调,必须手动关闭对话框:layerFn.dialogBtn3Fn3(title,domDivId,['width_px','height_px'],btn1,function(layerIndex,layero){},btn2,function(layerIndex,layero){},btn3,function(layerIndex,layero){},function(){});*/
        dialogBtn3Fn3 : function(title,domDivId,area,btn1,fn1Call,btn2,fn2Call,btn3,fn3Call,cancelCall){
            return layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                resize : false,
                btn : [btn1,btn2,btn3],
                yes : function(index,layero){
                    if (fn1Call != null && fn1Call != ''){
                        fn1Call(index,layero);
                    }
                },
                btn2: function(index,layero){
                    if (fn2Call != null && fn2Call != ''){
                        fn2Call(index,layero);
                    }
                    return false;
                },
                btn3: function(index,layero){
                    if (fn3Call != null && fn3Call != ''){
                        fn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(){
                    if (cancelCall != null && cancelCall != ''){
                        cancelCall();
                    }
                }
            });
        },
        /*弹出输入框*/
        dialogPrompt : function(){
            return layer.prompt({
                formType: 2,
                title: '请输入数据',
                value:'',
                area: ['400px','250px'],
                btnAlign: 'c',
                btn: ['通过','不通过','取消'],
                yes: function(index, layero){
                    var value = $('#layui-layer'+index + " .layui-layer-input").val();
                    if (!value) {
                        return false;
                    }
                    layer.close(index);
                    console.log('通过',value);
                },
                btn2 : function(index, layero){
                    var value = $('#layui-layer'+index + " .layui-layer-input").val();
                    if (!value) {
                        return false;
                    }
                    layer.close(index);
                    console.log('不通过',value);
                },
                btn3 : function(index){
                    layer.close(index);
                    console.log('取消');
                },
                cancel: function(){
                    console.log('关闭');
                }
            });
        },
        /**用于编辑|添加|查看的居中的弹出框,但是IE8不兼容:layerFn.winUrl('编辑','page?page=edit',[width,height],function(layerIndex,layero){},btn);最后一个参数不为空时则是单个按钮*/
        winUrl : function(title,url,area,callback,btn){
            return top.layer.alert('',{
                shade : 0.3,
                type : 2,
                title : title,
                content : url,
                btnAlign: 'c',
                closeBtn: 1,
                resize : false,
                area : area,
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的居中的弹出框:layerFn.winDom(title,domDivId,[width,height],function(layerIndex,layero){},btn);最后一个参数btn不为空时则是单个按钮,且callback可以为null*/
        winDom : function(title,domDivId,area,callback,btn){
            return top.layer.alert('',{
                shade : 0.3,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                closeBtn: 1,
                resize : false,
                area : area,
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的右边Right的弹出框,但是IE8不兼容:layerFn.winRUrl('编辑','page?page=edit','1000px',function(layerIndex,layero){},btn);最后一个参数不为空时则是单个按钮*/
        winRUrl : function(title,url,width,callback,btn){
            return top.layer.alert('',{
                offset: 'rt',
                shade : 0.3,
                anim : 2,
                type : 2,
                title : title,
                content : url,
                btnAlign: 'c',
                closeBtn: 0,
                resize : false,
                move: false,
                area : [width,'100%'],
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**用于编辑|添加|查看的右边Right的弹出框:layerFn.winRDom(title,domDivId,'1000px',function(layerIndex,layero){},btn);最后一个参数btn不为空时则是单个按钮,且callback可以为null*/
        winRDom : function(title,domDivId,width,callback,btn){
            return top.layer.alert('',{
                offset: 'rt',
                shade : 0.3,
                anim : 2,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                closeBtn: 0,
                resize : false,
                move: false,
                area : [width,'100%'],
                btn : !btn ? [AppKey.submit,AppKey.cancel]:[btn],
                yes : !btn ? function(index,layero){
                    callback(index,layero);
                }:''
            });
        },
        /**KindEditor编辑;调用方法:layerFn.winKindEditor(title,domDivId,textareaId,[width,height],function(layerIndex,layero){},btn3,btn3Call);最后1个参数是倒数第2个按钮名称[支持html]回调,不含右上角关闭的事件*/
        winKindEditor : function (title,domDivId,textareaId,area,closedCall,btn3,btn3Call){
            var WKE = (self==top)?parent:window;
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return WKE.layer.open({
                type : 1,
                title : title,
                content: $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);//在后台处理成功时可以KindEditor.remove('#news_id_input_edit');//移除编辑器
                    }
                },
                btn2: function(index,layero){//第2个按钮-取消按钮
                    KindEditor.remove(textareaId);//取消按钮时移除编辑器
                },
                btn3 : function(index,layero){
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(index,layero){//右上角关闭按钮
                    KindEditor.remove(textareaId);//右上角关闭按钮关闭时移除编辑器
                },
                success: function(layero,index){//成功打开layer时执行
                    window.TypEditor = KindEditor.create(textareaId,{
                        resizeType: 0,
                        allowPreviewEmoticons: false,
                        allowImageUpload: false,
                        afterBlur: function(){
                            this.sync();
                        },
                        items: ['|','selectall','quickformat','forecolor', 'hilitecolor','lineheight', 'undo', 'redo', 'cut', 'copy', 'paste','plainpaste', 'wordpaste','|','justifyleft', 'justifycenter', 'justifyright','justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript','superscript', '|', 'title', 'fontname', 'fontsize', '|', 'bold','italic', 'underline', 'strikethrough', 'removeformat', '|', 'image','flash', 'media', 'advtable', 'hr', 'emoticons', 'link', 'unlink','|']
                    });
                }
            });
        },
        /**获取KindEditor数据:layerFn.getKindEditorData();*/
        getKindEditorData : function (){
            if(TypEditor == null || TypEditor == undefined || TypEditor == 'undefined' ||  $.trim(TypEditor) == ""){
                return null;
            }
            return TypEditor.html();
        },
        /**百度ueditor编辑;调用方法:layerFn.winUeditor(title,domDivId,'script_editor',[width,height],data,function(layerIndex,layero){},btn3,btn3Call);最后1个参数是倒数第2个按钮名称[支持html]回调,不含右上角关闭的事件,注意script_editor是不带#或.的选择器*/
        winUeditor : function (title,domDivId,scriptId,area,data,closedCall,btn3,btn3Call){
            var WUR = (self==top)?parent:window;
            var btns = (btn3 == null || btn3 == '') ? [AppKey.submit,AppKey.cancel] : [AppKey.submit,AppKey.cancel,btn3];
            return WUR.layer.open({
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                area : area,
                resize : false,
                btn : btns,
                yes : function(index,layero){
                    if (closedCall != null && closedCall != ''){
                        closedCall(index,layero);
                    }
                },
                btn2: function(index,layero){//第2个按钮-取消按钮
                    //UE.getEditor(scriptId).destroy();//销毁
                },
                btn3 : function(index,layero){
                    //UE.getEditor(scriptId).destroy();//销毁
                    if (btn3Call != null && btn3Call != '' && btn3 != null && btn3 != ''){
                        btn3Call(index,layero);
                    }
                    return false;
                },
                cancel: function(index,layero){//右上角关闭按钮
                    //UE.getEditor(scriptId).destroy();//销毁
                },
                success: function(layero,index){//成功打开layer时执行
                    var ue = UE.getEditor(scriptId);//实例化编辑器
                    setTimeout(function(){
                        ue.ready(function(){
                            if(data != null && data != ''){
                                ue.setContent(data,false);
                            }else{
                                ue.setContent('');
                            }
                        });
                    },300);
                }
            });
        },
        /**打印功能:layerFn.winPrint(title,domDivId,[width,height],callback);*/
        winPrint : function(title,domDivId,area,callback){
            return top.layer.alert('',{
                shade : 0.3,
                type : 1,
                title : title,
                content : $(domDivId),//这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                btnAlign: 'c',
                resize : false,
                area : area,
                btn :['打印',AppKey.cancel],
                yes : function(index,layero){
                    callback(index,layero);
                },
            });
        },
        /**获取layerFn.winUrl()或layerFn.winRUrl()打开新页面的js方法及属性:var iframeWin = layerFn.getIframe(layero);*/
        getIframe : function(layero){
            return window.window[layero.find('iframe')[0]['name']];
        },
        /**适用于查看,带自动关闭的,当time为空或为0时是不会自动关闭;layerFn.viewDialog(title,domDivId,[width,height],time);*/
        viewDialog : function(title,domDivId,area,time){
            time = (time == null || time == '') ? 0 : time;
            return top.layer.open({
                type : 1,
                title : title,
                content : $(domDivId),
                area : area,
                btn : [AppKey.close],
                time : time,
                resize : false
            });
        },
        /**跳转到登录页面:layerFn.pageLogin(msg,code);*/
        pageLogin : function(msg,code){
            layerFn.closeEvent(msg,code,function(){
                window.location.href = AppKey.loginUrl;
            });
        },
        /**跳转到后台页面:layerFn.pageControl(msg,code);*/
        pageControl : function(msg,code){
            layerFn.closeEvent(msg,code,function(){
                window.location.href = AppKey.control;
            });
        },
        /**提示框:layerFn.tips('#id或者.class','请填写内容');*/
        tips : function(dom,msg){
            layer.tips(msg,dom,{
                tips: [2,'#459df5']//2;4
            });
        },
        /*用法:layerFn.prompt(function(value,index){},title);*/
        prompt : function(callback,title){
            title = (title == null || title =='') ? "系统提示" : title;
            layer.prompt({title:title,move: false},function(value,index){
                layer.close(index);
                callback(value,index);
            });
        },
        /**居中偏上top;layerFn.showTop(msg,AppKey.code.code200);*/
        showTop : function(msg,code){
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'icon_ok.png';
            switch (code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    break;
                case AppKey.code.code199:
                    msg = verifyFn.isEmpty(msg)?"操作失败":msg;
                    imagerUrl = AppKey.iconDir+'warn.png';
                    break;
                default:
                    msg = verifyFn.isEmpty(msg)?"操作有误":msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    break;
            }
            layer.open({
                type : 1,
                title : false,
                shade : 0, //遮罩透明度
                closeBtn : 0, //不显示关闭按钮
                area : ['auto', '40px'],
                offset : '8px',
                content : '<div style="line-height:40px;height:40px;padding-left:16px;padding-right:16px;display:inline-block;"><img src='+imagerUrl+' style="margin-bottom:2px;margin-right:4px;"/>'+msg+'</div>',
                time : 1500
            });
        },
        /**单个按钮的提示框,不带回调事件:layerFn.alert(msg,code);code不为空且值为[AppKey.code.code198,199,200,201,202,203,204,205,206,207]之一时显示图标*/
        alert : function(msg,code){
            if(code == null || code == ''){
                opts.dialog({msg : msg});
            }else{
                opts.dialog({msg : msg,code:code});
            }
        },
        /**提示输入框,layerFn.inputValue(function(value){});*/
        inputValue : function(callBack){
            layer.prompt({title:'请输入'},function(value,index){
                layer.close(index);
                callBack(value);
            });
        },
        /**对话框关闭事件,不管按确定按钮或右上角的按钮都会触发事件:layerFn.closeEvent(msg,code,function(){});*/
        closeEvent : function (msg,code,callBack){
            msg = verifyFn.isEmpty(msg)?"操作有误":msg;
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            var img_style = '"vertical-align:middle;margin-right:3px;"';
            var content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
            switch(code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code204:
                case AppKey.code.code205:
                case AppKey.code.code207:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                default:
                    break;
            }
            top.layer.alert(content,{
                title : AppKey.title,
                btn : [AppKey.confirm],
                area : AppKey.layerArea,
                cancel : callBack,
            },function(index){
                layerFn.closeIndex(index);
                if(callBack != null && callBack != ''){
                    callBack();
                }
            });
        },
        /*表单提交,表单+文件异步提交,后端不能用@RequestBody接收!!!,用法:
        var formData = new FormData();
        formData.append('KID',winFn.getDomValue(inputEditKeyId));
        var files = document.getElementById("framImages").files;//支持单个|多个文件上传
        for(var index in files){
            formData.append("image"+index,files[index]);
        }
        layerFn.formSubmit(url,formData,function(data){});
        */
        formSubmit : function(url,formData,succeed,error){
            var layerIndex = layerFn.loading('正在处理……');
            $.ajax({
                url : urlPrefix + url,
                type : "POST",
                data : formData,
                contentType : false,
                processData : false,
                headers : {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem("refreshToken") || ''},
                dataType : "json",
                success : function(data){
                    layerFn.closeIndex(layerIndex);
                    if(data.code == AppKey.code.code200){
                        succeed(data);
                    }else{
                        layerFn.alert(data.msg,data.code);
                    }
                },
                error : function (err){
                    layerFn.closeIndex(layerIndex);
                    if(error){
                        error(err);
                    }else{
                        layerFn.connectFailure();
                    }
                },
                complete : function(response,status){
                    layerFn.closeIndex(layerIndex);
                    ajaxComplete(response.responseJSON);
                }
            });
        }
    };
    /**插件定义配置*/
    window.opts = {
        /**layer单个按钮的提示框,不带回调事件:opts.dialog(options);不带code就不显示图标;*/
        dialog : function (options){
            var title = options.title || AppKey.title;
            var msg = options.msg || '操作有误';
            var code = options.code || null;
            code = parseInt(code);
            var imagerUrl = AppKey.iconDir+'warn.png';
            var img_style = '"vertical-align:middle;margin-right:3px;"';
            var content = '<div>'+msg+'</div>';
            switch(code){
                case AppKey.code.code200:
                    msg = verifyFn.isEmpty(msg)?"操作成功":msg;
                    imagerUrl = AppKey.iconDir+'success.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                case AppKey.code.code204:
                    msg = verifyFn.isEmpty(msg)?AppKey.sysError:msg;
                    imagerUrl = AppKey.iconDir+'error.png';
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
                default:
                    content = '<div><img src='+imagerUrl+' style='+img_style+'/>'+msg+'</div>';
                    break;
            }
            if(isNaN(code)){
                content = msg;
            }
            var exitIndex = top.layer.alert(content,{
                title : title,
                area : AppKey.layerArea,
                btn : [AppKey.confirm],
            });
            layerFn.EscLayer(exitIndex);
        },
        /**按键盘监听事件回调:opts.event({code:27,fn:function(){}});*/
        event : function(options){
            var code = options.code || null;
            var fn = options.fn || null;
            $(document).keydown(function(e){
                if(e.keyCode == code){
                    if (fn !=null && fn != undefined){
                        fn();
                    }
                }
            });
        },
    };
    /**alert('好的,谢谢!',function(){alert('嗯,再见!')})*/
    window.alert = function(msg,callback){
        var al_t = (self==top)?parent:window;
        al_t.layer.alert(msg,{
            title : AppKey.title,
            btn : [AppKey.confirm],
            cancel : function(index,layero){
                al_t.layer.close(index);
                if(callback != null && callback != ''){
                    callback();
                }
            }
        },function (index) {
            al_t.layer.close(index);
            if(callback != null && callback != ''){
                callback();
            }
        });
    };
    /**confirm('你好,需要服务吗?',function() {alert('好的,谢谢!',function(){alert('嗯,再见!')})});*/
    window.confirm = function(msg,callback){
        var conf_m = (self==top)?parent:window;
        conf_m.layer.confirm(msg,{
                title : AppKey.title,
                btn : [AppKey.confirm,AppKey.cancel],
                area : 'auto'
            },
            function(index){
                if(typeof(callback) === "function"){
                    conf_m.layer.close(index);
                    callback("ok");
                }
            }
        );
    };
    $.ajaxSetup({
        //headers: {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem("refreshToken") || ''},//这个属性不怎么好使
    });
    $.ajaxPrefilter(function(options,originalOptions,jqXHR){
        // var accessToken = sessionStorage.getItem('accessToken') || '';//当前面的为null后面的是默认值
        // var refreshToken = sessionStorage.getItem("refreshToken") || ''; //当前面的为null后面的是默认值
        // options.data += '&accessToken='+accessToken+'&refreshToken='+refreshToken;//这个好使,意思是 所有的请求都要加上accessToken=52556
        // 所有的post属性都要加上ssid
        /*if(options.method && options.method.toLowerCase() == 'post'){
            var ssid = sessionStorage.getItem('ssid'+window.ourpalmSdk) || '';
            if(!options.data){
            }else{
                if(!getQueryString('ssid',options.data)){
                    options.data += '&ssid='+ ssid
                }
            }
        }*/
    });
    verifyFn.initExtend();
    //不带请求动画少后面两个参数,一般用于select查询方法
    function getQuery(url,params,success){
        ajaxRequestGet(url,params,null,function(data){
            success(data);
        });
    }
    //含带请求动画,方法beforeSend不为空,一般用于select查询方法
    function getQueryHint(url,params,success,msg){
        msg = (msg == null || msg == '') ? "正在处理……" : msg;
        self.layerIndex = layerFn.loading(msg);
        ajaxRequestGet(url,params,function(request){},function(data){
            layerFn.closeIndex(self.layerIndex);
            success(data);
        },function(response,err){
            layerFn.closeIndex(self.layerIndex);
        },function(){
            layerFn.closeIndex(self.layerIndex);
        });
    }
    //不带请求动画少后面两个参数,一般用于insert,update,delete方法
    function postRow(url,params,success){
        ajaxRequestPost(url,params,null,function(data){
            success(data);
        });
    }
    //有带请求动画,方法beforeSend不为空,一般用于insert,update,delete方法
    function postRowHint(url,params,success,msg){
        msg = (msg == null || msg == '') ? "正在处理……" : msg;
        self.layerIndex = layerFn.loading(msg);
        ajaxRequestPost(url,params,function(request){},function(data){
            layerFn.closeIndex(self.layerIndex);
            success(data);
        },function(response,err){
            layerFn.closeIndex(self.layerIndex);
        },function(){
            layerFn.closeIndex(self.layerIndex);
        });
    }
    //后面的两个参数error,complete是给带请求动画用的
    function ajaxRequestGet(url,params,beforeSend,success,error,complete){
        ajaxGet(url,params,function(request){
            if(beforeSend){
                beforeSend(request);
            }
        },function(data){
            var bl = layerFn.checkLogin(data);
            if(bl)return;
            success(data);
        },function(response,err){
            var statusText = response.statusText;
            if(statusText == 'parsererror'){
                layerFn.noticeLogin();
                return;
            }else if(statusText == 'error'){
                layerFn.connectFailure();
                return;
            }else{
                layerFn.connectFailure();
            }
        },function(response,status){
            if(complete){
                complete(response,status);
            }
        });
    }
    function ajaxRequestGetError(url,params,beforeSend,success,error,complete){
        ajaxGet(url,params,function(request){
            if(beforeSend){
                beforeSend(request);
            }
        },function(data){
            var bl = layerFn.checkLogin(data);
            if(bl)return;
            success(data);
        },function(response,err){
            if(error){
                error(response,err);
            }
        },function(response,status){
            if(complete){
                complete(response,status);
            }
        });
    }
    //后面的两个参数error,complete是给带请求动画用的
    function ajaxRequestPost(url,params,beforeSend,success,error,complete){
        ajaxPost(url,params,function(request){
            if(beforeSend){
                beforeSend(request);
            }
        },function(data){
            success(data);
        },function(response,err){
            var statusText = response.statusText;
            if(statusText == 'parsererror'){
                layerFn.noticeLogin();
                return;
            }else if(statusText == 'error'){
                layerFn.connectFailure();
                return;
            }else{
                layerFn.alert('未知错误,稍后重试');
                return;
            }
        },function(response,status){
            if(complete){
                complete(response,status);
            }
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
            //xhrFields : {withCredentials: true},crossDomain : true,
            headers : {'accessToken': sessionStorage.getItem('accessToken') || '',"refreshToken":sessionStorage.getItem("refreshToken") || ''},
            dataType : "json",
            data : params,
            crossDomain: true == !(document.all),
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
            statusCode : {
                404 : function(response) {
                    status404(response);
                },
                500 : function(){
                    status500();
                },
                502 : function(response){
                    layerFn.alert("网关代理出错,稍候重试");
                }
            },
            complete : function(response,status){
                if(complete !=null && complete != undefined && complete != ''){
                    complete(response,status);
                }
                ajaxComplete(response.responseJSON);
            }
        });
    }
    function status404(response){
        var json = eval('('+ response.responseText +')');
        layerFn.alert("访问("+json.path+")路径不存在,你可以试试<a style='color:#1e9fff;' href='http://www.dwz.cloud' target='_blank'>引路者</a>解决找不到网页问题",AppKey.code.code199);
    }
    function status500(){
        layerFn.alert(AppKey.msg.msg204,AppKey.code.code204);
    }
    function ajaxComplete(json){
        try{
            if(json.code == AppKey.code.code205){
                layerFn.tokenLogin();return;
            }
            if(json.code == AppKey.code.code200){
                var renewal = json.renewal;
                if(renewal){
                    if(refreshFlag){
                        refreshFlag = false;
                        renewalToken();
                    }
                }
            }
        }catch(e){}
    }
    /*私有更新令牌方法*/
    function renewalToken(){
        var params = {'accessToken': (sessionStorage.getItem('accessToken') || '')};
        $.ajax({
            type : "POST",
            url : urlPrefix + '/user/renewalToken',
            dataType : "json",
            data : params,
            success : function(data){
                setTimeout(function(){
                    refreshFlag = true;
                },120000);//2分钟后可以再刷新,防止重复刷新
                if(data.code == AppKey.code.code200){
                    var token = data.data;
                    sessionStorage.setItem("accessToken",token.accessToken);
                    sessionStorage.setItem("refreshToken",token.refreshToken);
                }else if(data.code == AppKey.code.code205){
                    layerFn.tokenLogin();return;
                }else{
                    layerFn.alert(data.msg, data.code);
                }
            }
        });
    }
})(jQuery);