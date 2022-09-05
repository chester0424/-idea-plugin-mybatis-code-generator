package ${packageName}.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ${packageName}.${className};

@Mapper
public interface I${className}Dao {

    ${className} get(String key);

    Page<${className}> get${className}PageList(String key,@Param("pageNumKey") int pageNum,  @Param("pageSizeKey") int pageSize);

    int Update(${className} ${tool.toLowerCaseFirstOne(className)});

    int delete(String key);
}