var domTree = 'proxyAreaTree';
var tree_obj = null;
var settingArea = {
    view : {
        expandSpeed : 100,
        showLine : true,
        showIcon : true,
        fontCss : {"color":"#000","outline":"none","text-decoration":"none","font-size":"14px"}
    },
    check : {
        enable : true,
        chkStyle : "checkbox",
        chkboxType : {"Y":"s","N":"ps"}
    },
    data : {
        simpleData : {
            enable : true,
            idKey : "kid",
            pIdKey : "pid"
        }
    },
    async : {
        enable : true,
        url : '/sysarea/queryAreaTree',
        cache : false,
        type: "GET",
        autoParam : ["kid"],
        dataFilter: function(treeId,parentNode,result){
            if(0 == result.code){
                return result.data
            }else if(205 == result.code){
                invalidAccess();
            }else{
                return '';
            }
        }
    },
    callback : {
        beforeAsync : function(treeId,treeNode){
            if(treeNode){
                var level = treeNode.level;
                if(level == 2){
                    return false;
                }
            }
        },
        onAsyncSuccess : function(data){},
        onAsyncError : function(){
            thisPage.ztreeGetError('连接服务器失败');
        },
        beforeClick : function(treeId,node,clickFlag){
            return false;//true时点击选中文本
        },
        onClick : function(event,treeIdDom,node,clickFlag){},
        beforeCheck : function(treeId,treeNode){
            if(!treeNode.isParent){
                return false;
            }
            return treeNode.level == 2;//仅支持县级代理
        },
        onCheck : function(event,treeId,treeNode){}
    }
};

$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户id', hidden:true, name: 'userId', index: "user_id", width: 45, key: true},
			{ label: '用户名', name: 'username', width: 46},
            { label: '所属经营主体名称', name: 'subjectName', width: 95},
            { label: '所属部门', name: 'deptName',index: "dept_name", width: 55},
            { label: '角色名称', name: 'roleName', width: 55},
			{ label: '邮箱', name: 'email', width: 75},
			{ label: '手机号', name: 'mobile', width: 46},
			{ label: '状态', name: 'status', width: 22, formatter: function(value, options, row){
			    var show = "";
			    switch (value){
                    case 0:
                        show = '<span class="label label-danger">禁用</span>';
			            break;
			        case 1:
                        show = '<span class="label label-success">正常</span>';
			            break;
			        case 2:
                        show = '<span class="label label-warning">待审核</span>';
			            break;
			        case 3:
                        show = '<span class="label label-warning">未缴费</span>';
			            break;
			        default:
			            break;
			    }
				return show;
			}},
            {label: '类型', name: 'employeeId',index: "employee_id",width: 20, formatter: function(value, options, row){
                if(value){
                    return (value == -1) ? '员工' : '主体';
                }else{
                    return '';
                }
			}},
			{label: '创建时间', name: 'createTime', index: "create_time", width: 35}
        ],
		viewrecords: true,
        height: (pageInfo().height-130),
        rowNum: 15,
		rowList : [15,50,99],
        loadtext : '<img src="../../statics/plugins/layer/skin/default/loading-1.gif" style="margin-right:10px;"/>正在加载,请稍候……',
        rownumbers: false,
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        loadComplete : function(data){
            if(data.code == 0){
                return data;
            }else if(data.code == 205){
                invalidAccess();
            }else{
                layer.alert(data.msg);
            }
        },
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

    thisPage = {
        initDom : function(){
            tree_obj = $.fn.zTree.init($("#"+domTree),settingArea);
        },
        getAreaData : function(pid,containerDom,selectDom,labelText){
            selectArea.getData('/sysarea/getListArea',pid,containerDom,selectDom,labelText);
        },
        ztreeGetError : function(msg){
            msg = (msg == null || msg == '') ? '获取数据失败' : msg;
            $('#treeDom').html("<li style='margin-top:2px;'><span>"+msg+"</span></li>");
        },
        //重置
        resetValue : function(){
            tree_obj.cancelSelectedNode();
            tree_obj.checkAllNodes(false);//仅对 chkStyle = "checkbox"有效
            $('#selected_areas').val('');
            $('#textarea_selected').val('');
        }
    }
    thisPage.initDom();
});

