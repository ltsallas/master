package wifi4eu.wifi4eu.abac.integration.essi;

import com.cryptolog.mss.client.PDFWidgetDescription;
import eu.europa.ec.digit.essi.common.util.ConfigurationProperties;
import eu.europa.ec.digit.essi.services.central.client.ClientConfiguration;
import eu.europa.ec.digit.essi.services.central.client.PadesSigner;
import eu.europa.ec.digit.essi.services.central.client.PdfSignatureField;
import eu.europa.ec.digit.essi.services.central.client.SigningException;
import eu.europa.ec.digit.essi.services.central.client.SigningFailedException;
import eu.europa.ec.digit.essi.services.central.client.SigningResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import wifi4eu.wifi4eu.abac.data.entity.Document;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class EssiService {

    private final Logger log = LoggerFactory.getLogger(EssiService.class);

    @Resource(name = "essi-client-config")
    private Properties essiClientConfigProperties;

    private ClientConfiguration essiClientConfiguration;

    @PostConstruct
    public void init() throws Exception {
        log.info("Initializing the ESSI service");

        try {
            ConfigurationProperties configProperties = ConfigurationProperties.fromProperties(essiClientConfigProperties,"EreceiptProperty Files");
            InputStream keyStore = this.getClass().getClassLoader().getResourceAsStream(essiClientConfigProperties.getProperty("scriptUser.keyStoreResourceName"));
            this.essiClientConfiguration = ClientConfiguration.getConfiguration(configProperties, IOUtils.toByteArray(keyStore));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Could not initialize ESSI Legacy Client. Reason %s", e.getMessage()));
        }
    }

    public void signDocument(Document document) throws Exception {
        PadesSigner psigner = new PadesSigner(essiClientConfiguration);
        PDFWidgetDescription pdfWidgetDescription = new PDFWidgetDescription();
        PDFWidgetDescription.Text text = new PDFWidgetDescription.Text(new PDFWidgetDescription.Position(100, 100), "Visible signature test");
        pdfWidgetDescription.addText(text);
        pdfWidgetDescription.setPosition(new PDFWidgetDescription.Position(/*x*/50, /*y*/50, /*width*/200, /*height*/100));
        psigner.setPdfSignatureField(new PdfSignatureField(PdfSignatureField.Policy.NEW_IF_NO_EXISTING, /*page*/7, "For the Agency", pdfWidgetDescription));

        SigningResult signingResult = psigner.signWithResult(document.getData());
        byte[] signedDocument = signingResult.getSignature();
        String requestId = signingResult.getRequestId();

        try (FileOutputStream fos = new FileOutputStream("c://PDM//tmp/WIFI//test_signed.pdf")) {
            fos.write(signedDocument);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
