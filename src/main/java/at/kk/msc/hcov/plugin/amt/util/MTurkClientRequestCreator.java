package at.kk.msc.hcov.plugin.amt.util;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import software.amazon.awssdk.services.mturk.model.Comparator;
import software.amazon.awssdk.services.mturk.model.CreateHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.CreateHitWithHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.CreateQualificationTypeRequest;
import software.amazon.awssdk.services.mturk.model.QualificationRequirement;
import software.amazon.awssdk.services.mturk.model.QualificationTypeStatus;

public class MTurkClientRequestCreator {

  public static CreateHitTypeRequest createHitTypeRequest(
      Map<String, Object> pluginConfiguration, String verificationName, Optional<String> qualificationTestId
  ) throws NumberFormatException {
    CreateHitTypeRequest.Builder builder = CreateHitTypeRequest.builder()
        .title(verificationName + " Type")
        .description(pluginConfiguration.get("Description").toString())
        .reward(pluginConfiguration.get("Reward").toString())
        .assignmentDurationInSeconds(Long.parseLong(pluginConfiguration.get("AssignmentDurationInSeconds").toString()))
        .keywords(pluginConfiguration.get("Keywords").toString())
        .autoApprovalDelayInSeconds(30L);
    qualificationTestId.ifPresent(s -> builder.qualificationRequirements(
        QualificationRequirement.builder()
            .qualificationTypeId(s)
            .comparator(Comparator.GREATER_THAN_OR_EQUAL_TO)
            .integerValues(80)
            .build()
    ));
    return builder.build();
  }

  public static CreateHitWithHitTypeRequest createHitWithHitTypeRequest(
      VerificationTask verificationTask, String hitTypeId, Map<String, Object> pluginConfiguration
  ) {
    return CreateHitWithHitTypeRequest.builder()
        .hitTypeId(hitTypeId)
        .question(wrapInHtmlQuestionTag(verificationTask.getTaskHtml()))
        .lifetimeInSeconds(Long.parseLong(pluginConfiguration.get("LifetimeInSeconds").toString()))
        .maxAssignments(Integer.parseInt(pluginConfiguration.get("MaxAssignments").toString()))
        .build();
  }

  public static CreateQualificationTypeRequest createQualificationTypeRequest(
      VerificationTask verificationTask, Map<String, Object> pluginConfiguration
  ) throws IOException {
    String test = FileUtils.readFileToString(new File(pluginConfiguration.get("QUALIFICATION_TEST_URI").toString()), "UTF-8");
    String answerKey = FileUtils.readFileToString(new File(pluginConfiguration.get("ANSWER_KEY_URI").toString()), "UTF-8");
    return CreateQualificationTypeRequest.builder()
        .name("Sample qualification test for: " + verificationTask.getVerificationName())
        .description("Description qualification " + verificationTask.getVerificationName())
        .qualificationTypeStatus(QualificationTypeStatus.ACTIVE)
        .test(test)
        .answerKey(answerKey)
        .testDurationInSeconds(600L)
        .build();
  }

  private static String wrapInHtmlQuestionTag(String htmlQuestion) {
    String openingTag =
        "<HTMLQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2011-11-11/HTMLQuestion.xsd\"><HTMLContent><![CDATA[";
    String closingTag = "]]></HTMLContent><FrameHeight>0</FrameHeight></HTMLQuestion>";
    return openingTag + htmlQuestion + closingTag;
  }
}
