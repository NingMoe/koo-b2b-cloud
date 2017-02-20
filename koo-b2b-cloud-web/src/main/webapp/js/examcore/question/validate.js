function isBlank(value) {
	var s = trim(value);
	return s=="";
}


function isAlphaOrNumber(value) {
    var Regx = /^[A-Za-z0-9]*$/;
    if (Regx.test(value)) {
        return true;
    }
    else {
        return false;
    }
} 

function isAlphaNumber(value) {
	if (isBlank(value)) return true;
	var foo = value.match("^\\w+$");
	return foo!=null;
}

function isNumber(value) {
	if (isBlank(value)) return true;
	return value.match("^-?\\d*\\.?\\d*$")!=null;
}

function isUnsignedNumber(value) {
	if (isBlank(value)) return true;
	return value.match("^\\d*\\.?\\d*$")!=null
}

function isInt(value) {
	if (isBlank(value)) return true;
	return value.match("^\\d+$")!=null
}

function isUnsignedInt(value) {
	if (isBlank(value)) return true;
	return value.match("^-?\\d+$")!=null
}

function isNumberLE(value1, value2) {
	if (isBlank(value1)) return true;
	var i1 = parseFloat(value1);
	var i2 = parseFloat(value2);
	return i1<=i2;
}

function isNumberGE(value1, value2) {
	if (isBlank(value1)) return true;
	var i1 = parseFloat(value1);
	var i2 = parseFloat(value2);
	return i1>=i2;
}

function isNumberLT(value1, value2) {
	if (isBlank(value1)) return true;
	var i1 = parseFloat(value1);
	var i2 = parseFloat(value2);
	return i1<i2;
}

function isNumberGT(value1, value2) {
	if (isBlank(value1)) return true;
	var i1 = parseFloat(value1);
	var i2 = parseFloat(value2);
	return i1>i2;
}

function isNumberScope(value, min, max) {
	if (isBlank(value)) return true;
	var i = parseFloat(value);
	var mi = parseFloat(min);
	var ma = parseFloat(max);
	return i>=mi && i<=ma;
}

function isLengthScope(value, min, max) {
	if (isBlank(value)) return true;
	return value.length>=min && value.length<=max;
}

function lengthGE(value, n) {
	if (isBlank(value)) return true;
	var s = trim(value);
	return s.length>=n;
}

function lengthLE(value, n) {
	if (isBlank(value)) return true;
	var s = trim(value);
	return s.length<=n;
}

function isDateFormat( value ) {
	//格式：yyyy-mm-dd
	if ( isBlank(value) ) return true;
	var b = value.match("^\\d{4}-\\d{1,2}-\\d{1,2}$")!=null;
	if (b) return true;
}

function isTimeFormat( value ) {
	if ( isBlank(value) ) return true;
	return value.match("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")!=null;
}

function isDateTimeFormat( value ) {
	if ( isBlank(value) ) return true;
	return value.match("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")!=null;
}

function isEmail( value ){
	if ( isBlank(value) ) return true;
	var regTextEmail = /^[\w-\._]+@[\w-_]+(\.(\w)+)*(\.(\w){2,3})$/;
	return regTextEmail.test(value);
}

function trim(value) {
	value = ltrim(value);
	value = rtrim(value);
	return value;
}

//去左空格; 
function ltrim(s){ 
	if(!s)
		s="";
	return s.replace( /^\s*/, ""); 
}

function rtrim(s){ 
	return s.replace( /\s*$/, ""); 
} 
//验证正整数
function isPlusInt(value){
	if ( isBlank(value) ) return true;
	var   type="^[0-9]*[1-9][0-9]*$";
	var   re   =   new   RegExp(type);
	if(value.match(re)==null)
	{
	    return false;
	}else{
		return true;
	}
} 

//长度大于某值,不判断空
function lengthLarger(value, n) {
	var s = trim(value);
	return s.length>=n;
}
//验证是否符合code编码格式 
function isCodePattern(value){
	if ( isBlank(value) ) return false;
	 var reg = /^[A-Za-z0-9\_\-\/]*$/;
	 var b=reg.test(value);
	 return b;
	
}	
