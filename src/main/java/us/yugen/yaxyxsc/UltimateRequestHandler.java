package us.yugen.yaxyxsc;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import se.walkercrou.places.RequestHandler;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andreas Hartmann
 */
public class UltimateRequestHandler implements RequestHandler {

    private final HttpClient client = HttpClients.createSystem();


    /**
     * Returns the character encoding used by this handler.
     *
     * @return character encoding
     */
    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    /**
     * Sets the character encoding used by this handler.
     *
     * @param characterEncoding to use
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        throw new UnsupportedOperationException();
    }

    private String readString(HttpResponse response) throws IOException {
        String str = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }

    @Override
    public InputStream getInputStream(String uri) throws IOException {
        try {
            HttpGet get = new HttpGet(uri);
            return client.execute(get).getEntity().getContent();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public String get(String uri) throws IOException {
        try {
            HttpGet get = new HttpGet(uri);
            return readString(client.execute(get));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public String post(HttpPost data) throws IOException {
        try {
            return readString(client.execute(data));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
