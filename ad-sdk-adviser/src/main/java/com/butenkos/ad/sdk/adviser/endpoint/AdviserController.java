package com.butenkos.ad.sdk.adviser.endpoint;

import com.butenkos.ad.sdk.adviser.model.domain.Country.CountryNotFoundException;
import com.butenkos.ad.sdk.adviser.model.request.AdNetworkAdviseRequest;
import com.butenkos.ad.sdk.adviser.model.response.AdNetworkAdviseResponse;
import com.butenkos.ad.sdk.adviser.model.response.ErrorResponse;
import com.butenkos.ad.sdk.adviser.service.validation.BeanValidationException;
import com.butenkos.ad.sdk.adviser.usecase.AdviseAdNetworks;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ad-network adviser")
@RestController
@Profile("default")
public class AdviserController {
  private static final Logger LOG = LoggerFactory.getLogger(AdviserController.class);
  private final AdviseAdNetworks useCase;

  public AdviserController(AdviseAdNetworks useCase) {
    this.useCase = useCase;
  }

  @PostMapping(path = "/advise", produces = "application/json")
  @Operation(
      method = "POST",
      operationId = "adviseAdNetwork",
      description = "returns a list of ordered by score and filtered by configured constraints ad network names"
  )
  @ApiResponses(
      {
          @ApiResponse(
              responseCode = "200",
              description = "OK",
              content = @Content(schema = @Schema(implementation = AdNetworkAdviseResponse.class))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//          @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//          @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
//          @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
//          @ApiResponse(responseCode = "503", description = "Service not available", content = @Content)
      }
  )
  public ResponseEntity<Object> adviseAdNetwork(@RequestBody AdNetworkAdviseRequest request) {
    try {
      LOG.debug("adviseAdNetwork(), request={}", request);
      return ResponseEntity.ok(useCase.giveAdvice(request));
    } catch (BeanValidationException | CountryNotFoundException e) {
      LOG.error("Validation error, request={}", request, e);
      return ResponseEntity.
          badRequest().
          body(new ErrorResponse("400", "Bad Request", e.getMessage(), "/advise"));
    } catch (Exception e) {
      LOG.error("error, request={}", request, e);
      return ResponseEntity.
          badRequest().
          body(new ErrorResponse("500", "Internal Server Error", e.getMessage(), "/advise"));
    }
  }

}
