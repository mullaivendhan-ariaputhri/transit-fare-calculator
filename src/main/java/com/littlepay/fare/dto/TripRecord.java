package com.littlepay.fare.dto;

import static com.littlepay.fare.constants.Constants.TRIP_RECORD;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "Started",
  "Finished",
  "DurationSecs",
  "FromStopId",
  "ToStopId",
  "ChargeAmount",
  "CompanyId",
  "BusID",
  "PAN",
  "Status"
})
public class TripRecord {

  @JsonProperty("Started")
  private OffsetDateTime started;

  @JsonProperty("Finished")
  private OffsetDateTime finished;

  @JsonProperty("DurationSecs")
  private Integer durationSecs;

  @JsonProperty("FromStopId")
  private String fromStopId;

  @JsonProperty("ToStopId")
  private String toStopId;

  @JsonProperty("ChargeAmount")
  private Double chargeAmount;

  @JsonProperty("CompanyId")
  private String companyId;

  @JsonProperty("BusID")
  private String busID;

  @JsonProperty("PAN")
  private String pan;

  @JsonProperty("Status")
  private String status;

  @Override
  public String toString() {
    return String.format(
        TRIP_RECORD,
        started,
        finished,
        durationSecs,
        fromStopId,
        toStopId,
        chargeAmount,
        companyId,
        busID,
        pan,
        status);
  }
}
