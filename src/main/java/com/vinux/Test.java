package com.vinux;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args) throws UnknownHostException, MalformedURLException {
		 URL url = new URL("http://www.baidu.com/index.html#aa?cansu=bjsxt");
         String protocol = url.getProtocol();
         System.out.println("协议:"+protocol);
         String host = url.getHost();
         System.out.println("主机名:"+host);
         int port = url.getPort();
         System.out.println("端口号:"+port);
         int defualtPort = url.getDefaultPort();
         System.out.println("默认端口:"+defualtPort);
         String file = url.getFile();
         System.out.println("资源路径:"+file);
         String path = url.getPath();
         System.out.println("资源路径:"+path);
	}
}
