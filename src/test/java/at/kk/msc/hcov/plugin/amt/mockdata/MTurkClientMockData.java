package at.kk.msc.hcov.plugin.amt.mockdata;

import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.HitStatus;
import at.kk.msc.hcov.sdk.crowdsourcing.platform.model.RawResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import software.amazon.awssdk.services.mturk.model.Assignment;
import software.amazon.awssdk.services.mturk.model.CreateHitWithHitTypeRequest;
import software.amazon.awssdk.services.mturk.model.GetHitResponse;
import software.amazon.awssdk.services.mturk.model.HIT;
import software.amazon.awssdk.services.mturk.model.ListAssignmentsForHitResponse;

public class MTurkClientMockData {
  /* MOCK DATA WHICH THE PLUGIN MOCK CAN BE USED FOR! */
  public static final String MOCKED_HIT_TYPE_ID = "HIT-TYPE-ID";
  public static final String FIRST_CS_MOCK_ID = "FIRST-CS-ID";
  public static final String FIRST_TASK_ASSIGNMENT_1 = "FA1";
  public static final String FIRST_TASK_ASSIGNMENT_2 = "FA2";
  public static final String FIRST_TASK_ASSIGNMENT_3 = "FA3";
  public static final String FIRST_TASK_ANSWER_1 = "FAN1";
  public static final String FIRST_TASK_ANSWER_2 = "FAN2";
  public static final String FIRST_TASK_ANSWER_3 = "FAN3";
  public static final String SECOND_CS_MOCK_ID = "SECOND-CS-ID";
  public static final String SECOND_TASK_ASSIGNMENT_1 = "SA1";
  public static final String SECOND_TASK_ANSWER_1 = "SAN1";

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


  public static ListAssignmentsForHitResponse MOCK_FIRST_ASSIGNMENTS_FOR_HIT() {
    return ListAssignmentsForHitResponse.builder()
        .assignments(
            Assignment.builder().assignmentId(FIRST_TASK_ASSIGNMENT_1).workerId("W1").hitId(FIRST_CS_MOCK_ID).answer(FIRST_TASK_ANSWER_1)
                .build(),
            Assignment.builder().assignmentId(FIRST_TASK_ASSIGNMENT_2).workerId("W2").hitId(FIRST_CS_MOCK_ID).answer(FIRST_TASK_ANSWER_2)
                .build(),
            Assignment.builder().assignmentId(FIRST_TASK_ASSIGNMENT_3).workerId("W3").hitId(FIRST_CS_MOCK_ID).answer(FIRST_TASK_ANSWER_3)
                .build()
        )
        .build();
  }

  public static ListAssignmentsForHitResponse MOCK_SECOND_ASSIGNMENTS_FOR_HIT() {
    return ListAssignmentsForHitResponse.builder()
        .assignments(
            Assignment.builder().assignmentId(SECOND_TASK_ASSIGNMENT_1).workerId("W1").hitId(SECOND_CS_MOCK_ID).answer(SECOND_TASK_ANSWER_1)
                .build()
        )
        .build();
  }

  public static List<RawResult> EXPECTED_FIRST_RAW_RESULTS() {
    return List.of(
        new RawResult(FIRST_TASK_ASSIGNMENT_1, FIRST_CS_MOCK_ID, "W1", FIRST_TASK_ANSWER_1),
        new RawResult(FIRST_TASK_ASSIGNMENT_2, FIRST_CS_MOCK_ID, "W2", FIRST_TASK_ANSWER_2),
        new RawResult(FIRST_TASK_ASSIGNMENT_3, FIRST_CS_MOCK_ID, "W3", FIRST_TASK_ANSWER_3)
    );
  }

  public static List<RawResult> EXPECTED_SECOND_RAW_RESULTS() {
    return List.of(
        new RawResult(SECOND_TASK_ASSIGNMENT_1, SECOND_CS_MOCK_ID, "W1", SECOND_TASK_ANSWER_1)
    );
  }
}
