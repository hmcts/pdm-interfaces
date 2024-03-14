 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.5.1
FROM hmctspublic.azurecr.io/base/java:17-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/pdmanager-1.0.war /opt/app/

EXPOSE 5432
CMD [ "pdmanager-1.0.war" ]