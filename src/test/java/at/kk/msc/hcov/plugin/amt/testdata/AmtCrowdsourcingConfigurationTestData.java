package at.kk.msc.hcov.plugin.amt.testdata;

import java.util.HashMap;
import java.util.Map;

public class AmtCrowdsourcingConfigurationTestData {

  public static Map<String, Object> getAmtCrowdsourcingConfigurationDataMock() {
    Map<String, Object> givenConfiguration = new HashMap<>();
    givenConfiguration.put("SANDBOX", true);
    givenConfiguration.put("Description", "This is a sample description");
    givenConfiguration.put("Reward", "0");
    givenConfiguration.put("AssignmentDurationInSeconds", "604800");
    givenConfiguration.put("Keywords", "verification,ontology,pizza");
    givenConfiguration.put("LifetimeInSeconds", "604800");
    givenConfiguration.put("MaxAssignments", "5");
    return givenConfiguration;
  }
}
