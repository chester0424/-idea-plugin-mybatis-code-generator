<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.dao.I${className}Dao">
    <insert id="insert">
        INSERT INTO ${tableName}(
<#list fields as field>
            ${field.columnName}<#sep>,
</#list>

        )
        VALUES (
<#list fields as field>
            ${r"#"}{${field.name},jdbcType=${field.jdbcType}}<#sep>,
</#list>

        )
    </insert>

    <insert id="insertBatch" parameterType="java.util.List">
        INSERT INTO ${tableName}(
<#list fields as field>
           ${field.columnName}<#sep>,
</#list>

        )
        VALUES
        <foreach collection="coll" item="item" separator=",">
        (
<#list fields as field>
           ${r"#"}{${field.name},jdbcType=${field.jdbcType}}<#sep>,
</#list>

        )
        </foreach>
    </insert>

    <update id="update">
        UPDATE ${tableName}
        <set>
<#list nonKeyFields as field>
            ${field.columnName} = ${r"#"}{field.name}<#sep>,
</#list>

        </set>
        WHERE ${keyField.columnName} = ${r"#"}{${keyField.name}}
    </update>

    <delete id="delete">
        DELETE ${tableName} WHERE ${keyField.columnName} = ${r"#"}{${keyField.name}}
    </delete>

    <select id="get" resultMap="resultMap">
        SELECT * FROM ${tableName}
        WHERE ${keyField.columnName} = ${r"#"}{${keyField.name}}
    </select>

    <select id="getList" resultMap="resultMap">
        SELECT * FROM ${tableName}
        <include refid="where"></include>
        <include refid="order"></include>
    </select>

    <select id="getCount" resultType="int">
        SELECT COUNT(*) FROM ${tableName}
        <include refid="where"></include>
        <include refid="order"></include>
    </select>

    <resultMap id="resultMap" type="${packageName}.${className}">
<#list fields as field>
        <result property="${field.name}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
</#list>
    </resultMap>

    <sql id="where">
        <trim prefix="WHERE" prefixOverrides="and |or ">
<#list fields as field>
            <!-- ${field.name} -->
<#if ["char","String","Char"]?seq_contains(field.javaTypeShort)>
            <if test="${field.name} != null">
                AND ${field.columnName} like ${r"#"}{${field.name}}
            </if>
            <if test="${field.name}_array != null">
                AND ${field.columnName} IN
                <foreach collection="${field.name}_array" item="value" open="(" separator="," close=")">${r"#"}{value,jdbcType=${field.jdbcType}}
                </foreach>
            </if>
</#if>
<#if ["short","int","long","float","double","Short","Integer","Long","Float","Double","BigDecimal","Date","DateTime"]?seq_contains(field.javaTypeShort)>
             <if test="${field.name} != null">
                AND ${field.name} like ${r"#"}{${field.name}}
             </if>
             <if test="${field.name}_range != null">
                 AND ${field.name} <![CDATA[ >= ]]> ${r"#"}{field.${field.name}_range.start} AND ${field.name} <![CDATA[ <= ]]>  ${r"#"}{field.${field.name}_range.end}
             </if>
</#if>
<#if ["boolean","Boolean"]?seq_contains(field.javaTypeShort)>
            <if test="${field.name} != null">
                AND ${field.name} like ${r"#"}{${field.name}}
            </if>
</#if>
</#list>
        </trim>
    </sql>

    <sql id="order">
        <trim prefix="ORDER BY">
            <if test="order_array != null">
                <foreach collection="order_array" item="value" separator=",">
<#list fields as field>
<#if ["char","String","Char","short","int","long","float","double","Short","Integer","Long","Float","Double","BigDecimal","Date","DateTime","boolean","Boolean"]?seq_contains(field.javaTypeShort)>
                    <!-- ${field.name} -->
                    <if test="value == '${field.name}_asc'">${field.columnName}</if>
                    <if test="value == '${field.name}_desc'">${field.columnName} DESC</if>
</#if>
</#list>
                </foreach>
            </if>
        </trim>
    </sql>
</mapper>

<#-- 修改模板生成路径 -->
${callback.setTargetFileDir(targetFileDir+"\\dao")}${callback.setTargetFileName("Mybatis-"+className+".xml")}