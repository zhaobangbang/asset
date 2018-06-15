document.write("<script src='http://maps.google.cn/maps/api/js?v=3&region=cn&language=cn_ZH&key=AIzaSyCXLBMoqpXV7ZAN2ZRlc72TE6fs1r9WPro&libraries=geometry&callback=LansMapIntialize'></script>");   
document.write("<script src='http://www.lansitec.com/asset/js/gps.js'></script>");   

function include(path){ 
    var a=document.createElement("script");
    a.type = "text/javascript"; 
    a.src=path; 
    var head=document.getElementsByTagName("head")[0];
    head.appendChild(a);
 }

Date.prototype.format =function(format)
       {
       var o = {
       "M+" : this.getMonth()+1, //month
       "d+" : this.getDate(), //day
       "h+" : this.getHours(), //hour
       "m+" : this.getMinutes(), //minute
       "s+" : this.getSeconds(), //second
       "q+" : Math.floor((this.getMonth()+3)/3), //quarter
       "S" : this.getMilliseconds() //millisecond
       }
       if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
       (this.getFullYear()+"").substr(4- RegExp.$1.length));
       for(var k in o)if(new RegExp("("+ k +")").test(format))
       format = format.replace(RegExp.$1,
       RegExp.$1.length==1? o[k] :
       ("00"+ o[k]).substr((""+ o[k]).length));
       return format;
       }      

function LansMapIntialize()
{
	google.maps.Map.prototype.IndoorPointToLatLng = function(x, y)
	{
		var newLat = null;
		var newLng = null;
		var sw_lati = null;
	    var sw_long = null;
	    
		if(x > 180)//室内地图信息
		{
			var	pmPoint = new google.maps.Point(x / 132791, y / 132791);
		    var latlng = this.getProjection().fromPointToLatLng(pmPoint);
		    
			newLat = latlng.lat();
			newLng = latlng.lng();
			
			if(null != this.sw_lati_ && undefined != this.sw_lati_)
		    {
				sw_lati = this.sw_lati_;
		        sw_long = this.sw_long_;

			    sw_lati = sw_lati + (newLat - sw_lati) * this.zoom_;
		        sw_long = sw_long + (newLng - sw_long) * this.zoom_;
		    }
			else {
				sw_lati = newLat;
				sw_long = newLng;
			}
		}
		else//室外地图
		{
		    var arr2 = GPS.gcj_encrypt(y, x);
		    sw_long = arr2['lon'];
		    sw_lati = arr2['lat'];
		}

		return new google.maps.LatLng(sw_lati, sw_long);
	}
	google.maps.Map.prototype.AddMarker = function(latLng){
    	var newMarker = new google.maps.Marker({
            map : this,
            position : latLng
        });

        return newMarker;
    };
    google.maps.Map.prototype.ListenClick = function(marker, content){
        google.maps.event.addListener(marker, "click", function(e) {
			var infowindow = new google.maps.InfoWindow({
		          content: content
		        });
			infowindow.open(this, marker);
        });
    };
    google.maps.Map.prototype.PolyLine = function(pathCoordinates){
        var flightPath = new google.maps.Polyline({  
            path: pathCoordinates,  
            strokeColor: "#FF0000",  
            strokeOpacity: 1.0,  
            strokeWeight: 1  
        });  
        flightPath.setMap(this); 
    };
    function LansLatLng()
    {
    }
    LansLatLng.prototype = google.maps.LatLng.prototype;
}

function checkMapBounds(map, mapRange)
{
    if(map)
    {
        if(mapRange.contains(map.getCenter()))
        {
            return;
        }
   
       var center = map.getCenter();
       var centerX = center.lng();
       var centerY = center.lat(); 

       var maxX = mapRange.getNorthEast().lng();
       var maxY = mapRange.getNorthEast().lat();
       var minX = mapRange.getSouthWest().lng();
       var minY = mapRange.getSouthWest().lat();

      if(centerX < minX) { centerX = minX; }
       if(centerX > maxX) { centerX = maxX; }
       if(centerY < minY) { centerY = minY; }
       if(centerY > maxY) { centerY = maxY; }
      map.panTo(new google.maps.LatLng(centerY, centerX));
   }
}

