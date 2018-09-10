/**
 * 
 */
//두 날짜 사이의 일자 구하기
function diffDays(startDate, endDate) {
    var format=/(\.)|(\-)|(\/)/g;
    startDate=startDate.replace(format, "");
    endDate=endDate.replace(format, "");
    
    format = /[12][0-9]{3}[0-9]{2}[0-9]{2}/;
    if(! format.test(startDate))
        return "";
    if(! format.test(endDate))
        return "";
    
    var sy = parseInt(startDate.substr(0, 4));
    var sm = parseInt(startDate.substr(4, 2));
    var sd = parseInt(startDate.substr(6, 2));
    
    var ey = parseInt(endDate.substr(0, 4));
    var em = parseInt(endDate.substr(4, 2));
    var ed = parseInt(endDate.substr(6, 2));

    var fromDate=new Date(sy, sm-1, sd);
    var toDate=new Date(ey, em-1, ed);
    
    var sn=fromDate.getTime();
    var en=toDate.getTime();
    
    var diff=en-sn;
    var day=Math.floor(diff/(24*3600*1000));
    
    return day;
}

//나이 계산
function toAge(birth) {
	var regexp=/(\.)|(\-)|(\/)/g;
	birth=birth.replace(regexp, "");
	
	if(birth.length!=8)
		return "";
	
	var by = parseInt(birth.substr(0, 4));
	var bm = parseInt(birth.substr(4, 2));
	var bd = parseInt(birth.substr(6, 2));
	var bdate=new Date(by, bm-1, bd);
	
	var now = new Date();
	
	var age=now.getFullYear() - bdate.getFullYear();
	if((bdate.getMonth() > now.getMonth()) ||
			((bdate.getMonth() == now.getMonth()) && 
					bdate.getDate() > now.getDate())) {
		age--;
	}
	
	return age;
}

// 날짜 8자리 표현 유효성 체크
function isValidDateFormat(data){
    var regexp = /[12][0-9]{3}[\.|\-|\/]?[0-9]{2}[\.|\-|\/]?[0-9]{2}/;
    if(! regexp.test(data))
        return false;

    regexp=/(\.)|(\-)|(\/)/g;
    data=data.replace(regexp, "");
    
	var y=parseInt(data.substr(0, 4));
    var m=parseInt(data.substr(4, 2));
    if(m<1||m>12) 
    	return false;
    var d=parseInt(data.substr(6));
    var lastDay = (new Date(y, m, 0)).getDate();
    if(d<1||d>lastDay)
    	return false;
		
	return true;     
}

//날짜를 문자열로
function dateToString(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    if(m < 10) m='0'+m;
    var d = date.getDate();
    if(d < 10) d='0'+d;
    
    return y + '-' + m + '-' + d;
}

// 문자열을 날짜로
function stringToDate(value) {
	var format=/(\.)|(\-)|(\/)/g;
	value=value.replace(format, "");
    
    format = /[12][0-9]{3}[0-9]{2}[0-9]{2}/;
    if(! format.test(value))
        return "";
    
    var y = parseInt(value.substr(0, 4));
    var m = parseInt(value.substr(4, 2));
    var d = parseInt(value.substr(6, 2));
    
    return new Date(y, m-1, d);
}

//숫자 정규식
function isValidNumber(data) {
	// 숫자만(1개 이상)
	//var format = /^(\d+)$/;
	
	//1~3자 숫자
	//var format = /^(\d){1,3}$/;
	//var format = /^[0-9]{1,3}$/;
	
	// 숫자(1개 이상, 앞의 +,-는 허용)
	var format = /^[+|-]?(\d)+$/;
	
	
	return format.test(data);
}

// 한글 정규식
function isValidKorean(data) {
	// 한글(공백안됨, 자모음 안됨)
	// var format = /^[가-힣]+$/;
	
	// 한글(시작은 한글, 그 다음부터 공백허용+자,모음 안됨)
	var format = /^[가-힣][가-힣\s]*$/;
	
	
	return format.test(data);
}

// 이미지파일 정규식(input type file)
function isValidImageFile(data) {
	// 확장자 제한
	var format = /(\.gif|\.jpg|\.jpeg|\.png)$/gi;
	
	return format.test(data);
}

//패스워드 정규식(영문자 + 숫자 or 특문 포함(예시=> e1, e#), 5~10자이내)
function isValidPwd(data) {
	// ?= 의미는 ?자리에 반드시 오른쪽 조건이 존재해야 한다. and .*의미는 모든 문자, 
	// 그래서 영어소문자가 존재하지만 앞에 어떤 것이 있어도 상관 없다는 의미임.
	var format = /^(?=.*[a-z])(?=.*[!@#$%\^&*]|.*[0-9]).{5,10}$)/i;
	
	return format.test(data);
}

// DB 명령어 제어
function filter(data) {

	var format = /(select|update|delete|insert|create|alter|drop)/gi;
	
	return format.test(data);
}

// 휴대폰 번호
function phone(data) {
	var format = /^01([016789]{1})-([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/; // -뒤에 ?를 붙이면 번호 입력시 -를 써도되고 안써도 됨.

	return format.test(data);
}

// 화면 중앙에 보내는 jQuery function
jQuery.fn.center = function() {
	this.css("position", "absolute");
	this.css("top", Math.max(0, (($(window).height()-$(this).outerHeight())/2)+$(window).scrollTop())+"px");
	this.css("left", Math.max(0, (($(window).width()-$(this).outerWidth())/2)+$(window).scrollLeft())+"px");
    return this;	
}

// 아이디(영어시작, 영숫자 5~10)
var format = /^[a-z][a-z0-9_]{4,9}$/i