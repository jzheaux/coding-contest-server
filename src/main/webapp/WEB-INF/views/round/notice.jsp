<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/codemirror.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/styles/contest.css" />
<script src="${pageContext.request.contextPath}/resources/scripts/jquery-1.10.2.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/codemirror.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/clike.js"></script>
<script src="${pageContext.request.contextPath}/resources/scripts/matchbrackets.js"></script>
<script type="text/javascript">
	window.onload = function() {
		var javaexample = document.getElementById("java-example"); 
		CodeMirror.fromTextArea(
			javaexample,
			{
		        mode: "text/x-java",
		        scrollBarStyle: "null",
		        readOnly : true
			}
		);
		
		var csharpexample = document.getElementById("csharp-example"); 
		CodeMirror.fromTextArea(
				csharpexample,
			{
		        mode: "text/x-csharp",
		        scrollBarStyle: "null",
		        readOnly : true
			}
		);
	};
</script>
<title>Round Introduction</title>
</head>
<body>
	<div id="content">
		<h1>Welcome to ${model.name}</h1>
		<p class="note">
		   This is an instruction page. Read through it, and once you click "Got it" at the bottom of the page, you will be taken to your actual problem.
		</p>
		<p>
			You will be presented a set of one or more problems and ${model.maxTime} minutes to solve them all.
			You may address them in any order and may submit multiple revisions. You will be judged on your
			final submission regardless of the correctness of previous submissions.
		</p>
		<p>
			You will read from <span class="help">standard input</span> and write
			the answer to <span class="help">standard output</span>.'
		</p>
		<p>Your submission should always be called Solution.</p>
		<section>
			<section class="problem">
				<h1>Example Problem</h1>
				<p class="description">Given a set of integers as a comma-delimited stream, read them
				in, calculate the average and output its integer part.</p>
				<table class="samples">
					<thead>
						<tr>
							<th>Input</th>
							<th>Expected Output</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1,2,3</td>
							<td>2</td>
						</tr>
						<tr>
							<td>0</td>
							<td>0</td>
						</tr>
						<tr>
							<td>2,4,1</td>
							<td>2</td>
						</tr>
					</tbody>
				</table>
			</section>
			<section class="solution">
				<h1>Example Solution</h1>
				<textarea id="java-example" disabled="disabled">
import java.util.Scanner;

public class Solution {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in); // read from standard input
		String input = console.nextLine();
		String[] numbers = input.split(",");
		Integer sum = 0;
		for ( String n : numbers ) {
			sum += Integer.parseInt(n);
		}
		System.out.println(sum / numbers.length); // write to standard output
	}
}
				</textarea>
				<hr/>
				<textarea id="csharp-example" disabled="disabled">
using System;
 
public class Solution {
	public static void Main(string[] args) {
		string input = Console.ReadLine(); // read from standard input
		string[] numbers = input.Split(',');
		int sum = 0;
		foreach (string t in numbers) {
			sum += int.Parse(t);
		}
		Console.WriteLine(sum / numbers.Length); // write to standard output
	}
}
				</textarea>
			</section>
		</section>
		<section id="submit">
			<a href="../${model.id}" class="button">Got it</a>
		</section>
	</div>
</body>
</html>