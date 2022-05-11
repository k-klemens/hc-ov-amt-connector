package at.kk.msc.hcov.plugin.amt;

import at.kk.msc.hcov.plugin.amt.util.MTurkClientCreator;
import at.kk.msc.hcov.plugin.amt.util.MTurkClientRequestCreator;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.mturk.MTurkClient;
import software.amazon.awssdk.services.mturk.model.Assignment;
import software.amazon.awssdk.services.mturk.model.GetHitRequest;
import software.amazon.awssdk.services.mturk.model.GetHitResponse;
import software.amazon.awssdk.services.mturk.model.ListAssignmentsForHitRequest;

@Component
public class AmtCrowdsourcingConnector implements ICrowdsourcingConnectorPlugin {

  private Map<String, Object> configuration;

  @Override
  public Map<UUID, String> publishTasks(List<VerificationTask> verificationTasks) throws PluginConfigurationNotSetException {
    validateConfigurationSetOrThrow();
    MTurkClient mTurkClient = MTurkClientCreator.getMTurkClient((boolean) getConfiguration().getOrDefault("SANDBOX", false));

    Map<UUID, String> createdHits = new HashMap<>();
    String hitTypeId = null;
    for(VerificationTask verificationTask : verificationTasks) {
      if(hitTypeId == null) {
        hitTypeId = mTurkClient.createHITType(
            MTurkClientRequestCreator.createHitTypeRequest(getConfiguration(), verificationTask.getVerificationName())
        ).hitTypeId();
      }

      String hitId = mTurkClient.createHITWithHITType(
          MTurkClientRequestCreator.createHitWithHitTypeRequest(verificationTask, hitTypeId, getConfiguration())
      ).hit().hitId();
      createdHits.put(verificationTask.getOntologyElementId(), hitId);
    }

    return createdHits;
  }

  @Override
  public Map<String, HitStatus> getStatusForHits(List<String> hitIds) throws PluginConfigurationNotSetException {
    validateConfigurationSetOrThrow();

    MTurkClient mTurkClient = MTurkClientCreator.getMTurkClient((boolean) getConfiguration().getOrDefault("SANDBOX", false));

    return hitIds.stream()
        .map(hitId -> mTurkClient.getHIT(GetHitRequest.builder().hitId(hitId).build()))
        .map(GetHitResponse::hit)
        .map(hit -> new HitStatus(hit.hitId(), hit.maxAssignments(), hit.numberOfAssignmentsCompleted()))
        .collect(
            Collectors.toMap(
            HitStatus::getCrowdsourcingId,
            hitStatus -> hitStatus
            )
        );
  }

  @Override
  public Map<String, List<RawResult>> getResultsForHits(List<String> hitIds) throws PluginConfigurationNotSetException {
    validateConfigurationSetOrThrow();
    MTurkClient mTurkClient = MTurkClientCreator.getMTurkClient((boolean) getConfiguration().getOrDefault("SANDBOX", false));

    Map<String, List<RawResult>> returnMap = new HashMap<>();
    for(String hitId : hitIds) {
      List<Assignment> assignments = mTurkClient.listAssignmentsForHIT(ListAssignmentsForHitRequest.builder().hitId(hitId).build()).assignments();
      List<RawResult> rawResults = assignments.stream()
          .map(assignment -> new RawResult(assignment.assignmentId(), assignment.hitId(), assignment.workerId(), assignment.answer()))
          .toList();
      returnMap.put(hitId, rawResults);
    }
    return returnMap;
  }

  @Override
  public void setConfiguration(Map<String, Object> configuration) {
    this.configuration = new HashMap<>(configuration);
  }

  @Override
  public Map<String, Object> getConfiguration() {
    return this.configuration;
  }

  @Override
  public void validateConfigurationSetOrThrow() throws PluginConfigurationNotSetException {
    ICrowdsourcingConnectorPlugin.super.validateConfigurationSetOrThrow();
    if(!getConfiguration().containsKey("Description")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'Description' is required!");
    }
    if(!getConfiguration().containsKey("Reward")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'Reward' is required!");
    }
    if(!getConfiguration().containsKey("AssignmentDurationInSeconds")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'AssignmentDurationInSeconds' is required!");
    } else if(!StringUtils.isNumeric(getConfiguration().get("AssignmentDurationInSeconds").toString())) {
      throw new PluginConfigurationNotSetException("Configuration entry 'AssignmentDurationInSeconds' must be numeric!");
    }
    if(!getConfiguration().containsKey("Keywords")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'Keywords' is required!");
    }
    if(!getConfiguration().containsKey("LifetimeInSeconds")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'LifetimeInSeconds' is required!");
    }else if(!StringUtils.isNumeric(getConfiguration().get("LifetimeInSeconds").toString())) {
      throw new PluginConfigurationNotSetException("Configuration entry 'LifetimeInSeconds' must be numeric!");
    }
    if(!getConfiguration().containsKey("MaxAssignments")) {
      throw new PluginConfigurationNotSetException("Configuration entry 'MaxAssignments' is required!");
    } else if(!StringUtils.isNumeric(getConfiguration().get("MaxAssignments").toString())) {
      throw new PluginConfigurationNotSetException("Configuration entry 'MaxAssignments' must be numeric!");
    }

  }

  @Override
  public boolean supports(String s) {
    return "AMT_CROWDSOURCING_CONNECTOR".equalsIgnoreCase(s);
  }
}
