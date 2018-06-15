var sX, sY, osX, osY;
var x1,y1,x2,y2;
var uList,btnEnable,btnReset,rangBox;
var result = "0.00";

var canvas = document.getElementById("indoormap");
var context2D = canvas.getContext("2d");

var marImg = new Image();
marImg.onLoad = function() {
	
}
marImg.src = '../images/ibeacon.png';

var LineImg = new Image();
LineImg.onLoad = function() {
	
}
LineImg.src = '../images/line.png';

var LineInfo = new Array;
for(var i = 0;i < LineInfo.length;i++) {
	LineInfo[i].onclick = function() {
	  alert(""+Line);
	}
}

var clientWidth = document.documentElement.clientWidth;
var clientHeight = document.documentElement.clientHeight;

var zoomScale = 1;
if (clientWidth <= clientHeight) {
    zoomScale = clientWidth / 720;
} else {
    zoomScale = clientHeight / 720;
}

var addDraw =false;
var addBtn = document.getElementById("button1");
var btnEnable = document.getElementById("button2");
var btnStop = document.getElementById("button3");
var btnReset = document.getElementById("button4");

function buttonEvent() {
	var contianer = document.body;
	
	uList = document.createElement("ul");
	uList.setAttribute("class", "nav navbar-nav");
	uList.style.marginTop="30px";
	
	li3 = document.createElement("li");
	li3.style.marginTop = "10px";
	li3.style.float = "left";
	li3.style.paddingLeft = "165px";
	rangBox = document.createElement('input');
	rangBox.setAttribute("type","text");
	rangBox.style.backgroundColor = "transparent";
	rangBox.setAttribute('value',''+ result +'m');
	rangBox.style.display = 'none';
	rangBox.style.border = 'none';
    li3.appendChild(rangBox);
    uList.appendChild(li3);
    
	contianer.appendChild(uList);
	
}

addBtn.onclick = function() {
	addDraw = true;
	addLine = false;
}

var addLine = false;
btnEnable.onclick = function() {
	btnEnable.style.display = "none";
	btnReset.style.display = "block";
	btnStop.style.display = "block";
  	rangBox.style.display = "block";
  	addLine = true;
  };
  
btnStop.onclick = function() {
	btnEnable.style.display = "block";
	btnReset.style.display = "none";
	btnStop.style.display = "none";
	rangBox.style.display = "none";
	addLine = false;
};

 btnReset.onclick = function() {
	LineInfo.length = 0;
	reLine.length = 0;
	result = 0.00;
	rangBox.setAttribute('value',''+ result +'m');
};

function DrawMapInfo(scaleX, scaleY, offsetX, offsetY) {
	sX = scaleX;
	sY = scaleY;
	osX = offsetX;
	osY = offsetY;

	for (i = 0; i < MAPINFO.length; i++) {
        DrawBlock(MAPINFO[i].x * scaleX + offsetX, MAPINFO[i].y * scaleY + offsetY, MAPINFO[i].width * scaleX, MAPINFO[i].height * scaleY, MAPINFO[i].imageurl);
    }
}

function DrawBlock(x, y, width, height, imageurl) {
    //context2D.fillRect(x, y, width, height);
    //context2D.strokeRect(x, y, width, height);
    var image = new Image();
    image.src = imageurl;
    context2D.drawImage(image, x, y, width, height);
}

function DrawIbeacon(scaleX, scaleY, offsetX, offsetY) {
	for (j = 0; j < ibeacons.length; j++) {
    	drawMarkers(ibeacons[j].x * scaleX + offsetX, ibeacons[j].y * scaleY + offsetY,ibeacons[j].alias);
    }
}

function drawMarkers(x, y, alias) {
	context2D.font = 8 * zoomScale + "pt Microsoft 宋体"; 
	context2D.textAlign = "left";
    context2D.fillStyle = "black"; 
    context2D.fillText(""+alias, x-15, y-20); 
    context2D.fillStyle = 'rgba(255,255,255,0)';
	context2D.beginPath();
	rad = 16;
	context2D.arc(x, y, rad, 0, 2*Math.PI, false);
	context2D.closePath();
	context2D.drawImage(marImg, x-20, y-20, 32, 32);
	//context2D.strokeStyle="green";
	//context2D.stroke();
	context2D.fill();
}

function addMarker(pLx, pLy) {
    x = (pLx-osX)/sX;
    y = (pLy-osY)/sY;
    rad = 16;
    color = 'rgba(255,255,255,0)';
    isSelected = false;
    
	var marker = {
		x: x,
		y: y,
		rad: rad,
		color: color
    };

	ibeacons.push(marker);

	DrawIbeacon(sX, sY, osX, osY);
   
}

