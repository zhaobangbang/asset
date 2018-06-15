var clientWidth = document.documentElement.clientWidth;
var clientHeight = document.documentElement.clientHeight;

var zoomScale = 1;

if (clientWidth <= clientHeight) {
	zoomScale = clientWidth / 720;
} else {
	zoomScale = clientHeight / 720;
}

var tipBorder = document.createElement("div");
var tipBg = document.createElement("div");
var divTip = document.createElement("div");
	
$('#indoormap').bind('mousewheel', function(event, delta) {
	var dir = delta > 0 ? 'Up' : 'Down';
    var counter = 0;
      
  	if (dir == 'Up') {
  		divTip.style.display = "none";
  		if (map.scale.x < 2.5) {
              map.scale.x += 0.05;
              map.scale.y += 0.05;
              map.position.x += -(clientWidth / 60);
              map.position.y += -(clientHeight / 60);
              counter++;
             
          } else {
              map.scale.x = 2.5;
              map.scale.y = 2.5;
              map.position.x = -(clientWidth / 2);
              map.position.y = -(clientHeight / 2);
          }
       
  	} else {
  		divTip.style.display = "none";
  		if (map.scale.x > 1) {
              map.scale.x -= 0.05;
              map.scale.y -= 0.05;
              map.position.x -= -(clientWidth / 60);
              map.position.y -= -(clientHeight / 60);
              counter++;
              
          } else {
              map.scale.x = 1;
              map.scale.y = 1;
              map.position.x = 0;
              map.position.y = 0;
          }
  	}
});

var MAPINFO = []; 
var Img = new Image();
Img.onLoad = function() {
    
}

var callbackfunction = function(jsondata) {
    if(jsondata == "") {
		alert("Nothing received from map server, please check the map ID!")
		return;
	}
    
	MapData_ = JSON.parse(jsondata);
    var x = MapData_.features[0].properties.x;
    var y = MapData_.features[0].properties.y;
    var width = MapData_.features[0].properties.width;
    var height = MapData_.features[0].properties.height;
    var imageurl = MapData_.features[0].properties.imageurl;
    
    var map_ = {
    	x: x,
		y: y,
		width: width*100,
		height: height*100,
		imageurl: imageurl
    };
    MAPINFO.push(map_);

};
   
