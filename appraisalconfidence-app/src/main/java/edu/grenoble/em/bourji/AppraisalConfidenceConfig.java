package edu.grenoble.em.bourji;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * Created by Moe on 8/16/2017.
 */
public class AppraisalConfidenceConfig extends Configuration {

    @Valid
    @NotEmpty
    @JsonProperty("authDomain")
    private String authDomain;

    @Valid
    @NotEmpty
    @JsonProperty("kid")
    private String kid;

    public String getKid() {
        return this.kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAuth0Domain() {
        return authDomain;
    }

    public void setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
    }
}