function addMarkerLine(pLinex, pLiney) {
	x = (pLinex-osX)/sX;
    y = (pLiney-osY)/sY;
    rad = 16;
    color = 'rgba(255,255,255,0)';
    isSelected = false;
    
	 var Line = {
		x: x,
		y: y,
		rad: rad,
		color: color,
		isSelected: isSelected
    };

	    LineInfo.push(Line);

	    DrawLineInfo(sX, sY, osX, osY);
}

function DrawLineInfo(scaleX, scaleY, offsetX, offsetY) {
	sX = scaleX;
	sY = scaleY;
	osX = offsetX;
	osY = offsetY;
	for (t = 0; t < LineInfo.length; t++) {
    	drawRanging(LineInfo[t].x * scaleX + offsetX, LineInfo[t].y * scaleY + offsetY, LineInfo[t].rad, LineInfo[t].color);
    }
	ligature(sX, sY, osX, osY);
}

function ligature(scaleX, scaleY, offsetX, offsetY) {
	for (p = 0; p < LineInfo.length; p++) {
    	drawLit(LineInfo[p].x * scaleX + offsetX, LineInfo[p].y * scaleY + offsetY,p);
    }
}


function drawRanging(x, y, rad,color) {	 
	context2D.fillStyle = color;
	context2D.beginPath();
	rad = rad;
	context2D.arc(x, y, rad, 0, 2*Math.PI, false);
	context2D.closePath();
	context2D.drawImage(LineImg, x-20, y-20, 32, 32);
	context2D.fill();
}


function drawLit(Lx, Ly, p) {
	context2D.beginPath();
	context2D.strokeStyle = "black";
	context2D.lineWidth = "2";
	 if(p == 0) {
		 x1 = Lx;
		 y1 = Ly;
		 x2 = x1;
		 y2 = y1;
		 context2D.moveTo(x1, y1);   
		 context2D.lineTo(x2, y2);
		 result = 0;
		 rangBox.setAttribute('value',''+result+'m');
	 }else {
		 x1 = x2;   
		 y1 = y2;
		 x2 = Lx;
		 y2 = Ly;
		 context2D.moveTo(x1, y1);
		 context2D.lineTo(x2, y2);
		 
		 var dX2 = LineInfo[p].x;
		 var dY2 = LineInfo[p].y;
		 
		 var dX1 = LineInfo[p-1].x;
		 var dY1 = LineInfo[p-1].y;
		 
		 var calX = dX2 - dX1;  
		 var calY = dY2 - dY1;

		 var count = calX * calX + calY * calY;
		 
		 result = Math.sqrt(count) + result;
		 
		 result = Math.round(result*100)/100;

		 rangBox.setAttribute('value',''+result+'m');
	 }
	 
	 context2D.stroke();
	 context2D.closePath();
}


function scaling() {
	$('#indoormap').bind('mousewheel', function(event, delta) {
       	var dir = delta > 0 ? 'Up' : 'Down';
        var counter = 0;
        
        $(".in").html('');
        $("#main").css('display','none');
           
       	if (dir == 'Up') {
       		if (gesturableCanvas.scale.x < 2.5) {
                   gesturableCanvas.scale.x += 0.05;
                   gesturableCanvas.scale.y += 0.05;
                   gesturableCanvas.position.x += -(clientWidth / 60);
                   gesturableCanvas.position.y += -(clientHeight / 60);
                   counter++;
                  
               } else {
                   gesturableCanvas.scale.x = 2.5;
                   gesturableCanvas.scale.y = 2.5;
                   gesturableCanvas.position.x = -(clientWidth / 2);
                   gesturableCanvas.position.y = -(clientHeight / 2);
               }
            
       	} else {
       		if (gesturableCanvas.scale.x > 1) {
                   gesturableCanvas.scale.x -= 0.05;
                   gesturableCanvas.scale.y -= 0.05;
                   gesturableCanvas.position.x -= -(clientWidth / 60);
                   gesturableCanvas.position.y -= -(clientHeight / 60);
                   counter++;
                   
               } else {
                   gesturableCanvas.scale.x = 1;
                   gesturableCanvas.scale.y = 1;
                   gesturableCanvas.position.x = 0;
                   gesturableCanvas.position.y = 0;
               }
       	}
       }); 
}

/*function stopDragging() {
  isDragging = false;
}

function dragMarker(e) {
    if (isDragging == true) {
      $(".in").html('');
      $("#main").css('display','none');
      if (previousSelectedCircle != null) {
        var x = (e.pageX-osX)/sX;
        var y = (e.pageY-osY)/sY;

        previousSelectedCircle.x = x;
        previousSelectedCircle.y = y;

        DrawMarkerInfo(sX, sY, osX, osY);
      }
    }
  }*/

