package com.abkan.consulting.coronavirustrackingapp.service;

import com.abkan.consulting.coronavirustrackingapp.model.ConfirmedData;
import com.abkan.consulting.coronavirustrackingapp.webclient.WebClient;
import java.io.IOException;
import java.io.StringReader;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoronaVirusDataService {

  @Value("${confirmed.data.url}")
  private String confirrmedDataSourceUrl;

  @Autowired private WebClient webClient;

  private List<ConfirmedData> allData = new ArrayList<>();

  @PostConstruct
  @Scheduled(cron = "* * 1 * * *")
  public List<ConfirmedData> getAllConfirmedCases() {
    try {
      HttpResponse<String> response = webClient.sendRequest(confirrmedDataSourceUrl);
      return readCSVData(response.body());
    } catch (Exception exception) {
      log.error(
          "An exception occurred while fetching the confirmed cases {}", exception.getMessage());
      return Collections.emptyList();
    }
  }

  private List<ConfirmedData> readCSVData(String dataString) throws IOException {
    StringReader reader = new StringReader(dataString);
    Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
    List<ConfirmedData> confirmedData = new ArrayList<>();
    for (CSVRecord record : records) {
      ConfirmedData data =
          ConfirmedData.builder()
              .state(getState(record))
              .country(record.get("Country/Region"))
              .totalCount(Integer.parseInt(record.get(record.size() - 1)))
              .build();
      confirmedData.add(data);
    }
    allData.addAll(confirmedData);
    return confirmedData;
  }

  private String getState(CSVRecord record) {
    return record.get("Province/State").isEmpty()
        ? record.get("Country/Region")
        : record.get("Province/State");
  }

  public List<ConfirmedData> getAllData() {
    return allData;
  }

  public Integer getTotalReportedCases() {
    return allData.stream().mapToInt(ConfirmedData::getTotalCount).sum();
  }
}
