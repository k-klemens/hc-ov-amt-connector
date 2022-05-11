package at.kk.msc.hcov.plugin.amt.util;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.net.CacheRequest;
import java.util.Map;
import software.amazon.awssdk.services.mturk.model.CreateHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.CreateHitWithHitTypeRequest;

public class MTurkClientRequestCreator {

  public static CreateHitTypeRequest createHitTypeRequest(
      Map<String, Object> pluginConfiguration, String verificationName
  )  throws NumberFormatException{
    return CreateHitTypeRequest.builder()
        .title(verificationName+" Type")
        .description(pluginConfiguration.get("Description").toString())
        .reward(pluginConfiguration.get("Reward").toString())
        .assignmentDurationInSeconds(Long.parseLong(pluginConfiguration.get("AssignmentDurationInSeconds").toString()))
        .keywords(pluginConfiguration.get("Keywords").toString())
        .build();
  }

  public static CreateHitWithHitTypeRequest createHitWithHitTypeRequest(
      VerificationTask verificationTask, String hitTypeId, Map<String, Object> pluginConfiguration
  ){
    return CreateHitWithHitTypeRequest.builder()
        .hitTypeId(hitTypeId)
        .question(verificationTask.getTaskHtml())
        .lifetimeInSeconds(Long.parseLong(pluginConfiguration.get("LifetimeInSeconds").toString()))
        .maxAssignments(Integer.parseInt(pluginConfiguration.get("MaxAssignments").toString()))
        .build();
  }
}