var Lansmap_ = null;
(function () {
    var root = this; 
    var desktop = true;
    
    function request(mapid) {
    	var b=document.createElement("script");
        b.type = "text/javascript"; 
        // b.src = "http://192.168.1.104:8080/pcasset/jsonp?id=" + mapid + "&type=json&callback=callbackfunction"; 
        b.src = "http://120.27.104.163/asset/jsonp?id=" + mapid + "&type=json&callback=callbackfunction"; 
        var headb=document.getElementsByTagName("head")[0];
        headb.appendChild(b);
    }
    
    var LansiMap = function (div, options) {
    	Lansmap_ = document.getElementById(div);
        if (undefined == options || undefined == div) {
            throw 'CanvasZoom constructor: missing arguments canvas';
        }

        this.canvas = Lansmap_;
        this.canvas.width = this.canvas.clientWidth;
        this.canvas.height = this.canvas.clientHeight;
        this.context = this.canvas.getContext('2d');

        this.desktop = desktop || false;
        
        this.mapid = options.id;
        if(this.mapid != undefined) {
        	request(mapid);
        }
            
        this.scaleAdaption = 1;

        var indoormap = document.getElementById(div);
        var pageWidth = parseInt(indoormap.getAttribute("width"));
        var pageHeight = parseInt(indoormap.getAttribute("height"));
        currentWidth = document.documentElement.clientWidth;
        currentHeight = document.documentElement.clientHeight;

        var offsetX = 0;
        var offsetY = 0;
        if (pageWidth < pageHeight) { 
            this.scaleAdaption = currentHeight / pageHeight;
            if (pageWidth * this.scaleAdaption > currentWidth) {
                this.scaleAdaption = this.scaleAdaption * (currentWidth / (this.scaleAdaption * pageWidth));
            }
        } else { 
            this.scaleAdaption = currentWidth / pageWidth;
            if (pageHeight * this.scaleAdaption > currentHeight) {
                this.scaleAdaption = this.scaleAdaption * (currentHeight / (this.scaleAdaption * pageHeight));
            }
        }

        indoormap.setAttribute("width", pageWidth * this.scaleAdaption);
        indoormap.setAttribute("height", pageHeight * this.scaleAdaption);

        this.positionAdaption = {
            x: (parseInt(currentWidth) - parseInt(indoormap.getAttribute("width"))) / 2,
            y: (parseInt(currentHeight) - parseInt(indoormap.getAttribute("height"))) / 2
        };

        indoormap.setAttribute("width", currentWidth);
        indoormap.setAttribute("height", currentHeight);

        this.position = {
            x: 0,
            y: 0
        };

        this.scale = {
            x: 1,
            y: 1
        };

        this.focusPointer = {
            x: 0,
            y: 0
        }

        this.lastZoomScale = null;
        this.lastX = null;
        this.lastY = null;

        this.mdown = false; 
        this.firstTime = null;
        this.lastTime = null;
        this.previousSelectedCircle = null;
        this.isSelected = false;
        this.remove = false;
        this.reInfo = null;
        
        this.sX = null;
        this.sY = null;
        this.osX = null;
        this.osY = null;
        this.MARKERINFO = [];
        this.LINEINFO = [];

        this.init = false;
        this.checkRequestAnimationFrame();
        requestAnimationFrame(this.animate.bind(this));

        this.setEventListeners();
    };

    LansiMap.prototype = {
        animate: function () {
        	mapinfo = MAPINFO;
            if (!this.init) {
                var scaleRatio = null;
                if (this.canvas.clientWidth > this.canvas.clientHeight) {
                    scaleRatio = this.scale.x;
                } else {
                    scaleRatio = this.scale.y;
                }
                this.scale.x = scaleRatio;
                this.scale.y = scaleRatio;
                this.init = true;
            }
            this.context.clearRect(0, 0, this.canvas.width, this.canvas.height); 
            
            this.sX = this.scale.x * this.scaleAdaption;
        	this.sY = this.scale.y * this.scaleAdaption;
        	this.osX = this.position.x + this.positionAdaption.x;
        	this.osY = this.position.y + this.positionAdaption.y;
          
            this.DrawMapInfo(this.sX, this.sY, this.osX, this.osY, MAPINFO);
            if(this.MARKERINFO != null) {
            	this.DrawMarkerInfo(this.MARKERINFO);
            }
            if(this.LINEINFO != null) {
            	this.drawLine(this.LINEINFO);
            }
            requestAnimationFrame(this.animate.bind(this));
        },
        
        //绘制地图
        DrawMapInfo: function (scaleX, scaleY, offsetX, offsetY, MAPINFO) {
        	for (i = 0; i < MAPINFO.length; i++) {
                this.DrawBlock(MAPINFO[i].x * scaleX + offsetX, MAPINFO[i].y * scaleY + offsetY, MAPINFO[i].width * scaleX, MAPINFO[i].height * scaleY, MAPINFO[i].imageurl,MAPINFO[i].color);
            }
        },
        
        DrawBlock: function (x, y, width, height, imageurl, color) {
        	 var image = new Image();
             image.src = imageurl;
             this.context.drawImage(image, x, y, width, height);
        },
        
        //刷新时绘制marker操作
        DrawMarkerInfo: function  (markerinfo) {
        	for (j = 0; j < markerinfo.length; j++) {
        		Img.src = markerinfo[j].icon;
            	width = 32 * this.sX;
            	height = 32 * this.sY;
            	x = markerinfo[j].x * this.sX + this.osX;
            	y = markerinfo[j].y * this.sY + this.osY;
            	
            	if(markerinfo[j].label != undefined) {
                	this.context.font = markerinfo[j].label.size * zoomScale + "pt Microsoft YaHei";
                	this.context.fillStyle = markerinfo[j].label.color; 
                    var text = markerinfo[j].label.text;
                	this.context.fillText(text, x, y);
            	}
                
                w = width/2;
                h = height/2;
                 
                this.context.strokeStyle = 'rgba(255,255,255,0)';
                this.context.strokeRect(x-w, y-h, width, height);
      
                this.context.drawImage(Img, x-w, y-h, width, height);
            }
        },
        
        //绘制marker
        addMarker: function (marker) {
        	if(this.remove) {
        		marker.info = this.reInfo;
        		this.remove = false;
        		this.reInfo = null;
        	}
        	if(marker.info == undefined) {
        		marker.info = null;
        	}
        	this.MARKERINFO.push(marker);
        	Img.src = marker.icon;
        	width = 32 * this.sX;
        	height = 32 * this.sY;
        	x = marker.x * this.sX + this.osX;
        	y = marker.y * this.sY + this.osY;
        	
        	w = width/2;
            h = height/2;
             
            this.context.strokeStyle = 'rgba(255,255,255,0)';
            this.context.strokeRect(x-w, y-h, width, height);
  
            this.context.drawImage(Img, x-w, y-h, width, height);
        	
            //对label进行判断
        	if(marker.label != undefined) {
            	this.context.font = marker.label.size * zoomScale + "pt Microsoft YaHei";
            	this.context.fillStyle = marker.label.color; 
                var text = marker.label.text;
            	this.context.fillText(text, x, y);
        	}
        },
         
        //移除单个marker
        removeMarker: function (marker) {
        	for(i = 0; i < this.MARKERINFO.length; i++) {
        		if((marker.map == this.MARKERINFO[i].map) && (marker.x == this.MARKERINFO[i].x) && (marker.y == this.MARKERINFO[i].y)
        				&& (marker.label == this.MARKERINFO[i].label) && (marker.icon == this.MARKERINFO[i].icon)) {
        			this.remove = true;
        			if(this.MARKERINFO[i].info != null) {
        				this.reInfo = this.MARKERINFO[i].info;
        			}
        			this.MARKERINFO.splice(i, 1);
        			break;
        		}
        	}
        },
        
        //移除所有marker
        setMap: function (option) {
        	this.MARKERINFO.length = 0;
        },
        
        //绘制轨迹
        drawLine: function(line) {
        	var x1,y1,x2,y2;
        	if(this.LINEINFO.length == 0) {
        		this.LINEINFO = line;
        	}
        	for(t = 0; t < line.length; t++) {
        		this.context.beginPath();
        		this.context.strokeStyle = "red";
        		this.context.lineWidth = "1";

    			if(t == 0) {
    				x1 = line[t].x * this.sX + this.osX;
    				y1 = line[t].y * this.sY + this.osY;
    				x2 = x1;
    				y2 = y1;
    				this.context.moveTo(x1, y1);   
    				this.context.lineTo(x2, y2);
    				
    				width = 32 * this.sX;
    	        	height = 32 * this.sY;
    	        	
    	        	w = width/2;
    	            h = height/2;
    	  
    				var image1 = new Image();
    	            image1.src = 'http://www.lansitec.com/asset/images/start.png';
    	            this.context.drawImage(image1, x1-w, y1-h, width, height);
    			}else {
    				x1 = x2;   
    				y1 = y2;
    				x2 = line[t].x * this.sX + this.osX;
    				y2 = line[t].y * this.sY + this.osY;
    				this.context.moveTo(x1, y1);
    				this.context.lineTo(x2, y2);
    			}
    			
    			if(t == line.length-1) {
    				width = 32 * this.sX;
    	        	height = 32 * this.sY;
    	        	
    	        	w = width/2;
    	            h = height/2;
    	  
    				var image2 = new Image();
    				image2.src = 'http://www.lansitec.com/asset/images/end.png';
    	            this.context.drawImage(image2, x2-w, y2-h, width, height);
    	    	}
    			 
    			this.context.stroke();
    			this.context.closePath();
        	}
        },
        
        LineEmpty: function () {
        	this.LINEINFO.length = 0;
        },
        
        gesturePinchZoom: function (event) {
            var zoom = false;
            if (event.targetTouches.length >= 2) {
                var p1 = event.targetTouches[0];
                var p2 = event.targetTouches[1];
                this.focusPointer.x = (p1.pageX + p2.pageX) / 2;
                this.focusPointer.y = (p1.pageY + p2.pageY) / 2;
                var zoomScale = Math.sqrt(Math.pow(p2.pageX - p1.pageX, 2) + Math.pow(p2.pageY - p1.pageY, 2));
                if (this.lastZoomScale) {
                    zoom = zoomScale - this.lastZoomScale;
                }
                this.lastZoomScale = zoomScale;
            }
            return zoom;
        },

        doZoom: function (zoom) {
            if (!zoom)
                return;
            var currentScale = this.scale.x;
            var newScale = this.scale.x + zoom / 400;

            if (newScale > 1) {
                if (newScale > 2.5) {
                    newScale = 2.5;
                } else {
                    newScale = this.scale.x + zoom / 400;
                }
            } else {
                newScale = 1;
            }
            this.scale.x = newScale;
            this.scale.y = newScale;

            var deltaScale = newScale - currentScale;
            var currentWidth = (this.canvas.width * this.scale.x);
            var currentHeight = (this.canvas.height * this.scale.y);
            var deltaWidth = this.canvas.width * deltaScale;
            var deltaHeight = this.canvas.height * deltaScale;
            var canvasmiddleX = this.focusPointer.x;
            var canvasmiddleY = this.focusPointer.y;
            var xonmap = (-this.position.x) + canvasmiddleX;
            var yonmap = (-this.position.y) + canvasmiddleY;
            var coefX = -xonmap / (currentWidth);
            var coefY = -yonmap / (currentHeight);
            var newPosX = this.position.x + deltaWidth * coefX;
            var newPosY = this.position.y + deltaHeight * coefY;
            
            var newWidth = currentWidth + deltaWidth;
            var newHeight = currentHeight + deltaHeight;
            if (newWidth < this.canvas.clientWidth)
                return;
            if (newPosX > 0) {
                newPosX = 0;
            }
            if (newPosX + newWidth < this.canvas.clientWidth) {
                newPosX = this.canvas.clientWidth - newWidth;
            }

            if (newHeight < this.canvas.clientHeight)
                return;
            if (newPosY > 0) {
                newPosY = 0;
            }
            if (newPosY + newHeight < this.canvas.clientHeight) {
                newPosY = this.canvas.clientHeight - newHeight;
            }

            this.scale.x = newScale;
            this.scale.y = newScale;
            this.position.x = newPosX;
            this.position.y = newPosY;
        },

        doMove: function (relativeX, relativeY) {
            if (this.lastX && this.lastY) {
                var deltaX = relativeX - this.lastX;
                var deltaY = relativeY - this.lastY;

                var currentWidth = (this.canvas.clientWidth * this.scale.x);
                var currentHeight = (this.canvas.clientHeight * this.scale.y);

                this.position.x += deltaX;
                this.position.y += deltaY;

                
                if (this.position.x > 0) {
                    this.position.x = 0;
                } else if (this.position.x + currentWidth < this.canvas.clientWidth) {
                    this.position.x = this.canvas.width - currentWidth;
                }
                if (this.position.y > 0) {
                    this.position.y = 0;
                } else if (this.position.y + currentHeight < this.canvas.clientHeight) {
                    this.position.y = this.canvas.height - currentHeight;
                }
            }
            this.lastX = relativeX;
            this.lastY = relativeY;
        },
        
        //存放infomation
        ListenClick: function(marker, content) {
        	for(t = 0; t < this.MARKERINFO.length; t++) {
        		if((marker.x == this.MARKERINFO[t].x) && (marker.y == this.MARKERINFO[t].y)) {
        			this.MARKERINFO[t].info = content;
        			break;
        		}
        	}
        },
        
        setEventListeners: function () {
            this.canvas.addEventListener('touchstart', function (e) {
                this.lastX = null;
                this.lastY = null;
                this.lastZoomScale = null;
            }.bind(this));

            this.canvas.addEventListener('touchmove', function (e) {
                e.preventDefault();

                if (e.targetTouches.length == 2) { 
                    this.doZoom(this.gesturePinchZoom(e));
                } else if (e.targetTouches.length == 1) {
                    var relativeX = e.targetTouches[0].pageX - this.canvas.getBoundingClientRect().left;
                    var relativeY = e.targetTouches[0].pageY - this.canvas.getBoundingClientRect().top;

                    this.doMove(relativeX, relativeY);
                }
            }.bind(this));

            if (this.desktop) {
                window.addEventListener('keyup', function (e) {
                    if (e.keyCode == 187 || e.keyCode == 61) { 
                        this.doZoom(15);
                    } else if (e.keyCode == 54) {
                        this.doZoom(-15);
                    }
                }.bind(this));

                window.addEventListener('mousedown', function (e) {
                	divTip.style.display = "none";
                    this.mdown = true;
                    this.lastX = null;
                    this.lastY = null;
                }.bind(this));

                window.addEventListener('mouseup', function (e) {
                	this.mdown = false;
                    this.lastX = null;
                    this.lastY = null;
                    
                	var clickX = (e.pageX-this.osX)/this.sX;
                    var clickY = (e.pageY-this.osY)/this.sY;
  
                	for(i = 0; i < this.MARKERINFO.length; i++) {
                		var marker = this.MARKERINFO[i];
                        var distanceFromCenter = Math.sqrt(Math.pow(marker.x - clickX, 2)
                            + Math.pow(marker.y - clickY, 2));
                        
                        if (distanceFromCenter <= 16) {
                              this.previousSelectedCircle = marker;
              		
                              this.isSelected = true;
                        }
                     }
                	
                    if(this.isSelected) {
                    	content = this.previousSelectedCircle.info;
                    	divTip.innerText = "";
                    	if(content != null) {
                    		tipBorder.style.position = "absolute";
                    		tipBorder.style.width = 0;
                    		tipBorder.style.lineHeight = 0;
                    		tipBorder.style.borderWidth = "20px 15px 0";
                    		tipBorder.style.borderStyle = "solid dashed dashed";
                    		tipBorder.style.borderLeftColor = "transparent";
                    		tipBorder.style.borderRightColor = "transparent";
                    		tipBorder.style.bottom = "-20px";
                    		tipBorder.style.right = "50%";

                    		
                    		tipBg.style.position = "absolute";
                    		tipBg.style.width = 0;
                    		tipBg.style.lineHeight = 0;
                    		tipBg.style.borderWidth = "20px 15px 0";
                    		tipBg.style.borderStyle = "solid dashed dashed";
                    		tipBg.style.borderLeftColor = "transparent";
                    		tipBg.style.borderRightColor = "transparent";
                    		tipBg.style.bottom = "-19px";
                    		tipBg.style.right = "50%";
                    		
                    		
                        	divTip.style.position = "fixed";
                        	divTip.style.zIndex = "10";
                        	divTip.style.width = content.width;
                        	divTip.style.height = content.height;
                        	divTip.style.lineHeight = content.lineHeight;
                        	divTip.style.background = content.bgColor;
                        	divTip.style.borderWidth = content.borderWidth;
                        	divTip.style.borderStyle = content.borderStyle;
                        	divTip.style.borderColor = content.borderColor;
                        	divTip.style.borderRadius = content.borderRadius;
                        	divTip.style.color = content.color;
                        	divTip.style.fontSize = content.size;
                        	
                        	tipBorder.style.color = content.borderColor;
                        	divTip.appendChild(tipBorder);
                        	
                        	tipBg.style.color = content.bgColor;
                        	//divTip.appendChild(document.createTextNode(content.text));
                        	divTip.innerText = ""+ content.text;
                        	
                        	dX = parseInt(content.width);
                        	dY = parseInt(content.height) + 20;
                        	
                        	dy = e.pageY - dY;
                        	dx = e.pageX - (dX/2);
                        	
                        	divTip.style.top = dy +"px";
                        	divTip.style.left = dx +"px";
                        	
                        	divTip.style.display = "block";
                        	divTip.appendChild(tipBg);
                        	document.body.appendChild(divTip);
                    	}else {
                    		divTip.style.display = "none";
                    	}
                    	this.isSelected = false;
                	}
                }.bind(this));

                window.addEventListener('mousemove', function (e) {
                    var relativeX = e.pageX - this.canvas.getBoundingClientRect().left;
                    var relativeY = e.pageY - this.canvas.getBoundingClientRect().top;
                    
                    if (e.target == this.canvas && this.mdown) {
                        this.doMove(relativeX, relativeY);
                        divTip.style.display = "none";
                    }

                    if (relativeX <= 0 || relativeX >= this.canvas.clientWidth || relativeY <= 0 || relativeY >= this.canvas.clientHeight) {
                        this.mdown = false;
                    }
                }.bind(this));
            }
        },

        checkRequestAnimationFrame: function () {
            var lastTime = 0;
            var vendors = ['ms', 'moz', 'webkit', 'o'];
            for (var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
                window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
                window.cancelAnimationFrame = window[vendors[x] + 'CancelAnimationFrame']
						|| window[vendors[x] + 'CancelRequestAnimationFrame'];
            }

            if (!window.requestAnimationFrame) {
                window.requestAnimationFrame = function (callback, element) {
                    var currTime = new Date().getTime();
                    var timeToCall = Math.max(0, 16 - (currTime - lastTime));
                    var id = window.setTimeout(function () {
                        callback(currTime + timeToCall);
                    }, timeToCall);
                    lastTime = currTime + timeToCall;
                    return id;
                };
            }

            if (!window.cancelAnimationFrame) {
                window.cancelAnimationFrame = function (id) {
                    clearTimeout(id);
                };
            }
        }
    };

    root.LansiMap = LansiMap;
}).call(this);