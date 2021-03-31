function setCookie(c_name,value,expiredays)
{
    var exdate=new Date()
    exdate.setDate(exdate.getDate()+365)
    document.cookie=c_name+ "=" +escape(value)+";expires="+exdate.toGMTString()+";path=/";
}

function getCookie(c_name)
{
    if (document.cookie.length>0){
        c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1){ 
            c_start=c_start + c_name.length+1;
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1) c_end=document.cookie.length;
            return unescape(document.cookie.substring(c_start,c_end));
        } 
    }
    return "";
}

function delCookie(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    document.cookie= name + "=;expires="+exp.toGMTString();
}
var bookUserName=getCookie("member_username");
function login(){
document.writeln("<div style=\"display:none\" >");
document.writeln("<script type=\"text/javascript\">var cnzz_protocol = ((\"https:\" == document.location.protocol) ? \" https://\" : \" http://\");document.write(unescape(\"%3Cspan id='cnzz_stat_icon_1258621064'%3E%3C/span%3E%3Cscript src='\" + cnzz_protocol + \"s4.cnzz.com/z_stat.php%3Fid%3D1258621064' type='text/javascript'%3E%3C/script%3E\"));</script>");
document.writeln("</div>");
document.writeln("<div class=\"ywtop\"><div class=\"ywtop_con\"><div class=\"ywtop_sethome\"><a onClick=\"this.style.behavior='url(#default#homepage)';this.setHomePage('http://www.motanku.com');\" href=\"#\">将本站设为首页</a></div>");
document.writeln("		<div class=\"ywtop_addfavorite\"><a href=\"javascript:window.external.addFavorite(\'http://www.motanku.com\',\'墨坛库\')\">收藏墨坛库</a></div>");
document.write('<div class="nri">');
if(bookUserName != ''){
  document.write('Hi,<a href="/userdetail.php" target="_top">'+bookUserName+'</a>&nbsp;&nbsp;<a href="/case.html" target="_top">我的书架</a>');
  document.write(' | <a href="/userdetail.php" target="_top">查看资料</a> | <a href="/logout.php" target="_self">退出登录</a>&nbsp;');
}else{
  var jumpurl="";
  if(location.href.indexOf("jumpurl") == -1){
    jumpurl=location.href;
  }
  document.write('<form name="mylogin" id="mylogin" method="post" action="/login.php?action=login&usecookie=1440&jumpurl="'+jumpurl+'>');
  document.write('<div class="cc"><div class="txt">账号：</div><div class="inp"><input type="text" name="username" id="username" /></div></div>');
  document.write('<div class="cc"><div class="txt">密码：</div><div class="inp"><input type="password" name="password" id="password" /></div></div>');
  document.write('<div class="frii"><input type="submit" class="int" value=" " /></div><div class="ccc"><div class="txtt"></div><div class="txtt"><a href="/register.php">用户注册</a></div></div></form>');
  }
 document.write('</div></div></div>');
}

function doSearch() {
    document.forms["searchForm"].action = "/searchbook.php";
    document.forms["searchForm"].submit();
}

