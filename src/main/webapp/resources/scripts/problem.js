/**
 * 
 */

$(function() {
	$(".newtest button").click(function() {
		var count = $("#testcases li.testcase").length;
		var input = $("input[name='input']").val();
		var output = $("input[name='output']").val();
		var maxTime = $("input[name='maxTime']").val();
		var isPublic = $("input[name='public']").is(":checked");
		
		var csrfParameterName = $("body").data("csrf-parameter-name");
		var csrfToken = $("body").data("csrf-token");
		
		$.ajax({
			url : "test",
			method : "POST",
			accept : "application/json",
			data : "input=" + encodeURI(input) + "&expected=" + encodeURI(output) + "&maxTime=" + encodeURI(maxTime) + "&public=" + isPublic  + "&" + csrfParameterName + "=" + csrfToken,
		    success : function(response) {
				var testId = response.id;
				$("#testcases").append("<li class='testcase' data-test-id='" + testId + "'><ul><li><label for='input[" + count + "]'>Input:</label><input name='input[" + count + "]' value='" + input + "'/></li>" +
						   "<li><label for='output[" + count + "]'>Expected:</label><input name='output[" + count + "]' value='" + output + "'/></li>" +
						   "<li><label for='maxTime[" + count + "]'>Time Limit:</label><input name='maxTime[" + count + "]' value='" + maxTime + "'/></li>" +
						   	"<li><label for='public[" + count + "]'>Is Public:</label><input type='checkbox' name='public[" + count + "]' " + (isPublic ? "checked" : "") + "/></li></ul></li>");
	
				$("input[name='input']").val("");
				$("input[name='output']").val("");
				$("input[name='maxTime']").val("");
				$("input[name='public']").removeAttr("checked");
		    }
		});
	});
	$(".testcase input").change(function() {
		var testId = $(this).parents(".testcase").data("test-id");
		var problemId = $("body").data("problem-id");
		var contextPath = $("body").data("context-path");
		
		var vals = $(this).parents(".testcase").find("input").map(function(i) { return $(this).val(); });

		var isPublic = $(this).parents(".testcase").find("input[type='checkbox']").is(":checked");
		
		var csrfParameterName = $("body").data("csrf-parameter-name");
		var csrfToken = $("body").data("csrf-token");
		
		var test = { "input" : encodeURI(vals[0]),
				"expected" : encodeURI(vals[1]),
				"maxTime" : encodeURI(vals[2]),
				"public" : isPublic
		};
		
		$.ajax({
			url : "test/" + testId,
			method : "POST",
			accept : "application/json",
			/*beforeSend : function(xhr) {
				xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
			},*/
			data : /*JSON.stringify(test)*/ "input=" + encodeURI(vals[0]) + "&expected=" + encodeURI(vals[1]) + "&maxTime=" + encodeURI(vals[2]) + "&public=" + ( "on" == vals[3] ) + "&" + csrfParameterName + "=" + csrfToken,
		});
	});
	
	$("button.removeTest").click(function() {
		var testId = $(this).parent("li").data("test-id");
		var problemId = $("body").data("problem-id");
		var contextPath = $("body").data("context-path");

		var csrfParameterName = $("body").data("csrf-parameter-name");
		var csrfToken = $("body").data("csrf-token");
		
		$.ajax({
			url : "test/" + testId,
			method : "DELETE",
			accept : "application/json",
			context : $(this).parent("li"),
			data : csrfParameterName + "=" + csrfToken,
			success : function () {
				$(this).remove();
			}
		});
	});
});
