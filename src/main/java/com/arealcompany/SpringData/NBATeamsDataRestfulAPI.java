package com.arealcompany.SpringData;

import com.arealcompany.SpringData.records.Team;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NBATeamsDataRestfulAPI implements ApplicationContextAware {

    private ApplicationContext context;

    private static final String BASE_HTML_DOC = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>NBA Teams</title>
            </head>
            <body>
            <table>
                <tbody>
                [PLACEHOLDER]
                </tbody>
            </table>
            </body>
            </html>""";

    @GetMapping("/teams")
    public String getTeams(@RequestParam(value = "maxcount", defaultValue = "-1") Integer maxCount) {
        List<Team> teams = context.getBean(NbaTeamsData.class).findAll();
        StringBuilder output = new StringBuilder();
        int count = 0;
        for (Team team : teams) {
            if (maxCount >= 0 && count >= maxCount) {
                break;
            }
            output.append("<tr>").append(team.prettyHtml()).append("</br></br></br></tr>");
            count++;
        }
        return BASE_HTML_DOC.replace("[PLACEHOLDER]",output.toString());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
