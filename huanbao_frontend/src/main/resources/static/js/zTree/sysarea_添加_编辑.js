$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sysarea/list',
        datatype: "json",
        colModel: [			
			{ label: '区域主键',name: 'kid', index: 'kid', width: 60, key: true },
			{ label: '区域名称', name: 'name', index: 'name', width: 100 },
			{ label: '父级名称', name: 'pName', index: 'pName', width: 100 },
			{ label: '区域级别', name: 'level', index: 'level', width: 30, formatter: function(value, options, row){
                var show = "";
                switch (value){
                    case 1:
                        show = '省';
                        break;
                    case 2:
                        show = '市';
                        break;
                    case 3:
                        show = '县';
                        break;
                    case 4:
                        show = '镇';
                        break;
                    case 5:
                        show = '村';
                        break;
                    default:
                        show = '';
                        break;
                }
                return show;
                }
            }
        ],
		viewrecords: true,
        height: (pageInfo().height-130),
        rowNum: 15,
        rowList : [15,50,99],
        loadtext : '<img src="../../statics/plugins/layer/skin/default/loading-1.gif" style="margin-right:10px;"/>正在加载,请稍候……',
        rownumbers: false,//行号
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        beforeRequest : function(){},
        loadComplete : function(data){
            if(data.code == 0){
                return data;
            }else if(data.code == 205){
                invalidAccess();
            }else{
                layer.alert(data.msg);
            }
        },
        loadError : function(xhr,status,error){},
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
});

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
        chkStyle : "radio",
        chkboxType : {"Y":"","N":"s"}
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
            if(treeNode){}
        },
        onAsyncSuccess : function(data){},
        onAsyncError : function(){
            thisPage.ztreeGetError('连接服务器失败');
        },
        beforeClick : function(treeId,node,clickFlag){
            return false;//true时点击选中文本
        },
        onClick : function(event,treeIdDom,node,clickFlag){},
        beforeCheck : function(treeId,treeNode){},
        onCheck : function(event,treeId,treeNode){}
    }
};

thisPage = {
    initDom : function(){
        thisPage.initTree();
    },
    initTree : function(){
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
        $('#parentValue').val('');
        $('#parentName').val('');
    },
    showAreaTree : function(){
        addOrEdit('选择代理区域','#proxyAreaLayer',['500px','500px'],function(indexLayero,layero){
            var nodes = tree_obj.getCheckedNodes(true);
            var length = nodes.length;
            if(length > 1){
                alert('仅支持一个父节点');
                return;
            }
            layer.close(indexLayero);
            nodes = nodes[0];
            if(nodes){
                vm.sysArea.pid = nodes.kid;
                $('#parentValue').val(nodes.kid);
                $('#parentName').val(nodes.name);
            }else{
                vm.sysArea.pid = '0';
                vm.sysArea.pName = '';
                $('#parentValue').val('');
                $('#parentName').val('');
            }
        });
    },
}
thisPage.initDom();

var vm = new Vue({
	el:'#witapp',
	data:{
		showList: true,
		title: null,
		sysArea: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
            thisPage.resetValue();
			vm.showList = false;
			vm.title = "新增";
			vm.sysArea = {level:0};
            $('#level').val('0');
		},
		update: function (event) {
			var kid = getSelectedRow();
			if(kid){
                thisPage.resetValue();
                vm.showList = false;
                vm.title = "修改";
                vm.getInfo(kid);
            }
		},
		saveOrUpdate: function (event) {
            var url = vm.sysArea.kid == null ? "sysarea/save" : "sysarea/update";
            vm.sysArea.level = $('#level').val();
            ajaxExecute(baseURL + url,JSON.stringify(vm.sysArea),function(data){
                if(data.code == 0){
                    dialogResult(data.msg);
                    vm.reload();
                }else{
                    alert(data.msg);
                }
            });
		},
		del: function (event) {
			var kids = getSelectedRows();
			if(kids){
                confirm('确定要删除选中的记录？', function(){
                    ajaxExecute(baseURL + "sysarea/delete",JSON.stringify(kids),function(data){
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
		getInfo: function(kid){
            ajaxQuery(baseURL + "sysarea/info/"+kid,{},function(data){
                if(data.code == 0){
                    $('#level').val(data.sysArea.level);
                    vm.sysArea = data.sysArea;
                }else{
                    alert(data.msg);
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData:{'name':$('#name').val()},
                page:page
            }).trigger("reloadGrid");
            thisPage.initTree();//刷新树
		}
	}
});