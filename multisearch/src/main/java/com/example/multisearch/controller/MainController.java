package com.example.multisearch.controller;

import com.example.multisearch.model.SearchResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class MainController {

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @GetMapping("/search")
    public String searchWords(@RequestParam("words") String words, Model model) {
        String[] wordArray = words.split("\\n");
        List<SearchResult> results = new ArrayList<>();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Desktop\\chromedriver.exe");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        for (String word : wordArray) {
            SearchResult result = new SearchResult();
            result.setWord(word);
            String definition = searchAndPrintDefinition(driver, word.trim());
            if (definition.startsWith("단어")) {
                result.setDefinition(definition);
                // Create URL for the word
                result.setUrl("https://endic.naver.com/search.nhn?sLn=kr&isOnlyViewEE=N&query=" + word.trim());
            } else {
                result.setError(definition);
            }
            results.add(result);
        }

        driver.quit();
        model.addAttribute("results", results);
        return "results";
    }


    public static String searchAndPrintDefinition(WebDriver driver, String word) {
        String url = "https://endic.naver.com/search.nhn?sLn=kr&isOnlyViewEE=N&query=" + word;
        driver.get(url);

        WebElement element = driver.findElement(By.cssSelector(".row"));
        String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Element row = doc.selectFirst(".row");
        if (row != null) {
            Elements meanings = row.select(".mean_item");
            Elements partsOfSpeech = row.select(".word_class");
            if (meanings.size() > 0 && partsOfSpeech.size() > 0) {
                StringBuilder definition = new StringBuilder();
                definition.append("단어: ").append(word).append("\n");
                definition.append("품사: ").append(partsOfSpeech.get(0).text()).append("\n");
                for (Element meaning : meanings) {
                    definition.append("뜻: ").append(meaning.text()).append("\n");
                }
                return definition.toString();
            } else {
                return "단어 '" + word + "'의 정보를 찾을 수 없습니다.";
            }
        } else {
            return "단어 '" + word + "'의 정보를 찾을 수 없습니다.";
        }
    }
}
