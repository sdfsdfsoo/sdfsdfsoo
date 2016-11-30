function DropDown(el, ti,fun) {
    
    this.dd = el;
    this.placeholder = this.dd.children('span');
    this.opts = this.dd.find('ul.dropdown > li');
    this.val = this.opts.first().attr('val');
    this.text = this.opts.first().text();
    this.index = 0;
    this.name = ti;
    this.itemClick = fun;
    this.initEvents();   
}
DropDown.prototype = {
    initEvents: function () {
        
        var obj = this;
        obj.placeholder.text(this.getName1())
        obj.dd.on('click', function (event) {
            $(this).toggleClass('active');
            return false;
        });

        obj.opts.on('click', function () {
            
            var opt = $(this);
            obj.text = opt.text();
            obj.val = opt.attr('val');
            obj.index = opt.index();
            obj.placeholder.text(obj.getName() + obj.text);
            strSort = obj.val;
            strSortText = obj.text;
            obj.itemClick();
        });
        //obj.opts.on('click', this.itemClick);
    },
    getText: function () {
        return this.text;
    },
    getIndex: function () {
        return this.index;
    },
    getValue: function () {
        return this.val;
    },
    getName: function () {
        if (this.name != null) {
            return this.name + ":";
        }
        else {
            return "";
        }
    },
    getName1: function () {
        if (this.name != null) {
            return this.name;
        }
        else {
            return "";
        }
    },
    setText: function (text) {
        this.placeholder.text(text);
    }
}
