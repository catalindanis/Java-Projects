<!DOCTYPE html>
<html lang="en">
<head>
<title>Code</title>
<link rel="stylesheet" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
<script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
<script>hljs.initHighlightingOnLoad();</script>
<meta charset="UTF-8">
</head>
<body>
<span id="load_date">{$code.getDate()}</span><br>
<#if code.viewsRestriction == true>
<span id="views_restriction">{$code.getViews()} more views allowed</span><br>
</#if>
<#if code.timeRestriction == true>
<span id="time_restriction">The code will be available for {$code.getTime()} seconds</span><br>
</#if>
<pre id="code_snippet"><code>{$code.getCode()}</code></pre>
</body>
</html>