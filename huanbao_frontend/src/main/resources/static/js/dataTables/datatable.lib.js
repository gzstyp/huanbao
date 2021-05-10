/**
 * DataTable工具
 * @作者 田应平
 * @创建时间 2020-03-25 19:35
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
;(function(){
    /*
        pageDataTable.buttons(thisTable);
        pageDataTable.action(thisTable);
        pageDataTable.select(thisTable,tableDom);
        pageDataTable.tooltip();
     */
    pageDataTable = {
        //pageDataTable.buttons(dataTable);
        buttons : function(dataTable){
            new $.fn.dataTable.Buttons(dataTable,{
                buttons: [
                    {
                        "text" : "<i class='ace-icon fa fa-filter bigger-110 blue' id='iconColumn'></i> <span class='hidden'>自定义显示|隐藏列</span>",
                        "className" : "btn btn-white btn-primary",
                    },
                    {
                        "extend" : "csv",
                        "text" : "<i class='fa fa-database bigger-110 orange'></i> <span class='hidden'>导出CSV文件</span>",
                        "className": "btn btn-white btn-primary"
                    },
                    {
                        "extend" : "print",
                        "text" : "<i class='fa fa-print bigger-110 grey'></i> <span class='hidden'>打印</span>",
                        "className" : "btn btn-white btn-primary",
                        "autoPrint" : false,
                        "message" : '用打印机打印当前页面'
                    },
                    {
                        "text" : "<i class='ace-icon glyphicon glyphicon-refresh bigger-110' id='iconRefresh'></i> <span class='hidden'>刷新</span>",
                        "className" : "btn btn-white btn-primary"
                    },
                ]
            } );
            dataTable.buttons().container().appendTo($('.tableTools-container'));
        },
        //pageDataTable.action(dataTable);
        action : function(dataTable){
            var defaultCopyAction = dataTable.button(1).action();
            dataTable.button(1).action(function (e, dt, button, config){
                try{
                    defaultCopyAction(e, dt, button, config);
                }catch(e){}
                $('.dt-button-info').addClass('gritter-item-wrapper gritter-info gritter-center white');
            });
            //表格右上角的按钮组
            var defaultColvisAction = dataTable.button(0).action();
            dataTable.button(0).action(function (e, dt, button, config){
                try{
                    defaultColvisAction(e, dt, button, config);
                }catch(e){}
                if($('.dt-button-collection > .dropdown-menu').length == 0) {
                    $('.dt-button-collection')
                    .wrapInner('<ul class="dropdown-menu dropdown-light dropdown-caret dropdown-caret" />')
                    .find('a').attr('href','javascript:;').wrap("<li />");
                }
                $('.dt-button-collection').appendTo('.tableTools-container .dt-buttons');
            });
            setTimeout(function() {
                $($('.tableTools-container')).find('a.dt-button').each(function() {
                    var div = $(this).find(' > div').first();
                    if(div.length == 1) div.tooltip({container: 'body', title: div.parent().text()});
                    else $(this).tooltip({container: 'body', title: $(this).text()});
                });
            }, 500);
        },
        //pageDataTable.select(dataTable,'#dynamic-table');
        select : function(dataTable,tableDom){
            /*选择*/
            dataTable.on('select', function ( e, dt, type, index ) {
                if ( type === 'row' ) {
                    $(dataTable.row(index).node()).find('td:eq(0) input:checkbox').prop('checked', true);
                }
            });

            /*取消选择*/
            dataTable.on('deselect', function ( e, dt, type, index ) {
                if ( type === 'row' ) {
                    $(dataTable.row(index).node()).find('td:eq(0) input:checkbox').prop('checked', false);
                }
            });

            //(基于表头的复选框的选择/取消选择而全选选择或全部取消选择)select/deselect all rows according to table header checkbox
            $(tableDom +' > thead > tr > th input[type=checkbox], '+tableDom+'_wrapper input[type=checkbox]').eq(0).on('click', function(){
                var th_checked = this.checked;
                $(tableDom).find('tbody > tr').each(function(){
                    var row = this;
                    if(th_checked) dataTable.row(row).select();
                    else  dataTable.row(row).deselect();
                });
            });

            //(基于每一行的第一列的选择/取消选择而该行被选择或取消选择,留着) a row when the checkbox is checked/unchecked
            /*$(tableDom).on('click', 'td:eq(0) input[type=checkbox]' , function(){
                var row = $(this).closest('tr').get(0);
                if(!this.checked) dataTable.row(row).deselect();
                else dataTable.row(row).select();
            });*/

            /*点击后显示更多???*/
            /*$(document).on('click',tableDom+'.dropdown-toggle', function(e) {
                e.stopImmediatePropagation();
                e.stopPropagation();
                e.preventDefault();
            });*/
        },
        // pageDataTable.tooltip();
        tooltip : function(){
            $('[data-rel="tooltip"]').tooltip(
                {
                    placement : function(context, source) {
                        var $source = $(source);
                        var $parent = $source.closest('table');
                        var off1 = $parent.offset();
                        var w1 = $parent.width();
                        var off2 = $source.offset();
                        if(parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
                        return 'left';
                    }
                }
            );
        },
        //iDisplayLength : pageDataTable.iDisplayLength,
        iDisplayLength : 20,
        //aLengthMenu : pageDataTable.aLengthMenu,
        aLengthMenu : [20,50,99],
        /*用法:var thisTable = pageDataTable.initDataTable({tableDom:'#table',sAjaxSource:url,fnServerParams : function(params){}});*/
        initDataTable : function(options){
            var tableDom = (options.tableDom) ? options.tableDom : '#';//必填
            var dom = (options.dom) ? options.dom : "t<'row'<'col-sm-2 hidden-480'l><'col-sm-3 hidden-480'i><'col-sm-7'p>>";
            var aaSorting = (options.aaSorting) ? options.aaSorting : [];
            var iDisplayLength = (options.iDisplayLength) ? options.iDisplayLength : pageDataTable.iDisplayLength;
            var aLengthMenu = (options.aLengthMenu) ? options.aLengthMenu : pageDataTable.aLengthMenu;
            var bStateSave = (options.bStateSave) ? options.bStateSave : false;
            var sAjaxSource = (options.sAjaxSource) ? options.sAjaxSource : '';//必填
            var fnServerParams = (options.fnServerParams) ? options.fnServerParams:null;//必填
            var aoColumns = (options.aoColumns) ? options.aoColumns:[];//必填
            var columnDefs = (options.columnDefs) ? options.columnDefs:[];//必填
            return $(tableDom).DataTable({
                dom : dom,
                bAutoWidth : false,
                aaSorting : [],
                iDisplayLength : iDisplayLength,
                aLengthMenu : aLengthMenu,
                bServerSide : true,
                serverSide : true,
                bDeferRender : true,
                select : {
                    style : 'multi',
                    //selector : 'td:first-child'//指定选择器,即指定哪一个列或哪一个元素作为该行的选中或取消选中
                    selector : 'td:first-child input'
                },
                searching : false,
                destroy : true,
                bSortMulti : false,
                bStateSave : bStateSave,
                fnServerData : winFn.dataTableData,//此处是 function (url,params,callback,oSettings){},//oSettings是各个参数,但已封装集成在一块了,方便维护管理
                sAjaxSource : sAjaxSource,
                fnServerParams : function(params){
                    if(fnServerParams != null){
                        fnServerParams(params);
                    }
                },
                aoColumns : aoColumns,
                columnDefs : columnDefs,
                /*每一行创建完调用的函数*/
                createdRow : function (row,data,index){
                    // row : tr dom
                    // data: row data
                    // dataIndex:row data's index
                    /*if (index === 1){
                        $(row).css({"background":"#81adee"});
                    }
                    if (index === 2){
                        $(row).addClass('selected');
                        $(row).find('td:eq(0) input[type=checkbox]').prop("checked",true);
                    }*/
                    //console.info(row);
                    //console.info(data);
                    //console.info(index);
                    //$(row).find("td:eq(0)").css({"cursor":"pointer"}).attr("title","选择|取消");//好使,显示第1列为手型的鼠标形状
                    $(row).find("td:eq(0)").attr("title","选择|取消");//好使,显示第1列为手型的鼠标形状
                    $(row).find("td:eq(0) input[type=checkbox]").attr("class","ace");
                },
                /*每一行被创建，但还没有被加载到document中，这个函数优先于createdRow,个人觉得用createdRow更好*/
                rowCallback : function (row,data,index) {
                    // row : tr dom
                    // data: row data
                    // index:row data's index
                    /*if(index === 3){
                        $('td:eq(2)',row).html('<b>已被rowCallback方法更改了内容</b>');
                    }*/
                },
                /*thead被描画后调用*/
                headerCallback : function (thead, data, start, end, display) {
                    //thead:dom， data:原始的datatable数据，display：当前显示排序好的datatable数据（有可能经过排序）
                    // start end ：当前dispaly数据的开始结束序号
                    //$(thead).find('th').eq(0).html( 'Displaying '+(end-start)+' records');
                    //console.info(thead);//输出结果:<tr role="row">
                    //console.info(data);//输出结果:是后端返回的原始数据
                    //console.info(start);//输出结果:0
                    //console.info(end);//输出结果:4]
                    //console.info(display);//输出结果:[0,1,2,3,4]
                    //$(thead).find('th').css("background","red");//表头样式
                    var len = $(thead).find('th').length;
                    $($(thead).find('th')).each(function(index,data){
                        if(index != 0 && index < len-1){
                            $(data).attr("title",$(data).text());
                        }
                    });
                },
                /*tfoot被描画后调用，通常可用于计算合计值*/
                footerCallback : function (tfoot, data, start, end, display) {
                        //tfoot:dom， data:原始的datatable数据，display：当前显示排序好的datatable数据（有可能经过排序）
                        // start end ：当前dispaly数据的开始结束序号
                    //var api = this.api();
                    //console.info(this);//输出结果:table dom
                    //console.info(api.column(2));//输出结果:Object[[2]],这个是数组,表示所有行的第3列
                    //console.info(api.column(2).data());//输出结果:Object["main", "parent_sys_setting", "sys_menu", "sys_user", "sys_role"],这个是数组，表示所有行的第3行依次从上到下的值
                    //console.info(api.column(3).data());//输出结果:Object[1, 1, 2, 2, 2],这个是数组表示第1行的第列的值是1;
                    //$(api.column(3).footer()).html(api.column(3).data().reduce(function(a,b){},0));//表示看不懂
                    var permissions = $("div").data("permissions");
                    if(permissions != null && permissions.length > 0){
                        setTimeout(function(){
                            $.each(permissions,function(index,value){
                                //$("[permission='"+data+"']").hide();
                                $("[permission='"+value+"']").css({"display":"inline"});
                                var exist = $('#'+value).attr('id');
                                if(exist){
                                    $('#' + value).val('1');/*有值就有权限*/
                                }
                            });
                        },30);
                    }
                },
                /*操作dom应放在initComplete操作???,可以在此操作判断后端返回的数据值是否显示列,https://www.cnblogs.com/daixinyu/p/6866516.html*/
                fnInitComplete : function(settings,data){
                    //$("table"+tableDom+" tbody tr").find("td:eq(0)").css({"cursor":"pointer"});
                    var columnData = sessionStorage.getItem(tableDom.substring(1));//获取之前已保存的自定义显示列
                    if(columnData){
                        var api = $(tableDom).DataTable();
                        var kv = JSON.parse(columnData);
                        $.each(kv,function(k,v){
                            api.column(k).visible(v,false);//api的用法
                        });
                        api.columns.adjust().draw(false);//api的用法
                    }
                }
            });
        },
        /*用法:pageDataTable.columnCustom(thisTable,tableDom,'#divKeyColumns');*/
        columnCustom : function(thisTable,tableDom,divDom){
            var thisApi = $(tableDom).DataTable();
            var header = thisTable.columns().header();//api用法
            var len = header.length - 1;
            var localColumnData = tableDom.substring(1);//有两个地方调用到:1是生成input[type=checkbox]时,2是当已保存后再此读取时赋值给input[type=checkbox]是否显示列
            var columnData = sessionStorage.getItem(localColumnData);
            var html = "<table class='table table-striped table-bordered table-hover no-margin-bottom no-border-top'>";
            html += "<thead>";
            html += "<tr>";
            html += "<th width='40' class='center'>";
            html += "<i class='ace-icon glyphicon glyphicon-list bigger-110'></i>";
            html += "</th>";
            html += "<th>列名</th>";
            html += "<th width='40' class='center'>";
            html += "<i class='ace-icon fa fa-eye bigger-110'></i>";
            html += "</th>";
            html += "</tr>";
            html += "</thead>";
            html += "<tbody>";
            for(var i = 1; i < len; i++){
                html += "<tr>";
                html += "<td class='center'>";
                html += "<label>"+i+"</label>";
                html += "</td>";
                html += "<td>"+header[i].innerText+"</td>";
                html += "<td class='center'>";
                html += "<input type='checkbox' id='"+(localColumnData+i)+"'/><label style='margin:0px;'></label>";
                html += "</td>";
                html += "</tr>";
            }
            html += "</tbody>";
            html += "</table>";
            $(divDom).html(html);
            if(columnData){
                var kv = JSON.parse(columnData);
                $.each(kv,function(k,v){
                    $('#'+(localColumnData+k)).prop("checked",v);
                });
            }else{
                for(var i = 1; i < len;i++){
                    var check = thisApi.column(i).visible();
                    $('#'+(localColumnData+i)).prop("checked",check);
                }
            }
            $(divDom).hcheckbox();//初始化样式
            layerFn.addOrEdit('选择自定义显示隐藏列',divDom,['500px','430px'],function(index,layero){
                var json = {};
                for(var i = 1; i < len; i++){
                    var checked = $('#'+localColumnData+i).prop("checked");
                    json[i] = checked;
                    thisApi.column(i).visible(checked,false);
                }
                thisApi.columns.adjust().draw(false);
                layerFn.closeIndex(index);
                if(!sessionStorage){
                    layerFn.alert('不支持sessionStorage,操作失败',AppKey.code.code199);
                }else{
                    sessionStorage.setItem(localColumnData,JSON.stringify(json));//保存的自定义显示列
                }
            });
        },
        //多于length个字符才显示省略号;注意需要在 方法 formatColumn 的前面执行
        columnEllipsis : function(value,length){
            if(value){
                if (value.length > length){
                    return "<span title='"+value+"'>"+value.substr(0,length) +"<a style='outline:none;text-decoration: none;' href='javascript:;' title='查看' onclick='javascript:layerFn.alert(\""+value+"\")'>…</a></span>";
                }else{
                    return "<span title='"+value+"'>"+value+"</span>";
                }
            }else{
                return "";
            }
        },
        formatColumn : function(value){
            var width = winFn.fnGetWidth();
            if(width >= 1280){
                if(width >= 1366){
                    if(width >= 1440){
                        if(value){
                            if (value.length > 25){
                                return "<span title='"+value+"'>"+value.substr(0,25) +"<a style='outline:none;text-decoration: none;' href='javascript:;' title='查看' onclick='javascript:layerFn.alert(\""+value+"\")'>…</a></span>";
                            }else{
                                return "<span title='"+value+"'>"+value+"</span>";
                            }
                        }else{
                            return "";
                        }
                    }else{
                        if(value){
                            if (value.length > 22){
                                return "<span title='"+value+"'>"+value.substr(0,22) +"<a style='outline:none;text-decoration: none;' href='javascript:;' title='查看' onclick='javascript:layerFn.alert(\""+value+"\")'>…</a></span>";
                            }else{
                                return "<span title='"+value+"'>"+value+"</span>";
                            }
                        }else{
                            return "";
                        }
                    }
                }else{
                    if(value){
                        if (value.length > 18){
                            return "<span title='"+value+"'>"+value.substr(0,18) +"<a style='outline:none;text-decoration: none;' href='javascript:;' title='查看' onclick='javascript:layerFn.alert(\""+value+"\")'>…</a></span>";
                        }else{
                            return "<span title='"+value+"'>"+value+"</span>";
                        }
                    }else{
                        return "";
                    }
                }
            }else{
                if(value){
                    if (value.length > 16){
                        return "<span title='"+value+"'>"+value.substr(0,16) +"<a style='outline:none;text-decoration: none;' href='javascript:;' title='查看' onclick='javascript:layerFn.alert(\""+value+"\")'>…</a></span>";
                    }else{
                        return "<span title='"+value+"'>"+value+"</span>";
                    }
                }else{
                    return "";
                }
            }
        }
    };
})(jQuery);