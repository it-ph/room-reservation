(function(){

	//$('.nav>.nav-item').first().addClass('active');

	$('.nav').on("click",'.nav-item',function(){
		alert('wa');
		$(this).siblings().removeClass('active');
		$(this).addClass('active');
	});
})();