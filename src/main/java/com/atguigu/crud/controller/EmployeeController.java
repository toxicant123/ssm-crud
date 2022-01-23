package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author toxicant123
 * @Description
 * @create 2021-11-24 12:11
 */
@Controller
public class EmployeeController {
    @Autowired
    EmployeeService employeeServiceImpl;

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeServiceImpl.getAll();
        PageInfo page = new PageInfo(emps, 5);
        List list = page.getList();
        return Msg.success().add("pageInfo", page);
    }

    //查询员工数据（分页查询）
    @Deprecated
//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn, Model model) {
        PageHelper.startPage(pn, 5);
        List<Employee> emps = employeeServiceImpl.getAll();
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", page);
        return "list";
    }

    /**
     * 员工保存
     *
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        Map<String, Object> map = new HashMap<>();
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {
            employeeServiceImpl.saveEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuser(String empName) {
        //判断用户名是否是合法的表达式
        if (!(Pattern.matches("(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})", empName))) {
            return Msg.fail().add("va_msg", "用户名必须是6-16位数字和字母的组合");
        }

        boolean b = employeeServiceImpl.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail();
        }
    }

    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeServiceImpl.getEmp(id);
        return Msg.success().add("emp", employee);
    }

    @ResponseBody
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    public Msg saveEmp(Employee employe) {
        employeeServiceImpl.updateEmp(employe);
        return Msg.success();
    }

    @ResponseBody
    @RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
    public Msg deleteEmpById(@PathVariable("ids") String ids) {
        if (ids.contains("-")) {
            List<Integer> del_ids = new ArrayList<>();
            String[] str_ids = ids.split("-");
            for (String str_id : str_ids) {
                del_ids.add(Integer.parseInt(str_id));
            }
            employeeServiceImpl.deleteBatch(del_ids);
        } else {
            int id = Integer.parseInt(ids);
            employeeServiceImpl.deleteEmp(id);
        }
        return Msg.success();
    }


}
