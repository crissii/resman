package com.sunsharing.hlvideoserver.sys;

import com.sunsharing.component.resvalidate.config.ConfigContext;
import com.sunsharing.eos.server.EosInit;
import com.sunsharing.memCache.Config;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.util.Properties;

/**
 *<pre><b><font color="blue">SysInit</font></b></pre>
 *
 *<pre><b>&nbsp;--系统初始化--</b></pre>
 * JDK版本：JDK1.5.0
 * @author  <b>李自立</b> 
 */
public class SysInit extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/** 记录日志 */
	private static Logger logger = Logger.getLogger(SysInit.class);

    private static Properties pro;

    public static String sysPath;

	public void init(){
		
		ServletContext sc = this.getServletContext();
		logger.info("系统开始初始化...");
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		ServiceLocator.init(ctx);
		logger.info("系统初始化上下文结束...");
		
		logger.info("初始化其它参数...");
		
		//初始化配置文件信息和数据库全局参数信息
		 sysPath = sc.getRealPath("/");
        logger.info("初始化其它参数结束...:path:"+sysPath);

        ConfigContext.instancesBean(SysProp.class);
//        Config.getInstance().init(SysProp.memcachedHost,SysProp.sessionTimeOut);
//        EosInit.start(ctx, "com.sunsharing.hlvideoserver.service");

	}
	
	@Override
	public void destroy() {
		super.destroy();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
