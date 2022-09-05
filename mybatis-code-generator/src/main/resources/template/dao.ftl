package ${packageName}.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ${packageName}.${className};

@Mapper
public interface I${className}Dao {

${className} get(String key);

Page<${className}> get${className}PageList(String key,@Param("pageNumKey") int pageNum,  @Param("pageSizeKey") int pageSize);

int Update(${className} ${className?uncap_first});

int delete(String key);
}

<#-- 修改模板生成路径 -->
${setTargetFileDir(targetFileDir+"\\dao")}${setTargetFileName("I"+targetFileName)}