package com.abkan.consulting.coronavirustrackingapp.controller;

import com.abkan.consulting.coronavirustrackingapp.service.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("confirmedCasesData", coronaVirusDataService.getAllData());
        model.addAttribute("totalNumberOfCases", coronaVirusDataService.getTotalReportedCases());
        return "home";
    }
}
