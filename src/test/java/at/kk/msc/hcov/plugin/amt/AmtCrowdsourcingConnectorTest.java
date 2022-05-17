package at.kk.msc.hcov.plugin.amt;

import static at.kk.msc.hcov.plugin.amt.mockdata.AmtCrowdsourcingConfigurationMockData.getAmtCrowdsourcingConfigurationDataMock;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.EXPECTED_CREATE_QUALIFICATION_TYPE_REQUEST;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.EXPECTED_FIRST_RAW_RESULTS;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.EXPECTED_HIT_STATUS_MAP;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.EXPECTED_PUBLISHED_TASK_MAPPINGS;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.EXPECTED_SECOND_RAW_RESULTS;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.FIRST_CS_MOCK_ID;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCKED_HIT_TYPE_ID;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCKED_QUALIFICATION_TEST_ID;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_FIRST_ASSIGNMENTS_FOR_HIT;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_FIRST_HIT_RESPONSE;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_SECOND_ASSIGNMENTS_FOR_HIT;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.MOCK_SECOND_HIT_RESPONSE;
import static at.kk.msc.hcov.plugin.amt.mockdata.MTurkClientMockData.SECOND_CS_MOCK_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.plugin.amt.mockdata.VerificationTaskMockData;
import at.kk.msc.hcov.plugin.amt.util.MTurkClientCreator;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import at.kk.msc.hcov.sdk.plugin.PluginConfigurationNotSetException;
import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import software.amazon.awssdk.services.mturk.MTurkClient;
import software.amazon.awssdk.services.mturk.model.CreateHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.CreateHitTypeResponse;
import software.amazon.awssdk.services.mturk.model.CreateHitWithHitTypeResponse;
import software.amazon.awssdk.services.mturk.model.CreateQualificationTypeRequest;
import software.amazon.awssdk.services.mturk.model.CreateQualificationTypeResponse;
import software.amazon.awssdk.services.mturk.model.GetHitRequest;
import software.amazon.awssdk.services.mturk.model.HIT;
import software.amazon.awssdk.services.mturk.model.ListAssignmentsForHitRequest;
import software.amazon.awssdk.services.mturk.model.QualificationType;

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

  @ParameterizedTest
  @ValueSource(strings = {"Description", "Reward", "AssignmentDurationInSeconds", "Keywords", "LifetimeInSeconds", "MaxAssignments"})
  public void testValidateConfigurationSetOrThrow_throwsExceptionIfConfigurationkeyIsMissing(String keyToRemove) {
    Map<String, Object> givenConfiguration = getAmtCrowdsourcingConfigurationDataMock();
    givenConfiguration.remove(keyToRemove);

    // when - then
    target.setConfiguration(givenConfiguration);
    assertThatThrownBy(() -> target.validateConfigurationSetOrThrow())
        .isInstanceOf(PluginConfigurationNotSetException.class)
        .hasMessageContaining(keyToRemove);
  }

  @ParameterizedTest
  @ValueSource(strings = {"a", "60a4", "450.4", "234,5"})
  public void testValidateConfigurationSetOrThrow_givenIncorrectAssignmentDurationInSeconds(String givenIncorrectNumber) {
    // given
    Map<String, Object> givenConfiguration = getAmtCrowdsourcingConfigurationDataMock();
    givenConfiguration.put("AssignmentDurationInSeconds", givenIncorrectNumber);

    // when - then
    target.setConfiguration(givenConfiguration);
    assertThatThrownBy(() -> target.validateConfigurationSetOrThrow())
        .isInstanceOf(PluginConfigurationNotSetException.class)
        .hasMessageContaining("Configuration entry 'AssignmentDurationInSeconds' must be numeric!");
  }
  @ParameterizedTest
  @ValueSource(strings = {"a", "60a4", "450.4", "234,5"})
  public void testValidateConfigurationSetOrThrow_givenIncorrectLifetimeInSeconds(String givenIncorrectNumber) {
    // given
    Map<String, Object> givenConfiguration = getAmtCrowdsourcingConfigurationDataMock();
    givenConfiguration.put("LifetimeInSeconds", givenIncorrectNumber);

    // when - then
    target.setConfiguration(givenConfiguration);
    assertThatThrownBy(() -> target.validateConfigurationSetOrThrow())
        .isInstanceOf(PluginConfigurationNotSetException.class)
        .hasMessageContaining("Configuration entry 'LifetimeInSeconds' must be numeric!");
  }
  @ParameterizedTest
  @ValueSource(strings = {"a", "60a4", "450.4", "234,5"})
  public void testValidateConfigurationSetOrThrow_givenIncorrectMaxAssignments(String givenIncorrectNumber) {
    // given
    Map<String, Object> givenConfiguration = getAmtCrowdsourcingConfigurationDataMock();
    givenConfiguration.put("MaxAssignments", givenIncorrectNumber);

    // when - then
    target.setConfiguration(givenConfiguration);
    assertThatThrownBy(() -> target.validateConfigurationSetOrThrow())
        .isInstanceOf(PluginConfigurationNotSetException.class)
        .hasMessageContaining("Configuration entry 'MaxAssignments' must be numeric!");
  }

  @Test
  public void testPublishTasks() throws PluginConfigurationNotSetException {
    MTurkClient mTurkClientMock = mock(MTurkClient.class);
    when(mTurkClientMock.createHITType(any(CreateHitTypeRequest.class)))
        .thenReturn(CreateHitTypeResponse.builder().hitTypeId(MOCKED_HIT_TYPE_ID).build());

    when(mTurkClientMock.createHITWithHITType(eq(MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST())))
        .thenReturn(CreateHitWithHitTypeResponse.builder().hit(
            HIT.builder().hitId(FIRST_CS_MOCK_ID).build()
        ).build());

    when(mTurkClientMock.createHITWithHITType(eq(MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST())))
        .thenReturn(CreateHitWithHitTypeResponse.builder().hit(
            HIT.builder().hitId(SECOND_CS_MOCK_ID).build()
        ).build());

    try (MockedStatic<MTurkClientCreator> mockedClientCreator = mockStatic(MTurkClientCreator.class)) {
      mockedClientCreator.when(() -> MTurkClientCreator.getMTurkClient(true)).thenReturn(mTurkClientMock);

      // given
      target.setConfiguration(getAmtCrowdsourcingConfigurationDataMock());
      List<VerificationTask> givenVerificationTasks = VerificationTaskMockData.MOCK_VERIFICATION_TASKS();

      // when
      Map<UUID, String> actual = target.publishTasks(givenVerificationTasks);

      // then
      assertThat(actual)
          .hasSize(2)
          .containsAllEntriesOf(EXPECTED_PUBLISHED_TASK_MAPPINGS());
      mockedClientCreator.verify(() -> MTurkClientCreator.getMTurkClient(eq(true)), times(1));
      verify(mTurkClientMock, times(1)).createHITWithHITType(eq(MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST()));
      verify(mTurkClientMock, times(1)).createHITWithHITType(eq(MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST()));
      verify(mTurkClientMock, never()).createQualificationType(any(CreateQualificationTypeRequest.class));
      verify(mTurkClientMock, times(1)).createHITType(any(CreateHitTypeRequest.class));
    }
  }

  @Test
  public void testPublishTasks_withQualificationTest() throws PluginConfigurationNotSetException {
    MTurkClient mTurkClientMock = mock(MTurkClient.class);
    when(mTurkClientMock.createHITType(any(CreateHitTypeRequest.class)))
        .thenReturn(CreateHitTypeResponse.builder().hitTypeId(MOCKED_HIT_TYPE_ID).build());

    when(mTurkClientMock.createQualificationType(eq(EXPECTED_CREATE_QUALIFICATION_TYPE_REQUEST())))
        .thenReturn(
            CreateQualificationTypeResponse.builder().qualificationType(
                QualificationType.builder().qualificationTypeId(MOCKED_QUALIFICATION_TEST_ID).build()
            ).build()
        );

    when(mTurkClientMock.createHITWithHITType(eq(MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST())))
        .thenReturn(CreateHitWithHitTypeResponse.builder().hit(
            HIT.builder().hitId(FIRST_CS_MOCK_ID).build()
        ).build());

    when(mTurkClientMock.createHITWithHITType(eq(MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST())))
        .thenReturn(CreateHitWithHitTypeResponse.builder().hit(
            HIT.builder().hitId(SECOND_CS_MOCK_ID).build()
        ).build());

    try (MockedStatic<MTurkClientCreator> mockedClientCreator = mockStatic(MTurkClientCreator.class)) {
      mockedClientCreator.when(() -> MTurkClientCreator.getMTurkClient(true)).thenReturn(mTurkClientMock);

      // given
      Map<String, Object> amtConfiguration = getAmtCrowdsourcingConfigurationDataMock();
      amtConfiguration.put("QUALIFICATION_TEST_URI", "src/test/resources/mocked-qualificaiton-test.xml");
      amtConfiguration.put("ANSWER_KEY_URI", "src/test/resources/mocked-answer-key.xml");
      target.setConfiguration(amtConfiguration);
      List<VerificationTask> givenVerificationTasks = VerificationTaskMockData.MOCK_VERIFICATION_TASKS();

      // when
      Map<UUID, String> actual = target.publishTasks(givenVerificationTasks);

      // then
      assertThat(actual)
          .hasSize(2)
          .containsAllEntriesOf(EXPECTED_PUBLISHED_TASK_MAPPINGS());
      mockedClientCreator.verify(() -> MTurkClientCreator.getMTurkClient(eq(true)), times(1));
      verify(mTurkClientMock, times(1)).createHITWithHITType(eq(MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST()));
      verify(mTurkClientMock, times(1)).createHITWithHITType(eq(MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST()));
      verify(mTurkClientMock, times(1)).createQualificationType(eq(EXPECTED_CREATE_QUALIFICATION_TYPE_REQUEST()));
      verify(mTurkClientMock, times(1)).createHITType(any(CreateHitTypeRequest.class));
    }
  }

  @Test
  public void testGetStatusForHits() throws PluginConfigurationNotSetException {
    MTurkClient mTurkClientMock = mock(MTurkClient.class);

    when(mTurkClientMock.getHIT(eq(GetHitRequest.builder().hitId(FIRST_CS_MOCK_ID).build())))
        .thenReturn(MOCK_FIRST_HIT_RESPONSE());
    when(mTurkClientMock.getHIT(eq(GetHitRequest.builder().hitId(SECOND_CS_MOCK_ID).build())))
        .thenReturn(MOCK_SECOND_HIT_RESPONSE());


    try (MockedStatic<MTurkClientCreator> mockedClientCreator = mockStatic(MTurkClientCreator.class)) {
      mockedClientCreator.when(() -> MTurkClientCreator.getMTurkClient(true)).thenReturn(mTurkClientMock);

      // given
      target.setConfiguration(getAmtCrowdsourcingConfigurationDataMock());

      // when
      Map<String, HitStatus> actual = target.getStatusForHits(List.of(FIRST_CS_MOCK_ID, SECOND_CS_MOCK_ID));

      // then
      assertThat(actual)
          .hasSize(2)
          .containsAllEntriesOf(EXPECTED_HIT_STATUS_MAP());
    }
  }

  @Test
  public void testGetResultsForHits() throws PluginConfigurationNotSetException {
    MTurkClient mTurkClientMock = mock(MTurkClient.class);

    when(mTurkClientMock.listAssignmentsForHIT(eq(ListAssignmentsForHitRequest.builder().hitId(FIRST_CS_MOCK_ID).build())))
        .thenReturn(MOCK_FIRST_ASSIGNMENTS_FOR_HIT());
    when(mTurkClientMock.listAssignmentsForHIT(eq(ListAssignmentsForHitRequest.builder().hitId(SECOND_CS_MOCK_ID).build())))
        .thenReturn(MOCK_SECOND_ASSIGNMENTS_FOR_HIT());


    try (MockedStatic<MTurkClientCreator> mockedClientCreator = mockStatic(MTurkClientCreator.class)) {
      mockedClientCreator.when(() -> MTurkClientCreator.getMTurkClient(true)).thenReturn(mTurkClientMock);

      // given
      target.setConfiguration(getAmtCrowdsourcingConfigurationDataMock());

      // when
      Map<String, List<RawResult>> actual = target.getResultsForHits(List.of(FIRST_CS_MOCK_ID, SECOND_CS_MOCK_ID));

      // then
      assertThat(actual)
          .hasSize(2);
      assertThat(actual.get(FIRST_CS_MOCK_ID)).containsExactlyInAnyOrderElementsOf(EXPECTED_FIRST_RAW_RESULTS());
      assertThat(actual.get(SECOND_CS_MOCK_ID)).containsExactlyInAnyOrderElementsOf(EXPECTED_SECOND_RAW_RESULTS());
    }
  }

}