var Lansdivmap__;
var Lansparameter_;
var Lansmap_;
var LansMapData_ = null;
var LansinitPosition_ = null;
function createLansMap(div, options)
{	
	Lansdivmap_ = div;
	Lansparameter_ = options;

	if (navigator.geolocation) {
		  navigator.geolocation.getCurrentPosition(function(position) {
			  LansinitPosition_ = {
		      lat: position.coords.latitude,
		      lng: position.coords.longitude
		    };
		    //map.setCenter(pos);
		  }, function() {
		    //handleLocationError(true, infoWindow, map.getCenter());
		  });
		}
	if(null == LansinitPosition_)
	{
		LansinitPosition_ = new google.maps.LatLng(32, 118, true);
	}
	
	var defaultZoom = 20;
	if(options.scale != undefined && options.scale > 0 && options.scale < 5)
		defaultZoom = 18 + options.scale - 1;
	
	var  MapOptions = {
	   center : LansinitPosition_,
	   zoom : defaultZoom,
	   mapTypeControl : false,
	   mapTypeControlOptions : {
	       style : google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
	       position : google.maps.ControlPosition.TOP_RIGHT
	   },
	   scaleControl : true,
	   scaleControlOptions : {
	       style : google.maps.ScaleControlStyle.DEFAULT,
	       position : google.maps.ControlPosition.BOTTOM_LEFT
	   },
	   streetViewControl : false,
	   streetViewControlOptions : {
	       position : google.maps.ControlPosition.LEFT_CENTER
	   },
	   panControl : true,
	   panControlOptions : {
	       position : google.maps.ControlPosition.LEFT
	   },
	   NavigationControl : false,
	   NavigationControlOptions : {
	       style : google.maps.NavigationControlStyle.ZOOM_PAN,
	       position : google.maps.ControlPosition.LEFT
	   },
	   zoomControl : true,
	   zoomControlOptions : {
	       style : google.maps.ZoomControlStyle.SMALL,
	       position : google.maps.ControlPosition.LEFT_CENTER
	   },
	   fullscreenControl: false,	    	   
	   overviewMapControl : true,
	   mapTypeIds : google.maps.MapTypeId.ROADMAP
	};
    
	Lansmap_ = new google.maps.Map(document.getElementById(Lansdivmap_), MapOptions);
    this.map_ = Lansmap_;
    
    request(options.id);
    
    return Lansmap_;
}

function request(id){	
	var b=document.createElement("script");
	b.type = "text/javascript"; 
	//b.src = "https://dev.lansitec.com/asset/jsonp?id=" + id + "&type=json&callback=callbackfunction"; 
	b.src = "http://www.lansitec.com/asset/jsonp?id=" + id + "&type=json&callback=callbackfunction"; 
	var headb=document.getElementsByTagName("head")[0];
	headb.appendChild(b);
}


