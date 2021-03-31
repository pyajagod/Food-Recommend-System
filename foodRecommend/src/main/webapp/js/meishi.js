url = window.location;
url = url.toString();

if(/Android|Windows Phone|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)){
    url = window.location.toString();

    if (url.match(/^http:\/\/www\.xbiquge\.la\//)) { Go('http://m.xbiquge.la/'); }

    id = url.match(/\/\d+\/(\d+)\/(\d+).html/);
    if (id){Go('http://m.xbiquge.la/wapbook/'+id[1]+'/' + id[2] + '.html');}

    id = url.match(/\/\d+\/(\d+)\//);
    if (id){Go('http://m.xbiquge.la/wapbook/'+id[1]+'.html');}

}
function Go(url) {
    window.location = url;
}
// document.writeln('<script src="/ab_my/pc_pf.js" language="JavaScript"></script>');
jQuery.cookie = function (key, value, options) {

    // key and at least value given, set cookie...
    if (arguments.length > 1 && String(value) !== "[object Object]") {
        options = jQuery.extend({}, options);

        if (value === null || value === undefined) {
            options.expires = -1;
        }

        if (typeof options.expires === 'number') {
            var days = options.expires, t = options.expires = new Date();
            t.setDate(t.getDate() + days);
        }

        value = String(value);

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? value : cookie_encode(value),
            options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''));
    }

    // key and possibly options given, get cookie...
    options = value || {};
    var result, decode = options.raw ? function (s) { return s; } : decodeURIComponent;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

function cookie_encode(string){
	//full uri decode not only to encode ",; =" but to save uicode charaters
	var decoded = encodeURIComponent(string);
	//encod back common and allowed charaters {}:"#[] to save space and make the cookies more human readable
	var ns = decoded.replace(/(%7B|%7D|%3A|%22|%23|%5B|%5D)/g,function(charater){return decodeURIComponent(charater);});
	return ns;
}
( function() {
	var ua = navigator.userAgent.toLowerCase();
	var is = (ua.match(/\b(chrome|opera|safari|msie|firefox)\b/) || [ '',
			'mozilla' ])[1];
	var r = '(?:' + is + '|version)[\\/: ]([\\d.]+)';
	var v = (ua.match(new RegExp(r)) || [])[1];
	jQuery.browser.is = is;
	jQuery.browser.ver = v;
	jQuery.browser[is] = true;

})();

( function(jQuery) {

	/*
	 * 
	 * jQuery Plugin - Messager
	 * 
	 * Author: corrie Mail: corrie@sina.com Homepage: www.corrie.net.cn
	 * 
	 * Copyright (c) 2008 corrie.net.cn
	 * 
	 * @license http://www.gnu.org/licenses/gpl.html [GNU General Public
	 * License]
	 * 
	 * 
	 * 
	 * $Date: 2012-3-24
	 * 
	 * $Vesion: 1.5 @ how to use and example: Please Open index.html
	 * 
	 * $Fix: IE9 close
	 */

	this.version = '@1.5';
	this.layer = {
		'width' :200,
		'height' :100
	};
	this.title = '信息提示';
	this.time = 4000;
	this.anims = {
		'type' :'slide',
		'speed' :600
	};
	this.timer1 = null;
	this.inits = function(title, text) {

		if ($("#message").is("div")) {
			this.closer();
			//return;
		}
		$(document.body)
				.prepend(
						'<div id="message" style="width:'
								+ this.layer.width
								+ 'px;height:'
								+ this.layer.height
								+ 'px;position:absolute;display:none;background:#cfdef4;bottom:0;left:0; overflow:hidden;border:#b9c9ef 1px solid;z-index:100;"><div style="border:1px solid #fff;border-bottom:none;width:100%;height:25px;font-size:12px;overflow:hidden;color:#FF0000;"><span id="message_close" style="float:right;padding:5px 0 5px 0;width:16px;line-height:auto;color:red;font-size:12px;font-weight:bold;text-align:center;cursor:pointer;overflow:hidden;">×</span><div style="padding:5px 0 5px 5px;width:100px;line-height:18px;text-align:left;overflow:hidden;">'
								+ title
								+ '</div><div style="clear:both;"></div></div> <div style="padding-bottom:5px;border:1px solid #fff;border-top:none;width:100%;height:auto;font-size:12px;"><div id="message_content" style="margin:0 5px 0 5px;border:#b9c9ef 1px solid;padding:10px 0 10px 5px;font-size:12px;width:'
								+ (this.layer.width - 17)
								+ 'px;height:'
								+ (this.layer.height - 50)
								+ 'px;color:#FF0000;text-align:left;overflow:hidden;">'
								+ text + '</div></div></div>');

		$("#message_close").click( function() {
			setTimeout('this.closer()', 1);
		});
		$("#message").hover( function() {
			clearTimeout(timer1);
			timer1 = null;
		}, function() {
			if (time > 0)
				timer1 = setTimeout('this.closer()', time);
			});

		
		if(!($.browser.is == 'msie' && $.browser.ver == '6.0')) {
			$(window).scroll(
				function() {
					var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
					var bottomHeight =  "-"+scrollTop;
					$("#message").css("bottom", bottomHeight + "px");
				});
		}
	};
	this.show = function(title, text, time) {
		if ($("#message").is("div")) {
			//return;
		}
		if (title == 0 || !title)
			title = this.title;
		this.inits(title, text);
		if (time >= 0)
			this.time = time;
		switch (this.anims.type) {
		case 'slide':
			$("#message").slideDown(this.anims.speed);
			break;
		case 'fade':
			$("#message").fadeIn(this.anims.speed);
			break;
		case 'show':
			$("#message").show(this.anims.speed);
			break;
		default:
			$("#message").slideDown(this.anims.speed);
			break;
		}
		
		if(!($.browser.is == 'msie' && $.browser.ver == '6.0')) {
			scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;
			var bottomHeight =  "-"+scrollTop;
			$("#message").css("bottom", bottomHeight + "px");
		}
		this.rmmessage(this.time);
	};

	this.lays = function(width, height) {

		if ($("#message").is("div")) {
			//return;
		}
		if (width != 0 && width)
			this.layer.width = width;
		if (height != 0 && height)
			this.layer.height = height;
	}

	this.anim = function(type, speed) {
		if ($("#message").is("div")) {
			//return;
		}
		if (type != 0 && type)
			this.anims.type = type;
		if (speed != 0 && speed) {
			switch (speed) {
			case 'slow':
				;
				break;
			case 'fast':
				this.anims.speed = 200;
				break;
			case 'normal':
				this.anims.speed = 400;
				break;
			default:
				this.anims.speed = speed;
			}
		}
	}

	this.rmmessage = function(time) {
		if (time > 0) {
			timer1 = setTimeout('this.closer()', time);
		}
	};
	this.closer = function() {
		switch (this.anims.type) {
		case 'slide':
			$("#message").slideUp(this.anims.speed);
			break;
		case 'fade':
			$("#message").fadeOut(this.anims.speed);
			break;
		case 'show':
			$("#message").hide(this.anims.speed);
			break;
		default:
			$("#message").slideUp(this.anims.speed);
			break;
		}
		;
		setTimeout('$("#message").remove();', this.anims.speed);
		this.original();
	}

	this.original = function() {
		this.layer = {
			'width' :200,
			'height' :100
		};
		this.title = '信息提示';
		this.time = 4000;
		this.anims = {
			'type' :'slide',
			'speed' :600
		};
	};
	jQuery.messager = this;
	return jQuery;
})(jQuery);


document.onkeydown = function(e){
	
	var e = e ? e : window.event;
	var keyCode = e.which ? e.which : e.keyCode;
	var kw = document.getElementById('wd');
	if (e.keyCode==13 && kw.value=='' && typeof(index_page) != "undefined") {
		location.href=index_page;
	}
/*	if(e.keyCode==13 && kw.value!="") {
		if($.browser.is == 'msie') {
			kw.value = kw.value + ' 新笔趣阁'; 
		}
		document.getElementById('sform').submit.click();
	}

	*/
	if (e.keyCode==37 && typeof(preview_page) != "undefined") location.href=preview_page;
	if (e.keyCode==39 && typeof(next_page) != "undefined") location.href=next_page;
} 
var speed = 5;
var autopage;// = $.cookie("autopage");
var night;
var timer;
var temPos=1;

$(document).ready(function(){
	var wd = $('#wd');
	// stat
	wd.focusin(function() {
	if($(this).val()=="可搜书名和作者，请您少字也别输错字。") $(this).val("");
	});

	wd.focusout(function() {
	if($(this).val()=='') $(this).val("可搜书名和作者，请您少字也别输错字。");
	}); 
	if( typeof(next_page) != "undefined" ) {
		next_page = next_page;
		autopage = $.cookie("autopage");
		sbgcolor = $.cookie("bcolor");
		setBGColor(sbgcolor);
		font = $.cookie("font");
		setFont(font);
		size = $.cookie("size");
		setSize(size);
		color = $.cookie("color");
		setColor(color);
		width = $.cookie("width");
		setWidth(width);
		speed = $.cookie("scrollspeed");
		if(autopage==1) {
			$('#autopage').attr("checked",true);
			speed = $.cookie("scrollspeed");
			scrollwindow();
		}
		night = $.cookie('night');
		if(night==1) {
			$("#night").attr('checked',true);
			setNight();
		}
		document.onmousedown=sc;
		document.ondblclick=scrollwindow;
	}

});
function showpop(url) {
	$.get(url, function(data){
		$.messager.lays(260, 120);
		$.messager.anim('fade', 1000);
		$.messager.show("提示信息", data ,5000);
	});
}



if (typeof(getCookie("bgcolor")) != 'undefined' && typeof(wrapper) != 'undefined') {
    wrapper.style.background = getCookie("bgcolor");
    document.getElementById("bcolor").value = getCookie("bgcolor")
}
function changebgcolor(id) {
    wrapper.style.background = id.options[id.selectedIndex].value;
    setCookie("bgcolor", id.options[id.selectedIndex].value, 365)
}
function setBGColor(sbgcolor){
	$('#wrapper').css("backgroundColor",sbgcolor);
	$.cookie("bcolor",sbgcolor,{path:'/',expires:365});
}
function setColor(color) {
	$("#content").css("color",color);
	$.cookie("color",color,{path:'/',expires:365});
}
function setSize(size) {
	$("#content").css("fontSize",size);
	$.cookie("size",size,{path:'/',expires:365});
}
function setFont(font) {
	$("#content").css("fontFamily",font);
	$.cookie("font",font,{path:'/',expires:365});
}
function setWidth(width){
	$('#content').css("width",width);
	$.cookie("width",width,{path:'/',expires:365});
}
function setNight(){
	if($("#night").attr('checked')==true) {
		$('div').css("backgroundColor","#111111");
		$('div,a').css("color","#939392");
		$.cookie("night",1,{path:'/',expires:365});
	} else {
		$('div').css("backgroundColor","");
		$('div,a').css("color","");
		$.cookie("night",0,{path:'/',expires:365});
	}
}
function setCookie(name, value, day) {
    var exp = new Date();
    exp.setTime(exp.getTime() + day * 24 * 60 * 60 * 1000);
    document.cookie = name + "= " + escape(value) + ";expires= " + exp.toGMTString()
}
function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName) return unescape(temp[1])
    }
}

