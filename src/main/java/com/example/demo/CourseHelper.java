package com.example.demo;

import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CourseHelper {
	private CourseHelper(){
		
	}
	
	
	public static int getCourseRemainingNums(Course course) {
		WebDriver driver;
		System.setProperty("webdriver.chrome.driver","C:\\Users\\SP12893678\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to("http://ecs.ntcu.edu.tw/pub/TchSchedule_Search.aspx");
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		/*學年輸入*/
		WebElement txtYearsInput = driver.findElement(By.id("txtYears"));
		txtYearsInput.sendKeys("value", course.getTxtYears());
		
		/*學期選擇*/
		Select txtTermInput = new Select(driver.findElement(By.id("txtTerm")));
		int txtTermIdx = IntStream.range(0, txtTermInput.getOptions().size())
	    .filter(i -> txtTermInput.getOptions().get(i).getAttribute("value").equals(course.getTxtTerm()))
	    .findFirst() 
	    .orElse(-1);
		
		txtTermInput.selectByIndex(txtTermIdx);
		
		/*學制選擇*/
		Select ddlEduInput = new Select(driver.findElement(By.id("ddlEdu")));
		int ddlEduIdx = IntStream.range(0, ddlEduInput.getOptions().size())
	    .filter(i -> ddlEduInput.getOptions().get(i).getAttribute("value").equals(course.getDdlEdu()))
	    .findFirst() 
	    .orElse(-1);
		
		ddlEduInput.selectByIndex(ddlEduIdx);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[id*='ddlDept']>option[value='-1']")));
	
		/*學系選擇*/
		Select ddlDeptInput = new Select(driver.findElement(By.id("ddlDept")));
		int ddlDeptIdx = IntStream.range(0, ddlDeptInput.getOptions().size())
			    .filter(i -> ddlDeptInput.getOptions().get(i).getAttribute("value").equals(course.getDdlDept()))
			    .findFirst() 
			    .orElse(-1);
		ddlDeptInput.selectByIndex(ddlDeptIdx);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("select[id*='ddlClass']>option[value='-1']")));
		driver.findElement(By.id("btnSearch")).click();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[id*='UpdatePanel1']>table[id*='dsCurList']")));
		
		List<WebElement> codeList = driver.findElements(By.xpath("//td[3]/p/label"));
		List<WebElement> studentNumsList = driver.findElements(By.xpath("//td[13]/p/label"));
			
		for(int i=0;i<codeList.size();i++) {
			if(codeList.get(i).getText().equals(course.getCode())) {
				String[] studentNums = studentNumsList.get(i).getText().split("/");
				int remainingNums = Integer.valueOf(studentNums[0]) - Integer.valueOf(studentNums[1]);
				driver.close();
				return remainingNums;
			}
		}
		
		driver.close();
		return 0;
	}
}
