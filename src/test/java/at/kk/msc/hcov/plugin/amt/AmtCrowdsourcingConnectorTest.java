package at.kk.msc.hcov.plugin.amt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class AmtCrowdsourcingConnectorTest {

  AmtCrowdsourcingConnector target;

  @BeforeEach
  void setUp() {
    target = new AmtCrowdsourcingConnector();
  }

  @ParameterizedTest
  @ValueSource(strings = {"AMT_CROWDSOURCING_CONNECTOR", "amt_crowdsourcing_connector", "AmT_CrOwDsOuRcInG_CoNnEcToR"})
  public void testSupports_givenSupportedStrings(String givenString) {
    // when - then
    assertThat(target.supports(givenString)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"AMT_CROWDSOURCING_CONNECTORS", "amt_crowdsourcing_connectors"})
  @NullAndEmptySource
  public void testSupports_givenUnsupportedStrings(String givenString) {
    // when - then
    assertThat(target.supports(givenString)).isFalse();
  }
}
