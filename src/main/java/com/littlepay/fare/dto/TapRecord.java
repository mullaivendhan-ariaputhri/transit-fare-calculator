package com.littlepay.fare.dto;

import static com.littlepay.fare.constants.Constants.TAP_RECORD;

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
@JsonPropertyOrder({"ID", "DateTimeUTC", "TapType", "StopId", "CompanyId", "BusID", "PAN"})
public class TapRecord {

  @JsonProperty("ID")
  private Integer id;

  @JsonProperty("DateTimeUTC")
  private OffsetDateTime dateTimeUTC;

  @JsonProperty("TapType")
  private String tapType;

  @JsonProperty("StopId")
  private String stopId;

  @JsonProperty("CompanyId")
  private String companyId;

  @JsonProperty("BusID")
  private String busId;

  @JsonProperty("PAN")
  private String pan;

  @Override
  public String toString() {
    return String.format(TAP_RECORD, id, dateTimeUTC, tapType, stopId, companyId, busId, pan);
  }
}