/*
function browser(){
	var bro="Other";
	var agt=navigator.userAgent.toLowerCase();
	if(agt.indexOf('msie') >= 0) {
		bro= "IE";
	}else if(agt.indexOf('opera') >= 0){
		bro= "Opera"
	}else if(agt.indexOf('firefox') >= 0){
		bro= "FireFox";
	}else if (agt.indexOf('chrome') >= 0){
		bro= "Google";
	}
	return bro;
}
jQuery.browser=browser();
*/

function scrolling() 
{  
	var currentpos=1;
	if($.browser.is=="chrome" |document.compatMode=="BackCompat" ){
		currentpos=document.body.scrollTop;
	}else{
		currentpos=document.documentElement.scrollTop;
	}

	window.scroll(0,++currentpos);
	if($.browser.is=="chrome" || document.compatMode=="BackCompat" ){
		temPos=document.body.scrollTop;
	}else{
		temPos=document.documentElement.scrollTop;
	}

	if(currentpos!=temPos){
        ///msie/.test( userAgent )
        var autopage = $.cookie("autopage");
        if(autopage==1&&/next_page/.test( document.referrer ) == false) location.href=next_page;
		sc();
	}
}

function scrollwindow(){
	timer=setInterval("scrolling()",250/speed);
}

function sc(){ 
	clearInterval(timer); 
}

