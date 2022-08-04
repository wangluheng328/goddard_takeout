
function isValidUsername (str) {
  return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

// Custom check whether the entered phone number is indeed in the format of a US phone number
//  - doesn't start with 0, and has a length of 10
function isCellPhone (val) {
  if ((val[0] != "0") && (val.length == 10)) {
    return true
  } else {
    return false
  }
}

//校验账号
function checkUserName (rule, value, callback){
  if (value == "") {
    callback(new Error("Please enter username"))
  } else if (value.length > 20 || value.length <3) {
    callback(new Error("Username length must be 3-20"))
  } else {
    callback()
  }
}

//校验姓名
function checkName (rule, value, callback){
  if (value == "") {
    callback(new Error("Please enter name"))
  } else if (value.length > 12) {
    callback(new Error("Name length must be 1-12"))
  } else {
    callback()
  }
}

function checkPhone (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("Please enter phone number"))
  } else if (!isCellPhone(value)) {// Use the method defined above to check the format of the phone number
    callback(new Error("Please enter correct phone number"))
  } else {
    callback()
  }
}


function validID (rule,value,callback) {
  // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
  let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  if(value == '') {
    callback(new Error('Please enter ID'))
  } else if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('ID incorrect'))
  }
}