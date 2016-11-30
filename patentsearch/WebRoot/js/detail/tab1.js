var imageZoom = function (img, options) {
    this.img = this.g(img);
    if (this.img.nodeName != "IMG") {
        if (this.img && this.img.children[0].nodeName == "IMG") this.img = this.img.children[0];
        else throw Error("Invalid argument [img] !");
    }
    this.setOptions(options);
    this.init();
};
imageZoom.prototype = {
    g: function (id) { return typeof (id) == "string" ? document.getElementById(id) : id; },
    ae: function (el, type, call) {
        if (el.addEventListener) el.addEventListener(type, call, false);
        else el.attachEvent("on" + type, call);
    },
    getPos: function (o) {//取元素坐标
        try {
            var x = 0, y = 0;
            var y0=o.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.scrollTop;
            var y1=o.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.scrollTop;
            do { x += o.offsetLeft; y += o.offsetTop; }
            while (o = o.offsetParent);
        } catch (e) { }
        return { 'x': x , 'y': y - y0 - y1};
    },
    setOptions: function (options) {
        this.options = {
            mul: 0, //默认为不放大（显示图片的原来大小）
            bigImg: null, //指定放大的图片路径（要跟小图成比例才好）
            viewer: null, //指定显示的位置(可以为一个div或者其id)
            viewerCla: "", //指定预览的div的类样式
            viewerMul: 1, //指定显示div的放大倍数，默认为原大小,设置viewer时此参数无效
            viewerPos: { h: 10, v: 0 }, //自定义展示层位置,h水平文向，v垂直方向 ,假如水平方向，正数表示右边，负数表示左边
            onShow: function () { }, //自定义事件，显示时触发，当viewer不为空时无效
            onHide: function () { }
        };
        for (var o in options) { this.options[o] = options[o]; }
        this.options.bigImg = this.options.bigImg || this.img.src;
    },
    getSize: function (o) {
        return { w: o.offsetWidth, h: o.offsetHeight };
    },
    createView: function () {
        var _is = this.getSize(this.img);
        var d = document;
        if (this.options.viewer) {
            this.viewer = this.g(this.options.viewer);
            this.viewer.style.overflow = "hidden";
            this.viewer.style.position = "relative";
        } else {
            this.viewer = d.createElement("div");
            this.viewer.className = this.options.viewerCla;
            var pos = this.getPos(this.img);

            var t = pos.y + this.options.viewerPos.v;
            if (this.options.viewerPos.v < 0) { t -= _is.h * this.options.viewerMul; }
            else if (this.options.viewerPos.v > 0) { t += _is.h; }
            else t = pos.y;

            var l = pos.x + this.options.viewerPos.h;
            if (this.options.viewerPos.h < 0) { l -= _is.w * this.options.viewerMul; }
            else if (this.options.viewerPos.h > 0) { l += _is.w; }
            else l = pos.x + _is.w;

            this.viewer.style.cssText = "display:none;overflow:hidden;position:absolute;top:" + (t) + "px;left:" + (l) + "px;height:" + _is.h * this.options.viewerMul + "px;width:" + _is.w * this.options.viewerMul + "px";
            d.body.appendChild(this.viewer);
        }
        this.viewimg = d.createElement("img");
        this.viewimg.style.cssText = "position:relative;left:-33%;top:-33%;z-index:1000;";
        this.viewimg.src = this.options.bigImg;
        if (this.options.mul) {//设置放大倍数
            this.viewimg.style.width = _is.w * this.options.mul + "px";
            this.viewimg.style.height = _is.h * this.options.mul + "px";
        }
        this.viewer.appendChild(this.viewimg);
    },
    move: function (e) {
        if (!this.options.mul)
            this.options.mul = this.viewimg.offsetHeight / this.img.offsetHeight;
        var pos = this.getPos(this.img);
        var l = e.clientX - pos.x + (document.documentElement.scrollLeft || document.body.scrollLeft); //鼠标位置相对于图片左上角的偏移
        var t = e.clientY - pos.y + (document.documentElement.scrollTop || document.body.scrollTop);
        var zs = this.getSize(this.viewer);
        var pl = -l * this.options.mul + zs.w / 2;
        var pt = -t * this.options.mul + zs.h / 2;
        pl = pl > 0 ? 0 : pl;
        pt = pt > 0 ? 0 : pt;

        var vs = this.getSize(this.viewimg);
        pl = Math.max(pl, zs.w - vs.w);
        pt = Math.max(pt, zs.h - vs.h);

        this.viewimg.style.left = pl + "px";
        this.viewimg.style.top = pt + "px";
    },
    init: function () {
        var o = this;
        var load = function (a) {//图片加载
            o.createView.call(o);
            o.img.setAttribute("alt", "");
            o.ae(o.img, "mousemove", function (event) { o.move.call(o, event); });
            if (!o.options.viewer) {
                o.ae(o.img, "mouseover", function () { o.options.onShow(); o.viewer.style.display = "" });
                o.ae(o.img, "mouseout", function () { o.options.onHide(); o.viewer.style.display = "none" });
            }
        };
        if (typeof (document.readyState) == "undefined" || window.opera) {
            var de = document.documentElement || document.body;
            var h = de.scrollHeight;
            var t = setInterval(function () {
                if (h == de.scrollHeight) {
                    clearInterval(t);
                    load();
                } else h = de.scrollHeight;
            }, 500);
        } else if (document.readyState == "complete")
            load();
        else
            o.ae(window, "load", load);
        this.img.style.cssText = "position:relative; zoom:100%; cursor:move;"
    },


    ////////////////add xiwl  图片缩放
    // 缩放图片
    imgToSize: function (oBool) {
        var oImg = this.img;
        oImg.style.zoom = parseInt(oImg.style.zoom) + (oBool ? 2 : -2) + '%';
    },

    // 旋转图片
    //oArcSize:= 1,
    imgRoll: function () {
        var oImg = this.img;
        if (!this.oArcSize) {
            this.oArcSize = 1;
        }
        oImg.style.filter = 'Progid:DXImageTransform.Microsoft.BasicImage(Rotation=' + this.oArcSize + ')';
        this.oArcSize += 1;
        this.oArcSize = this.oArcSize == 5 ? 1 : this.oArcSize;
    },

    // 翻转图片
    imgReverse: function (arg) {
        var oImg = this.img;
        if (oImg.style.filter != "") {
            oImg.style.filter = '';
        } else {
            oImg.style.filter = 'Flip' + arg;
        }
    }
};

