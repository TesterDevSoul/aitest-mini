package com.ceshiren.aitestmini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jayway.jsonpath.JsonPath;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

//@SpringBootTest
class AitestMiniApplicationTests {
	@Autowired
	private RestTemplate restTemplate;

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Test
	public void getId() throws URISyntaxException, IOException {
		JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(
				new URI("http://81.70.96.121:10240/"), "hogwarts", "ceshiren");
		String s = jenkinsHttpClient.get("http://81.70.96.121:10240/job/mini01_job/api/json");
		Integer id = JsonPath.read(s, "$.builds[0].number");
		System.out.println(id);
	}
	public String getPublicIPAddress() {
		String ip = "";
		try {
			URL whatIsMyIp = new URL(
	"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wwecb0ab2da18a7c3b&corpsecret=6Yg1wwQ_gZIUTXezXfVtnbSfD01619-jo1LpopQT7w0");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatIsMyIp.openStream()));
			ip = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

	@Test
	public void t(){
		System.out.println(getPublicIPAddress());
		System.out.println(StringUtils.applyRelativePath("http://81.70.96.121:10240/","d"));
	}

	@Test
	void contextLoads() throws Exception {
		String jobConfigXml = getJobXml("src/main/resources/jenkins/hogwarts_jenkins_test_start.xml");
		JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(
				new URI("http://81.70.96.121:10240/"), "hogwarts", "ceshiren");
		JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
		jenkinsServer.createJob(null, "hogwarts",jobConfigXml , true);

	}
	private static String getJobXml(String path) throws Exception {

		File file = new File(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		StreamResult streamResult = new StreamResult(outputStream);

		transformer.transform(domSource, streamResult);
		String xmlString = outputStream.toString();
		System.out.println(xmlString);
		return xmlString;
	}
}
