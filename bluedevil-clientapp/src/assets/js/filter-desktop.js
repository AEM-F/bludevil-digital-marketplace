$( document ).ready(function() {
  if($(window).width()>= 1200){
    if($('.filter-inner-container').length && $('.platform-collapse').length)
    $('.filter-inner-container').collapse();
    $('.platform-collapse').collapse();
  }
});
