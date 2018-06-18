import me.yevgeny.q2jwxmigrator.model.qpackObject.QpackObject;
import me.yevgeny.q2jwxmigrator.model.qpackWebObject.QpackWebObject;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClient;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClientException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QpackSoapClientTests {

    @Test
    @DisplayName("Test getQpackObject @ QpackSoapClient")
    void testGetQpackObject() throws QpackSoapClientException {
        int id = 72028;
        String objectFieldNameKey = "Name";
        String objectFieldNameValue = "Stage_0: Pre Condition";
        QpackObject qpackObject = QpackSoapClient.getInstance().getQpackObject(id);
        assertEquals(qpackObject.getFieldValue(objectFieldNameKey), objectFieldNameValue);
    }

    @Test
    @DisplayName("Test getQpackWebObject @ QpackSoapClient")
    void testGetQpackWebObject() throws QpackSoapClientException {
        int id = 73368;
        String objectFieldPathValue = "\\3102\\3103\\30903\\101231\\";
        QpackWebObject qpackwebObject = QpackSoapClient.getInstance().getQpackWebObject(id);
        System.out.println("- Description: " + qpackwebObject.getDescription() + "\n\n");
        List<QpackWebObject.Steps.Step> stepList = qpackwebObject.getSteps().getStep();
        System.out.println("- Total Steps: " + qpackwebObject.getSteps().getStep().size());
        for (QpackWebObject.Steps.Step step : stepList) {
            System.out.println(String.format("---> Step %s [ID=%s]:\nDescription:\n%s\nExpected:\n%s", step.getStepno(), step.getID(), step.getDescription(), step.getExpectedresult()));
        }

        assertEquals(qpackwebObject.getPath(), objectFieldPathValue);
    }
}
