package com.andresinho20049.authservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {
  AuthServiceApplication.class})
@ActiveProfiles("test")
class AuthServiceApplicationTest {

}