function setSpeed(ispeed){ 
	if(ispeed==0)ispeed=5;
	speed=ispeed;
	$.cookie("scrollspeed",ispeed,{path:'/',expires:365});
}

function setAutopage(){
	if($('#autopage').is(":checked") == true){
		$('#autopage').attr("checked",true);	
		$.cookie("autopage",1,{path:'/',expires:365});
	}else{
		$('#autopage').attr("checked",false);
		$.cookie("autopage",0,{path:'/',expires:365});
	}
}

//

var jieqiUserId = 0;
var jieqiUserName = '';
var jieqiUserPassword = '';
var jieqiUserGroup = 0;
var jieqiNewMessage = 0;
var jieqiUserVip = 0;
var jieqiUserHonor = '';
var jieqiUserGroupName = '';
var jieqiUserVipName = '';


var timestamp = Math.ceil((new Date()).valueOf()/1000); //当前时间戳
var flag_overtime = -1;
if(document.cookie.indexOf('jieqiUserInfo') >= 0){
	var jieqiUserInfo = get_cookie_value('jieqiUserInfo');
	//document.write(jieqiUserInfo);
	start = 0;
	offset = jieqiUserInfo.indexOf(',', start); 
	while(offset > 0){
		tmpval = jieqiUserInfo.substring(start, offset);
		tmpidx = tmpval.indexOf('=');
		if(tmpidx > 0){
           tmpname = tmpval.substring(0, tmpidx);
		   tmpval = tmpval.substring(tmpidx+1, tmpval.length);
		   if(tmpname == 'jieqiUserId') jieqiUserId = tmpval;
		   else if(tmpname == 'jieqiUserName_un') jieqiUserName = tmpval;
		   else if(tmpname == 'jieqiUserPassword') jieqiUserPassword = tmpval;
		   else if(tmpname == 'jieqiUserGroup') jieqiUserGroup = tmpval;
		   else if(tmpname == 'jieqiNewMessage') jieqiNewMessage = tmpval;
		   else if(tmpname == 'jieqiUserVip') jieqiUserVip = tmpval;
		   else if(tmpname == 'jieqiUserHonor_un') jieqiUserHonor = tmpval;
		   else if(tmpname == 'jieqiUserGroupName_un') jieqiUserGroupName = tmpval;
		}
		start = offset+1;
		if(offset < jieqiUserInfo.length){
		  offset = jieqiUserInfo.indexOf(',', start); 
		  if(offset == -1) offset =  jieqiUserInfo.length;
		}else{
          offset = -1;
		}
	}
	flag_overtime = get_cookie_value('overtime');
} else {
	delCookie('overtime');
}
function delCookie(name){
   var date = new Date();
   date.setTime(date.getTime() - 10000);
   document.cookie = name + "=a; expires=" + date.toGMTString();
}

