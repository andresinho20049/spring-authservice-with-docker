########### Stage BUILD ###########
FROM maven:3.6.3-jdk-8-slim as builder

# Define Workdir
WORKDIR /opt/app
#Copy pom.xml to container
COPY pom.xml ./

RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean
RUN mvn install package

########### Stage RUN ###########
FROM eclipse-temurin:8-jre-alpine@sha256:f6485e494f9927cb6a51e23134b3556c62c32c3df20fc4317f7f493343726977 as runner
SHELL ["/bin/sh", "-c"]

RUN mkdir -p /etc/skel/

RUN echo 'history -c \
/bin/rm -f /opt/remote/.mysql_history \
/bin/rm -f /opt/remote/.history \
/bin/rm -f /opt/remote/.bash_history' >> /etc/skel/.logout

RUN echo 'set autologout = 30 \
set prompt = "$ " \
set history = 0 \
set ignoreeof' >> /etc/skel/.cshrc

RUN cp /etc/skel/.cshrc /etc/skel/.profile

# dumb-init occupies PID 1 and takes care of all the responsibilities.
RUN apk add dumb-init

# Define Workdir
WORKDIR /opt/app

# Add user javauser based minumum privileges
RUN addgroup -S -g 1000 javagroup && \
    adduser -S -s /bin/false -G javagroup -u 999 javauser

RUN echo "javauser:secretPass" | chpasswd

# Copy target from build stage
COPY --from=builder /opt/app/target/AuthService.war ./

RUN chmod 0 /home \
    && chmod 0 /var \
    && chmod 0 /usr \
    && chmod 0 /dev \
    && chmod 0 /mnt \
    && chmod go-rw /etc \
    && chmod 0 /etc/ssl \
    && chmod 0 /etc/skel \
    && chmod a+rwx /usr/bin/dumb-init

RUN chown -R javauser:javagroup /opt/app
USER javauser

ENTRYPOINT ["dumb-init", "java", "-jar", "AuthService.war"]
