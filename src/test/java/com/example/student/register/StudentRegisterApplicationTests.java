package com.example.student.register;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.student.register.entity.Student;
import com.example.student.register.service.StudentService;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@SpringBootTest
class StudentRegisterApplicationTests {
	
	@Autowired
	private StudentService studentService;

	@Test
	void contextLoads() {
		List<Student> students =  studentService.findAllStudent();
		students.forEach(System.out::println);
	}
	
//	   @Test
	   public void main() {
//		   System.setProperty("webdriver.chrome.driver", "C:\\Users\\waiyanmyintmyat\\Desktop\\student\\path\\to\\chromedriver.exe");

	        WebDriver driver = new ChromeDriver();

	        driver.get("https://www.example.com");

	        WebElement element = driver.findElement(By.cssSelector("a#some-link"));

	        element.click();

	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }

	        driver.quit();
	    }

}
