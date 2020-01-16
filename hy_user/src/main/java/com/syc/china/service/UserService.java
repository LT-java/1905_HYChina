package com.syc.china.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.syc.china.enums.ExceptionEnums;
import com.syc.china.exception.HChinaException;
import com.syc.china.mapper.RoleMapper;
import com.syc.china.mapper.UserMapper;
import com.syc.china.pojo.PageResult;
import com.syc.china.pojo.Role;
import com.syc.china.pojo.User;
import com.syc.china.utils.CodecUtils;
import com.syc.china.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    public List<User> queryUsers() {
        return userMapper.selectAll();
    }



    public void sendSms(String phone) {
        //生成验证码
        String code = NumberUtils.generateCode(6);
        try{
            Map<String,String> msg = new HashMap<>();
            msg.put("code",code);
            msg.put("phone",phone);
            amqpTemplate.convertAndSend("wlkg.sms.exchange","sms.verify.code",msg);


            //将code存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public void register(User user,String code) {
        //获取redis中的key
        String key = KEY_PREFIX + user.getUsername();
        //获取redis中存储的验证码
        String redisCode = redisTemplate.opsForValue().get(key);

        //检查是否正确
        if(StringUtils.isEmpty(redisCode) || !redisCode.equals(code)){
            throw new HChinaException(ExceptionEnums.INVALID_VERFIY_CODE);
        }

        ///String salt = CodecUtils.generateSalt();
        //user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //user.setSalt(salt);
        boolean boo  = userMapper.insertSelective(user) == 1;

        userMapper.insertMid(user.getId(),user.getMemberId());

        if(boo){
            redisTemplate.delete(key);
        }

    }

    public void deleteUser(Integer id) {

        userMapper.deleteByPrimaryKey(id);

        userMapper.deleteMid(id);
    }


    @Transactional
    public void updateUser(User user,List<Integer> ids) {
        Integer roleId = ids.get(0);
        user.setMemberId(roleId);
        //更新用户表
        userMapper.updateByPrimaryKeySelective(user);
        //先根据用户Id删除中间表的信息
        int row = userMapper.deleteMid(user.getId());

        if(row > 0){
            //若删除成功再根据用户id和新的roleId向中间表中添加数据
            userMapper.insertMid(user.getId(),roleId);
        }else{
            throw new RuntimeException();
        }

    }



    public String selectRoleByUsername(String username) {
        return userMapper.selectRoleByUsername(username);
    }

    public String selectSaltByUsername(String username) {
        return userMapper.selectSaltByUsername(username);
    }

    public String getPassword(String username) {
        return userMapper.getPassword(username);
    }


    public User selectUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        return userMapper.selectOne(user);
    }

    public String queryMemberName(Integer memberId) {
        return userMapper.queryMemberName(memberId);
    }

    public User queryUsersById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public PageResult<User> queryUserList(Integer page, Integer rows, String sortBy, Boolean desc, String key) {

        //分页
        PageHelper.startPage(page,rows);

        //过滤
        Example example = new Example(User.class);

        if(org.apache.commons.lang.StringUtils.isNotBlank(key)){
            example.createCriteria().andLike("name","%" + key + "%")
                    .orEqualTo("letter",key);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(sortBy)){
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        List<User>  users= userMapper.selectByExample(example);

        for(User user : users){
            user.setMemberName(userMapper.queryMemberName(user.getId()));
            //user.setRole(roleMapper.selectByPrimaryKey(user.getMemberId()));
        }
        // 查询
        Page<User> pageInfo = (Page<User>) users;

        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public void addUser(User user,List<Integer> ids) {
        //user.setPassword(PasswordUtil.encode(user.getPassword()));
        Integer roleId = ids.get(0);
        user.setMemberId(roleId);
        userMapper.insertSelective(user);

        userMapper.insertMid(user.getId(),roleId);
    }
}

