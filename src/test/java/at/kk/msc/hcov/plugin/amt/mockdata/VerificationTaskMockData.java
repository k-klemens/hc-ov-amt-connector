package at.kk.msc.hcov.plugin.amt.mockdata;

import at.kk.msc.hcov.sdk.verificationtask.model.VerificationTask;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VerificationTaskMockData {


  public static final String MOCKED_VERIFICATION_NAME = "MOCK-Verification";
  public static final String MOCKED_ONTOLOGY_NAME = "ONTOLOGY-TEST-NAME";
  public static final UUID FIRST_MOCK_UUID = UUID.fromString("903e7448-591b-4b70-948a-9b7d6cc0de8e");
  public static final UUID SECOND_MOCK_UUID = UUID.fromString("a8c192f6-5971-4812-bc2a-d0d4aae5d19e");

  public static final String EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>FIRST-CONTEXT</span> requires verification for <span>FIRST-ELEMENT</span>  is verified";

  public static final String
      EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT =
      "Ontology with name <span>ONTOLOGY-TEST-NAME</span> from <span>SECOND-CONTEXT</span> requires verification for <span>SECOND-ELEMENT</span>  is verified";

  private static final VerificationTask.VerificationTaskBuilder VERIFICATION_TASK_BUILDER = VerificationTask.builder()
      .verificationName(MOCKED_VERIFICATION_NAME)
      .ontologyName(MOCKED_ONTOLOGY_NAME);

  public static final List<VerificationTask> MOCK_VERIFICATION_TASKS() {
    List<VerificationTask> expectedTasksWithContext = new ArrayList<>();
    expectedTasksWithContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(FIRST_MOCK_UUID).taskHtml(EXPECTED_FIRST_TEMPLATE_WITH_CONTEXT).build()
    );
    expectedTasksWithContext.add(
        VERIFICATION_TASK_BUILDER.ontologyElementId(SECOND_MOCK_UUID).taskHtml(EXPECTED_SECOND_TEMPLATE_WITH_CONTEXT).build()
    );

    return expectedTasksWithContext;
  }

}
