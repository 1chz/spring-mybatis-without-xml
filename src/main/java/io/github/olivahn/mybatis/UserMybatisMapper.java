package io.github.olivahn.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

@Mapper
public interface UserMybatisMapper {
    @SelectProvider(type = UserQueryBuilder.class, method = "findByUsernameAndPassword")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    class UserQueryBuilder {
        public String findByUsernameAndPassword(String username, String password) {
            return new SQL() {
                {
                    SELECT("*");
                    FROM("users");
                    if (username != null) WHERE("username = #{username}");
                    if (password != null) WHERE("password = #{password}");
                    ORDER_BY("id");
                }
            }.toString();
        }
    }
}
