//document.write("<script src=\"../js/jqzoom/js/jquery.jqzoom-core.js\"></script>");
//document.write('<link rel="stylesheet" href="../js/jqzoom/css/jquery.jqzoom.css" type="text/css">');
//$(document).ready(function () {
//    $('.jqzoom').jqzoom({
//        zoomType: 'standard',
//        lens: true,
//        preloadImages: false,
//        alwaysOn: false,
//        zoomWidth: '400',
//        zoomHeight: '400',
//        position: 'left',
//        xOffset: 30,
//        yOffset: 0
//    });
//});

//function regimg(elementid) {
//    //
//    var file = $('#img' + elementid).attr('src');
//    var img = new Image();
//    img.src = file;
//    if (img.height == "165" && img.width == "200") {
//        return;
//    }
//    $('#aimg' + elementid).jqzoom({
//        zoomType: 'standard',
//        lens: true,
//        preloadImages: false,
//        alwaysOn: false,
//        zoomWidth: '400',
//        zoomHeight: '400',
//        position: 'left',
//        xOffset: 30,
//        yOffset: 0
//    });
//}

document.write("<script src=\"../js/imageZoom.js\"></script>")
function regimg(obj, src) {
    var img = new Image();

    img.onload = function () {
        var imgObj = this;

        if (imgObj.height == 165 && imgObj.width == 200) {
            $(obj).attr("onload", "");
            $(obj).attr("src", "../Images/NoImg_300.jpg");
            return;
        }
        else {
            $(obj).attr("onload", "");
            obj.src = imgObj.src;
            new imageZoom(obj, {
                mul: 5, //放大5倍
                viewerPos: { h: 10, v: 0 },
                viewerMul: 1.2//展示层以小图片的1.2倍大小
            });
        }

    }
    img.onerror = function () {
        $(obj).attr("onload", "");
        $(obj).attr("src", "../Images/NoImg_300.jpg");
    }
    img.src = src;
    //   
    //    var filesize = img.fileSize;
    //    if (filesize == "undefined") filesize = img.files[0].fileSize;
    //    if (img.src.indexOf("noimg_128.jpg") <= 0) {
    //        
    //        if (img.width == "128" && img.height == "128" && filesize == "") {
    //        }
    //        else {
    //           
    //        }
    //    }
}