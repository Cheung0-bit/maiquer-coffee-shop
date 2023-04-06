package tech.maiquer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.maiquer.system.domain.SysUser;
import tech.maiquer.system.service.SysUserService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @GetMapping("/edit")
    public String editUser(Model model, SysUser sysUser) {
        model.addAttribute("myUser", sysUserService.queryById(sysUser.getId()).getData());
        return "user/edit" ;
    }


}
