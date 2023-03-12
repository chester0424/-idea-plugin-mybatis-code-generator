[English](./README.en.md)

# 介绍
使用java项目中，使用Mybatis做ORM很常见，大多是逆向工程方式，根据数据库结构生成代码，这种方式也便于使用，开发也挺高效。 \
而 Mybatis Forward CodeGenerator 插件使用 **“正向"工程** 思路，根据编写的类，通过一些简便的操作生成代码。 \
某种程度上说，它可能更加契合面向对象、领域驱动设计等构建思路，不妨一试。

# 使用说明

## 安装

* 打开集成开发环境 Idea
* 依次打开 文件(File)-> 设置(Setting) -> 插件(Plugins) -> 市场(MarketPlace)
* 搜索框中输入 Mybatis Forward CodeGenerator，搜索该插件
* 点击“安装” 即可

## 插件配置

### 基本设置

打开：文件->设置->其他设置-> Mybatis Forward CodeGenerator \
设置 AuthorName、ConfigFilePath 信息

### 模板配置

”Template“栏目下配置模板

* 点击“Add"按钮，在弹出的提示框中输入模板名称 \
  注意：模板名称通常需要带后缀，比如“Dao.java”,默认生成的代码文件会保存在“类文件”相同目录下，文件名为：[类名]Dao.java
* 模板仅支持使用FreeMaker语法
* 模板可以从上下文中获取到的信息(Map结构)有：
    * className 类名称
    * packageName 包名
    * fileName 类文件文件名
    * fileDir 类文件所在目录
    * filePath 文件全路径
    * tableName 表名
    * fields 字段列表（对象列表结构）
        * name 字段名称
        * javaType 字段Java类型全称
        * javaTypeShort 字段Java类型
        * columnName 列名
        * jdbcType JDBC类型
        * primaryKey 是否是主键
        * typeHandler 类型处理器
        * extend 扩展信息（key-value结构）
    * keyField 主键列（如果未设置则不能获取到该值）
    * nonKeyFields 非主键列列表
    * fieldTypeImports 字段类型依赖导入（字符串列表结构）
    * projectBasePath 项目路径
    * authorName 作者
* 上下支持回调操作
    * callback
        * setTargetFileDir() 修改默认代码文件生成目录
        * setTargetFileName() 修改默认代码文件生成文件名

### 类型映射配置

* 插件已经内置了基本的Java类型与JDBC类型的映射
* 可以根据实际需求，在“Type Mapping”栏目下，对映射关系调整、添加、删除等操作

### 扩展列配置

* 插件默认提供 自定义表名、自定义列名、是否忽略类字段、主键配置、JdbcType类型配置、TypeHandler配置
* 如果需要更多属性，在“Extend Config Column”中维护
* 点击“Add”，在弹框中输入扩展列的名称
* 在Type（数据类型）列选择扩展配置列的数据属性，目前仅支持：布尔类型（BOOLEAN)、下拉选择(SELECT)、文本输入(INPUT)
* Options列仅在Type列是“SELECT”时的输入值有效，多个选项使用英文逗号(“,”) 隔开

注：配置好配置信息以后，记得点击“确定”或“应用”，以保存配置。

## 代码生成

* 在编辑器中编写好Java类，鼠标右键选择“GenerateMybatis”选项
* 在弹出的代码生成窗口中，根据实际情况配置相应信息：表名、主键等
* 保存配置
    * 点击“Save Config”按钮保存配置
* 生成代码
    * 首先在“Optional Templates”中勾选需要生成代码的模板
    * 点击“Generate Code"按钮生成代码

## 其他

* &#x1F308;[手册](https://gitee.com/chesterone/idea-plugin-mybatis-code-generator/blob/master/mybatis-code-generator/src/main/resources/operation-manual/OperationManual.docx) --有助于直观感受使用插件的意义
* 模板编辑参考
    * dao         [resources/template/Dao.java.txt]
    * mapper      [resources/template/Mapper.xml.txt]
    * queryParam  [resources/template/QueryParam.java.text]
    * sql         [resources/template/Sql.sql.txt]
* 如果查询需要分页，建议使用插件方式
* [https://github.com/mybatis/](https://github.com/mybatis/) ; [FreeMaker在线手册](http://freemarker.foofun.cn/toc.html)
* MainPage [Gitee](https://gitee.com/chesterone/idea-plugin-mybatis-code-generator/) [GitHub](https://github.com/chester0424/idea-plugin-mybatis-code-generator)
* 其他帮助 QQ:373934650 Email:373934650@qq.com 
<img src="mybatis-code-generator/src/main/resources/contact/QQ-Qun.png"  width="100" height="100" title="QQ">