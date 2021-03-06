package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author toxicant123
 * @Description 处理和部门有关的请求
 * @create 2021-11-25 13:32
 */
@Controller
public class DepartmentController {
    @Autowired
    private DepartmentService departmentServiceImpl;

    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts() {
        List<Department> list = departmentServiceImpl.getDepts();
        return Msg.success().add("depts", list);
    }

}
