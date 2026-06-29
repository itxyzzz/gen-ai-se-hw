package edu.setu.banking.pipeline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRecord {
  @JsonProperty("transaction_id")
  public String transactionId;
  public String timestamp;
  @JsonProperty("source_account")
  public String sourceAccount;
  @JsonProperty("destination_account")
  public String destinationAccount;
  public String amount;
  public String currency;
  @JsonProperty("transaction_type")
  public String transactionType;
  public String description;
  public Map<String, Object> metadata;
}
