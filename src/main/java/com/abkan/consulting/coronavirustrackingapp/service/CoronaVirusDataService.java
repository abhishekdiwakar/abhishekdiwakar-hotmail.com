package com.abkan.consulting.coronavirustrackingapp.service;

import com.abkan.consulting.coronavirustrackingapp.model.ConfirmedData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {

    @Value("${confirmed.data.url}")
    private String confirrmedDataSourceUrl;

    private List<ConfirmedData> allData = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(confirrmedDataSourceUrl))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        readCSVData(httpResponse.body());
    }

    private void readCSVData(String dataString) throws IOException {
        StringReader reader = new StringReader(dataString);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        List<ConfirmedData> confirmedData = new ArrayList<>();
        for (CSVRecord record : records) {
            ConfirmedData data = ConfirmedData.builder()
                    .state(getState(record))
                    .country(record.get("Country/Region"))
                    .totalCount(Integer.parseInt(record.get(record.size()-1)))
                    .build();
            confirmedData.add(data);
        }
        allData.addAll(confirmedData);
    }

    private String getState(CSVRecord record) {
        return record.get("Province/State").isEmpty() ? record.get("Country/Region") : record.get("Province/State");
    }

    public List<ConfirmedData> getAllData() {
        return allData;
    }

    public Integer getTotalReportedCases() {
        return allData.stream().mapToInt(ConfirmedData::getTotalCount).sum();
    }

}
