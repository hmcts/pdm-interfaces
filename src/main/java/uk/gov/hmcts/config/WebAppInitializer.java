package uk.gov.hmcts.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import uk.gov.hmcts.pdm.publicdisplay.initialization.InitializationService;

public class WebAppInitializer implements ServletContextInitializer {

    public static final String SERVLET_NAME = "InitServlet";

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        InitializationService.getInstance().setEntityManagerFactory(entityManagerFactory);
    }

}