var vm = new Vue({
    el:'#witapp',
    data:{
        q:{
            username: null
        },
        showList: true,
        flagAdd : true,
        title:null,
        roleList:[],
        webmaster:null,
        optsValue:null,
        user:{
            status:1,
            deptId:null,
            deptName:null,
            roleIdList:[]
        }
    },
    watch : {
        optsValue: function (options){
            switch (options){
                case 2:
                    $('#div_proxy_area').css({'display':''});
                    break;
                case 3:
                    $('#div_proxy_area').css({'display':''});
                    break;
                default:
                    $('#div_proxy_area').css({'display':'none'});
                    break;
            }
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.flagAdd = true;
            vm.title = "添加主体账号";
            vm.roleList = [];
            vm.user = {deptName:null, deptId:null, status:1, type:0, roleIdList:[]};
            //获取角色信息
            this.getRoleList();
        },
        update: function (){
            var dataRow = getDataRow();
            if(dataRow){
                var username = dataRow['username'];
                if(username == 'admin'){
                    alert('不允许操作超级管理员!');
                    return;
                }
                var employeeId = dataRow['employeeId'];
                if(employeeId == '员工'){
                    alert('员工账号不支持操作');
                    return;
                }
                var userId = dataRow['userId'];
                vm.showList = false;
                vm.flagAdd = false;
                vm.title = "编辑主体账号";
                vm.getUser(userId);
                //获取角色信息
                this.getRoleList();
            }
        },
        /*审核*/
        check: function () {
            var dataRow = getDataRow();
            if(dataRow){
                var userId = dataRow['userId'];
                var username = dataRow['username'];
                confirm('你确定要审核'+username+'通过吗?',function(){
                    ajaxExecute(baseURL + "sys/user/check/"+userId,{},function(data){
                        if(data.code == 0){
                            dialogResult(data.msg);
                            vm.reload();
                        }else{
                            alert(data.msg);
                        }
                    });
                });
            }
        },
        resetting:function () {
            var dataRow = getDataRow();
            if(dataRow){
                var username = dataRow['username'];
                var userId = dataRow['userId'];
                top.layer.prompt({title:'请给['+username+']输入新的密码'},function(val,indexTemp){
                    var expr = /([a-zA-Z0-9!@#$%^&*()_?<>{}]){6,16}/;
                    if(!expr.test(val)){
                        alert("密码长度在6-16位!");
                        return;
                    }
                    top.layer.close(indexTemp);
                    ajaxExecute("/sys/user/resetting?value="+val+"&userId="+userId,{},function(data){
                        if(0 == data.code){
                            dialogResult(data.msg);
                        }else{
                            alert(data.msg);
                        }
                    });
                });
            }
        },
        del: function () {
            var userIds = getSelectedRows();
            if(userIds){
                confirm('确定要删除选中['+userIds.length+']条记录？', function(){
                    ajaxExecute(baseURL + "sys/user/delete",JSON.stringify(userIds),function(data){
                        if(data.code == 0){
                            vm.reload();
                        }else{
                            alert(data.msg);
                        }
                    });
                });
            }
        },
        saveOrUpdate: function () {
            var user_username = $('#user_username').val();
            if(user_username == null || user_username.length <= 0){
                alert("请输入用户名!");
                return;
            }
            var areaJson = $('#selected_areas').val();
            vm.user.areaJson = areaJson;
            var user_mobile = $('#user_mobile').val();
            if(user_mobile == null || user_mobile.length <= 0){
                alert("请输入手机号码!");
                return;
            }
            var expr = new RegExp("^1[34578][0-9]{9}$",'i');
            if(!expr.test(vm.user.mobile)){
                alert("手机号码有误!");
                return;
            }
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            ajaxExecute(baseURL + url,JSON.stringify(vm.user),function(data){
                if(data.code == 0){
                    dialogResult(data.msg);
                    vm.reload();
                }else{
                    alert(data.msg);
                }
            });
        },
        getUser: function(userId){
            $.get(baseURL + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                var options = r.user.options;
                if(options == 2 || options == 3){
                    $('#div_proxy_area').css({'display':''});
                }else{
                    $('#div_proxy_area').css({'display':'none'});
                }
                vm.user.password = null;
            });
        },
        getRoleList: function(){
            thisPage.resetValue();
            ajaxDataQuery('/sys/role/select',{},function(data){
                vm.roleList = data.list;
                if(data.webmaster){
                    $('#div_controller_role').css({"display":""});
                    vm.webmaster = data.webmaster;
                }else{
                    $('#div_controller_role').css({"display":"none"});
                }
            });
        },
        roleOptions : function(options){
            vm.optsValue = options;
            vm.user.options = options;
        },
        showAreaTree : function(){
            addOrEdit('选择代理区域','#proxyAreaLayer',['500px','500px'],function(indexLayero,layero){
                var nodes = tree_obj.getCheckedNodes(true);
                var names = '';
                $.each(nodes,function(index,data){
                    var value = data.name;
                    if(value != null && value != ''){
                        if(names.length > 0)
                            names += ",";
                        names += value;
                    }
                });
                var areas = {};
                for(var k=0;k<nodes.length;k++){
                    var obj = nodes[k];
                    areas[obj.kid] = obj.name;
                }
                layer.close(indexLayero);
                $('#selected_areas').val(JSON.stringify(areas));//JSON.parse(columnData);
                $('#textarea_selected').val(names);
            },'<span style="color:#f84009;">重置</span>',function(){
                thisPage.resetValue();
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'username': vm.q.username},
                page:page
            }).trigger("reloadGrid");
        }
    }
});