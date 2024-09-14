package com.littlepay.fare.constants;

public class Constants {

  // General Constants
  public static final String COMMA_SEPARATOR = ",";

  // Tap Record
  public static final String TAP_RECORD = "%d,%s,%s,%s,%s,%s,%s";
  public static final String ID = "ID";
  public static final String DATE_TIME_UTC = "DateTimeUTC";
  public static final String TAP_TYPE = "TapType";
  public static final String STOP_ID = "StopId";
  public static final String COMPANY_ID = "CompanyId";
  public static final String BUS_ID = "BusID";
  public static final String PAN = "PAN";

  // Trip Record
  public static final String TRIP_RECORD = "%s,%s,%d,%s,%s,$%.2f,%s,%s,%s,%s";
  public static final String CHARGE_AMOUNT = "$%.2f";

  private Constants() {
    // Private constructor to prevent instantiation
  }
}