package at.kk.msc.hcov.plugin.amt.util;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
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
        .autoApprovalDelayInSeconds(30L)
        .build();
  }

  public static CreateHitWithHitTypeRequest createHitWithHitTypeRequest(
      VerificationTask verificationTask, String hitTypeId, Map<String, Object> pluginConfiguration
  ){
    return CreateHitWithHitTypeRequest.builder()
        .hitTypeId(hitTypeId)
        .question(wrapInHtmlQuestionTag(verificationTask.getTaskHtml()))
        .lifetimeInSeconds(Long.parseLong(pluginConfiguration.get("LifetimeInSeconds").toString()))
        .maxAssignments(Integer.parseInt(pluginConfiguration.get("MaxAssignments").toString()))
        .build();
  }

  private static String wrapInHtmlQuestionTag(String htmlQuestion){
    String openingTag = "<HTMLQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2011-11-11/HTMLQuestion.xsd\"><HTMLContent><![CDATA[";
    String closingTag = "]]></HTMLContent><FrameHeight>0</FrameHeight></HTMLQuestion>";
    return openingTag + htmlQuestion + closingTag;
  }
}
