package com.abkan.consulting.coronavirustrackingapp.controller;

import com.abkan.consulting.coronavirustrackingapp.model.ConfirmedData;
import com.abkan.consulting.coronavirustrackingapp.service.CoronaVirusDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "coronavirusData", description = "Collects all the data related to coronavirus cases.")
@RequestMapping(value = "/v1/coronavirusData")
public class DataController {

  @Autowired private CoronaVirusDataService coronaVirusDataService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Find all confirmed cases for coronavirus",
      description = "This end point will fetch all the confirmed cases from coronavirus",
      tags = {"service-provider"})
  @ApiResponses(
      value = {
        @ApiResponse(
            description = "OK",
            responseCode = "200",
            content =
                @Content(
                    array = @ArraySchema(schema = @Schema(implementation = ConfirmedData.class)))),
      })
  public ResponseEntity<List<ConfirmedData>> getAllConfirmedCases() {
    return ResponseEntity.ok(coronaVirusDataService.getAllConfirmedCases());
  }
}
