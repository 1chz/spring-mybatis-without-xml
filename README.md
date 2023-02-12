# 마이바티스 동적 쿼리 샘플

## 동적 쿼리(Dynamic Query)란?
조건의 유무에 따라 다른 쿼리를 실행하는 것을 동적 쿼리라고 한다.

Spring Data JPA의 경우 이러한 동적 쿼리를 작성하는게 꽤 까다로우며, Querydsl이나 jOOQ가 많이 사용되지만 설정이 귀찮고 러닝커브가 꽤 있다.

JdbcTemplate은 즉시 사용하기에는 편리하지만 ResultSet을 매핑하는게 귀찮다.

단지 동적 쿼리만 편하게 처리하고 싶다면 Spring Data JPA + Mybatis의 조합이 생각보다 괜찮으니 한번 살펴볼 가치가 있지 않을까?

## 핵심 의존성
XML을 배제하고 사용하기 위해서는 Mybatis3 이상의 버전이 필요하다.

```kotlin
implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.0")
```

## XML을 사용하지 않고 동적 쿼리를 구현하는 방법

[📜 관련 마이바티스 문서](https://mybatis.org/mybatis-3/ko/java-api.html#%EC%9E%90%EB%B0%94-api)

[📜 예제 코드](src/main/java/io/github/olivahn/mybatis/UserMybatisMapper.java)

[📜 HTTP 요청](fetch-users.http)

```java
@Mapper
public interface UserMybatisMapper {
    // 어떤 클래스의 어떤 메소드를 통해 SELECT 쿼리를 제공할 것인지를 명시할 수 있다
    // 메소드로 넘어온 파라미터를 @Param을 통해 마이바티스 쿼리에 바인딩한다
    @SelectProvider(type = UserQueryBuilder.class, method = "findByUsernameAndPassword")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    // 쿼리를 구현하여 제공할 클래스를 작성한다
    class UserQueryBuilder {
        // 실질적으로 쿼리를 작성해 제공하는 메소드이며 접근제한자는 public이어야 한다
        public String findByUsernameAndPassword(String username, String password) {
            // 마이바티스에서 제공하는 SQL 클래스를 통해 쿼리 편하게 구현할 수 있다
            // StringBuilder나 StringBuffer등을 사용해서 쿼리를 구현해도 무방하다
            // 혹은 자바 17이상을 사용한다면 텍스트 블록을 사용할수도 있다
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
```

## XML을 사용하지 않고 단순 쿼리를 구현하는 방법

만약 쿼리가 길다면 자바17의 텍스트 블록을 사용해 더욱 깔끔하게 구현할 수 있다.

사실 이 부분은 Spring Data JPA를 같이 사용하고 있다면 쿼리 메소드로 인해 아무 의미가 없을수도 있다.

```java
@Mapper
public interface UserMybatisMapper {
    @Insert("INSERT INTO users (username, password) VALUES (#{username}, #{password})")
    void save(@Param("username") String username, @Param("password") String password);

    @Update("UPDATE users SET username = #{username}, password = #{password} WHERE id = #{id}")
    void update(@Param("id") Long id, @Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(@Param("id") Long id);
    
    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteById(@Param("id") Long id);
}
```
