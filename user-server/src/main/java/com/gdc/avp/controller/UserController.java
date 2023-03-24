package com.gdc.avp.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gdc.avp.component.BCryptPasswordEncoder;
import com.gdc.avp.component.JwtGenerator;
import com.gwm.avp.entity.Captcha;
import com.gwm.avp.entity.Role;
import com.gwm.avp.entity.User;
import com.gdc.avp.service.ResourceService;
import com.gdc.avp.service.RoleService;
import com.gdc.avp.service.UserService;
import com.gwm.avp.util.*;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.gwm.avpdatacloud.basicpackages.log.GDCLog;
import com.gwm.avpdatacloud.basicpackages.log.annotation.GDC;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@GDC
@Api(tags = "用户管理API")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerator jwtGenerator;

    @Value("${session-exp.login:30}")
    private int logoutTime;

    @ApiOperation(value = "分页查询",httpMethod = "GET")
    @GetMapping("/page")
    @ResponseBody
    public ResultData page(@RequestParam Integer page, @RequestParam Integer pageSize, String userName, String roleId){
        Page<User> userPage= new Page<>(page,pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(userName),User::getUserName,userName);
        wrapper.eq(StringUtils.isNotEmpty(roleId),User::getRoleId,roleId);
        List<User> userList= userService.page(userPage,wrapper).getRecords();
        userList.forEach(user -> {
            user.setRoleName(roleService.getById(user.getRoleId()).getName());});
        return ResultData.SUCCESS("",userService.page(userPage,wrapper).setRecords(userList));
    }

    @ApiOperation(value = "新增用户",httpMethod = "POST")
    @PostMapping("/insert")
    @ResponseBody
    public ResultData insert(@RequestBody @Validated User user){
//        user.setPwd(passwordEncoder.encode(user.getPwd()));
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        user.setEnabled(Common.User.Rs.DISABLE.toValue());
        user.setPasswordUpdateTime(DateUtil.getDaDate());
        try {
            userService.save(user);
        }catch (DuplicateKeyException e){
            return ResultData.FAILL("新增失败！名称已存在！");
        }
        return ResultData.SUCCESS("新增成功！");
    }

    @ApiOperation(value = "更新用户",httpMethod = "POST")
    @PostMapping("/update")
    @ResponseBody
    public ResultData update(@RequestBody @Validated User user){
        if(user.getPwd() != null && !user.getPwd().isEmpty()){
            user.setPwd(passwordEncoder.encode(user.getPwd()));
            user.setPasswordUpdateTime(DateUtil.getDaDate());
        }else{
            User old = userService.getById(user.getId());
            user.setPwd(old.getPwd());
        }
        try {
            userService.updateById(user);
        }catch (DuplicateKeyException e){
            return ResultData.FAILL("修改失败！名称已存在！");
        }
        return ResultData.SUCCESS("修改成功！");
    }

    /**
     * 用户修改密码
     * @param old
     * @param pwd
     * @return
     */
    @ApiOperation(value = "修改密码",httpMethod = "POST")
    @PostMapping("/resetPwd")
    @ResponseBody
    public ResultData resetPwd(@RequestParam String userId,@RequestParam String old,@RequestParam String pwd){
        User su = userService.getById(userId);
        if (su == null || !passwordEncoder.matches(old,su.getPwd())) {
            return ResultData.FAILL("密码错误！");
        }
        su.setPwd(passwordEncoder.encode(pwd));
        su.setPasswordUpdateTime(DateUtil.getDaDate());
        userService.updateById(su);
        return ResultData.SUCCESS("操作成功！");
    }

    @ApiOperation(value = "修改用户状态",httpMethod = "POST")
    @PostMapping("/status")
    @ResponseBody
    public ResultData updateStatus(@RequestParam Long userId,@RequestParam @Max(value = 1) @Min(value = 0) Integer status){
        User user = new User();
        user.setId(userId);
        user.setEnabled(status);
        userService.updateById(user);
        return ResultData.SUCCESS("修改成功！");
    }

    @ApiOperation(value = "删除用户",httpMethod = "POST")
    @PostMapping("/delete")
    @ResponseBody
    public ResultData delete(@RequestParam String userId){
        userService.removeById(userId);
        return ResultData.SUCCESS("操作成功！");
    }

    @ApiOperation(value = "验证用户密码是否正确",httpMethod = "POST")
    @PostMapping("/valid")
    @ResponseBody
    public ResultData validUser(String userName,String pwd){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        User oldUser = userService.getOne(lambdaQueryWrapper);
        if (oldUser == null || !BCrypt.checkpw(pwd,oldUser.getPwd())) {
            return ResultData.FAILL("密码错误！");
        }
        return ResultData.SUCCESS("登陆成功");
    }

    @ApiOperation(value = "登录系统",httpMethod = "POST")
    @PostMapping("/login")
    @ResponseBody
    public ResultData login(String userName,String pwd,String captchaId,String captchaCode){
        String oldCode = redisTemplate.opsForValue().get(captchaId);
        if (StringUtils.isEmpty(oldCode)||!oldCode.equals(captchaCode)){
            return ResultData.FAILL("验证码错误！");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        User oldUser = userService.getOne(lambdaQueryWrapper);
        if(oldUser == null || oldUser.getId() == null){
            return ResultData.FAILL("用户不存在！");
        }
        //检验当前用户是否为启用状态
        if(oldUser.getEnabled() == Common.User.Rs.DISABLE.toValue()){
            return ResultData.FAILL("用户已被禁用！");
        }
        //检验当前用户是否为解锁状态
        int passwordErrorCount = oldUser.getPasswordErrorCount();
        if(passwordErrorCount == 5){
            return ResultData.FAILL("用户帐号已被锁定，请稍后再试或联系管理员解除锁定！");
        }
        if (!BCrypt.checkpw(pwd,oldUser.getPwd())) {
            oldUser.setPasswordErrorCount(passwordErrorCount + 1);
            userService.updateById(oldUser);
            return ResultData.FAILL("密码错误！");
        }
        Role role = roleService.getById(oldUser.getRoleId());
        //判断用户关联的角色是否为禁用状态
        if(role.getStatus() == Common.User.Rs.DISABLE.toValue()){
            return ResultData.FAILL("用户角色已被禁用！");
        }
        boolean passwordNonExpired = false;
        if (( (new Date().getTime() - oldUser.getPasswordUpdateTime().getTime()) < (1000l * 3600 * 24 * 90))) {
            passwordNonExpired = true;
        }
        //生成jwt
        Map<String,Object> claims = new HashMap<>();
        claims.put("user_name",userName);
        claims.put("authorities",new String[]{role.getName()});
        claims.put("roleIds",new Long[]{role.getId()});
        try {
            String jwt = jwtGenerator.generator(claims);
            JSONObject json = new JSONObject();
            json.put("token",jwt);
            json.put("expiresIn",jwtGenerator.getExpMin());
            json.put("passwordNonExpired",passwordNonExpired);
            //redis 中 记录jwt
            redisTemplate.opsForValue().set(userName,jwt,logoutTime, TimeUnit.MINUTES);
            return ResultData.SUCCESS("登陆成功",json);
        } catch (Exception e) {
            GDCLog.error(null,userName,"login"+e.getMessage());
            return ResultData.ERROR(e.getMessage());
        }
    }

    @ApiOperation(value = "登出系统",httpMethod = "GET")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResultData logout(@RequestHeader("Authorization") String authorization){
        String jwt = authorization.replace("Bearer ", "");
        String userName = null;
        try {
            JWSObject jwsObject = JWSObject.parse(jwt);
            userName = jwsObject.getPayload().toJSONObject().getAsString("user_name");
            redisTemplate.delete(userName);
            return ResultData.SUCCESS("用户注销成功");
        } catch (ParseException e) {
            GDCLog.error(null,userName,"com.gdc.avp.controller.UserController.logout"+e.getMessage());
            return ResultData.ERROR(e.getMessage());
        }
    }

    @ApiOperation(value = "获取验证码图片",httpMethod = "GET")
    @GetMapping("/captcha")
    @ResponseBody
    public ResultData generateCaptcha(){
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(300, 100, 4, 4);
        String imageBase64 = captcha.getImageBase64();
        String uuid = UUID.randomUUID().toString();
        Captcha c = new Captcha();
        c.setCaptchaImageBase64("data:image/png;base64,"+imageBase64);
        c.setCaptchaId(uuid);
        redisTemplate.opsForValue().set(uuid,captcha.getCode());
        redisTemplate.expire(uuid,1, TimeUnit.MINUTES);
        return ResultData.SUCCESS("ok",c);
    }
    @ApiOperation(value = "根据用户名查询用户",httpMethod = "POST")
    @RequestMapping("/getUserByName")
    public User getUserByName(@RequestParam String userName){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,userName);
        User user =  userService.getOne(lambdaQueryWrapper);
        if(user==null){
            return null;
        }
        Long roleId = user.getRoleId();
//        List<Long> resourceIds = resourceService.getSelectedResourceIdByRoleId(roleId);
//        if(resourceIds==null||resourceIds.isEmpty()){
//            return null;
//        }
        Role role = roleService.getById(roleId);
        if(role.getStatus().equals(0)){
            return null;
        }
        user.setRoleName(role.getName());
        return user;
    }

    @ApiOperation(value = "登陆失败次数计数",httpMethod = "POST")
    @RequestMapping("/countPasswordError")
    public void countPasswordError(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        User user = userService.getOne(lambdaQueryWrapper);
        if (user != null) {
            user.setPasswordErrorCount(user.getPasswordErrorCount() + 1);
            userService.updateById(user);
        }
    }

    @ApiOperation(value = "查询密码是否过期",httpMethod = "POST")
    @RequestMapping("/isPasswordNonExpired")
    public ResultData isPasswordNonExpired(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        User user = userService.getOne(lambdaQueryWrapper);
        if (user == null)
            return ResultData.FAILL("用户名错误！");
        if (( (new Date().getTime() - user.getPasswordUpdateTime().getTime()) < (1000l * 3600 * 24 * 90))) {
            return ResultData.SUCCESS("OK");
        }
        return ResultData.FAILL("密码过期");
    }

    /**
     * 用户修改密码
     * @param old
     * @param pwd
     * @return
     */
    @ApiOperation(value = "定期更新密码",httpMethod = "POST")
    @PostMapping("/updatePwd")
    @ResponseBody
    public ResultData updatePwd(@RequestParam String old,@RequestParam String pwd){
        if(old.equals(pwd)){
            return ResultData.FAILL("请输入不同密码！");
        }
        String userName = CurrentUserUtil.getUserName();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);
        User su = userService.getOne(lambdaQueryWrapper);
        if (su == null || !passwordEncoder.matches(old,su.getPwd())) {
            return ResultData.FAILL("密码错误！");
        }
        su.setPwd(passwordEncoder.encode(pwd));
        su.setPasswordUpdateTime(new Date());
        userService.updateById(su);
        return ResultData.SUCCESS("操作成功！");
    }

    @ApiOperation(value = "账户解锁",httpMethod = "POST")
    @PostMapping("/unlock")
    @ResponseBody
    public ResultData unlock(Long userId){
        User user = new User();
        user.setId(userId);
        user.setPasswordErrorCount(0);
        if(userService.updateById(user)){
            return ResultData.SUCCESS("解锁成功！");
        }else{
            return ResultData.SUCCESS("解锁失败！");
        }
    }

}