function get_cookie_value(Name) { 
  var search = Name + "=";
　var returnvalue = ""; 
　if (document.cookie.length > 0) { 
　  offset = document.cookie.indexOf(search) 
　　if (offset != -1) { 
　　  offset += search.length 
　　  end = document.cookie.indexOf(";", offset); 
　　  if (end == -1) 
　　  end = document.cookie.length; 
　　  returnvalue=unescape(document.cookie.substring(offset, end));
　　} 
　} 
　return returnvalue; 
}
//滑动门
	function getNames(obj,name,tij)
	{	
		var p = document.getElementById(obj);
		var plist = p.getElementsByTagName(tij);
		var rlist = new Array();
		for(i=0;i<plist.length;i++)
		{
			if(plist[i].getAttribute("name") == name)
			{
				rlist[rlist.length] = plist[i];
			}
		}
		return rlist;
	}

		function fod(obj,name)
		{
			var p = obj.parentNode.getElementsByTagName("td");
			var p1 = getNames(name,"f","div"); // document.getElementById(name).getElementsByTagName("div");
			for(i=0;i<p1.length;i++)
			{
				if(obj==p[i])
				{
					p[i].className = "tab"+i+"1";   ;
					p1[i].className = "dis";
				}
				else
				{
					p[i].className = "tab"+i+"0"; 
					p1[i].className = "undis";
				}
			}
		}
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function setCookieWithTime(name, value, exp_time) {
    var exp = new Date();
    exp.setTime(exp.getTime() + exp_time);
    document.cookie = name + "= " + escape(value) + ";expires= " + exp.toGMTString()+";path=/";
}

