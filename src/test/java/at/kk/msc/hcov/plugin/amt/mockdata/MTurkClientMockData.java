package at.kk.msc.hcov.plugin.amt.mockdata;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import software.amazon.awssdk.services.mturk.model.CreateHitWithHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.GetHitResponse;
import software.amazon.awssdk.services.mturk.model.HIT;

public class MTurkClientMockData {
  /* MOCK DATA WHICH THE PLUGIN MOCK CAN BE USED FOR! */
  public static final String MOCKED_HIT_TYPE_ID = "HIT-TYPE-ID";
  public static final String FIRST_CS_MOCK_ID = "FIRST-CS-ID";
  public static final String SECOND_CS_MOCK_ID = "SECOND-CS-ID";

  public static CreateHitWithHitTypeRequest MOCK_FIRST_CREATE_HIT_WITH_HIT_TYPE_REQUEST() {
    return CreateHitWithHitTypeRequest.builder()
        .hitTypeId(MOCKED_HIT_TYPE_ID)
        .question(VerificationTaskMockData.EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT)
        .lifetimeInSeconds(604800L)
        .maxAssignments(5)
        .build();
  }

  public static CreateHitWithHitTypeRequest MOCK_SECOND_CREATE_HIT_WITH_HIT_TYPE_REQUEST() {
    return CreateHitWithHitTypeRequest.builder()
        .hitTypeId(MOCKED_HIT_TYPE_ID)
        .question(VerificationTaskMockData.EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT)
        .lifetimeInSeconds(604800L)
        .maxAssignments(5)
        .build();
  }

  public static Map<UUID, String> EXPECTED_PUBLISHED_TASK_MAPPINGS() {
    Map<UUID, String> returnMap = new HashMap<>();
    returnMap.put(VerificationTaskMockData.FIRST_MOCK_UUID, FIRST_CS_MOCK_ID);
    returnMap.put(VerificationTaskMockData.SECOND_MOCK_UUID, SECOND_CS_MOCK_ID);
    return returnMap;
  }

  public static GetHitResponse MOCK_FIRST_HIT_RESPONSE() {
    return GetHitResponse.builder()
        .hit(
            HIT.builder()
                .hitId(FIRST_CS_MOCK_ID)
                .maxAssignments(5)
                .numberOfAssignmentsCompleted(3)
                .build()
        )
        .build();
  }

  public static GetHitResponse MOCK_SECOND_HIT_RESPONSE() {
    return GetHitResponse.builder()
        .hit(
            HIT.builder()
                .hitId(SECOND_CS_MOCK_ID)
                .maxAssignments(5)
                .numberOfAssignmentsCompleted(1)
                .build()
        )
        .build();
  }

  public static Map<String, HitStatus> EXPECTED_HIT_STATUS_MAP() {
    Map<String, HitStatus> returnMap = new HashMap<>();
    returnMap.put(FIRST_CS_MOCK_ID, new HitStatus(FIRST_CS_MOCK_ID, 5, 3));
    returnMap.put(SECOND_CS_MOCK_ID, new HitStatus(SECOND_CS_MOCK_ID, 5, 1));
    return returnMap;
  }
}
