<html lang="en">
<header>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="multipart/form-data; charset=utf-8" />
    <meta name="renderer" content="webkit">
    <title>宝塔Linux面板</title>
</header>
<body>
    <h2>Hello World!</h2>

    <h3>SpringMvc 上传文件</h3>
    <form action="${pageContext.request.contextPath}/manage/product/upload.do" name="form1" method="post" enctype="multipart/form-data">

        <input type="file" name="upload_file">
        <input type="submit" value="上传文件 ">
    </form>

    <h3>富文本图片上传</h3>
    <form action="${pageContext.request.contextPath}/manage/product/richtext_img_upload.do" name="form2" method="post" enctype="multipart/form-data">
        <input type="file" name="upload_file">
        <input type="submit" value="上传">
    </form>
</body>
</html>