var objFtZoom = null;

function resizeFt(obj, src) {
	
    var nSizi = 300;
    var imgTmp = new Image();
    imgTmp.onload = function () {
        var imgObj = this;
        if (imgObj.height == 165 && imgObj.width == 200) {  //&& imgObj.fileSize == "16301"
            $('#'+obj.id).attr("onload", "");
            $('#'+obj.id).attr("src", "../Images/NoImg_300.jpg");
            return;
        }

        if (imgObj.height > imgObj.width && imgObj.height > nSizi) {
            imgObj.width = imgObj.width / (imgObj.height / nSizi);
            imgObj.height = nSizi;
        } else if (imgObj.width > nSizi) {
            imgObj.height = imgObj.height / (imgObj.width / nSizi);
            imgObj.width = nSizi;
        }

        $('#divImgFt').html("");
        imgObj.id = "ImageFt";
        imgObj.alt = '摘要附图'
        $('#divImgFt').append(imgObj);

        objFtZoom = new imageZoom(obj.id, { mul: 2, viewerPos: { h: -1, v: 0 },viewerMul: 1.2});
        $('#divFtZoom').show();
    };

    imgTmp.onerror = function () {
        $('#'+obj.id).attr("onload", ""); //unbind("onload");
        $('#'+obj.id).attr("src", "../Images/NoImg_300.jpg");
    }

    imgTmp.src = src;
}

function ToSize(i) {
    objFtZoom.imgToSize(i);
}

function imgRoll() {
    objFtZoom.imgRoll();
}

function imgReverse(i) {
    objFtZoom.imgReverse(i);
}
function imgReverse(i) {
    objFtZoom.imgReverse(i);
}