//javascript 去字符串两边的空格
String.prototype.Trim = function () {

    if (arguments.length == 1) {

        var reg = eval("/(^" + arguments[0] + "*)|(" + arguments[0] + "*$)/g");
        return this.replace(reg, "");
    }
    else {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    }
}
//javascript 去字符串两边的空格
String.prototype.trim = function () {
    if (arguments.length == 1) {

        var reg = eval("/(^" + arguments[0] + "*)|(" + arguments[0] + "*$)/g");
        //var reg = /(^\arguments[0]*)|(\arguments[0]*$)/g;
        return this.replace(reg, "");
    }
    else {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    }
}

//javascript 去字符串两边的空格
String.prototype.Insert = function (index, strValue) {
    return this.substring(0, index) + strValue + substring(index);
}

//javascript 去字符串首位两端的空格以及链接字符+-*()等
String.prototype.TrimLink = function () {
    var temp = this.replace(/^([\%|\（|\）|\(|\)|\s*|\-|+|*]*)/g, "");
    temp = temp.replace(/([\%|\（|\）|\(|\)|\s*|\-|+|*]*)$/g, "");
    return temp;

}
//javascript 去字符串两边的空格
String.prototype.TrimEnd = function () {
    return this.substring(0, this.length - 1);
}
//javascript 去掉所有空格
String.prototype.DeleteSpace = function () {
    return this.replace(/(\s*)/g, "");
}

//javascript 去掉首位字符，并将多个空格替换成一个空格
String.prototype.ReduceSpace = function () {
    return this.replace(/(\s+)/g, " ");
}
//javascript 去掉首位字符，并将多个空格替换成一个空格
String.prototype.GetLength = function () {
    return this.replace(/[^\x00-\xff]/g, "**").length;
}
String.Format = function () {
    if (arguments.length == 0)
        return null;

    var str = arguments[0];
    for (var i = 1; i < arguments.length; i++) {
        var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
        str = str.replace(re, arguments[i]);
    }
    return str;
}
String.prototype.PadLeft = function (b, c) {
    var d = this;
    while (d.length < b) {
        d = c + d;
    }
    return d;
}
String.prototype.PadRight = function (b, c) {
    var d = this;
    while (d.length < b) {
        d = d + c;
    }
    return d;
}


String.prototype.EndWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substring(this.length - s.length) == s)
        return true;
    else
        return false;
    return true;
}

String.prototype.StartWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
}