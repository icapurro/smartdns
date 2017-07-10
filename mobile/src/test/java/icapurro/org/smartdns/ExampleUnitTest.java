package icapurro.org.smartdns;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import icapurro.org.smartdns.data.remote.SmartDnsService;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String in = "<IPController.APIResponse xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://schemas.datacontract.org/2004/07/\">\n" +
                "<Message>\n" +
                "Ignacio, Your IP 167.56.188.111 has been activated.\n" +
                "</Message>\n" +
                "<Status>Success</Status>\n" +
                "</IPController.APIResponse>";

        SimpleXmlConverterFactory.create();

        Serializer serializer = new Persister();
        SmartDnsService.Response student = serializer.read(SmartDnsService.Response.class, in);
        System.out.println(student.getMessage());
    }
}