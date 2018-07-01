package me.yevgeny.q2jwxmigrator.wsclient.qpack;

import me.yevgeny.q2jwxmigrator.model.qpackobject.QpackObject;
import me.yevgeny.q2jwxmigrator.model.qpackwebobject.QpackWebObject;
import me.yevgeny.q2jwxmigrator.utilities.ConfigurationManager;
import me.yevgeny.q2jwxmigrator.utilities.QpackXmlHelper;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class QpackSoapClient {
    private static final String QPACK_API_PATH = "/QPack/QPackServ/QPackServ.asmx/";
    private static final Logger logger = Logger.getLogger(QpackSoapClient.class.getSimpleName());
    private static QpackSoapClient ourInstance;

    private CloseableHttpClient client;
    private String qpackUrl;
    private String qpackUsername;
    private String qpackPassword;

    public static QpackSoapClient getInstance() throws QpackSoapClientException {
        if (null == ourInstance) {
            ourInstance = new QpackSoapClient();
            try {
                ourInstance.qpackUrl = ConfigurationManager.getInstance().getConfigurationValue("qpackUrl");
                ourInstance.qpackUsername = ConfigurationManager.getInstance().getConfigurationValue("qpackUsername");
                ourInstance.qpackPassword = ConfigurationManager.getInstance().getConfigurationValue("qpackPassword");
            } catch (FileNotFoundException e) {
                throw new QpackSoapClientException(e.getMessage());
            }

            ourInstance.client = HttpClientBuilder.create().build();
        }

        return ourInstance;
    }

    public QpackObject getQpackObject(int id) throws QpackSoapClientException {
        String result;

        try {
            result = httpPost("Get_Object", new BasicNameValuePair("User_Name", ourInstance.qpackUsername),
                    new BasicNameValuePair("User_Password", ourInstance.qpackPassword), new BasicNameValuePair("ID",
                            Integer.toString(id)));
        } catch (HttpException e) {
            throw new QpackSoapClientException(e.getMessage());
        }

        result = QpackXmlHelper.fixQpackObjectStringTags(result);
        result = QpackXmlHelper.fixQpackObjectXmlTagContent(result, "Field");

        QpackObject qpackObject;

        try {
            JAXBContext context = JAXBContext.newInstance(QpackObject.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            qpackObject = (QpackObject) unmarshaller.unmarshal(new StringReader(result));
            return qpackObject;
        } catch (JAXBException e) {
            throw new QpackSoapClientException(String.format("XML conversion error. Got the following from API:\n%s",
                    result));
        }
    }

    public QpackWebObject getQpackWebObject(int id) throws QpackSoapClientException {
        String result;

        try {
            result = httpPost("QPack_Web_RootTree_Item", new BasicNameValuePair("User_Name", ourInstance.qpackUsername),
                    new BasicNameValuePair("User_Password", ourInstance.qpackPassword), new BasicNameValuePair
                            ("ItemId", Integer.toString(id)), new BasicNameValuePair("WebsiteURL", ourInstance
                            .qpackUrl));
        } catch (HttpException e) {
            throw new QpackSoapClientException(e.getMessage());
        }

        result = QpackXmlHelper.removeQpackWebObjectItemTags(result);
        result = QpackXmlHelper.surroundQpackWebObjectDescriptionWithTags(result);

        QpackWebObject qpackWebQpackWebObject;

        try {
            JAXBContext context = JAXBContext.newInstance(QpackWebObject.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            qpackWebQpackWebObject = (QpackWebObject) unmarshaller.unmarshal(new StringReader(result));
            return qpackWebQpackWebObject;
        } catch (JAXBException e) {
            throw new QpackSoapClientException(String.format("XML conversion error. Got the following from API:\n%s",
                    result));
        }
    }

    private String httpPost(String method, NameValuePair... params) throws HttpException {
        try {
            // Check connection to Qpack:
            new URL(ourInstance.qpackUrl);
        } catch (MalformedURLException e) {
            throw new HttpException(String.format("Can't reach QPACK @%s", ourInstance.qpackUrl));
        }

        String response;

        HttpPost httpPostUrl = new HttpPost(ourInstance.qpackUrl + QPACK_API_PATH + method);
        logger.debug(String.format("Sending HTTP Post to: %s with: %s", httpPostUrl.getURI(), Arrays.toString
                (params)));

        try {
            httpPostUrl.setEntity(new UrlEncodedFormEntity(Arrays.asList(params), "UTF-8"));
            CloseableHttpResponse httpResponse = ourInstance.client.execute(httpPostUrl);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                EntityUtils.consume(httpResponse.getEntity());
                throw new HttpException(String.format("Got response status code: %s", httpResponse.getStatusLine()
                        .getStatusCode()));
            } else {
                response = EntityUtils.toString(httpResponse.getEntity());
                if (response.contains("<Error>")) {
                    throw new HttpException(String.format("QPACK API error:\n%s", response));
                }
            }
        } catch (Exception e) {
            closeConnection();
            throw new HttpException(e.getMessage());
        }

        return response;
    }

    private void closeConnection() {
        try {
            ourInstance.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private QpackSoapClient() {
    }
}
