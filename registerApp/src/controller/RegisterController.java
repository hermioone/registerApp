package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import bean.JsonMessage;

@Controller
public class RegisterController {
	
	//The name of xml file which store username and password
	private static final String USER_PATH = "user.xml";
	
	@RequestMapping(value="/")
	public String forward() {
		return "register";
	}
	
	/**
	 * Get username and password from html form and register
	 * @param username:The username from input
	 * @param password:The password from input
	 * @param model
	 * @param request
	 * @return
	 * @throws DocumentException
	 */
	@RequestMapping(value="/register")
	public String register(String username, String password, Model model, HttpServletRequest request) throws DocumentException {
		username = username.trim();
		password = password.trim();
		String path = request.getServletContext().getRealPath(USER_PATH);
		addUser(path, username, password);
		model.addAttribute("user", username);
		return "success";
	}
	
	/**
	 * Validate username using jQuery
	 * @param username
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/validate")
	@ResponseBody
	public JsonMessage validate(String username, HttpServletRequest request) throws Exception {
		JsonMessage message = new JsonMessage();
		String name = username.trim();
		if(name.length() < 5) {
			message.setFlag(false);
			message.setMessage("Your username is not valid.The length should be no less than 5 characters");
			return message;
		}
		for(int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if(c < '0' || (c >'9' && c < 'A') || (c > 'Z' && c < 'a') || (c > 'z')) {
				message.setFlag(false);
				message.setMessage("Your username is not valid.It accepts alpha-numeric values only");
				return message;
			}
		}
		if(queryUsername(request.getServletContext().getRealPath(USER_PATH), name)) {
			message.setFlag(false);
			message.setMessage("Your username is not valid.It has been registered");
			return message;
		}
		message.setFlag(true);
		return message;
	}
	
	/**
	 * validate password using jQuery
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/validatePass")
	@ResponseBody
	public JsonMessage validatePass(String password) {
		JsonMessage message = new JsonMessage();
		password = password.trim();
		if(password.length() < 8) {
			message.setFlag(false);
			message.setMessage("Your password should have a minimum length of 8 characters");
			return message;
		}
		boolean num = false;
		boolean upper = false;
		boolean lower = false;
		for(int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if(c >= '0' && c <= '9') num = true;
			else if(c >= 'A' && c <= 'Z') upper = true;
			else if(c >= 'a' && c <= 'z') lower = true;
		}
		if((!num) || (!upper) ||(!lower)) {
			message.setFlag(false);
			message.setMessage("Your password should contains at least 1 number, 1 uppercase, and 1 lowercase character");
			return message;
		}
		message.setFlag(true);
		return message;
	}
	
	/**
	 * Decide whether the username has been stored in the xml file.
	 * @param path:The path of xml file
	 * @param username
	 * @return
	 * @throws Exception
	 */
	private boolean queryUsername(String path, String username) throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(path);
		Element root = document.getRootElement();
		List<Element> list = root.elements("user");
		for(Element element : list) {
			Element usernameElement = element.element("username");
			String name = usernameElement.getText();
			if(name.equals(username)) return true;
		}
		return false;
	}
	
	/**
	 * Write username and password into xml file
	 * @param path:The path of xml file
	 * @param username
	 * @param password
	 * @throws DocumentException
	 */
	private void addUser(String path, String username, String password) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(path);
		Element root = document.getRootElement();
		Element user = root.addElement("user");
		Element usernameElement = user.addElement("username");
		Element passwordElement = user.addElement("password");
		usernameElement.setText(username);
		passwordElement.setText(password);
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new FileOutputStream(path), format);
			xmlWriter.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	
}