function search() {
    document.writeln("<form name=\"searchForm\" id=\"searchForm\" action=\"/searchbook.php\" stye=\"margin:0px;padding:0px;\">");

    document.writeln("<input class=\"search\" name=\"keyword\"  type=\"text\" maxlength=\"30\" value=\"可搜书名和作者，请您少字也别输错字。\" title=\"请正确输入\" onfocus=\"this.style.color = '#000000';this.focus();if(this.value=='可搜书名和作者，请您少字也别输错字。'){this.value='';}\" onblur=\"if(this.value==''){this.value='可搜书名和作者，请您少字也别输错字';}\" ondblclick=\"javascript:this.value=''\" /><input type=\"button\" class=\"searchBtn\" value=\"搜索\" title=\"搜索\" onclick=\"doSearch()\" />");
    document.writeln("</form>");
}
function banner()
{
/**
document.writeln("<!-- Baidu Button BEGIN -->");
document.writeln("<div id=\"bdshare\" class=\"bdshare_t bds_tools_32 get-codes-bdshare\">");
document.writeln("<a class=\"bds_tsina\"><\/a>");
document.writeln("<a class=\"bds_renren\"><\/a>");
document.writeln("<span class=\"bds_more\"><\/span>");
document.writeln("<a class=\"shareCount\"><\/a>");
document.writeln("<\/div>");
document.writeln("<script type=\"text\/javascript\" id=\"bdshare_js\" data=\"type=tools&amp;uid=0\" ><\/script>");
document.writeln("<script type=\"text\/javascript\" id=\"bdshell_js\"><\/script>");
document.writeln("<script type=\"text\/javascript\">");
document.writeln("document.getElementById(\"bdshell_js\").src = \"http:\/\/bdimg.share.baidu.com\/static\/js\/shell_v2.js?cdnversion=\" + Math.ceil(new Date()\/3600000)");
document.writeln("<\/script>");
document.writeln("<!-- Baidu Button END -->")
**/
}
function list1(){

}
function read1(){

}
function read2(){
}
function read3(){}

function read4(){
}

function chaptererror()
{
document.writeln("<div align=\"center\" ><a href=\"javascript:postError();\" style=\"text-align:center;color:red;\">章节错误,点此举报(免注册)</a>,举报后维护人员会在两分钟内校正章节内容,请耐心等待,并刷新页面。</div>");
}
function footer(){
/**
document.writeln("<script type=\"text/javascript\">(function(){document.write(unescape('%3Cdiv id=\"bdcs\"%3E%3C/div%3E'));var bdcs = document.createElement('script');bdcs.type = 'text/javascript';bdcs.async = true;bdcs.src = 'http://znsv.baidu.com/customer_search/api/js?sid=13851178133715436012' + '&plate_url=' + encodeURIComponent(window.location.href) + '&t=' + Math.ceil(new Date()/3600000);var s = document.getElementsByTagName('script')[0];s.parentNode.insertBefore(bdcs, s);})();</script>");
**/
}

var prevpage='';
var nextpage='';
var index_page = '';
var bookId,chapterId;
function bookOperate(bookid,lchapter,chapterid,nchapter,chaptername){
	document.onkeydown=keypage;
 prevpage=lchapter;
 nextpage=nchapter;
 index_page = "./";
 bookId=bookid;
 chapterId=chapterid;
 if(typeof addHit!= 'undefined' &&addHit instanceof Function)
	addHit(bookid);

 if(typeof addBookMarkByJs!= 'undefined' &&addBookMarkByJs instanceof Function)
       addBookMarkByJs(chapterid,bookid,chaptername);

}

function postError(){
    if(typeof postErrorChapter!= 'undefined' &&postErrorChapter instanceof Function)
	postErrorChapter(chapterId,bookId);
}

function keypage(s) {
if (event.keyCode==37) location=prevpage
if (event.keyCode==39) location=nextpage
if (event.keyCode == 13) document.location = index_page
}

