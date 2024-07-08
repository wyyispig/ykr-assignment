Python 3.9

含有大量摸索使用的代码，后续删除

前后端的音频上传与下载暂未实现，采用文件读写

`requirements.txt`中的库存在问题

ORM模型映射三步骤
1. flask db init:  该步骤只需要执行一次.
2. flask db migrate: 识别ORM模型的改变. 生成迁移脚本.
3. flask db upgrade: 运行迁移脚本,同步到数据库中.