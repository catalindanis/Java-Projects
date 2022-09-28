<!DOCTYPE html>
<html lang="en">
<head>
    <title>Latest</title>
    <link rel="stylesheet"
           href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
    <style>
        #code_snippet{
        font-size:15px;
            border:solid 1px;
            width:600px;
            background-color:lightgray;
        }
        span{
            color:green;
        }
    </style>
    <#list codes as code>
        <div>
            <span>${code.getCreationDate()}</span>
            <br>
            <pre id="code_snippet"><code>${code.toString()}</code></pre>
        </div>
    </#list>
</body>
</html>