/**
 * splitScren.js
 * v1.2
 * 2015-5-14
 * by linxia
 **/
var splitScreen = function(elem, options) {
	this.elem = elem;
	this.options = options;
	this.initialize.apply(this);
}

splitScreen.prototype = {
	initialize: function() {
		this.handlers = {};
		this.screenMode(1);
	},
	screenMode: function(mode, callback) {
		this.elem.empty();
		this.data = null;
		this.defaultShow = null; //默认展示页面的索引
		switch (Number(mode)) {
			case 1:
				data = [
					['fp-1-1']
				];
				this.defaultShow = [0];
				break;
			case 2:
				data = [
					['fp-2-1'],
					['fp-2-2']
				];
				this.defaultShow = [0, 1];
				break;
			case 3:
				data = [
					['fp-3-1'],
					['fp-3-2', 'fp-3-3']
				];
				this.defaultShow = [0, 1, 2];
				break;
			case 4:
				data = [
					['fp-4-1', 'fp-4-2'],
					['fp-4-3', 'fp-4-4']
				];
				this.defaultShow = [0, 1, 2, 3];
				break;
			case 5:
				data = [
					['fp-5-1'],
					['fp-5-2'],
					['fp-5-3', 'fp-5-4', 'fp-5-5']
				];
				this.defaultShow = [4, 5, 1, 2, 3];
				break;
			case 6:
				data = [
					['fp-6-1'],
					['fp-6-2', 'fp-6-3'],
					['fp-6-4', 'fp-6-5', 'fp-6-6']
				];
				this.defaultShow = [4, 5, 6, 7, 8, 8];
				break;
			default:
				alert("不支持" + mode + "分屏");
		}

		if (typeof this.data != null) {
			this.renderPanel();
			this.bindPanel();
		}
		callback && callback();
	},
	//渲染DOM结构
	renderPanel: function() {
		var that = this;
		var options = this.options;
		var htmlstr = '';

		for (var item = 0; item < options.length; item++) {
			htmlstr += '<option value="' + options[item].src + '" label = "' + options[item].frameName + '">' + options[item].frameName + '</option>';
		}
		for (var i = 0; i < data.length; i++) {
			var moduleDiv = $('<div></div>').addClass('fp-module-' + i + '');

			for (var j = 0; j < data[i].length; j++) {
				var fpDiv = $('<div>').addClass(data[i][j]).addClass('fp-box');
				var topbarDiv = $('<div class="topbarDiv" style="display: none;">' +
					'<span class="optionsWrap">' +
					'<a class="btnBig" title="放大" href="javascript:void(0);">放大</a><a class="btnSmall" title="缩小" href="javascript:void(0);" style="display:none;">缩小</a> <a href="javascript:void(0);" class="btnCls" title="关闭"style="display:none;">关闭</a>' +
					'</span>' +
					'<div class="dropDiv">' +
					'<select class="fp-select"><option value="-1">请选择</option>' + htmlstr +
					'</select>' +
					'</div>' +
					'</div>');
				var iframe = $('<iframe width="100%" height="100%" frameBorder="0" scrolling = "no"></iframe>');
				if (i == 0) {
					fpDiv.attr('zp', 'zp');
				}
				fpDiv.append(topbarDiv);
				fpDiv.append(iframe);
				moduleDiv.append(fpDiv);
				this.elem.append(moduleDiv);
			}
		}
		// render iframe
		this.elem.find('iframe').each(function(i) {
			if (options[i]['src']) {
				var frameSrc = options[that.defaultShow[i]]['src'];
				var frameName = options[that.defaultShow[i]]['frameName'];
				var curtopbar = $(this).siblings('.topbarDiv');
				that.loadIframe($(this), frameSrc, frameName);
				curtopbar.find('option').each(function() {
					if ($(this).attr('label') == frameName) {
						$(this).attr('selected', 'selected');
					}
				});

			}
		});
	},
	bindPanel: function() {
		var that = this;
		// add select Event
		this.elem.on('change', '.fp-select', function() {
			var value = $(this).find('option:selected').val();
			var label = $(this).find('option:selected').attr('label');
			var iframe = $(this).closest('.fp-box').find('iframe');
			if (value != "-1") {
				that.loadIframe(iframe, value, label);
			}
		});

		// btnbig Event
		this.elem.on('click', '.btnBig', function() {
			var obj = Panel.getSize();
			var btnSmall = $(this).siblings('.btnSmall');
			var btnCls = $(this).siblings('.btnCls');
			var fpbox = $(this).closest('.fp-box');
			fpbox.css({
				'zIndex': 999
			}).stop().animate({
				'top': 0,
				'left': 0,
				'width': obj.w - 2,
				'height': obj.h,
				'right': 0,
				'bottom': 0

			}, 300).attr('scaleMode', 'large');
			that.elem.find('.fp-box:not([scaleMode="large"])').hide();
			$(this).hide();
			$('html,body').css({
				'overflow': 'hidden'
			});
			btnSmall.show();
			//btnCls.show();
		});
		// btnsmall Event
		this.elem.on('click', '.btnSmall', function() {
			var btnBig = $(this).siblings('.btnBig');
			var fpbox = $(this).closest('.fp-box');
			var btnCls = $(this).siblings('.btnCls');
			fpbox.removeAttr('style').removeAttr('scaleMode');
			$(this).hide();
			that.elem.find('.fp-box').show();
			$('html,body').css({
				'overflow': 'visible'
			});
			btnCls.hide();
			btnBig.show();
		});
		// btncls Event
		this.elem.on('click', '.btnCls', function() {
			var fpbox = $(this).closest('.fp-box');
			fpbox.remove();
			that.elem.find('.fp-box').show();
			that.fire('removeSettingCls');
		});
	},
	toggleTopbar: function(callback) {
		if (this.elem.find('.topbarDiv:visible').length > 0) {
			this.elem.find('.topbarDiv').hide();
		} else {
			this.elem.find('.topbarDiv').show();
		}
		callback && callback();
	},
	loadIframe: function(iframe, src, framename) {
		$(iframe).attr('src', src);
		$(iframe).attr('framename', framename);
	},
	_on: function(type, handler) {
		if (typeof this.handlers[type] == "undefined") {
			this.handlers[type] = [];
		}
		this.handlers[type].push(handler);
		return this;
	},
	fire: function(type, data) {
		if (this.handlers[type] instanceof Array) {
			var handlers = this.handlers[type];
			for (var i = 0, len = handlers.length; i < len; i++) {
				handlers[i](data);
			}
		}
	}
};

var Panel = {
	config: {
		header: $('.header'),
		container: $('.container'),
		footer: $('.footer'),
		win: $(window)
	},
	resize: function() {
		var topH = Panel.config.header.height();
		var botH = Panel.config.footer.height();
		var mainH = Panel.config.win.height() - topH - botH;
		mainH = mainH < 0 ? 100 : mainH;
		Panel.config.container.height(mainH);
		if ($('.fp-box[scaleMode="large"]').length > 0) {
			$('.fp-box[scaleMode="large"]').css({
				'width': Panel.config.win.width() - 2,
				'height': mainH
			});
		}
	},
	getSize: function() {
		var obj = {
			w: Panel.config.container.width(),
			h: Panel.config.container.height()
		};
		return obj;
	}
};