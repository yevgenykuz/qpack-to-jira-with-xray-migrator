package me.yevgeny.q2jwxmigrator;

import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObject;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClient;
import me.yevgeny.q2jwxmigrator.wsclient.qpack.QpackSoapClientException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HtmlToJiraMarkupConvertertionTests {

    @Test
    @DisplayName("Qpack object description HTML to Jira Markup Conversion")
    void testQpackObjectDescriptionIsProperlyConverted() throws QpackSoapClientException {
        int id = 72028;
        String objectFieldNameKey = "Description";
        String expectedContained = "RF Plan:";
        QpackObject qpackObject = QpackSoapClient.getInstance().getQpackObject(id);
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String htmlText = qpackObject.getFieldValue(objectFieldNameKey);
        String xhtmlText = converter.convertXHtmlToWikiMarkup(htmlText);
        String jiraMarkupText = converter.convertXHtmlToWikiMarkup(xhtmlText);
        System.out.println(jiraMarkupText);
        assertTrue(jiraMarkupText.contains(expectedContained));
    }
}
