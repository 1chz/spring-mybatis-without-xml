package io.github.olivahn.mybatis;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

@Mapper
public interface UserMybatisMapper {
    @Insert("INSERT INTO users (username, password) VALUES (#{username}, #{password})")
    void save(@Param("username") String username, @Param("password") String password);

    @Update("UPDATE users SET username = #{username}, password = #{password} WHERE id = #{id}")
    void update(@Param("id") Long id, @Param("username") String username, @Param("password") String password);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteById(@Param("id") Long id);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);

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