var callbackfunction = function(jsondata) {
    if(jsondata == "")
	{
		alert("Nothing received from map server, please check the map ID!")
		return Lansmap_;
	}
    var myoverlay = new google.maps.OverlayView();
	  LansMapData_ = JSON.parse(jsondata);
      var mapType = LansMapData_.features[0].properties.mapType;
      var centerLong = LansMapData_.features[0].properties.centerLong;
      var centerLati = LansMapData_.features[0].properties.centerLati;
    
      if(centerLong && centerLati)
    	  Lansmap_.setCenter(new google.maps.LatLng(centerLati, centerLong, true));

      google.maps.event.addListener(Lansmap_, 'zoom_changed',function() {
	    	   if(Lansparameter_.indoor == undefined || Lansparameter_.indoor == true)
	    	   {
	    		   if (Lansmap_.getZoom() < 18) map.setZoom(18);
	    	   }
	           else
	           {
	        	   if (Lansmap_.getZoom() < 2) map.setZoom(2);
	           }	            	 
        });
      
      if(mapType == "json" || mapType == undefined)
      {
    	  Lansmap_.zoom_ = 1;
    	  Lansmap_.data.addGeoJson(LansMapData_);	
    	  Lansmap_.data.setStyle(function(feature) {
     	  return /** @type {google.maps.Data.StyleOptions} */({
     		clickable: false,
     	    fillColor: feature.getProperty('fill'),
     	    fillOpacity: feature.getProperty('fill-opacity'),
     	    strokeColor: feature.getProperty('stroke'),
     	    strokeWeight: feature.getProperty('stroke-width'),
     	    strokeOpacity: feature.getProperty('stroke-opacity')
     	  });
     	}); 
      }
      else if(mapType == "svg")
      {
   	   var sw_lati = LansMapData_.features[0].properties.sw_lati;
   	   var sw_long = LansMapData_.features[0].properties.sw_long;
   	   var ne_lati = LansMapData_.features[0].properties.ne_lati;
   	   var ne_long = LansMapData_.features[0].properties.ne_long;
   	    
   	   var zoom = Lansparameter_.zoom;
   	   if(zoom == null || zoom == undefined || zoom <= 0 || zoom >= 10)
   		   zoom = 1;
   	 
   	Lansmap_.zoom_ = zoom;
   	Lansmap_.sw_lati_ = sw_lati;
   	Lansmap_.sw_long_ = sw_long;
   	   
   	   ne_lati = ne_lati + (ne_lati - sw_lati) * (zoom - 1);
   	   ne_long = ne_long + (ne_long - sw_long) * (zoom - 1);
   	   
   	   myoverlay.LansBounds_ = new google.maps.LatLngBounds(
	    		      new google.maps.LatLng(sw_lati, sw_long),
	    		      new google.maps.LatLng(ne_lati, ne_long)
	    		  );
   	   myoverlay.img_ = LansMapData_.features[0].properties.srcSvg;
   	   
	       google.maps.event.addListener(Lansmap_, 'dragend', function(){
		    	   if(Lansparameter_.indoor == undefined || Lansparameter_.indoor == true)
		    		{
	    	     checkMapBounds(Lansmap_, myoverlay.LansBounds_);
		    		}
	    	 });
	       
   	   myoverlay.setMap(Lansmap_);
      }
          
    myoverlay.draw = function () {
      // this will be executed whenever you change zoom level
    	  var overlayProjection = this.getProjection();

    	  // Retrieve the south-west and north-east coordinates of this overlay
    	  // in LatLngs and convert them to pixel coordinates.
    	  // We'll use these coordinates to resize the div.
    	  var sw = overlayProjection.fromLatLngToDivPixel(this.LansBounds_.getSouthWest());
    	  var ne = overlayProjection.fromLatLngToDivPixel(this.LansBounds_.getNorthEast());

    	  // Resize the image's div to fit the indicated dimensions.
    	  var div = this.div_;
    	  div.style.left = sw.x + 'px';
    	  div.style.top = ne.y + 'px';
    	  div.style.width = (ne.x - sw.x) + 'px';
    	  div.style.height = (sw.y - ne.y) + 'px';
    };

    myoverlay.onRemove = function () {
      // this will be executed whenever you call setMap(null) on this overlay
    	 this.div_.parentNode.removeChild(this.div_);
    	 this.div_ = null;
    };

    myoverlay.onAdd = function () {
      // this will be executed when you call setMap(your map) on this object
    	 var panes = this.getPanes();

    	    // create a div and append it to the markerLayer pane        
    	    var mydiv = document.createElement('div');
    	    mydiv.style.borderStyle = "none";
    	    mydiv.style.borderWidth = "0px";
    	    mydiv.style.position = "absolute";
    	   // mydiv.style.paddingTop = "0";
    	   // mydiv.style.fontSize = "0";
    	    
    	    var img = document.createElement("img");
    	    img.src = this.img_;
    	    img.style.width = "100%";
    	    img.style.height = "100%";
    	    //img.style.top = "0";
    	    //img.style.verticalAlign = "bottom";
    	   // img.style.position = "absolute";

    	    mydiv.appendChild(img);
    	    
    	    this.div_ = mydiv;
    	    //panes.markerLayer.appendChild(mydiv);
    	    panes.overlayLayer.appendChild(mydiv);
    	    // create an SVG element and append it to your div
    	    //var svgns = "../map/zwwl.svg";
    	    //var mysvg = document.createElementNS(svgns, 'svg');
    	    //mysvg.setAttribute('width', "200");
    	   // mysvg.setAttribute('height', "300");

    	    //mydiv.appendChild(mysvg);
    	    //document.getElementById(div).appendChild(mysvg);
    };
    return Lansmap_;
};

