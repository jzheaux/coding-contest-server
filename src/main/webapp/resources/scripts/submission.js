/**
 *
 */ 

var editor;

var testSolution = function(id, roundId, problemId, code, language) {
	$("tr .result").html("");
	
	var token = $("body").data("csrf-token");
	var header = $("body").data("csrf-header");
	  
	$.ajax({
		url : $("body").data("context-path") + '/tournament/' + id + '/round/' + roundId + '/problem/' + problemId + '/solution/test',	
		accept : 'application/json',
		context : $("tr .result"),
		data : "language=" + language + "&code=" + encodeURI(code).replace(/\+/g, '%2B').replace(/=/g, '%3D').replace(/&/g, '%26'),
		type : 'POST',
		beforeSend : function( xhr ) {
			xhr.setRequestHeader(header, token);
		}
	}).done(function(data) {
		if ( data.messages && data.messages.length > 0 ) {
			var mes = "";
			for ( var i = 0; i < data.messages.length; i++ ) {
				mes += "<li>" + data.messages[i] + "</li>";
			}
			toastr.error("<ul>" + mes + "</ul>", 'Compiler Errors!');
		} else {
			$( this ).each(function() {
				var id = $(this).data("test-id");
				$(this).removeClass("passed");
				if ( data.map[id].passed ) {
					$(this).addClass("passed");
					$(this).html("Passed");
				} else {
					$(this).html(data.map[id].messages);
				}
			});
			
			if ( data.passed ) {
				toastr.warning('Okay, it appears that your code compiled and all the basic tests passed. When you\'re ready, submit your solution to see if you have a correct submission.', 'Compiled.');
			}
		}
	});
}

var submitSolution = function(url, code, language, method) {
	var token = $("body").data("csrf-token");
	var header = $("body").data("csrf-header");

	$("#submit button:last").addClass("submitting").unbind("click");
	
	$.ajax({
		url : url,	
		accept : 'application/json',
		context : $("tr .result"),
		data : "language=" + language + "&code=" + encodeURI(code).replace(/\+/g, '%2B').replace(/=/g, '%3D').replace(/&/g, '%26'),
		type : method,
		beforeSend : function( xhr ) {
			xhr.setRequestHeader(header, token);
		}
	}).done(function(data) {
		var submission = data;
		$("body").data("submission-id", submission.id);
		if ( submission.passed ) {
			toastr.success('Your submission passed all tests!', 'Congratulations!');
		} else {
			toastr.error('Your submission did not pass all tests.', 'Sorry!');
		}
		
		$("#submit button:last").removeClass("submitting").click(submitClicked);
	});
}

var submitClicked = function() {
	if ( $("body").data("submission-id") ) {
		putSolution($("body").data("submission-id"), editor.doc.getValue(), $("#languageselector button.selected").data("language"));
	} else {
		postSolution($("body").data("tournament-id"), $("body").data("round-id"), $("body").data("problem-id"), editor.doc.getValue(), $("#languageselector button.selected").data("language"));
	}
}

var postSolution = function(id, roundId, problemId, code, language) {
	submitSolution( $("body").data("context-path") + '/tournament/' + id + '/round/' + roundId + '/problem/' + problemId + '/solution', code, language, 'POST');
}

var putSolution = function(id, code, language) {
	submitSolution( $("body").data("context-path") + '/submission/' + id, code, language, 'POST' );
}

$(function() {
	var solution = document.getElementById("solution"); 
	var language = $("#languageselector button.selected").data("language");
	
	editor = CodeMirror.fromTextArea(
			solution,
			{
				mode: (language == "JAVA" ? "text/x-java" : "text/x-csharp")
			}
		);
	
	$("#submit button:first").click(function() {
		testSolution($("body").data("tournament-id"), $("body").data("round-id"), $("body").data("problem-id"), editor.doc.getValue(), $("#languageselector button.selected").data("language"));
	});

	$("#submit button:last").click(submitClicked);
	
	$("#languageselector button").click(function() {
		$("#languageselector button").removeClass("selected");
		$(this).addClass("selected");
		
		editor.setOption("mode", $("#languageselector button.selected").data("language") == "JAVA" ? "text/x-java" : "text/x-csharp");
	});
});
