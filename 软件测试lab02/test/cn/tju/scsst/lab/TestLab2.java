package cn.tju.scsst.lab;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestLab2 {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        String driverPath = System.getProperty("user.dir") + "/resources/driver/geckodriver.exe";
        System.setProperty("webdriver.gecko.driver", driverPath);
        System.setProperty("webdriver.firefox.bin","D:\\firefox\\firefox.exe");
        driver = new FirefoxDriver();
        baseUrl = "http://121.193.130.195:8800";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testLab2() throws Exception {
        String filePath = System.getProperty("user.dir") + "/resources/杞欢娴嬭瘯鍚嶅崟.xlsx";
        HashMap<User, String> accounts = this.getAccount(filePath, 3, 'B','D');
        for (Map.Entry<User, String> account : accounts.entrySet()) {
            driver.get(baseUrl + "/");
            driver.findElement(By.name("id")).clear();
            driver.findElement(By.name("id")).sendKeys(account.getKey().accout);
            driver.findElement(By.name("password")).clear();
            driver.findElement(By.name("password")).sendKeys(account.getKey().password);
            driver.findElement(By.id("btn_login")).click();
            WebElement git = driver.findElement(By.id("student-git"));
            if (!account.getValue().isEmpty()) {
                assertEquals(account.getValue(), git.getText().trim());
            }
            driver.findElement(By.id("btn_logout")).click();
            driver.findElement(By.id("btn_return")).click();
        }

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
          fail(verificationErrorString);
        }
    }

    private class User {
        String accout;
        String password;

        public User(String account, String password){
            this.accout = account;
            this.password = password;
        }
    }

    private HashMap<User, String> getAccount(String filePath, int startRow, Character accountColumn, Character gitColumn)
            throws Exception {
        HashMap<User, String> res = new HashMap<>();
        int accountCol = accountColumn - 'A';  // 鑾峰彇excel涓处鍙锋墍鍦ㄥ垪
        int gitCol = gitColumn - 'A';  // 鑾峰彇excel涓璯it鍦板潃鎵�鍦ㄥ垪
        Workbook wb = new XSSFWorkbook(filePath);
        Sheet sheet = wb.getSheetAt(0);  // 鑾峰彇瀛樻斁鏁版嵁鐨勮〃
        int maxRow = sheet.getPhysicalNumberOfRows();
        for (int i = startRow - 1; i < maxRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                NumberFormat NF = NumberFormat.getInstance();
                NF.setGroupingUsed(false);  // 璁剧疆璇诲彇鍒扮殑璐﹀彿鐨勬牸寮�
                String account = NF.format(row.getCell(accountCol).getNumericCellValue());
                String password = account.substring(account.length() - 6);  // 鏍规嵁璐﹀彿鑾峰彇瀵嗙爜
                String gitAddress = row.getCell(gitCol).getStringCellValue().trim();
//            String account = (new BigDecimal(row.getCell(accCol).getNumericCellValue()).toPlainString());
                res.put(new User(account, password), gitAddress);
            }
        }
        return res;
    }
}
