package com.irfan.ecommerce.api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irfan.ecommerce.util.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import static org.hamcrest.Matchers.lessThan;

/**
 * BaseApiClient: The "Architectural Foundation" for Multi-Service Automation.
 * * 🚀 THE WALMART-SCALE "WHY":
 * Optimized for high-concurrency and environment resilience by utilizing 
 * a Context-Aware constructor instead of static lock-ins.
 */
public abstract class BaseApiClient {
    protected static final Logger logger = LogManager.getLogger(BaseApiClient.class);
    protected RequestSpecification requestSpec; // Instance-based for Multi-Tenant support
    protected static ResponseSpecification responseSpec;
    protected static final ObjectMapper mapper = new ObjectMapper();
    private final String projectPrefix;

    /**
     * CONSTRUCTOR: Dynamic Project Initialization
     * @param projectPrefix The key used in properties (e.g., "demoblaze" or "booker")
     */
    public BaseApiClient(String projectPrefix) {
        this.projectPrefix = projectPrefix;
        initializeFramework();
    }

    private void initializeFramework() {
        String baseUri = PropertyReader.getProperty(projectPrefix + ".api.base.uri");
        
        if (baseUri == null) {
            throw new RuntimeException("🛑 CONFIG_ERROR: base.uri missing for project: " + projectPrefix);
        }

        // 🛡️ THE CIRCUIT BREAKER (Health Check)
        // SITUATION: Protects CI budget by killing the run if the environment is 500-ing.
        performHealthCheck(baseUri);

        // 🏗️ REQUEST SPECIFICATION
        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .build();

        // ⚠️ RESPONSE SPECIFICATION (Global SLAs)
        responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(5000L))
                .build();

        logger.info("✅ {} INFRASTRUCTURE READY: {}", projectPrefix.toUpperCase(), baseUri);
    }

    private void performHealthCheck(String baseUri) {
        try {
            logger.info("📡 CIRCUIT BREAKER: Checking health of {}...", projectPrefix);
            int status = RestAssured.get(baseUri).getStatusCode();
            if (status >= 500) {
                logger.fatal("🛑 CIRCUIT BREAKER TRIGGERED: {} returned {}. Aborting run.", baseUri, status);
                System.exit(1);
            }
        } catch (Exception e) {
            logger.fatal("🚨 ENVIRONMENT UNREACHABLE: {} is down.", baseUri);
            System.exit(1);
        }
    }

    /**
     * 🛡️ handleApiFailure: The "CSI Evidence Collector"
     */
    protected void handleApiFailure(Response response, String endpoint) {
        int statusCode = response.getStatusCode();
        if (statusCode >= 400) {
            String requestId = response.getHeader("X-Request-ID");
            logger.error("🚨 API FAILURE | Endpoint: {} | Status: {}", endpoint, statusCode);
            logger.error("🆔 Correlation ID: {}", (requestId != null ? requestId : "N/A"));
            logger.error("📦 Evidence Body: \n{}", response.getBody().asPrettyString());

            if (statusCode >= 500) {
                throw new RuntimeException("🛑 SERVER_CRASH at " + endpoint);
            }
        }
    }

    /**
     * 🆔 getRequestSpec: The "Traceability Injector"
     */
    protected RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .addHeader("X-Request-ID", UUID.randomUUID().toString())
                .addHeader("X-Project-Context", projectPrefix)
                .addRequestSpecification(requestSpec)
                .build();
    }

    /**
 * THE WALMART RESUME REF: "Built a Namespace-Aware property wrapper 
 * to handle microservice configuration dynamically."
 */
protected String getProperty(String keyTail) {
    // This connects the 'booker' prefix with 'api.endpoint.booking'
    return PropertyReader.getProperty(projectPrefix + "." + keyTail);
}

    protected void logPayload(Object payload, String description) {
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
            logger.info("📦 PAYLOAD [{}]: \n{}", description, json);
        } catch (Exception e) {
            logger.error("❌ SERIALIZATION_ERROR for [{}].", description);
        }
    }
}