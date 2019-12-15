package com.sina.aspect;

import utility.ControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.dao.DataAccessException;
import utility.DaoAcessException;

import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

public class ControllerAspect {
    public static final Logger logger = LogManager.getLogger(ControllerAspect.class);
//    public static final Log logger = LogFactory.getLog(ControllerAspect.class);

    public void beforeInfo() {
        System.out.println("-------------------begin-------------------");
        logger.info("beforInfo");
    }

    public void afterInfo() {
        System.out.println("-------------------end-------------------");
        logger.info("afterInfo");
    }

    public void retrunInfo() {
        System.out.println("-------------------return-------------------");
        logger.info("retrunInfo");
    }

    public void throwInfo() {
        System.out.println("-------------------throw-------------------");
        logger.info("throwInfo");
    }

    public String aroundInfo(ProceedingJoinPoint joinPoint) {
        logger.info("-------------------Controller begin-------------------");
        System.out.println("-------------------throw-------------------");
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request;
        String page = "error";
        try {
            page = (String) joinPoint.proceed();
        } catch (DataAccessException e) {
            e.printStackTrace();
            DaoAcessException daoAcessException = new DaoAcessException("DAO处理异常，请联系管理员！", e);
            logger.info("" +
                    "\n【源类名】：" + this.getClass().toString() +
                    "\n【方法名】：" + joinPoint.getSignature().toString() +
                    "\n【异常类】：" + e.getRootCause() +
                    "\n【异常】：" + daoAcessException +
                    "\n【原因】：" + e.getMessage());
            for (int i = 0; args != null && i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    request.setAttribute("exception", daoAcessException);
                    break;
                }
            }
            throw daoAcessException;
        } catch (ControllerException e) {
            e.printStackTrace();
            ControllerException controllerException = new ControllerException("控制逻辑处理异常，请联系管理员！", e);
            logger.info("" +
                    "\n【源类名】：" + this.getClass().toString() +
                    "\n【方法名】：" + joinPoint.getSignature().toString() +
                    "\n【异常类】：" + e.getCause() +
                    "\n【异常】：" + controllerException +
                    "\n【原因】：" + e.getMessage());
            throw controllerException;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.info("" +
                    "\n【源类名】：" + this.getClass().toString() +
                    "\n【方法名】：" + joinPoint.getSignature().toString() +
                    "\n【异常类】：" + e.getCause() +
                    "\n【异常】：" + e +
                    "\n【原因】：" + e.getMessage());
            for (int i = 0; args != null && i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    request.setAttribute("exception", e);
                    break;
                }
            }
            throw e;
        } finally {
            logger.info("-------------------Controller end -------------------");
            return page;
        }
    }
}
