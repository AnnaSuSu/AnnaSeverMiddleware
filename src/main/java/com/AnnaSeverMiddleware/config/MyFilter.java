package com.AnnaSeverMiddleware.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

/*
 * spring boot自定义扫描策略
 */
public class MyFilter implements TypeFilter {

	private static final Logger log = LoggerFactory.getLogger(MyFilter.class);

	/**
	 * NettyTcp服务端包名
	 */
	// 选择tcp的整个包
	public static String tcpServerPackage = "com.AnnaSeverMiddleware.netty.tcp";

	// 选择NettyTcpServerRun类
//	public static String tcpServerPackage = "com.AnnaSeverMiddleware.netty.tcp.NettyTcpServerRun";

	/**
	 * NettyWs包名
	 */
	public static String websocketPackage = "com.AnnaSeverMiddleware.netty.ws";

	/**
	 * 加载配置
	 */
	private List<String> filterConfigs = new ArrayList<>();

	public MyFilter() {
		// 读取配置文件资源流
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// NettyTcp服务端是否启动
		// 得到配置文件中的server.tcp-server-run属性
		String tcpServerRun = properties.getProperty("server.tcp-server-run");
		// NettyWs是否启动
		// 得到配置文件中的server.ws-server-run属性
		String wsServerRun = properties.getProperty("server.ws-server-run");

		// 在构造方法加载配置，若读取到的数据为false，则不扫描该包下的类
		// 如果配置文件中的属性为false，则加入到filterConfigs集合
		if ("false".equals(tcpServerRun)) {
			filterConfigs.add(tcpServerPackage);
		}
		if ("false".equals(wsServerRun)) {
			filterConfigs.add(websocketPackage);
		}
	}

	/*
	 * MetadataReader：当前正在扫描的类的信息； MetadataReaderFactory：可以通过它来获取其他类的信息。
	 */
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		// 获取当前正在扫描的类的信息，因为@ComponentScan注解在启动类下，所以他会扫描启动类一下的所有类。
		// 得到所有类
		ClassMetadata classMetadata = metadataReader.getClassMetadata();
		// 得到所有类的类名
		String className = classMetadata.getClassName();
		log.info("当前启动类下的所有包为：" + className);
		// 判断是否需要扫描
		for (String filterConfig : filterConfigs) {
			// 判断包是不是在所有类中
			if (className.contains(filterConfig)) {
				return true;
			}
		}
		return false;
	}

}
