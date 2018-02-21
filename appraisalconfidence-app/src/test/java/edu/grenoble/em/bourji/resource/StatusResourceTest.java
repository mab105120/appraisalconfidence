package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.AppraisalConfidenceApp;
import edu.grenoble.em.bourji.AppraisalConfidenceConfig;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created by Moe on 9/23/2017.
 */
public class StatusResourceTest {

    @ClassRule
    public static final DropwizardAppRule<AppraisalConfidenceConfig> RULE =
            new DropwizardAppRule<>(AppraisalConfidenceApp.class,
                    ResourceHelpers.resourceFilePath("test.yml"));

    private final String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik5URkRPRVF3TlRVNE9UZzFRVE5DTXpZeE9FUkNRVFU1UWpSRVJEbERNRGRCTnpnNE5VWkRRdyJ9.eyJuYW1lIjoibW9oZC5ib3VyamlAZ21haWwuY29tIiwibmlja25hbWUiOiJtb2hkLmJvdXJqaSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci8yMmQ2ZTM3ZWE4ZjgxNWNlOTRhYTU0ZGIyZTJjZGQwMT9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRm1vLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDE3LTA5LTI0VDAxOjQ3OjEwLjA5NFoiLCJpc3MiOiJodHRwczovL2FwcHJhaXNhbC1ncmVub2JsZS1ib3VyamkuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU5OTUxN2JlM2M4ZTcyNjRjMDA4YzMyOCIsImF1ZCI6ImtqbFhnNm1ZU040NEQ1WFNoQlNEM2M2UDBlcVdWRDR6IiwiZXhwIjoxNTA2MjUzNjMwLCJpYXQiOjE1MDYyMTc2MzAsIm5vbmNlIjoiU3R1MkxjVEdSdkYxc3NsdmJlcjhGdkRGVlp3RFhTX2kiLCJhdF9oYXNoIjoic1hXZlJTR3RPa1RBS2w2c01icnp1ZyJ9.b5MATltC3Dpu86100wg0vF_k8UujWGKpJ-JX-U8_jSyQ90IKgMbCPZKz-p0QQ4Wd_tU4SP6v_QBRMOX3saTOE_O4WbeMPdTqTOjN4vKGihKs0EieKMZWttrK9XQVBwJPDNoKChguFpnt0zNM4CyLprowYMbcxI9px2SFNEpZ03Osm-BNtRr3f2cgkE_xWu4ZlxwUMX0F-AOs4wYemU2I0FjUPbrYErinvH1hrCIQhzaDT_Az0T1q33ngZsgXpBz81NQgaNgIQFaRy5xmDcFOd8hOdLLn1anmf5-e6jPOIDU71aI97V0D1D1PZkQQGqm4wtGoaT3L8C-xWcIam1lJ0g";

    @Test
    public void postStatusTest() {
        Client client = ClientBuilder.newClient();
        Response response = client.target(String.format("http://localhost:%d", RULE.getLocalPort()))
                .path("/api/status/USER_EXPERIENCE")
                .request()
                .header("Authorization", "Bearer " + access_token)
                .post(Entity.json(""));
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

}
