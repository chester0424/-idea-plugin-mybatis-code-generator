package ${config.packageName}.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ${config.packageName}.${config.className};

@Mapper
public interface I${config.className}Dao {

    ${config.className} get(String key);

    Page<${config.className}> get${config.className}PageList(String key,@Param("pageNumKey") int pageNum,  @Param("pageSizeKey") int pageSize);

    int Update(${config.className} product);

    int delete(String key);
}