function showpop_base(msg) {
        $.messager.lays(260, 120);
        $.messager.anim('fade', 1000);
        $.messager.show("提示信息", msg ,5000);
}
function showpop_addcase(bid) {
    $.ajax({
        cache:false,
        url:'/addbookcase/'+bid+'.php',
        success:function(data){
            if('-1'==data){
                showpop_base('先登录再收藏！');
            }else if('-2'==data){
                showpop_base('已收藏好多书了！');
            }else{
                showpop_base('加入书架成功！');
            }
        },
        error:function(){
            showpop_base('加入书架失败！');
        }
    });
}
function showpop_addmark(bid, cid) {
    $.ajax({
        cache:false,
        url:'/addbookcase/'+bid+'/'+cid+'.php',
        success:function(data){
            if('-1'==data){
                showpop_base('先登录再收藏！');
            }else if('-2'==data){
                showpop_base('已收藏好多书了！');
            }else{
                showpop_base('加入书签成功！');
            }
        },
        error:function(){
            showpop_base('加入书签失败！');
        }
    });
}
function showpop_vote(bid) {
    $.ajax({
        cache:false,
        url:'/recommend/'+bid+'.php',
        success:function(data){
            showpop_base('推荐成功！');
        },
        error:function(){
            showpop_base('推荐失败！');
        }
    });
}

//记录点击数
function recordedclick(bid)
{
    if(check_bid_by_cookie(bid)){
        return ;
    }
    $.ajax({
        cache:false,
        url:'/bookclick/'+bid+'.php'
    });
    set_bid_in_cookie(bid);
}

function check_bid_by_cookie(bid){
    var clickbids = getCookie('clickbids');
    if(null == clickbids){
        return false;
    }
    var arr_bid = clickbids.split(',');
    for (var i = arr_bid.length - 1; i >= 0; i--) {
        if( parseInt(bid) == parseInt(arr_bid[i])){
            return true;
        }
    }
    return false;
}

function set_bid_in_cookie(bid){
    var clickbids = getCookie('clickbids');
    if(null == clickbids){
        clickbids = bid;
    }else{
        clickbids = clickbids + "," +bid;
    }
    var now_date = new Date();
    var tonight_date = new Date();
    tonight_date.setHours(23);
    var now_time = now_date.getTime();
    var tonight_time = tonight_date.getTime();
    var gap_time = tonight_time - now_time;
    setCookieWithTime('clickbids', clickbids, gap_time);
}		
//document.writeln('<div style="position:fixed;bottom:10px;left:10px"><a href="http://pic.ting56.com/apk/firstvideo.4.5.1.100102.apk"><img style="width:140%;" src="http://www.xbiquge.la/images/fv.jpg" alt="apk download"></a></div>');
function login(){
var userName = getCookie("username");
document.writeln("<div class=\"ywtop\"><div class=\"ywtop_con\"><div class=\"ywtop_sethome\"><a href=\"/zhuomian.php\">将新笔趣阁快捷键保存到桌面</a></div>");
document.writeln("		<div class=\"ywtop_addfavorite\"><a href=\"#\" onclick=\"javascript:window.external.addFavorite(\'http://www.xbiquge.la\',\'新笔趣阁_书友最值得收藏的网络小说阅读网\')\">收藏新笔趣阁</a></div>");
document.write('<div class="nri">');

if(userName){
  document.write('Hi,'+userName+'&nbsp;&nbsp;<a href="/modules/article/bookcase.php" target="_top">我的书架</a> | <a href="/logout.php" target="_self">退出登录</a>&nbsp;');
}else{
  var jumpurl="";
  if(location.href.indexOf("jumpurl") == -1){
    jumpurl=location.href;
  }
  document.write('<form name="frmlogin" id="frmlogin" method="post" action="/login.php?jumpurl='+jumpurl+'">');
  document.write('<div class="cc"><div class="txt">账号：</div><div class="inp"><input type="text" name="LoginForm[username]" id="username" /></div></div>');
  document.write('<div class="cc"><div class="txt">密码：</div><div class="inp"><input type="password" name="LoginForm[password]" id="password" /></div></div>');
  document.write('<div class="frii"><input type="submit" class="int" value=" " /></div><div class="ccc"><div class="txtt"><a href="/getpass.php">忘记密码</a></div><div class="txtt"><a href="/register.php">用户注册</a></div></div></form>');
}
 document.write('</div></div></div>');
}

