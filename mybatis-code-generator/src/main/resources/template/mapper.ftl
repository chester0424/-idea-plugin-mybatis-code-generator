<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${packageName}.dao.I${className}">
    <resultMap id="resultMap" type="${packageName}.${className}">
        <#list fields as field>
            <result property="${field.name}" column="${field.columnName}" jdbcType="${field.jdbcType}"/>
        </#list>
    </resultMap>

    <select id="get">
        SELECT * FROM ${tableName}
        WHERE ${keyField.columnName} = #\{${keyField.name},${keyField.jdbcType}}
    </select>

    <select id="getPageList" resultType="resultMap">
        SELECT * FROM Product
        <trim prefix="WHERE" prefixOverrides="and |or ">
            <if test="id != null">and t.ID like #\{id}</if>
            <if test="name != null">and t.name like #\{id}</if>
            <if test="amount != null">and t.amount like #\{id}</if>
        </trim>
    </select>

    <insert id="inert">
        INSERT INTO Product(
        id,
        name,
        amount
        )
        VALUES (
        #\{id,jdbcType=VARCHAR},
        #\{name,jdbcType=VARCHAR},
        #\{amount,jdbcType=INTEGER},
        )
    </insert>

    <update id="Update">
        UPDATE Product
        <set>
            <if test="name != null">name = #{name},</if>
            <if test="amount != null">amount = #{amount},</if>
        </set>
        where id = #{id}
    </update>

    <delete id="delete">
        DELETE Product WHERE id = #{id}
    </delete>

    <sql id="where">
        <where>
            <if test="id != null">
                and id like #\{id,jdbcType=VARCHAR}
            </if>
            <if test="ids != null">
                and id in
                <foreach collection="ids" item="value" open="(" separator=","
                         close=")">#\{value,jdbcType=VARCHAR}</foreach>
            </if>
            <if test="id != null">and id like #\{id,jdbcType=VARCHAR}</if>
        </where>
    </sql>
    <sql id="orderBy">

    </sql>
</mapper>