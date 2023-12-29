package ${packageName}.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ${packageName}.${className};
import ${packageName}.${className}QueryParam;

import java.util.List;


/**
* ${className} Dao
 ${comment}
*
* @author ${authorName}
* @since ${.now?string("yyyy-MM-dd HH:mm:ss")}
*/
@Mapper
public interface I${className}Dao {

    /**
     * 插入 ${comment}
     *
     * @param ${className?uncap_first} ${comment}
     * @return
     */
    int insert(${className} ${className?uncap_first});

    /**
     * 批量插入 ${comment}
     *
     * @param ${className?uncap_first}List ${comment} 列表
     * @return
     */
    int insertBatch(@Param("coll")List<${className}> ${className?uncap_first}List);

    /**
     * 更新
     *
     * @param ${className?uncap_first} ${comment}
     * @return
     */
    int update(${className} ${className?uncap_first});

    /**
     * 删除
     *
     * @param ${keyField.name} 标识符
     * @return
     */
    int delete(@Param("${keyField.name}") String ${keyField.name});

    /**
     * 获取
     *
     * @param ${keyField.name} 标识符
     * @return
     */
    ${className} get(@Param("${keyField.name}") String ${keyField.name});

    /**
     * 获取列表
     *
     * @param param    查询条件
     * @param pageNum  页号
     * @param pageSize 页大小
     * @return
     */
    List<${className}> getList(${className}QueryParam param, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * 获取条数
     *
     * @param param 查询条件
     * @return
     */
    int getCount(${className}QueryParam param);
}

<#-- 修改模板生成路径 -->
${callback.setTargetFileDir(targetFileDir+"\\dao")}
${callback.setTargetFileName("I"+targetFileName)}