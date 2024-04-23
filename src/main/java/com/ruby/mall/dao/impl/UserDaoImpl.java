package com.ruby.mall.dao.impl;

import com.ruby.mall.dao.UserDao;
import com.ruby.mall.dto.UserRegisterRequest;
import com.ruby.mall.model.Role;
import com.ruby.mall.model.User;
import com.ruby.mall.rowmapper.RoleRowMapper;
import com.ruby.mall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO `user` (" +
                "email, password, created_date, last_modified_date) " +
                "VALUES (:email, :pwd, :createDate, :lastModifiedDate);";

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("pwd", userRegisterRequest.getPwd());

        Date now = new Date();
        map.put("createDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer userId = keyHolder.getKey().intValue();
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM `user` WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date FROM `user` WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if (userList.size() > 0) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createUserRole(UserRegisterRequest userRegisterRequest, Integer userId) {
        String sql = "INSERT INTO user_has_role (user_id, role_id) VALUES (:usrId, :roleId)";
        Map<String, Object> map = new HashMap<>();
        map.put("usrId", userId);
        map.put("roleId", userRegisterRequest.getRole());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public Role getRoleNameByURoleId(Integer uRoleId) {
        String sql = "SELECT Role.role_id, Role.role_name\n" +
                "FROM user_has_role AS UR RIGHT OUTER JOIN role AS Role\n" +
                "ON UR.role_id = Role.role_id\n" +
                "WHERE UR.id = :id";

        Map<String, Object> map = new HashMap<>();
        map.put("id", uRoleId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());
        if(!roleList.isEmpty()){
            return roleList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        String sql = "SELECT role.role_id, role.role_name FROM role\n" +
                "JOIN user_has_role ON user_has_role.role_id = role.role_id\n" +
                "WHERE user_has_role.user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());
        return roleList;
    }
}
