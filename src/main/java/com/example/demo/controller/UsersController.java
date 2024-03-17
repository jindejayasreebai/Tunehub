package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.LoginData;
import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

//@CrossOrigin("*")
//@RestController
@Controller
public class UsersController {
	
	@Autowired
	UsersService service;
	
	@Autowired
	SongService songService;
	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user) {
		
		boolean userStatus=service.emailExists(user.getEmail());
		if(userStatus==false) {
		service.addUser(user);
		System.out.println("user added successsfully");
		}
		else {
			System.out.println("user already exists");
		}
		return "login";
	}
	@PostMapping("validate")
	public String validate(@ModelAttribute LoginData data,
			HttpSession session,Model model) {
		
		System.out.println("call received");
	String email=data.getEmail();
	String password=data.getPassword();
	if(service.validateUser(email,password)==true)	{
		String role=service.getRole(email);
		session.setAttribute("email", email);
		if(role.equals("admin")) {
			
			
		return "adminhome";
	}
	else {
		Users user=service.getUser(email);
		boolean userStatus=user.isPremium();
		List<Song> songsList=songService.fetchAllSongs();
		model.addAttribute("songs", songsList);
	
		model.addAttribute("isPremium", userStatus);
		return "customerhome";
	}
	}
		else {
			return "login";
		}
	}

	
}