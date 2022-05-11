package at.kk.msc.hcov.plugin.amt;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.ICrowdsourcingConnectorPlugin;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class AmtCrowdsourcingConnector implements ICrowdsourcingConnectorPlugin {

  @Override
  public Map<UUID, String> publishTasks(List<VerificationTask> list) throws PluginConfigurationNotSetException {
    throw new UnsupportedOperationException("Not yet implemented!");
  }

  @Override
  public Map<String, HitStatus> getStatusForHits(List<String> list) throws PluginConfigurationNotSetException {
    throw new UnsupportedOperationException("Not yet implemented!");
  }

  @Override
  public Map<String, List<RawResult>> getResultsForHits(List<String> list) throws PluginConfigurationNotSetException {
    throw new UnsupportedOperationException("Not yet implemented!");
  }

  @Override
  public void setConfiguration(Map<String, Object> map) {

  }

  @Override
  public Map<String, Object> getConfiguration() {
    throw new UnsupportedOperationException("Not yet implemented!");
  }

  @Override
  public boolean supports(String s) {
    return "AMT_CROWDSOURCING_CONNECTOR".equalsIgnoreCase(s);
  }
}
