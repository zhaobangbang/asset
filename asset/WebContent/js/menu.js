$(function(){
  var menuwidth  = 210;  
  var menuspeed  = 400;  
  
  var $bdy       = $('body');
  var $container = $('.page');
  var $burger    = $('#hamburgermenu');
  var negwidth   = "-"+menuwidth+"px";
  var poswidth   = menuwidth+"px";
  
  $('.menubtn').on('click',function(e){
    if($bdy.hasClass('openmenu')) {
      jsAnimateMenu('close');
    } else {
      jsAnimateMenu('open');
    }
  });
  
  $('.overlay').on('click', function(e){
	$("#hamburgermenu ul li ul").hide();
    if($bdy.hasClass('openmenu')) {
      jsAnimateMenu('close');
    }
  });
  
  function jsAnimateMenu(tog) {
    if(tog == 'open') {
      $bdy.addClass('openmenu');
      $('.glossymenu').hide();
      $('.page').css('left','5px');
      $container.animate({marginRight: negwidth, marginLeft: poswidth}, menuspeed);
      $burger.animate({width: poswidth}, menuspeed);
      $('.overlay').animate({left: poswidth}, menuspeed);
    }
    
    if(tog == 'close') {
      $bdy.removeClass('openmenu');
      $('.glossymenu').show();
      $('.page').css('left','40px');
      $("#hamburgermenu ul li ul").hide();
      $container.animate({marginRight: "0", marginLeft: "0"}, menuspeed);
      $burger.animate({width: "0"}, menuspeed);
      $('.overlay').animate({left: "0"}, menuspeed);
    }
  }
});

function openMenutitle(){
	
}