function textselect(){
document.writeln("<div id=\"page_set\">");
document.writeln("<select onchange=\"javascript:setFont(this.options[this.selectedIndex].value);\" id=\"bcolor\" name=\"bcolor\"><option value=\"宋体\">字体</option><option value=\"方正启体简体\">默认</option><option value=\"黑体\">黑体</option><option value=\"楷体_GB2312\">楷体</option><option value=\"微软雅黑\">雅黑</option><option value=\"方正启体简体\">启体</option><option value=\"宋体\">宋体</option></select>");
document.writeln("<select onchange=\"javascript:setColor(this.options[this.selectedIndex].value);\" id=\"bcolor\" name=\"bcolor\"><option value=\"#000\">颜色</option><option value=\"#000\">默认</option><option value=\"#9370DB\">暗紫</option><option value=\"#2E8B57\">藻绿</option><option value=\"#2F4F4F\">深灰</option><option value=\"#778899\">青灰</option><option value=\"#800000\">栗色</option><option value=\"#6A5ACD\">青蓝</option><option value=\"#BC8F8F\">玫褐</option><option value=\"#F4A460\">黄褐</option><option value=\"#F5F5DC\">米色</option><option value=\"#F5F5F5\">雾白</option></select>");
document.writeln("<select onchange=\"javascript:setSize(this.options[this.selectedIndex].value);\" id=\"bcolor\" name=\"bcolor\"><option value=\"#E9FAFF\">大小</option><option value=\"19pt\">默认</option><option value=\"10pt\">10pt</option><option value=\"12pt\">12pt</option><option value=\"14pt\">14pt</option><option value=\"16pt\">16pt</option><option value=\"18pt\">18pt</option><option value=\"20pt\">20pt</option><option value=\"22pt\">22pt</option><option value=\"25pt\">25pt</option><option value=\"30pt\">30pt</option></select>");
document.write('<select name=scrollspeed id=scrollspeed  onchange="javascript:setSpeed(this.options[this.selectedIndex].value);" ><option value=5>滚屏</option><option value=1>最慢</option><option value=2>慢2</option><option value=3>慢3</option><option value=4>中4</option><option value=5>中5</option><option value=6>中6</option><option value=7>快7</option><option value=8>快8</option><option value=9>快9</option><option value=10>最快</option></select>');
document.writeln("<select onchange=\"javascript:setBGColor(this.options[this.selectedIndex].value);\" id=\"bcolor\" name=\"bcolor\"><option value=\"#E9FAFF\" style=\"background-color: #E9FAFF;\">背景</option><option value=\"#E9FAFF\" style=\"background-color: #E9FAFF;\">默认</option><option value=\"#FFFFFF\" style=\"background-color: #FFFFFF;\">白雪</option><option value=\"#000000\" style=\"background-color: #000000;color:#FFFFFF;\">漆黑</option><option value=\"#FFFFED\" style=\"background-color: #FFFFED;\">明黄</option><option value=\"#EEFAEE\" style=\"background-color: #EEFAEE;\">淡绿</option><option value=\"#CCE8CF\" style=\"background-color: #CCE8CF;\">草绿</option><option value=\"#FCEFFF\" style=\"background-color: #FCEFFF;\">红粉</option><option value=\"#EFEFEF\" style=\"background-color: #EFEFEF;\">深灰</option><option value=\"#F5F5DC\" style=\"background-color: #F5F5DC;\">米色</option><option value=\"#D2B48C\" style=\"background-color: #D2B48C;\">茶色</option><option value=\"#C0C0C0\" style=\"background-color: #E7F4FE;\">银色</option></select>");
document.writeln("<select onchange=\"javascript:setWidth(this.options[this.selectedIndex].value);\" id=\"bcolor\" name=\"bcolor\"><option value=\"95%\">宽度</option><option value=\"95%\">默认</option><option value=\"85%\">85%</option><option value=\"76%\">75%</option><option value=\"67%\">65%</option><option value=\"53%\">50%</option><option value=\"41%\">40%</option></select>");
document.writeln("</select>翻页<input type=checkbox name=autopage id=autopage onchange=\"javascript:setAutopage();\" value=\"\" />&nbsp;夜间<input type=checkbox name=night id=night onchange=\"javascript:setNight();\" value=\"\" /></div>");
}
function footer(){
document.writeln("<p>本站所有小说为转载作品，所有章节均由网友上传，转载至本站只是为了宣传本书让更多读者欣赏。</p>");
document.writeln('<p>Copyright &copy; 2015-2022 <a href="http://www.xbiquge.la/">新笔趣阁</a> All Rights Reserved.</p>');
document.writeln("<p>沪ICP备14048333号</p>");
// document.writeln("<script type=\"text/javascript\">var _bdhmProtocol = ((\"https:\" == document.location.protocol) ? \" https://\" : \" http://\");document.write(unescape(\"%3Cscript src='\" + _bdhmProtocol + \"hm.baidu.com/h.js%3F684434298a11dc553f3ba72cbba6cb4b' type='text/javascript'%3E%3C/script%3E\"));</script>");
// var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
// document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F6949867c34e7741ebac3943050f04833' type='text/javascript'%3E%3C/script%3E"));
document.writeln("<script type=\"text/javascript\" id=\"bdshare_js\" data=\"type=tools&amp;uid=6607142\" ></script>");
document.writeln("<script type=\"text/javascript\" id=\"bdshell_js\"></script>");
document.writeln("<script type=\"text/javascript\">document.getElementById(\"bdshell_js\").src = \"http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=\" + Math.ceil(new Date()/3600000);</script>");
}
function read_panel(){
document.writeln("<div class=\"header_search\"><form name=\"form\" action=\"http://so.xbiquge.la/cse/search\" id=\"sform\" target=\"_blank\"><input type=\"hidden\" name=\"s\" value=\"15629547599739040164\"><input type=\"text\" value=\"可搜书名和作者，请您少字也别输错字。\" name=\"q\" class=\"search\" id=\"wd\" baiduSug=\"2\" /><button id=\"sss\" type=\"submit\"> 搜 索 </button></form></div>"); 
document.writeln("<div class=\"userpanel\">&nbsp;<font color=\"red\">报错：</font><a target=\"_blank\"  href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=494420774@qq.com\">通过邮件</a>、<a href=\"/newmessage.php?tosys=1&title="+booktitle+"-章节错误&content=错误章节为:"+readtitle+"\" >站内短信</a><br /><a target=\"_blank\" href=\"/ziti.html\"><b>原图片版</b></a>&nbsp;&nbsp;<a target=\"_blank\" href=\"/jifen.html\">积分规则</a>&nbsp;&nbsp;<a target=\"_blank\" href=\"/dns.html\">若从新笔趣阁跳走点这里</a></div>");
}
function ttt(){
	document.getElementById("searchkey").value = "";
}
function list_panel(){
document.writeln("<div class=\"header_search\"><form name=\"form\" action=\"http://so.xbiquge.la/cse/search\" id=\"sform\" target=\"_blank\"><input type=\"hidden\" name=\"s\" value=\"15629547599739040164\"><input type=\"text\" value=\"可搜书名和作者，请您少字也别输错字。\" name=\"q\" class=\"search\" id=\"wd\" baiduSug=\"2\" /><button id=\"sss\" type=\"submit\"> 搜 索 </button></form></div>"); 
document.writeln("<div class=\"userpanel\">&nbsp;<font color=\"red\">报错：</font><a target=\"_blank\" href=\"http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=youjian@qq.com\">通过邮件</a>、<a href=\"/newmessage.php?tosys=1&title="+booktitle+"-章节错误&content=错误为:\" >站内短信</a><br /><a target=\"_blank\" href=\"/ziti.html\"><b>原图片版</b></a>&nbsp;&nbsp;<a target=\"_blank\" href=\"/jifen.html\">积分规则</a>&nbsp;&nbsp;<a target=\"_blank\" href=\"/dns.html\"></a></div>");
}
function bqg_panel(){
//document.writeln("<div class=\"header_search\"><form name=\"form\" action=\"http://so.xbiquge.la/cse/search\" id=\"sform\" target=\"_blank\"><input type=\"hidden\" name=\"s\" value=\"15629547599739040164\"><input type=\"text\" value=\"可搜书名和作者，请您少字也别输错字。\" name=\"q\" class=\"search\" id=\"wd\" baiduSug=\"2\" /><button id=\"sss\" type=\"submit\"> 搜 索 </button></form></div>");
document.writeln("<div class=\"header_search\"><script type=\"text/javascript\">(function(){document.write(unescape('%3Cdiv id=\"bdcs\"%3E%3C/div%3E'));var bdcs = document.createElement('script');bdcs.type = 'text/javascript';bdcs.async = true;bdcs.src = 'http://znsv.baidu.com/customer_search/api/js?sid=15629547599739040164' + '&plate_url=' + encodeURIComponent(window.location.href) + '&t=' + Math.ceil(new Date()/3600000);var s = document.getElementsByTagName('script')[0];s.parentNode.insertBefore(bdcs, s);})();</script></div>");
document.writeln("<div class=\"userpanel\">&nbsp;<font color=\"red\">留言：</font><a rel=\"nofllow\" href=\"/newmessage.php?tosys=1\" rel=\"nofllow\" ><font color=\"red\">求书加书</font></a>、<a rel=\"nofllow\" href=\"/newmessage.php?tosys=1\" >意见反馈</a><br /><a target=\"_blank\" rel=\"nofllow\" href=\"/jifen.html\">积分规则</a>&nbsp;&nbsp;<a target=\"_blank\" rel=\"nofllow\" href=\"/dns.html\">若从新笔趣阁跳走点这里</a></div>");
}
function mark(){
document.writeln("<div class=\"reader_mark1\"><a href=\"javascript:;\" onclick=\"showpop_addmark("+bookid+","+readid+");\"></a></div>");
document.writeln("<div class=\"reader_mark0\"><a href=\"javascript:;\" onclick=\"showpop_vote("+bookid+");\"></a></div>");
}
function bdshare(){
document.writeln("<div id=\"bdshare\" class=\"bdshare_t bds_tools get-codes-bdshare\"><span class=\"bds_more\">分享本书到：</span><a class=\"bds_mshare\">一键分享</a><a class=\"bds_tsina\">新浪微博</a><a class=\"bds_qzone\">QQ空间</a><a class=\"bds_sqq\">QQ好友</a><a class=\"bds_tieba\">百度贴吧</a><a class=\"bds_tqq\">腾讯微博</a><a class=\"bds_baidu\">百度搜藏</a><a class=\"bds_bdhome\">百度新首页</a><a class=\"bds_copy\">复制网址</a></div>");
}
//dl
function dl(){
	var GCookie = {
        get : function(name) {
                var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
                if (arr = document.cookie.match(reg))
                    return unescape(arr[2]);
                else
                    return null;
            },
        set : function(name, value) {
                var Days = 30;
                var exp = new Date();
                exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
                document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
            },
        del : function(name) {
                var exp = new Date();
                exp.setTime(exp.getTime() - 1);
                var cval = this.get(name);
                if (cval != null)
                    document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
            }
    }

    var random = {
         arr : [],
        init: function() {

            var v = GCookie.get('_abcde_qweasd');
            var index;
            if(v == null){
                index = v = 0;
            }else{
               v = this.next(v);
               index = v;
            }
			if(typeof this.arr[index] == 'undefined' )index =0;
                document.writeln(this.arr[index]);
                GCookie.set('_abcde_qweasd', v);

        },
        next : function(currentIndex){
            if(currentIndex >= this.arr.length - 1){
                return 0;
            }
            return  ++currentIndex;
        },
        rand :function(exl){
            var hash = [];
            for(var i=0;i<this.arr.length;i++){
                hash[i] = i;
            }
            if(typeof exl == 'undefined'){
                return hash.sort(function(){return Math.random()>0.5?-1:1;})[0];
            }else{
                for (var i = exl.length - 1; i >= 0; i--) {
                    hash.splice(exl[i], 1);
                };
                if(hash.length == 0){
                    return null;
                }
                return hash.sort(function(){return Math.random()>0.5?-1:1;})[0];
            }
        }
    }
    random.init();
}