var xiaoshuo_score = {
	texts:['超酷','好看','一般','无聊','差劲'],
	data:{},
	pvid:{},
	hasSend:function(){
		var voted = xiaoshuo_score.Cookie.get("jqxsvoted");
		if(typeof(voted)=="undefined"|voted==null){
			return false;
		}
		else{
			return (','+voted+',').indexOf(','+xiaoshuo_score.pvid.id+',')==-1?false:true;
		}
	},
	getS:function(){
	    $.get("/ad.php?action=GetScore&BookId="+xiaoshuo_score.pvid.id+"&t="+new Date().getTime(),function(d){										 
			eval(d);
			xiaoshuo_score.data=Scorepl;
			xiaoshuo_score.initScore();
		});
	},
	getsendScore:function(s){
	    $.get("/ad.php?action=getsendScore&BookId="+xiaoshuo_score.pvid.id+"&s="+s+"&t="+new Date().getTime(),function(d){
			var s2="";											 
			var s=d;
			if(s==1){
				var k1=xiaoshuo_score.Cookie.get("jqxsvoted");
				if(typeof(k1)=="undefined"|k1==null)
					xiaoshuo_score.Cookie.set("jqxsvoted",xiaoshuo_score.pvid.id+',');	
				else	
					xiaoshuo_score.Cookie.set("jqxsvoted",k1+xiaoshuo_score.pvid.id+',');
				alert('评分已提交，非常感谢您的参与！');
			}else{
				alert('非常抱歉，评分失败,请稍候重试！');
			}
		});
	},
	initScore:function(){
		var data = this.data;
		var t = data.t;
		var ret = [];
		var _max = Math.max.apply(Math,data.s);
		var _len = 46;
		var _avg = data.t>0?((data.s[0]+2*data.s[1]+3*data.s[2]+4*data.s[3]+5*data.s[4])/t*2).toFixed(1):'0.0';
		_avg = _avg.substring(0,3);
		ret.push('<div class="score_avg"><span><em>' + _avg + '</em><i>' + _avg + '</i></span></div>');
		ret.push('<div class="score_total">共 <span>'+data.t+'</span> 人<br />参与评分</div>');
		ret.push('<ul class="score_list">');	
		for(var i=0;i<=4;i++){
			var len = _max>0?(_len*data.s[4-i]/_max).toFixed(0):0;
			ret.push('<li><span>'+xiaoshuo_score.texts[i]+'</span><i style="width:'+len+'px"></i> <em>'+data.s[4-i]+'人</em></li>');
		}
		ret.push('</ul>');
		$('#score_content').html(ret.join(''));
	},
	initStar:function(){
		var star_width = 26;
		$('#starlist > li').click(function(){
			if(!xiaoshuo_score.hasSend()){
				var i = $(this).attr('i');
				xiaoshuo_score.getsendScore(i);
				xiaoshuo_score.data.t++;
				xiaoshuo_score.data.s[i-1]++;
				xiaoshuo_score.initScore();
				$('#starlist').html('<li style="background: url(/images/stars.gif) 0 -50px repeat-x;width:'+(star_width*i)+'px;z-index:6;height:26px;" class="star_five">&nbsp;</li>');
				$('#star_desc').html('评分已提交，' + i + '星，' + xiaoshuo_score.texts[5-i]);
			}else{
				alert('囧,-_-|||，您已经评过分了哦？');
			}
		});
		$('#starlist > li').hover(function(){
										   var i = $(this).attr('i');
										   $('#star_tip').show();
										   $('#star_tip_arrow').css('left',star_width*i-20 + 'px');
										   $('#star_desc').html(i + '星，' + xiaoshuo_score.texts[5-i]);
										   },function(){
											   $('#star_tip').hide();
										   }
		);
	},
	Cookie:{
		set:function(b,d,a,e,c){
			if(typeof a=="undefined"){
				a=new Date(new Date().getTime()+1000*3600*24*30)
			}
			document.cookie=b+"="+escape(d)+((a)?"; expires="+a.toGMTString():"")+((e)?"; path="+e:"; path=/")+((c)?";domain="+c:"")},
		get2:function(b){
			var aCookie = document.cookie.split("; ")
			for(var i=0; i < aCookie.length; i++){
				var aCrumb = aCookie[i].split("=")
				if(b == unescape(aCrumb[0]))	return unescape(aCrumb[1])
			}
			return null
		},
		get3:function(a,b){
			if(a==null) return null
			var aCookie = a.split("&")
			for(var i=0; i < aCookie.length; i++){
				var aCrumb = aCookie[i].split("=")
				if(b == unescape(aCrumb[0]))	return unescape(aCrumb[1])
			}
			return null
		},		
		get:function(b){
			var a=document.cookie.match(new RegExp("(^| )"+b+"=([^;]*)(;|$)"));
			if(a!=null){
				return unescape(a[2])
			}
			return null
		},		
		clear:function(a,c,b){
			if(this.get(a)){
				document.cookie=a+"="+((c)?"; path="+c:"; path=/")+((b)?"; domain="+b:"")+";expires=Fri, 02-Jan-1970 00:00:00 GMT"}
		}
	}
};