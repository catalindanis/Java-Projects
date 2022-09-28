package platform;

import org.apache.tomcat.jni.Local;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@SpringBootApplication
@Controller
public class CodeSharingPlatform {
    static CodeRepository codeRepository;
    public static void main(String[] args) {
       ConfigurableApplicationContext configurableApplicationContext =  SpringApplication.run(CodeSharingPlatform.class, args);
       codeRepository = configurableApplicationContext.getBean(CodeRepository.class);
    }

    @ResponseBody
    @GetMapping(value="/code/{id}")
    public String getCodeByNumber(@PathVariable String id){
        try {
            Code currentCode = codeRepository.getById(UUID.fromString(id));
            currentCode.setViews(currentCode.getViews()-1);
            codeRepository.save(currentCode);

            if((currentCode.isTimeRestriction() && currentCode.getTotalTimeSeconds().compareTo(LocalTime.now()) <= 0)
                    || (currentCode.isViewsRestriction() && currentCode.getViews() == 0))
                codeRepository.delete(currentCode);

            String timeCode = currentCode.isTimeRestriction()? "<span id=\"time_restriction\">The code will be available for " + currentCode.getTotalTimeSeconds().minusSeconds(LocalTime.now().toSecondOfDay()).toSecondOfDay() + " seconds</span><br>\n":"";
            String viewsCode = currentCode.isViewsRestriction()? "<span id=\"views_restriction\">" + currentCode.getViews() + " more views allowed</span><br>\n":"";

            return String.format("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title>Code</title>\n" +
                    "<link rel=\"stylesheet\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                    "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                    "<script>hljs.initHighlightingOnLoad();</script>\n" +
                    "<meta charset=\"UTF-8\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<span id=\"load_date\">%s</span><br>\n" +
                    viewsCode +
                    timeCode +
                    "<pre id=\"code_snippet\"><code>%s</code></pre>\n" +
                    "</body>\n" +
                    "</html>",currentCode.getDate(),currentCode.getCode());

        }catch (EntityNotFoundException entityNotFoundException){
            return "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "<title>Code</title>\n" +
                    "<link rel=\"stylesheet\"\n" +
                    "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                    "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                    "<script>hljs.initHighlightingOnLoad();</script>" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<pre id=\"code_snippet\"><code>404 Not Found</code></pre>" +
                    "</body>\n" +
                    "</html>";
        }
    }

    @ResponseBody
    @GetMapping("/api/code/{id}")
    public String getCodeByNumberAPI(@PathVariable String id){
        try {
            Code currentCode = codeRepository.getById(UUID.fromString(id));
            currentCode.setViews(currentCode.getViews()-1);
            codeRepository.save(currentCode);
            //System.out.println(currentCode.getTotalTimeSeconds());
            if((currentCode.isTimeRestriction() && currentCode.getTotalTimeSeconds().compareTo(LocalTime.now()) <= 0)
                    || (currentCode.isViewsRestriction() && currentCode.getViews() == 0))
                codeRepository.delete(currentCode);
            String timeCode;
            if(!currentCode.isViewsRestriction())
                timeCode = currentCode.isTimeRestriction()? ",\n\"time\": " + currentCode.getTotalTimeSeconds().minusSeconds(LocalTime.now().toSecondOfDay()).toSecondOfDay() + "\n":"";
            else
                timeCode = currentCode.isTimeRestriction()? ",\n\"time\": " + currentCode.getTotalTimeSeconds().minusSeconds(LocalTime.now().toSecondOfDay()).toSecondOfDay() + ",\n":"";
            String viewsCode = currentCode.isViewsRestriction()? ",\n\"views\": " + currentCode.getViews():"";

            return "{\n" +
                    String.format("\"code\": \"%s\",\n" +
                                    "\"date\": \"%s\"" +
                                    timeCode +
                                    viewsCode +
                                    "\n}",
                            currentCode.getCode(), currentCode.getDate());
        }catch (EntityNotFoundException entityNotFoundException){
            return "404 Not Found";
        }

    }

    @ResponseBody
    @PostMapping("/api/code/new")
    public String postCodeNew(@RequestBody Code code){
        code.checkRestrictions();
        codeRepository.save(code);
        return String.format("{\n \"id\": \"%s\" \n}",code.getId());
    }

    @ResponseBody
    @GetMapping("/code/new")
    public String getCodeNew(){
        return String.format("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<title>Create</title>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\"\n" +
                "       href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<script>function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value\n" +
                "    };\n" +
                "\n" +
                "    let json = JSON.stringify(object);\n" +
                "\n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "\n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}</script>" +
                "<textarea id=\"code_snippet\">// write your code here</textarea>\n" +
                "<br>\n" +
                "<span>Time restriction</span>\n<br>" +
                "<input id=\"time_restriction\" type=\"text\"/>\n" +
                "<br>\n" +
                "<span>Views restriction</span>\n<br>" +
                "<input id=\"views_restriction\" type=\"text\"/>\n" +
                "<br>\n" +
                "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>" +
                "</body>\n" +
                "</html>");
    }

    @GetMapping("/code/latest")
    public String getCodeLatest(Model model){
        List<Code> allCodes = codeRepository.findAll();
        LinkedList<Code> latestCodes = new LinkedList<>();
        for(int i=allCodes.size()-1; i >= 0 && latestCodes.size() < 10; i--)
            latestCodes.add(allCodes.get(i));
        model.addAttribute("codes",latestCodes);
        return "latestCodes";
    }

    @ResponseBody
    @GetMapping("/api/code/latest")
    public String getCodeLatestAPI(){
        List<Code> allCodes = codeRepository.findAll();
        StringBuilder result = new StringBuilder();
        result.append("[\n");
        for(int i=allCodes.size()-1; i >= 0 && allCodes.size()-i <= 10; i--) {
            result.append(String.format(
                    "{\n" +
                            String.format("\"code\": \"%s\",\n" +
                                            "\"date\": \"%s\",\n" +
                                            "\"time\": %d,\n" +
                                            "\"views\": %d" +
                                            "\n}",
                                    allCodes.get(i).getCode(), allCodes.get(i).getDate(),
                                    allCodes.get(i).getTotalTimeSeconds().minusSeconds(LocalTime.now().toSecondOfDay()).toSecondOfDay(), allCodes.get(i).getViews()+1)));
            if(allCodes.size()-i < 10 && i > 0)
                result.append(",\n");
        }
        result.append("\n]");
        System.out.println(result.toString());
        return result.toString();
    }
}
