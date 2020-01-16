package com.syc.china.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.syc.china.annotation.SysLogger;
import com.syc.china.pojo.Admin;
import com.syc.china.pojo.PageResult;
import com.syc.china.pojo.Role;
import com.syc.china.pojo.User;
import com.syc.china.service.AdminService;
import com.syc.china.service.RoleService;
import com.syc.china.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private RoleService roleService;


    /**
     * 验证码生成
     * */
/*    @RequestMapping("/code")
    public void defaultKaptcha(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();

            redisTemplate.opsForValue().set("code",createText);
            //httpServletRequest.setAttribute("code",createText);
            System.out.println(redisTemplate.opsForValue().get("code"));
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }*/

    /**
     * 登录
     */
   /* @PostMapping("/login")
    public String login(UserDTO userDTO,HttpServletRequest httpServletRequest, Model model) {


        System.out.println(userDTO);

        String code = redisTemplate.opsForValue().get("code");

        //String parameter = httpServletRequest.getParameter("code");

        System.out.println("原code+" + code + "+填写的code+" + userDTO.getCaptcha());
        if (!code.equals(userDTO.getCaptcha())) {
            System.out.println("验证码有误");
            return "www.test.com";
        }
        Subject subject = SecurityUtils.getSubject();

        String password = userDTO.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(userDTO.getUsername(), password);

        subject.login(token);

        if (subject.isAuthenticated()) {
            //当前用户经过了认证
            model.addAttribute("username",token.getUsername());
            return "list";
        } else {
            token.clear();
            return "login";
        }
    }*/

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/login.html";
    }





    /**
     * 展示薪资的同时,还要把该操作过程记录下来.
     * AOP:面向切面+自定义注解
     */
    //@SysLogger(value = "展示工资") //SysLogger注解被监控
    @GetMapping("/get")
    @ResponseBody
    public String test(){

        return "5000元";
    }

    /**
     * 展示修改用户信息的页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/showUpdate")
    public String showUpdate(@RequestParam("id")Integer id,Model model){
        User user = userService.queryUsersById(id);
        user.setMemberName(userService.queryMemberName(user.getMemberId()));
        List<Role> roles = roleService.queryRoles();
        model.addAttribute("roles",roles);
        model.addAttribute("user",user);
        return "updateUser";
    }

    @GetMapping("/showLogin")
    public String showLogin(){
        return "login";
    }

    /**
     * 查询所有用户
     * @return
     */
    /*@GetMapping("/queryAll")
    public String queryUsers(Model model){
        List<User> users = userService.queryUsers();

        for(User user : users){
            user.setMemberName(userService.queryMemberName(user.getMemberId()));
        }
        model.addAttribute("users",users);
        return "userList";
    }*/




    /**
     * 后台登录
     * @param admin
     * @param model
     * @param session
     * @return
     */
   /* @PostMapping("/login")
    public String postLogin(Admin admin, Model model, HttpSession session){

        Admin admin1 = adminService.login(admin);
        if(admin1 == null){
            throw new RuntimeException();
        }
        List<User> users = userService.queryUsers();

        for(User user : users){
            user.setMemberName(userService.queryMemberName(user.getMemberId()));

        }
        model.addAttribute("users",users);
        session.setAttribute("admin",admin1);
        return "userList";

    }
*/

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("/phoneCode")
    public ResponseEntity<Void> sendSms(@RequestParam("phone")String phone){
        userService.sendSms(phone);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/showRegister")
    public String showRegister(){
        return "register";
    }


    /**
     * 前台注册用户
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(User user, @RequestParam("code")String code){
        userService.register(user,code);
        return ResponseEntity.ok(null);
    }


    /**
     * 查询用户列表
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */
    @GetMapping("/queryAll")
    public ResponseEntity<PageResult<User>> getUserList(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "true") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ){

        PageResult<User> result = userService.queryUserList(page,rows,sortBy,desc,key);
        return ResponseEntity.ok(result);
    }

    /**
     * 从后台添加一个用户
     * @param user
     * @return
     */
    @PostMapping("/changeUser")
    public ResponseEntity<Void> addUser(User user,@RequestParam("ids") List<Integer> ids){
        userService.addUser(user,ids);
        return ResponseEntity.ok(null);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Void> deleteUser(@RequestParam("id")Integer id,Model model){
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }


    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PutMapping("/changeUser")
    public ResponseEntity<Void> update(User user,@RequestParam("ids") List<Integer> ids){

        userService.updateUser(user,ids);

        return ResponseEntity.ok(null);
    }


    /**
     * 查询角色集合
     * @return
     */
    @GetMapping("/getRoles")
    public ResponseEntity<List<Role>> getRoles(){

        List<Role> roles = roleService.queryRoles();
        return ResponseEntity.ok(roles);

    }









}
