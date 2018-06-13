
(function () {
    var root = this;
    var CanvasZoom = function (options) {
        if (!options || !options.canvas) {
            throw 'CanvasZoom constructor: missing arguments canvas';
        }

        this.canvas = options.canvas;
        this.canvas.width = this.canvas.clientWidth;
        this.canvas.height = this.canvas.clientHeight;
        this.context = this.canvas.getContext('2d');

        this.desktop = options.desktop || false; 

        this.scaleAdaption = 1;

        var indoormap = document.getElementById("indoormap");
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
        this.previousSelectedCircle = null;
        this.firstTime = null;
        this.lastTime = null;
        //this.sn = null;

        this.mdown = false; 
        this.isDragging = false;
        this.isLineDragging = false;
        this.flag = false;
        this.isUpdate = false;
        
        this.init = false;
        this.checkRequestAnimationFrame();
        requestAnimationFrame(this.animate.bind(this));
    	
        this.setEventListeners();
    };
    
    CanvasZoom.prototype = {
        animate: function () {
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
            
            DrawMapInfo(this.scale.x * this.scaleAdaption, this.scale.y * this.scaleAdaption, this.position.x + this.positionAdaption.x, this.position.y + this.positionAdaption.y);
            DrawIbeacon(this.scale.x * this.scaleAdaption, this.scale.y * this.scaleAdaption, this.position.x + this.positionAdaption.x, this.position.y + this.positionAdaption.y);
            DrawLineInfo(this.scale.x * this.scaleAdaption, this.scale.y * this.scaleAdaption, this.position.x + this.positionAdaption.x, this.position.y + this.positionAdaption.y);
            requestAnimationFrame(this.animate.bind(this));
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
        
        setEventListeners: function () {
        	
            this.canvas.addEventListener('touchstart', function (e) {
                this.lastX = null;
                this.lastY = null;
                this.lastZoomScale = null;
            }.bind(this));

            this.canvas.addEventListener('mousewheel', function (e) {
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
                var sn = null;
                window.addEventListener('keyup', function (e) {
                    if (e.keyCode == 187 || e.keyCode == 61) {  
                        this.doZoom(15);
                    } else if (e.keyCode == 54) { 
                        this.doZoom(-15);
                    }
                }.bind(this));
            	
                this.canvas.addEventListener('mousedown', function (e) {
                	var clickX = (e.pageX-osX)/sX;
                    var clickY = (e.pageY-osY)/sY;
                    var pX = e.pageX;    
                    var pY = e.pageY;
  
                	for(var i = ibeacons.length-1; i >= 0; i--) {
                        var marker = ibeacons[i];
                        var distanceFromCenter = Math.sqrt(Math.pow(marker.x - clickX, 2)
                            + Math.pow(marker.y - clickY, 2))
                            
                        if (distanceFromCenter <= 16) {
                        	if (this.previousSelectedCircle != null) 
                        		this.previousSelectedCircle.isSelected = false;
                            this.previousSelectedCircle = marker;
                            sn = ibeacons[i].sn;
          				  
                            this.isSelected = true;

                            this.isDragging = true;

                            DrawIbeacon(sX, sY, osX, osY);
                        }
                    }
                	
                	for(var j = LineInfo.length-1; j >= 0; j--) {
                        var line = LineInfo[j];
                        var distanceFromCenter = Math.sqrt(Math.pow(line.x - clickX, 2)
                            + Math.pow(line.y - clickY, 2))
                            
                        if (distanceFromCenter <= line.rad) {
                        	var y = confirm("该位置是否要删除?");
              		       	if (y == true) {
              		       		LineInfo.splice(j,1);
              		    		for(var k = 0; k < LineInfo.length; k++) {
              		    			var info = {
              		    				x: LineInfo[k].x,
              		    				y: LineInfo[k].y,
              		    				rad: LineInfo[k].rad,
              		    				color: LineInfo[k].color,
              		    				isSelected: LineInfo[k].isSelected
              		    			}
              		    			reLine.push(info);
              		    		}
              		    		LineInfo.length = 0;
              		    		LineInfo = reLine;
              		    		//DrawLineInfo(sX, sY, osX, osY);
              		       	}else {
                        	if (this.previousSelectedCircle != null) 
                        		this.previousSelectedCircle.isSelected = false;
                        	this.previousSelectedCircle = line;
          				  
                        	line.isSelected = true;

                        	this.isLineDragging = true;
                          
                            addLine = false;

                            DrawLineInfo(sX, sY, osX, osY);
              		       	}
                        }
                     }
                	
                	if(this.isDragging == false && this.isLineDragging == false) {
                		this.mdown = true;
                        this.lastX = null;
                        this.lastY = null;   
                	}
                	
                	this.firstTime = new Date().getTime();
                }.bind(this));

               
                this.canvas.addEventListener('mouseup', function (e) {
                	this.mdown = false;
                	
                	var x = (e.pageX-osX)/sX;
     		        var y = (e.pageY-osY)/sY;
     		        
     		        //var mapid = this.previousSelectedCircle.mapid;
                    
                	if(addDraw) {
                		addMarker(e.pageX, e.pageY);
                		dbx = (e.pageX-osX)/sX;
                	    dby = (e.pageY-osY)/sY;
                	    dbx = Math.round(dbx*100)/100;
                	    dby = Math.round(dby*100)/100;
                	    
                		$("#iform").css('display','block');
                		$("#label1").css('display','block');
                		$('#label2').css('display','block');
                		$('#label4').css('display','block');
                		$('#label5').css('display','block');
                		
                		$('#btnAdd').css('display','block');
                		$('#btnEdit').css('display','none');
                		$('#btnDel').css('display','none');
                		
                		$('#label3').css('display','none');
                		$("#x").val(''+dbx);
                		$("#y").val(''+dby);
                		addDraw = false;
                	}
                	
                	if(addLine == true) {
                		addMarkerLine(e.pageX, e.pageY);
                	}
                	
                	/*if(this.isSelected == true && this.isUpdate == false) {
                		var t = confirm("是否删除信标?");
          		       	if (t == true) {
          		        	//更新beacon位置到数据库 
              		        var updateInfo = {
          		    			sn: this.previousSelectedCircle.sn
              		    	};
              		    		
          		    		$.ajax({
          						async: false,
          						type: "POST",
          						url: "../BeaconManager/doPost.do",
          						data: updateInfo,
          						success: function(data) {
          					   		var status = eval("("+data+")");
          					    	if (status == "0") {				    					 			    					 
          					    		alert("删除成功�?);	    					
          							}
          					   }		
          		    		});  
          		       	}
          		      this.isSelected = false;
                	}*/
                	
                	if(this.isUpdate == true && this.isDragging == true) {
                		var r = confirm("确定更新位置?");
                		var oper = "posEdit";
          		       	if (r == true) {
          		        	//更新beacon位置到数据库 
              		        var updateInfo = {
          		    			sn: sn,
          		    			mapid:mapid,
          		    			x: x,
          		    			y: y,
          		    			oper: oper
              		    	};
              		    		
          		    		$.ajax({
          						async: false,
          						type: "POST",
          						url: "../BeaconManager/doPost.do",
          						data: updateInfo,
          						success: function(data) {
          					   		var status = eval("("+data+")");
          					    	if (status == "0") {				    					 			    					 
          					    		alert("位置更新成功!");	    					
          							}
          					    	else if (status == "1") {				    					 			    					 
          					    		alert("位置更新失败 !");	    					
          							}
          						}		
          		    		});  
          		       	}
          		        this.isUpdate = false;
                	}else if(this.isDragging == true) {
                		$('#iform').css('display','block');
                    	$('#label1').css('display','none');
                    	$('#label2').css('display','none');
                    	$('#label3').css('display','none');
                    	$('#label4').css('display','none');
                    	$('#label5').css('display','none');
                    	
                    	
                    	$("#alias").val(''+this.previousSelectedCircle.alias);
                		$('#rssi1').val(''+this.previousSelectedCircle.rssi1);
                		$('#rssi2').val(''+this.previousSelectedCircle.rssi2);
                		$("#alarmType").val(''+this.previousSelectedCircle.alarmtype);
                		
                		$('#btnAdd').css('display','none');
                		$('#btnEdit').css('display','block');
                		$('#btnDel').css('display','block');
                		
                		var delBtn = document.getElementById("btnDel");
                		delBtn.onclick = function() {
                			//删除信标
                			var oper = "del";
                			var delInfo = {
                		    	sn: sn,
                		    	mapid:mapid,
                		    	oper: oper
                		    };
                			
                			$.ajax({
                				async: false,
                				type: "POST",
                				url: "../BeaconManager/doPost.do",
                				data: delInfo,
                				success: function(data) {
                			   		var status = eval("("+data+")");
                			    	if (status == "0") {				    					 			    					 
                			    		alert("删除成功!");
                					}
                				}		
                		    });
                			
                			$('#iform').css('display','none');
                			ibeacons.length = 0;
                    		$.get("../BeaconManager/doGet.do?mapid="+mapid, function(data) {
                            	for(i = 0; i < data.length; i++) {
                            		var ibeacon = {
                            			sn: data[i].sn,
                            			alias: data[i].alias,
                            			x: data[i].x,
                            			y: data[i].y,
                            			mapid: data[i].mapid,
                            			rssi1: data[i].rssi1,
                            			rssi2: data[i].rssi2,
                            			alarmtype: data[i].alarmtype
                            		}
                            		ibeacons.push(ibeacon);
                            	}
                			});
                    		DrawIbeacon(sX, sY, osX, osY);
                		}

                		var editBtn = document.getElementById("btnEdit");
                		editBtn.onclick = function() {
                			//更改信标信息 
                			var r1 = $("#rssi1").val();
                			var r2 = $("#rssi2").val();
                			if(Number(r2) > Number(r1)) {
                				alert("RSSI1 must be larger than RSSI2.");
                				return; 
                			}
                			var a = Math.abs(r1);
                			var n1 = (r2-r1)/3.01;
                			var n = n1.toFixed(5);
                			var oper = "edit";
                			
                			var BeaconInfo = {
                				sn: sn,
                				alias: $("#alias").val(),
                				alarmtype: $("#alarmType").val(),
                				rssi1: $("#rssi1").val(),
                				rssi2: $("#rssi2").val(),
                				mapid:mapid,
                				a: a,
                				n: n,
                				oper: oper
                			};
                			
                			$.ajax({
                				async: false,
                				type: "POST",
                				url: "../BeaconManager/doPost.do",
                				data: BeaconInfo,
                				success: function(data) {
                			   		var status = eval("("+data+")");
                			    	if (status == "0") {				    					 			    					 
                			    		alert("信息更改成功!"); 	    					
                					}
                			    }		
                			});
                			$('#iform').css('display','none');
                			ibeacons.length = 0;
                    		$.get("../BeaconManager/doGet.do?mapid="+mapid, function(data) {
                            	for(i = 0; i < data.length; i++) {
                            		var ibeacon = {
                            			sn: data[i].sn,
                            			alias: data[i].alias,
                            			x: data[i].x,
                            			y: data[i].y,
                            			mapid: data[i].mapid,
                            			rssi1: data[i].rssi1,
                            			rssi2: data[i].rssi2,
                            			alarmtype: data[i].alarmtype
                            		}
                            		ibeacons.push(ibeacon);
                            	}
                			});
                    		DrawIbeacon(sX, sY, osX, osY);
                		}
                	}
                	
                	
                	this.lastTime = new Date().getTime();
                    if( (this.lastTime - this.firstTime) < 220) {
                    	this.flag = true;
                    	this.firstTime = null;
                    	this.lastTime = null;
                    }

                	if(this.flag) {
                		this.isLineDragging = false;
                		this.isDragging = false;
    	        		this.mdown = false;
                		this.flag = false;
                	}else {
                		this.isLineDragging = false;
                		this.isDragging = false;
    	        		this.mdown = false;
                	}
                	
  
                }.bind(this));

                this.canvas.addEventListener('mousemove', function (e) {
            	   if (this.isDragging == true || this.isLineDragging == true) {
            		   if (this.previousSelectedCircle != null && this.isDragging == true) {
            		       var x = (e.pageX-osX)/sX;
            		       var y = (e.pageY-osY)/sY;

            		       this.previousSelectedCircle.x = x;
            		       this.previousSelectedCircle.y = y;
                            
            		       DrawIbeacon(sX, sY, osX, osY);
            		       
            		       this.isUpdate = true;
            		        
        		      }else if(this.previousSelectedCircle != null && this.isLineDragging == true) {
        		    	  var x = (e.pageX-osX)/sX;
          		          var y = (e.pageY-osY)/sY;

          		          this.previousSelectedCircle.x = x;
          		          this.previousSelectedCircle.y = y;

          		          DrawLineInfo(sX, sY, osX, osY);
          		          
        		      }
	    		    }else {
	    		    	var relativeX = e.pageX - this.canvas.getBoundingClientRect().left;
	                    var relativeY = e.pageY - this.canvas.getBoundingClientRect().top;
	
	                    if (e.target == this.canvas && this.mdown) {
	                        this.doMove(relativeX, relativeY);
	                    }
	
	                    if (relativeX <= 0 || relativeX >= this.canvas.clientWidth || relativeY <= 0 || relativeY >= this.canvas.clientHeight) {
	                        this.mdown = false;
	                    }
	    		    }
            	  // this.flag = false;
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

    root.CanvasZoom = CanvasZoom;
}).call(this);