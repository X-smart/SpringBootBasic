package cn.com.taiji.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	
	@RequestMapping("/xyz")
	public String hello() {
		return "hello";
	
	}
	
}
