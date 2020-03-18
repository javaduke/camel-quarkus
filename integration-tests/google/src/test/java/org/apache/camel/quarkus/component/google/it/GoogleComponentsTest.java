/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.component.google.it;

import java.util.UUID;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.hamcrest.Matchers.is;

@EnabledIfEnvironmentVariable(named = "GOOGLE_API_APPLICATION_NAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "GOOGLE_API_CLIENT_ID", matches = ".+")
@EnabledIfEnvironmentVariable(named = "GOOGLE_API_CLIENT_SECRET", matches = ".+")
@EnabledIfEnvironmentVariable(named = "GOOGLE_API_REFRESH_TOKEN", matches = ".+")
@QuarkusTest
class GoogleComponentsTest {

    @Test
    public void testGoogleDriveComponent() {
        String title = UUID.randomUUID().toString();

        // Create
        String fileId = RestAssured.given()
                .contentType(ContentType.TEXT)
                .body(title)
                .post("/google-drive/create")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        // Read
        RestAssured.given()
                .queryParam("fileId", fileId)
                .get("/google-drive/read")
                .then()
                .statusCode(200)
                .body(is(title));

        // Delete
        RestAssured.given()
                .queryParam("fileId", fileId)
                .delete("/google-drive/delete")
                .then()
                .statusCode(204);

        RestAssured.given()
                .queryParam("fileId", fileId)
                .get("/google-drive/read")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGoogleMailComponent() {
        String message = "Hello Camel Quarkus Google Mail";

        // Create
        String messageId = RestAssured.given()
                .contentType(ContentType.TEXT)
                .body(message)
                .post("/google-mail/create")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        // Read
        RestAssured.given()
                .queryParam("messageId", messageId)
                .get("/google-mail/read")
                .then()
                .statusCode(200)
                .body(is(message));

        // Delete
        RestAssured.given()
                .queryParam("messageId", messageId)
                .delete("/google-mail/delete")
                .then()
                .statusCode(204);

        RestAssured.given()
                .queryParam("messageId", messageId)
                .get("/google-mail/read")
                .then()
                .statusCode(404);
    }
}
