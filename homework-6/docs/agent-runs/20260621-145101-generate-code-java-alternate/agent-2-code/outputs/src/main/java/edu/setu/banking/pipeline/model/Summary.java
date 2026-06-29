package edu.setu.banking.pipeline.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Summary {
  public int total_transactions;
  public int settled;
  public int rejected;
  public int review_required;
  public int error;
  public Map<String, Integer> reason_code_groups = new LinkedHashMap<>();
}
