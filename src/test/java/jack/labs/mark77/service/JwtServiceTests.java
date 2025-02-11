package jack.labs.mark77.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtServiceTests {

    @Test
    void parseClaims_ValidToken() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoidG9ueSIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MzkyMzAzMDIsImV4cCI6MTc0OTIzMDMwMn0.A7qnN7RduUXS4AIVd46GzMj2OfIJIZ5YABwVEzS5tMI";
        String secretKey = "i123am123iron123man123For123tony123stark99mark77";
        Claims c = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        String actual = c.get("user_id", String.class);
        String expected = "tony";

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEqualTo(expected);

    }
}
