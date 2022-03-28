package com.msb.mapper;

import com.msb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public void insert(User user);
  //  public List<User> select(User user);
  //  public  void delete(User user);
}
