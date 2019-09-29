<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>个人设置</title>
    <link href="${ctx}/css/bootstrap/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
    <link href="${ctx}/css/zui/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet">
    <script src="${ctx}/css/zui/lib/datetimepicker/datetimepicker.min.js"></script>
    <style>
        body,html{
            background-color: #EBEBEB;
            padding: 0;
            margin: 0 auto;
            width: 1240px;
            height:100%;
        }
        body {
            font: 14px/1.5 "PingFang SC","Lantinghei SC","Microsoft YaHei","HanHei SC","Helvetica Neue","Open Sans",Arial,"Hiragino Sans GB","微软雅黑",STHeiti,"WenQuanYi Micro Hei",SimSun,sans-serif;
        }

        .main-content a{
            text-decoration: none;
        }
        .title-content a{
            text-decoration:none;
        }
        .stats-buttons  a{
            text-decoration:none;
        }

        a {
            color: inherit;
            outline: 0;
        }

        .designer-card img {
            border-radius: 50%;
            vertical-align: middle;
        }
        img {
            border: 0;
        }
        a {
            color: inherit;
            outline: 0;
        }
        a:-webkit-any-link {
            cursor: pointer;

        }
        .foot-nav-col li{
            list-style: none;
            margin-left: 70px;
        }
        .foot-nav-col h3{
            margin-left:90px;
        }
        .foot-nav-col a{
            text-decoration:none;
            color:grey;

        }
        .foot-nav-col a:link,a:visited { color:grey;}
        .foot-nav-col a:hover,a:active { color: #6318ff;}

        .foot-nav-col{
            margin-top: 10px;
            float: left;
        }

        .author img {
            width: 35px;
            height: 35px;
            border-radius: 35px;
            padding: 0;
            margin-right: 10px;
        }
        fieldset, img {
            border: 0;
        }
        .author a, .author span {
            float: left;
            font-size: 14px;
            font-weight: 700;
            line-height: 35px;
            color: #9b8878;
            text-decoration: none;
        }

        .tab li{list-style:none}
        .table tr:hover{background-color: #dafdf3;}
    </style>


</head>
<body>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid" style="padding-left: 40px;">
        <!-- 导航头部 -->
        <div class="navbar-header">
            <!-- 移动设备上的导航切换按钮 -->
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
                <span class="sr-only">切换导航</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!-- 品牌名称或logo -->
            <a class="navbar-brand" href="javascript:void(0);">个人空间</a>
        </div>
        <!-- 导航项目 -->
        <div class="collapse navbar-collapse navbar-collapse-example">
            <!-- 一般导航项目 -->
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">我的梦</a></li>
                <li><a href="${ctx}/index_list">首页</a></li>

                <!-- 导航中的下拉菜单 -->
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">设置 <b class="caret"></b></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="#">任务</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li><a href="#">消息</a></li>
            </ul>

            <ul class="nav navbar-nav">
                <li><a href="${ctx}/writedream?id=${user.id}">写梦</a></li>
            </ul>
            <ul class="nav navbar-nav" style="margin-left: 550px">
                <li><a href="${ctx}/list?id=${user.id}">${user.nickName}

                </a></li>
            </ul>
            <img src="${ctx}/images/q.png" width="30" style="margin-top: 4px"/>
        </div><!-- END .navbar-collapse -->
    </div>

</nav>
    <!-- -->
<div style="margin-left: 75px;margin-top: 20px;" class="main-content">
    <div style="width: 300px;height: 600px;background-color:#D1D1D1 ;float: left">
        <div>
        <div class="personal-image" style="width: 300px;height: 240px;">

                <a  title="${user.nickName}" class="avatar"><img id="img-change"  src="${user.imgUrl}" onclick="selectImg();" width="100" height="100" style="border-radius:50%;margin-top: 60px;margin-left: 90px"></a>
                <form id="upload-form"   style="width:auto;" >
                    <input type="file"  id="change-img" name="uploadImg" onchange="changeImg();" style="display:none;">
                </form>

            <div style="float: left;width:280px;text-align:center;margin-top: 10px;font-size: 18px">
                ${user.nickName}
            </div>

        </div>

        </div>




        <div  style="width: 300px;height: 100%">

            <div id="base" onclick="base_set();" style="background-color: white" >
                <div style="width:100%; height:2px; border-top:1px solid #F2F2F2; clear:both;"></div>
            <div  style="font-size: 18px;text-align: center;height: 60px;line-height: 60px" >
                <i class="icon-x icon-spin icon-cog"></i>&nbsp;基本设置
            </div>
                <div style="width:100%; height:2px; border-top:1px solid #F2F2F2; clear:both;"></div>
            </div>

            <div  id="account" onclick="account_set();">

                <div style="font-size: 18px;text-align: center;height: 60px;line-height: 60px">
                    <i class="icon icon-tags"></i> 账号设置
               </div>
                <div style="width:100%; height:2px; border-top:1px solid #F2F2F2; clear:both;"></div>
            </div>

            <div id="binding"   onclick="binding_set();">

            <div style="font-size: 18px;text-align: center;height: 60px;line-height: 60px" >
                <i class="icon icon-link"></i>&nbsp;绑定设置
            </div>
                <div style="width:100%; height:2px; border-top:1px solid #F2F2F2; clear:both;"></div>
            </div>
        </div>

    </div>






    <div style="width: 800px;height: 600px;background-color: white;float: left;">
        <div style="width: 100%;font-size: 22px;margin-top: 80px;margin-left: 60px">
           <strong id="set_title">基本设置</strong>
          <hr>
        </div>


        <div id="base_content" style="margin-top: 40px;margin-left: 60px;font-size: 18px;" >

            <div>
                <form id="userInfo_form" action="${ctx}/saveUserInfo" method="post">

                    姓名：<input id="name" name="name" value="${userInfo.name}"/><br/><br/>

                    昵称：<input id="nick_name" name="nick_name" value="${user.nickName}"/><br/><br/>


                    性别：  <input type="radio" name="sex" value="0" checked <c:if test="${userInfo.sex eq '0'}"> checked="checked"</c:if>/>男 &nbsp;&nbsp;
                            <input type="radio" name="sex" value="1" <c:if test="${userInfo.sex eq '1'}"> checked="checked"</c:if>/>女<br/><br/>

                    生日： <input  style="width: 198px;float: right;margin-right: 484px;margin-top: -4px" class="form-control form-date"  readonly="readonly" placeholder="选择一个日期：yyyy-MM-dd" type="text" id="txtEndDate"  name="birthday" value="${userInfo.formateBirthday==null?"":userInfo.formateBirthday}"/><br/><br/>


                    地址： <input id="address" name="address" value="${userInfo.address}"/><br/><br/>

                    <button onclick="saveUserInfo();" style="background-color: #4876FF;width: 80px;margin-top: 30px"  class="btn btn-block " type="button"><span style="color: white">保存</span></button>
                </form>
            </div>
        </div>

        <div id="account_content" style="margin-top: 40px;margin-left: 60px;font-size: 18px;display: none" >

            <div>
                <form>
                账号：${user.email}<br/><br/>
                    密码：******  <a href="${ctx}/repassword"><span id="password_span" style="color: grey;margin-left: 142px" onmouseover="changeColor(this);" onmouseout="backColor(this);" >修改</span></a><br/><br/>
                    <c:if test="${not empty user.phone}">
                    手机号：${user.phone.substring(0,3)}****${user.phone.substring(7,11)}  <span id="phone_span" style="color: grey;margin-left: 68px" onmouseover="changeColor(this);" onmouseout="backColor(this);">修改</span>
                    </c:if>
                    <c:if test="${empty user.phone}">
                        手机号：<span id="phone_span" style="color: grey;margin-left: 180px" onmouseover="changeColor(this);" onmouseout="backColor(this);">修改</span>
                    </c:if>
                </form>
            </div>
        </div>


        <div id="binding_content" style="margin-top: 40px;margin-left: 60px;font-size: 18px;display: none" >

            <div>
                <form>
                    <c:if test="${empty weixin}">
                        <div style="float: left">
                            微信：未绑定
                        </div>
                        <div style="float: left;margin-left: 150px">
                            <span id="wx_span" style="color: grey" onmouseover="changeColor(this);" onmouseout="backColor(this);">立即绑定</span>
                        </div>
                    </c:if>
                    <c:if test="${not empty weixin}" >
                        <div style="float: left">
                            微信：已绑定
                        </div>
                        <div style="float: left;margin-left: 150px">
                            <span id="wx_span_remove" style="color: grey" onmouseover="changeColor(this);" onmouseout="backColor(this);">解除绑定</span>
                        </div>
                    </c:if>

                    <br/><br/>
                    <c:if test="${empty qq}">
                    <div style="float: left;">
                        Q Q：未绑定
                    </div>
                    <div style="float: left;margin-left: 150px" onclick="binding_qq();">
                        <span id="qq_span" style="color: grey"  onmouseover="changeColor(this);" onmouseout="backColor(this);">立即绑定</span>
                    </div>
                    </c:if>
                    <c:if test="${not empty qq}">
                        <div style="float: left;">
                            Q Q：已绑定
                        </div>
                        <div style="float: left;margin-left: 150px" onclick="qq_span_remove();">
                            <span id="qq_span_remove" style="color: grey" onmouseover="changeColor(this);" onmouseout="backColor(this);">解除绑定</span>
                        </div>
                    </c:if>

                    <br/><br/>
                    <c:if test="${empty weibo}">
                    <div style="float: left;">
                        微博：未绑定
                    </div>
                    <div style="float: left;margin-left: 150px">
                        <span id="wb_span" style="color: grey" onmouseover="changeColor(this);" onmouseout="backColor(this);">立即绑定</span>
                    </div>
                    </c:if>
                    <c:if test="${not empty weibo}">
                        <div style="float: left;">
                            微博：已绑定
                        </div>
                        <div style="float: left;margin-left: 150px">
                            <span id="wb_span_remove" style="color: grey" onmouseover="changeColor(this);" onmouseout="backColor(this);">解除绑定</span>
                        </div>
                    </c:if>

                </form>
            </div>
        </div>

    </div>

</div>




</body>
<script>
    //点击图片事件
    function selectImg() {
        document.getElementById("change-img").click();
    }
    //图片选择后事件
    function changeImg() {
        var formData = new FormData($( "#upload-form" )[0]);
        $.ajax({
            url: '/fileUpload' ,
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var msg = data["error"];
                if(msg==0){
                    //上传成功
                    var url = data["url"];
                    document.getElementById("img-change").src = url;
                    saveImg(url);
                }
            }
        });
    }
    //保存个人头像
    function saveImg(url) {
        $.ajax({
            type:'post',
            data: {"url":url},
            url: '/saveImage' ,
            dataType:'json',
            success: function (data) {
                // alert(data["msg"]);
            }
        });
    }

    /**
     * 切换成基本设置
     */
    function base_set() {
        document.getElementById("base").style.backgroundColor = "white";
        document.getElementById("account").style.backgroundColor = "#D1D1D1";
        document.getElementById("binding").style.backgroundColor = "#D1D1D1 ";

        document.getElementById("set_title").innerHTML = "基本设置";

        document.getElementById("base_content").style.display = "";
        document.getElementById("account_content").style.display = "none";
        document.getElementById("binding_content").style.display = "none";
    }

    function account_set() {
        document.getElementById("base").style.backgroundColor = "#D1D1D1";
        document.getElementById("account").style.backgroundColor = "white";
        document.getElementById("binding").style.backgroundColor = "#D1D1D1 ";

        document.getElementById("set_title").innerHTML = "账号设置";

        document.getElementById("base_content").style.display = "none";
        document.getElementById("account_content").style.display = "";
        document.getElementById("binding_content").style.display = "none";
    }

    function binding_set() {
        document.getElementById("base").style.backgroundColor = "#D1D1D1";
        document.getElementById("account").style.backgroundColor = "#D1D1D1";
        document.getElementById("binding").style.backgroundColor = "white";

        document.getElementById("set_title").innerHTML = "绑定设置";

        document.getElementById("base_content").style.display = "none";
        document.getElementById("account_content").style.display = "none";
        document.getElementById("binding_content").style.display = "";
    }

    // 仅选择日期
    $(".form-date").datetimepicker(
        {
            language:  "zh-CN",
            weekStart: 1,
            todayBtn:  1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
            format: "yyyy-mm-dd"
        });

    function saveUserInfo() {
        $("#userInfo_form").submit();
    }

    function changeColor(changeSpan) {
        changeSpan.style.color = "#9F79EE";
    }
    function backColor(backColor) {
        backColor.style.color = "grey";
    }

    //qq绑定
    function binding_qq() {
        window.location.href = "${ctx}/to_login";
    }

    //qq解除绑定
    function qq_span_remove() {
        if(confirm("确定解除qq绑定吗？")){
            window.location.href = "${ctx}/remove_qq";
        }
    }

</script>
</html>