package com.vinux.websocket;

//import java.io.IOException;
//import java.net.URI;
//
//import javax.websocket.ClientEndpoint;
//import javax.websocket.ContainerProvider;
//import javax.websocket.DeploymentException;
//import javax.websocket.Session;
//import javax.websocket.WebSocketContainer;
//
//@ClientEndpoint
//public class WebSocketTest {
//	
//	private Session session;
//	private String deviceId;
//	
//	public WebSocketTest () {
//        
//    }
//	
//	public WebSocketTest(String deviceId){
//		this.deviceId = deviceId;
//	}
//	
//	
//	
//	public boolean start() {
//		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//		String uri = "ws://localhost:7080/websocket";
//		try {
//			session = container.connectToServer(WebSocketTest.class, URI.create(uri));
//			System.out.println(deviceId);
//			return true;
//		} catch (DeploymentException | IOException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	public static void main(String[] args) {
//		int count = 1000;
//		for(int i = 1; i < count; i++) {
//			WebSocketTest wst = new WebSocketTest(String.valueOf(i));
//			if(!wst.start()) {
//				System.out.println("测试结束！");
//                break;
//			}
//		}
//	}
//}