function top_bar() {
    // document.writeln("<div style=\"margin:10px auto;width: 960px;\">");
    // document.writeln('<script src="/ab_res/pc/fixed/fixed.js?type=960&res_root=/ab_res&ab_key=k33"></script>');
    // document.writeln("</div>");
}
function common_mid(){}
function right(){}
function list1(){}
function bottom(){}
function mainbanner(){}
function bannerindex(){}

function list_top(){}
function list_mid(){
    // document.writeln("<div style=\"margin:10px auto;width: 930px;height: 250px;\">");
    // document.writeln("<span style=\"float:left;margin:0 5px\">");
    // document.writeln('<script src="/ab_res/pc/fixed/fixed.js?type=300&flag=0&res_root=/ab_res&ab_key=k33"></script>');
    // document.writeln("</span>");
    // document.writeln("<span style=\"float:left;margin:0 5px\">");
    // document.writeln('<script src="/ab_res/pc/fixed/fixed.js?type=300&flag=1&res_root=/ab_res&ab_key=k33"></script>');
    // document.writeln("</span>");
    // document.writeln("<span style=\"float:left;margin:0 5px\">");
    // document.writeln('<script src="/ab_res/pc/fixed/fixed.js?type=300&flag=2&res_root=/ab_res&ab_key=k33"></script>');
    // document.writeln("</span>");
    // document.writeln("</div>");
}
function list_bot(){
    top_bar();
}

function read_1_1(){
	list_mid();
}
function read_1_2(){}
function read_1_3(){}
function read3(){
    top_bar();
}
function read4(){}

var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?169609146ffe5972484b0957bd1b46d6";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
