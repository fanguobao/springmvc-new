package com.sina.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sina.domain.Course;
import com.sina.service.CourseService;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import utility.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;
import utility.CError;
import utility.CErrors;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@Controller
public class TestController {
    private static Log logger = LogFactory.getLog(TestController.class);
    private CErrors mErrors = new CErrors();
    @Autowired
    //@Qualifier("serviceProxy")
    private CourseService courseService;



    @RequestMapping({"/","/test"})
    public String test(HttpServletRequest request, HttpServletResponse response,Model model)  {

        try {
            //1.测试字符集HttpServletRequest request
            logger.info(request.getCharacterEncoding());

            //2.验证通过ContextLoaderListener监听器加载的Spring根上下文WebApplicationContext，存储到ServletContext中
            WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
            WebApplicationContext springMVCContext = RequestContextUtils.getWebApplicationContext(request);

            //3.验证通过DispatcherServlet加载的SpringMVC，从ServletContext中获取之前的根上下文(即WebApplicationContext)作为自己上下文的parent上下文。，再初始化自己持有的上下文。
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();

            //4.测试字符集HttpServletResponse response
            // 设置响应内容类型
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            logger.info(out.toString());
            //5.测试数据源basicDataSource
            BasicDataSource basicDataSource= (BasicDataSource)webApplicationContext.getBean("basicDataSource");
            logger.info(basicDataSource.getDriverClassName());
        } catch (Exception e) {

            CError tError = new CError();
            tError.errorMessage = CError.BL_BQ;
            tError.functionName = "test(HttpServletRequest request,Model model)";
            tError.moduleName = this.getClass().toString();
            mErrors.addOneError(tError);
            throw new BusinessException(CError.BL_BQ);
        }

        return "test";
    }

    @RequestMapping({"/test3"})
    public String test2(HttpServletRequest request) {
        List list=new ArrayList();
        Course tSchem = new Course();
        tSchem.setCname("OKOK2");
        list.add(tSchem);

        tSchem = new Course();
        tSchem.setCname("沃尔玛范德萨发撒的发上来的发生单发开始的发了三大发的发阿萨德发撒旦教发骚的发斯蒂芬举案说法大 阿斯蒂芬沙发大厦发的");
        list.add(tSchem);
        courseService.saveCourseList(list);

        return "succes";
    }

    public CErrors getmErrors() {
        return mErrors;
    }


    @RequestMapping("/test2")
    public  String  test(String name){
        System.out.println(name);
        return "succes";
    }


    public static void main(String[] args) {
//        logger.trace("我是trace信息");
//        logger.debug("我是debug信息");
//        logger.info("我是info信息");
//        logger.warn("我是warn信息");
//        logger.error("我是error信息");
//        logger.fatal("我是fatal信息");
    }


}
