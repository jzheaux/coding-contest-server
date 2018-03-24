/**
 * countdown.js - Will change a counter to go down every second, starting from a value contained in data-seconds
 */

$(function() {
	$(".countdown").html("<span class='hours'></span>:<span class='minutes'></span>:<span class='seconds'></span>");
	
	var secondsLeft = $(".countdown").data("seconds");
	
	if ( secondsLeft ) {
		var hours = Math.floor(secondsLeft / 3600);
		secondsLeft = secondsLeft - hours * 3600;
		var minutes = Math.floor(secondsLeft / 60);
		secondsLeft = secondsLeft - minutes * 60;
		var seconds = secondsLeft;
		
		function countdownOneSecond() {
			seconds--;
			
			if ( seconds < 0 ) {
				minutes--;
				seconds = 59;
				
				if ( minutes < 0 ) {
					hours--;
					minutes = 59;
				}
			}
			
			if ( hours < 0 ) {
				location.reload();
			} else {
				$(".countdown .hours").html(hours < 10 ? ("0" + hours) : hours);
				$(".countdown .minutes").html(minutes < 10 ? ("0" + minutes) : minutes);
				$(".countdown .seconds").html(seconds < 10 ? ("0" + seconds) : seconds);
			}
		}
		
		setInterval(countdownOneSecond, 1000);
	}
});